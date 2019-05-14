package com.ischoolbar.programmer.controller.home;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ischoolbar.programmer.entity.Subject;
import com.ischoolbar.programmer.entity.SubjectItem;
import com.ischoolbar.programmer.entity.User;
import com.ischoolbar.programmer.entity.Vote;
import com.ischoolbar.programmer.entity.VoteType;
import com.ischoolbar.programmer.service.SubjectItemService;
import com.ischoolbar.programmer.service.SubjectService;
import com.ischoolbar.programmer.service.VoteService;
import com.ischoolbar.programmer.util.DateFormatUtil;

@Controller
@RequestMapping("/home")
public class VoteController {
	
	@Autowired
	private SubjectService subjectService;
	@Autowired
	private SubjectItemService subjectItemService;
	@Autowired
	private VoteService voteService;
	
	@RequestMapping(value="/vote_list",method=RequestMethod.GET)
	public ModelAndView index(ModelAndView model){
		Map<String,Object> queryMap = new HashMap<String, Object>();
		queryMap.put("name", "%%");
		queryMap.put("startIndex", 0);
		queryMap.put("pageSize", 9999);
		List<Subject> findByName = subjectService.findByRelation(queryMap);
		model.addObject("voteList", findByName);
		model.setViewName("home/vote_list");
		model.addObject("singleType", "SINGLE");
		model.addObject("muilteType", "MUILTE");
		model.addObject("nowTime", System.currentTimeMillis());
		return model;
	}
	
	@RequestMapping(value="/vote_history_list",method=RequestMethod.GET)
	public ModelAndView historyList(ModelAndView model,HttpServletRequest request){
		Map<String,Object> queryMap = new HashMap<String, Object>();
		User user = (User)request.getSession().getAttribute("user");
		if(user == null){
			model.setViewName("home/login");
			return model;
		}
		queryMap.put("name", "%%");
		queryMap.put("startIndex", 0);
		queryMap.put("pageSize", 9999);
		List<Subject> subjectList = subjectService.findByRelation(queryMap);
		queryMap.put("userId", user.getId());
		List<Vote> voteList = voteService.findList(queryMap);
		List<Subject> ret = new ArrayList<Subject>();
		DecimalFormat df = new DecimalFormat("0.00");
		for(Subject s:subjectList){
			for(Vote v:voteList){
				if(s.getId() == v.getSubjectId()){
					s.getVotedList().add(v);
					if(!ret.contains(s))
						ret.add(s);
					for(SubjectItem si:s.getSubjectItems()){
						String format = df.format((float)si.getVoteNumber()/s.getVoteNumber()*100);
						si.setPer(Float.valueOf(format));
						if(v.getSubjectItemId() == si.getId()){
							si.setFlag("true");
						}
					}
				}
			}
			
		}
		model.addObject("voteList", ret);
		model.setViewName("home/vote_history_list");
		model.addObject("singleType", "SINGLE");
		model.addObject("muilteType", "MUILTE");
		model.addObject("nowTime", System.currentTimeMillis());
		return model;
	}
	
	
	@RequestMapping(value="/add_vote",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, String> addVote(
			@RequestParam(name="subjectId",required=true) Long subjectId,
			@RequestParam(name="subjectItemIds",required=true) String subjectItemIds,
			HttpServletRequest request
			){
		Map<String, String> ret = new HashMap<String, String>();
		User user = (User)request.getSession().getAttribute("user");
		if(user == null){
			ret.put("type", "error");
			ret.put("msg", "���ȵ�¼��ͶƱ��");
			return ret;
		}
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("userId", user.getId());
		queryMap.put("subjectId", subjectId);
		List<Vote> findList = voteService.findList(queryMap);
		for(Vote v:findList){
			if(v.getSubject().getType() == VoteType.SINGLE_ONE_TIME || v.getSubject().getType() == VoteType.MUILTE_ONE_TIME){
				ret.put("type", "voted");
				ret.put("msg", "���Ѿ�Ͷ��Ʊ�ˣ�");
				return ret;
			}
			if(v.getCreateTime().getTime() > DateFormatUtil.getTimestamp()){
				ret.put("type", "voted");
				ret.put("msg", "�������Ѿ�Ͷ��Ʊ�ˣ�����������");
				return ret;
			}
		}
		Vote vote = new Vote();
		vote.setSubjectId(subjectId);
		vote.setUserId(user.getId());
		String[] split = subjectItemIds.split(",");
		for(String s:split){
			Long subjectItemId = Long.valueOf(s);
			vote.setSubjectItemId(subjectItemId);
			vote.setCreateTime(new Date(System.currentTimeMillis()));
			if(voteService.add(vote) <= 0){
				ret.put("type", "error");
				ret.put("msg", "ͶƱʧ�ܣ�");
				return ret;
			}
			if(subjectItemService.addOneVote(subjectItemId) <= 0){
				ret.put("type", "error");
				ret.put("msg", "ͶƱ�����޸�ʧ�ܣ�");
				return ret;
			}
			if(subjectService.addOneVote(subjectId) <= 0){
				ret.put("type", "error");
				ret.put("msg", "ͶƱ�����޸�ʧ�ܣ�");
				return ret;
			}
		}
		ret.put("type", "success");
		ret.put("msg", "ͶƱ�ɹ���");
		return ret;
	}
	
}

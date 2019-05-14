package com.ischoolbar.programmer.controller.admin;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ischoolbar.programmer.entity.Subject;
import com.ischoolbar.programmer.entity.SubjectItem;
import com.ischoolbar.programmer.entity.VoteType;
import com.ischoolbar.programmer.page.Page;
import com.ischoolbar.programmer.service.SubjectItemService;
import com.ischoolbar.programmer.service.SubjectService;

@Controller
@RequestMapping("/admin/subject_item")
public class SubjectItemController {
	
	@Autowired
	private SubjectItemService subjectItemService;
	
	@Autowired
	private SubjectService subjectService;
	
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public ModelAndView index(ModelAndView model){
		Map<String,Object> queryMap = new HashMap<String, Object>();
		queryMap.put("name", "%%");
		queryMap.put("startIndex", 0);
		queryMap.put("pageSize", 9999);
		List<Subject> subjects = subjectService.findByName(queryMap);
		model.addObject("subjectsJson",JSONArray.fromObject(subjects));
		model.addObject("subjects",subjects);
		model.setViewName("admin/subject_item/list");
		return model;
	}
	
	@RequestMapping(value="/add",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> add(SubjectItem subjectItem){
		Map<String,Object> ret = new HashMap<String,Object>();
		subjectItem.setCreateTime(new Date(System.currentTimeMillis()));
		if(subjectItemService.add(subjectItem) <= 0){
			ret.put("type", "error");
			ret.put("msg", "添加失败!");
			return ret;
		}
		ret.put("type", "success");
		return ret;
	}
	
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> edit(SubjectItem subjectItem){
		Map<String,Object> ret = new HashMap<String,Object>();
		if(subjectItemService.edit(subjectItem) <= 0){
			ret.put("type", "error");
			ret.put("msg", "修改失败!");
			return ret;
		}
		ret.put("type", "success");
		return ret;
	}
	
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> delete(String id){
		Map<String,Object> ret = new HashMap<String,Object>();
		try {
			if(subjectItemService.delete("(" + id + ")") <= 0){
				ret.put("type", "error");
				ret.put("msg", "删除失败!");
				return ret;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			ret.put("type", "error");
			ret.put("msg", "改主题下存在投票信息，无法删除，请先去删除投票选项!");
			return ret;
		}
		ret.put("type", "success");
		return ret;
	}
	
	@RequestMapping(value="/get_list",method=RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> list(@RequestParam(value="name", required=false,defaultValue="") String name,
			Page page
			){
		Map<String,Object> queryMap = new HashMap<String,Object>();
		queryMap.put("title", "%" + name + "%");
		queryMap.put("startIndex", page.getOffset());
		queryMap.put("pageSize", page.getRows());
		List<SubjectItem> findByName = subjectItemService.findByName(queryMap);
		Map<String,Object> ret = new HashMap<String,Object>();
		ret.put("total", subjectItemService.getTotalByName(queryMap));
		ret.put("rows", findByName);
		return ret;
	}
}

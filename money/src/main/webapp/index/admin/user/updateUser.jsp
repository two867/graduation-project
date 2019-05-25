<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%
	String path = request.getContextPath() + "/";
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<base href="<%=basePath%>"/>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>无标题文档</title>
	<link href="<%=basePath %>resource/admin/css/style.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="<%=basePath %>resource/commons/utils.js"></script>
	<script type="text/javascript" src="<%=basePath %>plugins/jQuery/jquery.js"></script>
	
    <script type="text/javascript">
	$(function(){
		$("#account").focus();
	})
	function add(){
		var account=$.trim($("#account").val());
		var password=$.trim($("#password").val());
		var tel=$.trim($("#tel").val());
		var idk=$.trim($("#idk").val());
		if(account.length==0){
				alert("账号不能为空");
				$("#account").focus();
				return false;
			}
		if(!utils.isChinaOrNumbOrLett(password)){
				alert("密码只能由汉字、字母、数字组成");
				$("#password").focus();
				return false;
			}
		if(!utils.checkMobile(tel)){
				alert("请输入正确的手机号码");
				$("#tel").focus();
				return false;
			}
		if(!utils.isIDno(idk)){
				alert("身份证号码不符合格式");
				$("#idk").focus();
				return false;
			}
			return true;
	}
	</script>
</head>

<body>

<div class="place">
	<span>位置：</span>
	<ul class="placeul">
		<li><a href="#">经费管理员管理</a></li>
		<li><a href="#">修改经费管理员信息</a></li>
	</ul>
</div>

<form action = "user/saveUpdateUser1" method = "post"  class="validate" onsubmit="return add();" >
	<div class="formbody">
	<input type="hidden" name="id" value="${user.id}"/>
		<div class="formtitle"><span>基本信息</span></div>
		<ul class="forminfo">
			<li>
				<label>姓名</label>
				<input name="account" id="account" type="text" value="${user.account}" class="dfinput  " placeholder="请输入账号" />&nbsp;&nbsp;&nbsp;<font color="red">*</font>
			</li>
			 <li>
				<label>年龄</label>
				<input name="age" id="password" type="text" value="${user.age}" class="dfinput  " placeholder="请输入年龄" />&nbsp;&nbsp;&nbsp;<font color="red">*</font>
			</li>
			<li>
				<label>联系方式</label>
				<input name="tel" id="tel" type="text" value="${user.tel}" class="dfinput  "  placeholder="请输入联系方式" />&nbsp;&nbsp;&nbsp;<font color="red">*</font>
			</li>
			<li>
				<label>家庭住址</label>
				<input name="address" id="name" type="text" value="${user.address}" class="dfinput  " placeholder="请输入家庭住址" />&nbsp;&nbsp;&nbsp;<font color="red">*</font>
			</li>
			 <li>
				<label>身份证号码</label>
				<input name="idk" id="idk" type="text" value="${user.idk}" class="dfinput  " placeholder="请输入身份证号码" />&nbsp;&nbsp;&nbsp;<font color="red">*</font>
			</li>
			<li>
				<label>&nbsp;</label>
				<input type="submit" class="btn" value="提交"  />
				<input type="reset" class="btn" value="重置"  />
			</li>
		</ul>
	</div>
</form>
<script type="text/javascript">
	/**
	 * 这个使用的单选框选择判断
	 */
	function toUpdate(){
		var id = $("input[name='id']:checked").val();
		if(!id){
			alert("请选择要操作的记录");
			return false;
		}else{
			alert( "您操作的值为："+$("input[name='id']:checked").val())
		}
	}
	$(function(){
		$('.tablelist tbody tr:odd').addClass('odd');
		
		$("#formtitle").click(function(){
			$("#forminfo").slideToggle("slow");
		});
	});
</script>
<script type="text/javascript" src="<%=basePath %>plugins/jQuery/jquery.js"></script>
<script type="text/javascript" src="<%=basePath %>resource/admin/js/validate.js"></script>
<script type="text/javascript" src="<%=path %>plugins/imagePreview/imagePreview.js"></script>
<script type="text/javascript" src="<%=path%>plugins/My97DatePicker/WdatePicker.js"></script> 
</body>
</html>
	
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!DOCTYPE html>  
<html lang="en">  
 <head>  
     <title>Login</title>  
     <%@include file="/include/commonCssJs.jsp" %>
     <link rel="stylesheet" type="text/css" href="../css/login.css"/>  
 </head>  
 <body>  
     <div class="index" id="login">  
         <h1>Login</h1>  
         <table id="editTab" cellpadding="5">
	         <input type="text"   required="required" placeholder="用户名" name="loginName"></input>  
	         <input type="password" required="required" placeholder="密码" name="password"></input>  
	         <button class="but" onclick="submit()">登录</button> 
	         <button class="but" onclick="register()">注册</button>
         </table> 
     </div>  
 </body> 
 <script type="text/javascript">
 
 //登录页面
 function submit(){
	var data = DomUtil.getFormData("login");
	MsgUtil.loading();
	AjaxUtil.post(contextPath+"/rest/talk/talkUser/login",data,function(data){
		MsgUtil.unLoading();
		if(data){
			if(data.success == 'Y'){
				MsgUtil.showInfo(data.message);
			}else{
				MsgUtil.showInfo(data.message);
			}
		}else{
			MsgUtil.showError("登录失败！");
		}
	}, function(jqXHR, textStatus, errorMsg) {
		MsgUtil.unLoading();
		MsgUtil.showError("登录失败！");
	});
 }
 
 //注册页面
 function register(){
	 toPage('register');
 }
 
 </script>
</html>
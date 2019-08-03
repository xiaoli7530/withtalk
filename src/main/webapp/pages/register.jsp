<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!DOCTYPE html>  
<html lang="en">  
 <head>  
     <title>注册</title>  
     <%@include file="/include/commonCssJs.jsp" %>
     <link rel="stylesheet" type="text/css" href="../css/login.css"/>  
 </head>  
 <body>  
     <div class="index" id="register">  
         <h1>注册</h1>  
         <table id="editTab" cellpadding="5">
	         <input type="text"   required="required" placeholder="用户名" name="userName"></input>
	         <input type="text"   required="required" placeholder="登录名" name="loginName"></input>  
	         <input type="password" required="required" placeholder="登录密码" name="password"></input>
	         <input type="password" required="required" placeholder="确认密码"></input>    
	         <button class="but" onclick="register()">注册</button>
         </table> 
     </div>  
 </body>
 <script type="text/javascript">
 	function register(){
 		var data = DomUtil.getFormData("register");
 		MsgUtil.loading();
 		AjaxUtil.post(contextPath+"/rest/talk/talkUser/register",data,function(data){
 			MsgUtil.unLoading();
 			if(data && data.success == 'Y'){
 				MsgUtil.showInfo(data.message);
 			}
 		}, function(jqXHR, textStatus, errorMsg) {
 			MsgUtil.unLoading();
 			MsgUtil.showError("注册失败！");
 		});
 	}
 </script>
</html>
<%@page pageEncoding="UTF-8"%>
<%@page language="java" import="java.util.Date,java.text.SimpleDateFormat" %>
<%
    SimpleDateFormat  sdf  = new  SimpleDateFormat("yyyy-MM-dd");
    String curDateStr = sdf.format(new Date());// 取服务器时间
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-control" content="no-cache">
<meta http-equiv="Cache" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">

<script type="text/javascript" src="<%=request.getContextPath() %>/jsdata/dicts.js?v=<%=curDateStr%>"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/pages/wms/app/css/style.css?v=<%=curDateStr%>" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/Insdep/themes/metro/easyui.css?v=<%=curDateStr%>">  
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/Insdep/themes/mobile.css?v=<%=curDateStr%>">  
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/Insdep/themes/icon.css?v=<%=curDateStr%>">  
<script type="text/javascript" src="<%=request.getContextPath() %>/Insdep/jquery.min.js?v=<%=curDateStr%>"></script>  
<script type="text/javascript" src="<%=request.getContextPath() %>/Insdep/jquery.easyui.min.js?v=<%=curDateStr%>"></script> 
<script type="text/javascript" src="<%=request.getContextPath() %>/Insdep/jquery.easyui.mobile.js?v=<%=curDateStr%>"></script> 
<script type="text/javascript" src="<%=request.getContextPath() %>/Insdep/locale/easyui-lang-zh_CN.js?v=<%=curDateStr%>"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/iscroll.js?v=<%=curDateStr%>"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/utils.js?v=<%=curDateStr%>"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/utils_app.js?v=<%=curDateStr%>"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/template-web.js?v=<%=curDateStr%>"></script>

<script type="text/javascript">
	<!--服务器相关参数-->
	var contextPath = '<%=request.getContextPath() %>';
	var curDateStr  = '<%=curDateStr%>';
	var ctfw = {};
</script>


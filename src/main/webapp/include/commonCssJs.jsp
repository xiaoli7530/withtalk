<%@page pageEncoding="UTF-8"%>
<%@page language="java" import="java.util.Date,java.text.SimpleDateFormat" %>
<%
    SimpleDateFormat  sdf  = new  SimpleDateFormat("yyyy-MM-dd");
    String curDateStr = sdf.format(new Date());// 取服务器时间
%>
	<meta http-equiv="X-UA-Compatible" content="IE=11">
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="Expires" content="0">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-control" content="no-cache">
	<meta http-equiv="Cache" content="no-cache">
	
	<meta name="renderer" content="webkit|ie-comp|ie-stand" />  
	
	<script type="text/javascript">
		var contextPath = '<%=request.getContextPath() %>';
		var curDateStr  = '<%=curDateStr%>';
		var ctfw = {};
		
	</script>
	<!-- easy ui css -->
	<link id="easyuiTheme" rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/Insdep/themes/insdep/easyui.css?v=<%=curDateStr%>">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/Insdep/themes/insdep/easyui_animation.css?v=<%=curDateStr%>">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/Insdep/themes/insdep/easyui_plus.css?v=<%=curDateStr%>">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/Insdep/themes/insdep/insdep_theme_default.css?v=<%=curDateStr%>">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/Insdep/themes/insdep/icon.css?v=<%=curDateStr%>">
 	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/Insdep/themes/insdep/easyui-ps-ext.css?v=<%=curDateStr%>">
 	
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/Insdep/plugin/cropper-2.3.4/assets/css/font-awesome.min.css?v=<%=curDateStr%>">
	
	
	<!-- easy ui js -->
	<!-- 数据字典 -->
	<script type="text/javascript" src="<%=request.getContextPath() %>/jsdata/dicts.js?v=<%=curDateStr%>"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/Insdep/jquery.min.js?v=<%=curDateStr%>"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/Insdep/jquery.cookie.js?v=<%=curDateStr%>"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/Insdep/jquery.easyui.min.js?v=<%=curDateStr%>"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/Insdep/themes/insdep/jquery.insdep-extend.min.js?v=<%=curDateStr%>"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/Insdep/locale/easyui-lang-zh_CN.js?v=<%=curDateStr%>"></script>
	
	<script type="text/javascript" src="<%=request.getContextPath() %>/Insdep/jquery.edatagrid.js"></script>
	
	<!-- 框架js -->
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/utils.js?v=<%=curDateStr%>"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/utils_wms.js?v=<%=curDateStr%>"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/utils_app.js?v=<%=curDateStr%>"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/searchbox.js?v=<%=curDateStr%>"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/lodash.core.js?v=<%=curDateStr%>"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery.usercombo.js?v=<%=curDateStr%>"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery.PrintArea.min.js?v=<%=curDateStr%>"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/bpm.js?v=<%=curDateStr%>"></script>
	
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/ajaxfileupload.js"></script>
	
	<!-- 那个界面用到，那个界面就引用 -->
	<%-- <script type="text/javascript" src="<%=request.getContextPath() %>/js/echarts.min.js"></script> --%>
	
	<script type="text/javascript" src="<%=request.getContextPath() %>/Insdep/ajaxfileupload.js?v=<%=curDateStr%>"></script>
	
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/easyuiUtils.js?v=<%=curDateStr%>"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery.ctFilebox.js?v=<%=curDateStr%>"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/datagrid-cellediting.js?v=<%=curDateStr%>"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/template-web.js?v=<%=curDateStr%>"></script>
	<%-- <script type="text/javascript" src="<%=request.getContextPath() %>/js/circleChart.js?v=<%=curDateStr%>"></script> --%>

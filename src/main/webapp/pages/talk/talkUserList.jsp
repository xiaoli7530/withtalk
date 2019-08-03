<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<%@include file="/include/commonCssJs.jsp" %>
		<script type="text/javascript" src="<%=request.getContextPath() %>/pages/report/js/layer/layer.js"></script>
		<title>加载中</title>
	</head>
<body class="easyui-layout" data-options="fit:true">
	<%@include file="/include/header.jsp" %>
	<div data-options="region:'center'" style="height:100%;border:none;" >
		<div class="easyui-layout" fit="true">
		    <div data-options="region:'north'" class="list-header">
				<div>
					<span class="breadcrumb-nav"></span>
					<span style="width:720px;float:right;margin:0 0px">
						<span style="width:120px;">
							<input class="easyui-combobox" id="isActive" name="isActive" value="Y" type="text" 
									data-options="prompt:'是否删除',
											valueField: 'code',
											textField: 'name',
											data: window.dicts['YES_NO']">
							</input>
						</span>	
						<span style="width:120px;margin:0 5px">
							<input class="easyui-datebox" id="createdDate" name="createdDate" type="text"
									data-options="prompt:'创建时间'">
							</input>
						</span>
						
						<span style="float:right;margin:0 5px">
							 <input class="easyui-ctSearcher"  style="width:300px" data-options="
										queryFields:[
																					{label:'主键',field:'userUuid',operator:'contains',type:'string'}
											 ,
											{label:'登录名',field:'loginName',operator:'contains',type:'string'}
											 ,
											{label:'密码',field:'password',operator:'contains',type:'string'}
											 ,
											{label:'用户名',field:'userName',operator:'contains',type:'string'}
											 ,
											{label:'账号类型(普通账号、管理账号)',field:'type',operator:'contains',type:'string'}
											 ,
											{label:'账号状态(正常、锁定)',field:'status',operator:'contains',type:'string'}
											 ,
											{label:'备注',field:'remark',operator:'contains',type:'string'}
											 ,
											{label:'最近登录时间',field:'lastLoginTime',operator:'ge',type:'date',format:'yyyy-MM-dd'}
 ,
											{label:'最近修改密码时间',field:'lastPasswordTime',operator:'ge',type:'date',format:'yyyy-MM-dd'}
 ,
											{label:'密码输入错误的次数',field:'checkErrorTime',operator:'contains',type:'string'}
											 ,
											{label:'备注1',field:'ext1',operator:'contains',type:'string'}
											 ,
											{label:'备注2',field:'ext2',operator:'contains',type:'string'}
											 ,
											{label:'备注3',field:'ext3',operator:'contains',type:'string'}
											 ,
											{label:'备注4',field:'ext4',operator:'contains',type:'string'}
											 ,
											{label:'备注5',field:'ext5',operator:'contains',type:'string'}
											 
											],
										extInputs:[
											{id:'isActive',field:'isActive',operator:'eq',type:'string'},
											{id:'createdDate',field:'createdDate',operator:'ge',type:'date',format:'yyyy-MM-dd'}
											],
										prompt: '请输入查询条件',
										gridId:'dg_talkUser'
										">
						</span>

						
					</span>
				</div>
				<div class="list-toolbar operation">
					<a href="javascript:void(0);" class="easyui-linkbutton" opCode="OP_ADD" onclick="openAdd();">新增</a>
					<a href="javascript:void(0);" class="easyui-linkbutton" opCode="OP_EDIT" onclick="openEdit();">编辑</a>
					<a href="javascript:void(0);" class="easyui-linkbutton" opCode="OP_DEL" onclick="del();">删除</a>
				</div>
			</div>
			<div data-options="region:'center'" class="list-body">
				<table id="dg_talkUser" class="easyui-datagrid" style="width:100%;height:100%;" 
						data-options="idField:'userUuid',rownumbers:true,singleSelect:false,pagination:true,pageSize:20,pageList:[10,20,50,100,200,500],
						onClickRow: DatagridUtil.rowSingleSelect,fitColumns:true,
							loader:DatagridUtil.searchLoader,sortName:'updatedDate',sortOrder:'desc'">
					<thead>
						<tr>
							<th data-options="field:'ck',checkbox:true"></th>
							<th data-options="field:'userUuid',width:'150px'" sortable="true">主键</th>
							<th data-options="field:'loginName',width:'150px'" sortable="true">登录名</th>
							<th data-options="field:'password',width:'150px'" sortable="true">密码</th>
							<th data-options="field:'userName',width:'150px'" sortable="true">用户名</th>
							<th data-options="field:'type',width:'150px'" sortable="true">账号类型(普通账号、管理账号)</th>
							<th data-options="field:'status',width:'150px'" sortable="true">账号状态(正常、锁定)</th>
							<th data-options="field:'remark',width:'150px'" sortable="true">备注</th>
							<th data-options="field:'lastLoginTime',width:'150px'" sortable="true">最近登录时间</th>
							<th data-options="field:'lastPasswordTime',width:'150px'" sortable="true">最近修改密码时间</th>
							<th data-options="field:'checkErrorTime',width:'150px'" sortable="true">密码输入错误的次数</th>
							<th data-options="field:'ext1',width:'150px'" sortable="true">备注1</th>
							<th data-options="field:'ext2',width:'150px'" sortable="true">备注2</th>
							<th data-options="field:'ext3',width:'150px'" sortable="true">备注3</th>
							<th data-options="field:'ext4',width:'150px'" sortable="true">备注4</th>
							<th data-options="field:'ext5',width:'150px'" sortable="true">备注5</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
	function openAdd(){
		WindowUtil.open(contextPath+'/p/talk/talkUserAddAndEdit', '新增',{close:function(){
			$("#dg_talkUser").datagrid("reload");
		}});
	}
	
	function openEdit(){
		DatagridUtil.singleRowOps("dg_talkUser",function(row){
			WindowUtil.open(contextPath+'/p/talk/talkUserAddAndEdit?userUuid='+row.userUuid, '编辑',{close:function(){
			$("#dg_talkUser").datagrid("reload");
		}});
		});
	}
	
	function del(){
		var rows = $("#dg_talkUser").datagrid("getSelections");
		var ids = [];
		if(rows.length>0){
			for(var i=0; i<rows.length; i++){
				ids.push(rows[i].userUuid);
			}
		}else{
			MsgUtil.showError("请先选择至少一条数据");
			return;
		}
		MsgUtil.confirm('删除', '您确认要执行删除操作吗?', function(r){
			if (r){
				MsgUtil.loading();
				AjaxUtil.del(contextPath+"/rest/talk/talkUser/deleteList",ids,function(data){
					MsgUtil.unLoading();
					MsgUtil.showInfo('删除成功！');
					$("#dg_talkUser").datagrid('unselectAll'); 
					$("#dg_talkUser").datagrid("reload");
				}, function(jqXHR, textStatus, errorMsg) {
					MsgUtil.unLoading();
					MsgUtil.showError('删除失败！');
				});
			}
		});
	}
	
	$(function() {
		
		var opts = $("#dg_talkUser").datagrid("options");
		opts.url = contextPath+'/rest/talk/talkUser/list';
		$('.easyui-ctSearcher').ctSearcher('doSearch');		
		DatagridUtil.initPagination("dg_talkUser");
		
		RoleManager.authRole();
	});
</script>
</html>

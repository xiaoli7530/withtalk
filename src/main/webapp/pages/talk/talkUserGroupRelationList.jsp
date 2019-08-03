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
																					{label:'group主键',field:'groupUuid',operator:'contains',type:'string'}
											 ,
											{label:'群等级',field:'groupLevel',operator:'contains',type:'string'}
											 ,
											{label:'建立联系时间',field:'relationStart',operator:'ge',type:'date',format:'yyyy-MM-dd'}
 ,
											{label:'结束联系时间',field:'relationEnd',operator:'ge',type:'date',format:'yyyy-MM-dd'}
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
											 ,
											{label:'主键',field:'userGroupRelationUuis',operator:'contains',type:'string'}
											 ,
											{label:'user主键',field:'userUuid',operator:'contains',type:'string'}
											 
											],
										extInputs:[
											{id:'isActive',field:'isActive',operator:'eq',type:'string'},
											{id:'createdDate',field:'createdDate',operator:'ge',type:'date',format:'yyyy-MM-dd'}
											],
										prompt: '请输入查询条件',
										gridId:'dg_talkUserGroupRelation'
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
				<table id="dg_talkUserGroupRelation" class="easyui-datagrid" style="width:100%;height:100%;" 
						data-options="idField:'userGroupRelationUuis',rownumbers:true,singleSelect:false,pagination:true,pageSize:20,pageList:[10,20,50,100,200,500],
						onClickRow: DatagridUtil.rowSingleSelect,fitColumns:true,
							loader:DatagridUtil.searchLoader,sortName:'updatedDate',sortOrder:'desc'">
					<thead>
						<tr>
							<th data-options="field:'ck',checkbox:true"></th>
							<th data-options="field:'groupUuid',width:'150px'" sortable="true">group主键</th>
							<th data-options="field:'groupLevel',width:'150px'" sortable="true">群等级</th>
							<th data-options="field:'relationStart',width:'150px'" sortable="true">建立联系时间</th>
							<th data-options="field:'relationEnd',width:'150px'" sortable="true">结束联系时间</th>
							<th data-options="field:'ext1',width:'150px'" sortable="true">备注1</th>
							<th data-options="field:'ext2',width:'150px'" sortable="true">备注2</th>
							<th data-options="field:'ext3',width:'150px'" sortable="true">备注3</th>
							<th data-options="field:'ext4',width:'150px'" sortable="true">备注4</th>
							<th data-options="field:'ext5',width:'150px'" sortable="true">备注5</th>
							<th data-options="field:'userGroupRelationUuis',width:'150px'" sortable="true">主键</th>
							<th data-options="field:'userUuid',width:'150px'" sortable="true">user主键</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
	function openAdd(){
		WindowUtil.open(contextPath+'/p/talk/talkUserGroupRelationAddAndEdit', '新增',{close:function(){
			$("#dg_talkUserGroupRelation").datagrid("reload");
		}});
	}
	
	function openEdit(){
		DatagridUtil.singleRowOps("dg_talkUserGroupRelation",function(row){
			WindowUtil.open(contextPath+'/p/talk/talkUserGroupRelationAddAndEdit?userGroupRelationUuis='+row.userGroupRelationUuis, '编辑',{close:function(){
			$("#dg_talkUserGroupRelation").datagrid("reload");
		}});
		});
	}
	
	function del(){
		var rows = $("#dg_talkUserGroupRelation").datagrid("getSelections");
		var ids = [];
		if(rows.length>0){
			for(var i=0; i<rows.length; i++){
				ids.push(rows[i].userGroupRelationUuis);
			}
		}else{
			MsgUtil.showError("请先选择至少一条数据");
			return;
		}
		MsgUtil.confirm('删除', '您确认要执行删除操作吗?', function(r){
			if (r){
				MsgUtil.loading();
				AjaxUtil.del(contextPath+"/rest/talk/talkUserGroupRelation/deleteList",ids,function(data){
					MsgUtil.unLoading();
					MsgUtil.showInfo('删除成功！');
					$("#dg_talkUserGroupRelation").datagrid('unselectAll'); 
					$("#dg_talkUserGroupRelation").datagrid("reload");
				}, function(jqXHR, textStatus, errorMsg) {
					MsgUtil.unLoading();
					MsgUtil.showError('删除失败！');
				});
			}
		});
	}
	
	$(function() {
		
		var opts = $("#dg_talkUserGroupRelation").datagrid("options");
		opts.url = contextPath+'/rest/talk/talkUserGroupRelation/list';
		$('.easyui-ctSearcher').ctSearcher('doSearch');		
		DatagridUtil.initPagination("dg_talkUserGroupRelation");
		
		RoleManager.authRole();
	});
</script>
</html>

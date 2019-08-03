<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="easyui-layout" data-options="fit:true">
	<div data-options="region:'north'" style="height:40px" class="button_div">
		<a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:void(0)" onclick="submitForm(this);">保存</a>
		<a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" onclick="closeWindow(this);">关闭</a>
	</div>
	<div data-options="region:'center'" class="form_div">
    	<table id="editTab" cellpadding="5">
    		    		<tr>
    			<td>group主键:</td>
    			<td>
    				<input class="easyui-textbox" type="text" name="groupUuid"></input>
    			</td>
    		</tr>
    		<tr>
    			<td>群等级:</td>
    			<td>
    				<input class="easyui-textbox" type="text" name="groupLevel"></input>
    			</td>
    		</tr>
    		<tr>
    			<td>建立联系时间:</td>
    			<td>
    				<input class="easyui-textbox" type="text" name="relationStart"></input>
    			</td>
    		</tr>
    		<tr>
    			<td>结束联系时间:</td>
    			<td>
    				<input class="easyui-textbox" type="text" name="relationEnd"></input>
    			</td>
    		</tr>
    		<tr>
    			<td>备注1:</td>
    			<td>
    				<input class="easyui-textbox" type="text" name="ext1"></input>
    			</td>
    		</tr>
    		<tr>
    			<td>备注2:</td>
    			<td>
    				<input class="easyui-textbox" type="text" name="ext2"></input>
    			</td>
    		</tr>
    		<tr>
    			<td>备注3:</td>
    			<td>
    				<input class="easyui-textbox" type="text" name="ext3"></input>
    			</td>
    		</tr>
    		<tr>
    			<td>备注4:</td>
    			<td>
    				<input class="easyui-textbox" type="text" name="ext4"></input>
    			</td>
    		</tr>
    		<tr>
    			<td>备注5:</td>
    			<td>
    				<input class="easyui-textbox" type="text" name="ext5"></input>
    			</td>
    		</tr>
    		<tr>
    			<td>主键:</td>
    			<td>
    				<input class="easyui-textbox" type="text" name="userGroupRelationUuis"></input>
    			</td>
    		</tr>
    		<tr>
    			<td>user主键:</td>
    			<td>
    				<input class="easyui-textbox" type="text" name="userUuid"></input>
    			</td>
    		</tr>
    	</table>
	</div>
</div>
<script type="text/javascript">
$(function() {
	if('${param.userGroupRelationUuis}'){
		AjaxUtil.get(contextPath+"/rest/talk/talkUserGroupRelation/get?userGroupRelationUuis=${param.userGroupRelationUuis}",function(data){
			$('#editTab').form('load',data);
		});
	}	
});

function submitForm(el){
	if($("#editTab").form("validate")){
		var data = DomUtil.getFormData("editTab");
		MsgUtil.loading();
		var subUrl = contextPath+"/rest/talk/talkUserGroupRelation/create";
		if(data.userGroupRelationUuis){//todo  判断主键是否有值(记得把主键放在form里,type=hidden)
			subUrl = contextPath+"/rest/talk/talkUserGroupRelation/update";
		}
		AjaxUtil.post(subUrl,data,function(data){
			MsgUtil.unLoading();
			MsgUtil.showInfo("保存成功！");
			WindowUtil.close(el);
		}, function(jqXHR, textStatus, errorMsg) {
			MsgUtil.unLoading();
			console.log(errorMsg);
			MsgUtil.showError("保存失败！");
		});
	}
}

function closeWindow(el){
	WindowUtil.close(el);
}
</script>


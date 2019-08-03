<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="easyui-layout" data-options="fit:true">
	<div data-options="region:'north'" style="height:40px" class="button_div">
		<a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:void(0)" onclick="submitForm(this);">保存</a>
		<a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" onclick="closeWindow(this);">关闭</a>
	</div>
	<div data-options="region:'center'" class="form_div">
    	<table id="addTab" class="input-table">
    	     		<tr>
    			<td>主键:</td>    			
    		</tr>
    		<tr>    			
    			<td>
    				<input class="easyui-textbox" type="text" name="groupUuid"></input>
    			</td>
    		</tr>
    		<tr>
    			<td>群名字:</td>    			
    		</tr>
    		<tr>    			
    			<td>
    				<input class="easyui-textbox" type="text" name="groupName"></input>
    			</td>
    		</tr>
    		<tr>
    			<td>创建人user_uuid:</td>    			
    		</tr>
    		<tr>    			
    			<td>
    				<input class="easyui-textbox" type="text" name="groupCreaterUuid"></input>
    			</td>
    		</tr>
    		<tr>
    			<td>创建人名字:</td>    			
    		</tr>
    		<tr>    			
    			<td>
    				<input class="easyui-textbox" type="text" name="groupCreateTime"></input>
    			</td>
    		</tr>
    		<tr>
    			<td>群介绍:</td>    			
    		</tr>
    		<tr>    			
    			<td>
    				<input class="easyui-textbox" type="text" name="groupIntroduction"></input>
    			</td>
    		</tr>
    		<tr>
    			<td>群可以容纳人的总数:</td>    			
    		</tr>
    		<tr>    			
    			<td>
    				<input class="easyui-textbox" type="text" name="groupTotalCount"></input>
    			</td>
    		</tr>
    		<tr>
    			<td>群人数的数量:</td>    			
    		</tr>
    		<tr>    			
    			<td>
    				<input class="easyui-textbox" type="text" name="groupUserCount"></input>
    			</td>
    		</tr>
    		<tr>
    			<td>群管理员user_uuid:</td>    			
    		</tr>
    		<tr>    			
    			<td>
    				<input class="easyui-textbox" type="text" name="groupManagementUuid"></input>
    			</td>
    		</tr>
    		<tr>
    			<td>群管理员名字:</td>    			
    		</tr>
    		<tr>    			
    			<td>
    				<input class="easyui-textbox" type="text" name="groupManagementName"></input>
    			</td>
    		</tr>
    		<tr>
    			<td>群主user_uuid:</td>    			
    		</tr>
    		<tr>    			
    			<td>
    				<input class="easyui-textbox" type="text" name="groupMasterUuid"></input>
    			</td>
    		</tr>
    		<tr>
    			<td>群主名字:</td>    			
    		</tr>
    		<tr>    			
    			<td>
    				<input class="easyui-textbox" type="text" name="groupMasterName"></input>
    			</td>
    		</tr>
    		<tr>
    			<td>备注1:</td>    			
    		</tr>
    		<tr>    			
    			<td>
    				<input class="easyui-textbox" type="text" name="ext1"></input>
    			</td>
    		</tr>
    		<tr>
    			<td>备注2:</td>    			
    		</tr>
    		<tr>    			
    			<td>
    				<input class="easyui-textbox" type="text" name="ext2"></input>
    			</td>
    		</tr>
    		<tr>
    			<td>备注3:</td>    			
    		</tr>
    		<tr>    			
    			<td>
    				<input class="easyui-textbox" type="text" name="ext3"></input>
    			</td>
    		</tr>
    		<tr>
    			<td>备注4:</td>    			
    		</tr>
    		<tr>    			
    			<td>
    				<input class="easyui-textbox" type="text" name="ext4"></input>
    			</td>
    		</tr>
    		<tr>
    			<td>备注5:</td>    			
    		</tr>
    		<tr>    			
    			<td>
    				<input class="easyui-textbox" type="text" name="ext5"></input>
    			</td>
    		</tr>
    	</table>
	</div>
</div>
<script type="text/javascript">
function submitForm(el){
	if($("#addTab").form("validate")){
		var data = DomUtil.getFormData("addTab");
		MsgUtil.loading();
		AjaxUtil.post(contextPath+"/rest/talk/talkGroup/create",data,function(data){
			MsgUtil.unLoading();
			MsgUtil.showInfo("保存成功！");
			WindowUtil.close(el);			
		}, function(jqXHR, textStatus, errorMsg) {
			MsgUtil.unLoading();
			MsgUtil.showError("保存失败！");
		});
	}
}

function closeWindow(el){
	WindowUtil.close(el);
}
</script>


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
    				<input class="easyui-textbox" type="text" name="userRelationUuid"></input>
    			</td>
    		</tr>
    		<tr>
    			<td>talk_user主键Y:</td>    			
    		</tr>
    		<tr>    			
    			<td>
    				<input class="easyui-textbox" type="text" name="userIdY"></input>
    			</td>
    		</tr>
    		<tr>
    			<td>talk_user主键M:</td>    			
    		</tr>
    		<tr>    			
    			<td>
    				<input class="easyui-textbox" type="text" name="userIdM"></input>
    			</td>
    		</tr>
    		<tr>
    			<td>建立联系时间:</td>    			
    		</tr>
    		<tr>    			
    			<td>
    				<input class="easyui-textbox" type="text" name="relationStart"></input>
    			</td>
    		</tr>
    		<tr>
    			<td>删除联系时间:</td>    			
    		</tr>
    		<tr>    			
    			<td>
    				<input class="easyui-textbox" type="text" name="relationEnd"></input>
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
		AjaxUtil.post(contextPath+"/rest/talk/talkUserRelation/create",data,function(data){
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


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
    				<input class="easyui-textbox" type="text" name="messageUuid"></input>
    			</td>
    		</tr>
    		<tr>
    			<td>发送人talk_user主键:</td>    			
    		</tr>
    		<tr>    			
    			<td>
    				<input class="easyui-textbox" type="text" name="sendUserUuid"></input>
    			</td>
    		</tr>
    		<tr>
    			<td>接收人talk_user主键:</td>    			
    		</tr>
    		<tr>    			
    			<td>
    				<input class="easyui-textbox" type="text" name="receiveUserUuid"></input>
    			</td>
    		</tr>
    		<tr>
    			<td>消息内容:</td>    			
    		</tr>
    		<tr>    			
    			<td>
    				<input class="easyui-textbox" type="text" name="messageContent"></input>
    			</td>
    		</tr>
    		<tr>
    			<td>发送时间:</td>    			
    		</tr>
    		<tr>    			
    			<td>
    				<input class="easyui-textbox" type="text" name="sendTime"></input>
    			</td>
    		</tr>
    		<tr>
    			<td>是否有效(Y:有效；N:无效):</td>    			
    		</tr>
    		<tr>    			
    			<td>
    				<input class="easyui-textbox" type="text" name="isSend"></input>
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
		AjaxUtil.post(contextPath+"/rest/talk/talkMessage/create",data,function(data){
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


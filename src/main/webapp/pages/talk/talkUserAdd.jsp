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
    				<input class="easyui-textbox" type="text" name="userUuid"></input>
    			</td>
    		</tr>
    		<tr>
    			<td>登录名:</td>    			
    		</tr>
    		<tr>    			
    			<td>
    				<input class="easyui-textbox" type="text" name="loginName"></input>
    			</td>
    		</tr>
    		<tr>
    			<td>密码:</td>    			
    		</tr>
    		<tr>    			
    			<td>
    				<input class="easyui-textbox" type="text" name="password"></input>
    			</td>
    		</tr>
    		<tr>
    			<td>用户名:</td>    			
    		</tr>
    		<tr>    			
    			<td>
    				<input class="easyui-textbox" type="text" name="userName"></input>
    			</td>
    		</tr>
    		<tr>
    			<td>账号类型(普通账号、管理账号):</td>    			
    		</tr>
    		<tr>    			
    			<td>
    				<input class="easyui-textbox" type="text" name="type"></input>
    			</td>
    		</tr>
    		<tr>
    			<td>账号状态(正常、锁定):</td>    			
    		</tr>
    		<tr>    			
    			<td>
    				<input class="easyui-textbox" type="text" name="status"></input>
    			</td>
    		</tr>
    		<tr>
    			<td>备注:</td>    			
    		</tr>
    		<tr>    			
    			<td>
    				<input class="easyui-textbox" type="text" name="remark"></input>
    			</td>
    		</tr>
    		<tr>
    			<td>最近登录时间:</td>    			
    		</tr>
    		<tr>    			
    			<td>
    				<input class="easyui-textbox" type="text" name="lastLoginTime"></input>
    			</td>
    		</tr>
    		<tr>
    			<td>最近修改密码时间:</td>    			
    		</tr>
    		<tr>    			
    			<td>
    				<input class="easyui-textbox" type="text" name="lastPasswordTime"></input>
    			</td>
    		</tr>
    		<tr>
    			<td>密码输入错误的次数:</td>    			
    		</tr>
    		<tr>    			
    			<td>
    				<input class="easyui-textbox" type="text" name="checkErrorTime"></input>
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
		AjaxUtil.post(contextPath+"/rest/talk/talkUser/create",data,function(data){
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


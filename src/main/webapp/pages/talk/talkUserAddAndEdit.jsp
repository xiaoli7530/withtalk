<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="easyui-layout" data-options="fit:true">
	<div data-options="region:'north'" style="height:40px" class="button_div">
		<a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:void(0)" onclick="submitForm(this);">保存</a>
		<a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" onclick="closeWindow(this);">关闭</a>
	</div>
	<div data-options="region:'center'" class="form_div">
    	<table id="editTab" cellpadding="5">
    		    		<tr>
    			<td>主键:</td>
    			<td>
    				<input class="easyui-textbox" type="text" name="userUuid"></input>
    			</td>
    		</tr>
    		<tr>
    			<td>登录名:</td>
    			<td>
    				<input class="easyui-textbox" type="text" name="loginName"></input>
    			</td>
    		</tr>
    		<tr>
    			<td>密码:</td>
    			<td>
    				<input class="easyui-textbox" type="text" name="password"></input>
    			</td>
    		</tr>
    		<tr>
    			<td>用户名:</td>
    			<td>
    				<input class="easyui-textbox" type="text" name="userName"></input>
    			</td>
    		</tr>
    		<tr>
    			<td>账号类型(普通账号、管理账号):</td>
    			<td>
    				<input class="easyui-textbox" type="text" name="type"></input>
    			</td>
    		</tr>
    		<tr>
    			<td>账号状态(正常、锁定):</td>
    			<td>
    				<input class="easyui-textbox" type="text" name="status"></input>
    			</td>
    		</tr>
    		<tr>
    			<td>备注:</td>
    			<td>
    				<input class="easyui-textbox" type="text" name="remark"></input>
    			</td>
    		</tr>
    		<tr>
    			<td>最近登录时间:</td>
    			<td>
    				<input class="easyui-textbox" type="text" name="lastLoginTime"></input>
    			</td>
    		</tr>
    		<tr>
    			<td>最近修改密码时间:</td>
    			<td>
    				<input class="easyui-textbox" type="text" name="lastPasswordTime"></input>
    			</td>
    		</tr>
    		<tr>
    			<td>密码输入错误的次数:</td>
    			<td>
    				<input class="easyui-textbox" type="text" name="checkErrorTime"></input>
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
    	</table>
	</div>
</div>
<script type="text/javascript">
$(function() {
	if('${param.userUuid}'){
		AjaxUtil.get(contextPath+"/rest/talk/talkUser/get?userUuid=${param.userUuid}",function(data){
			$('#editTab').form('load',data);
		});
	}	
});

function submitForm(el){
	if($("#editTab").form("validate")){
		var data = DomUtil.getFormData("editTab");
		MsgUtil.loading();
		var subUrl = contextPath+"/rest/talk/talkUser/create";
		if(data.userUuid){//todo  判断主键是否有值(记得把主键放在form里,type=hidden)
			subUrl = contextPath+"/rest/talk/talkUser/update";
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


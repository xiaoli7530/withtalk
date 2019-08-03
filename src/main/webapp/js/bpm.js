/**
 * bpm相关js
 * @author dais
 * */
var bpm = {};
//配置审核页面或查看页面
var urlMapping4Check = {
		'bsd':{url:'ps/bsd/psPpBsdCheck'},
		'issue':{url:'/p/ps/issue/psPpIssueCheck'},
		'smartNoChg':{url:'ps/sync/psBuildReqCheck'},
		'BUILDBOOK_KEY':{url:'ps/psBbRecordCheck'}
	};	

/**
 * 启动流程
 * variables:{
 * 		key:必填，流程定义ID，
 * 		businessKey:必填，业务单据号
 * 		opType:流程分支走向变量
 * 		selectPersonTypes:选人类型，默认ALL
 * 		callBack:后台service业务回调方法
 * 		callbackData:后台service业务回调方法参数数据
 * 		comment:审核意见（默认为“启动流程”）
 * 		// 其他字段视业务情况增加
 * }
 * 
 * success:前台回调方法
 */
bpm.startProcess = function(variables, success){
	if(!variables){
		MsgUtil.showError('variables不能为空！');
		return;
	}
	if(StringUtil.isEmpty(variables.key)) {
		MsgUtil.showError('variables.key不能为空！');
		return;
	}
	if(StringUtil.isEmpty(variables.businessKey)) {
		MsgUtil.showError('variables.businessKey不能为空！');
		return;
	}
	if(!variables.selectPersonTypes) {
		variables.selectPersonTypes = 'ALL';
	}
	if(!variables.comment) {
		variables.comment = '启动流程';
	}
	if(!variables.callBack){
		variables.callBack = '';
	}
	MsgUtil.loading();
	var url = contextPath+"/bpm/startProcess?key=" + variables.key 
		+ "&businessKey=" + variables.businessKey 
		+ "&callBack=" + variables.callBack;
	variables.bpm_comments = variables.comment;
	
	AjaxUtil.post(url, variables, function(data){
		MsgUtil.unLoading();
		MsgUtil.showInfo("流程启动成功！");
		if(success){
			success.call(this,data);
		}
	});
}

/**
 * 撤销
 */
bpm.undoProcess = function(processInstanceId,comment,callBack,variables,callbackData,success){
	if(!callBack){
		callBack = '';
	}
	if(!variables){
		variables = {};
	}
	MsgUtil.loading();
	var url = contextPath+"/bpm/undoProcess?processInstanceId="+processInstanceId + "&comment=" + comment +"&callBack=" + callBack;
	variables.callbackData = callbackData;
	variables.bpm_comments = comment;
	AjaxUtil.post(url,variables,function(data){
		MsgUtil.unLoading();
		MsgUtil.showInfo("退回成功！");
		if(success){
			success.call(this,data);
		}
	});
}

/**
 * 拒绝
 */
bpm.refuseProcess = function(processInstanceId,comment,callBack,variables,callbackData,success){
	if(!callBack){
		callBack = '';
	}
	if(!variables){
		variables = {};
	}
	MsgUtil.loading();
	var url = contextPath+"/bpm/refuseProcess?processInstanceId="+processInstanceId + "&comment=" + comment +"&callBack=" + callBack;
	variables.callbackData = callbackData;
	variables.bpm_comments = comment;
	AjaxUtil.post(url,variables,function(data){
		MsgUtil.unLoading();
		MsgUtil.showInfo("拒绝成功！");
		if(success){
			success.call(this,data);
		}
	});
}

/**
 * 撤回
 */
bpm.toBack = function(processInstanceId,comment,callBack,variables,callbackData,success){
	if(!callBack){
		callBack = '';
	}
	if(!variables){
		variables = {};
	}
	MsgUtil.loading();
	var url = contextPath+"/bpm/toBack?processInstanceId="+processInstanceId + "&comment=" + comment +"&callBack=" + callBack;
	variables.callbackData = callbackData;
	variables.bpm_comments = comment;
	AjaxUtil.post(url,variables,function(data){
		MsgUtil.unLoading();
		MsgUtil.showInfo("撤回成功！");
		if(success){
			success.call(this,data);
		}
	});
}
/**
 * 退回
 * 
 */
bpm.returnTask = function(taskId,comment,callBack,variables,callbackData,success){
	if(!callBack){
		callBack = '';
	}
	if(!variables){
		variables = {};
	}
	MsgUtil.loading();
	variables.bpm_comments = comment;
	variables.callbackData = callbackData;
	var url = contextPath+"/bpm/returnTask?taskId="+taskId + "&comment=" + comment +"&callBack=" + callBack;
	AjaxUtil.post(url,variables,function(data){
		MsgUtil.unLoading();
		MsgUtil.showInfo("退回成功！");
		if(success){
			success.call(this,data);
		}
	});
}


/**
 * 审核任务，完成当前任务并流转到下一个节点（若没有下一个节点则结束流程）
 * variables:{
 * 		taskId:必填，流程定义ID，
 * 		opType:流程分支走向变量
 * 		callBack:后台service业务回调方法
 * 		callbackData:后台service业务回调方法参数数据
 * 		comment:审核意见
 * 		// 其他字段视业务情况增加
 * }
 * 
 * success:前台回调方法
 */
bpm.completeTask = function(variables, success){
	if(!$("#setCherkerForm").form("validate")) {
		return;
	}
	if(!variables){
		MsgUtil.showError('variables不能为空！');
		return;
	}
	if(StringUtil.isEmpty(variables.taskId)) {
		MsgUtil.showError('variables.taskId不能为空！');
		return;
	}
	if(!variables.callBack){
		variables.callBack = '';
	}
	MsgUtil.loading();
	variables.bpm_comments = variables.comment;
	var url = contextPath+"/bpm/completeTask?taskId="+ variables.taskId + "&comment=" + variables.comment +"&callBack=" + variables.callBack;
	AjaxUtil.post(url,variables,function(data){
		MsgUtil.unLoading();
		MsgUtil.showInfo("流程审核成功！");
		if(success){
			success.call(this,data);
		}
	});
}

bpm.delegateProcess = function(taskId, assignee, comment, success) {
	MsgUtil.loading();
	var url = contextPath+"/bpm/delegate";
	var variables = {
			taskId: taskId,
			assignee: assignee,
			comment: comment
	};
	AjaxUtil.post(url,variables,function(data){
		MsgUtil.unLoading();
		if(data.tag == 'fail') {
			MsgUtil.showError(data.msg);
		} else {
			MsgUtil.showInfo("流程转发成功！");
			if(success){
				success.call(this,data);
			}
		}
		
	});
}

/**
 * 关闭流程
 * String processInstanceId,String comment,String callBack,@RequestBody Map<String,Object> variables
 */
bpm.closeProcess = function(processInstanceId,comment,callBack,callbackData,success){
	if(!callBack){
		callBack = '';
	}
	if(!callbackData){
		callbackData = {};
	}
	MsgUtil.loading();
	callbackData.bpm_comments = comment;
	var url = contextPath+"/bpm/closeProcess?processInstanceId="+processInstanceId + "&comment=" + comment +"&callBack=" + callBack;
	AjaxUtil.post(url,callbackData,function(data){
		MsgUtil.unLoading();
		MsgUtil.showInfo("流程关闭成功！");
		if(success){
			success.call(this,data);
		}
	});
}


/**
 * 标记task读取
 */
bpm.taskRead = function(taskId,success){
	//MsgUtil.loading();
	var url = contextPath+"/bpm/taskRead?taskId="+taskId;
	AjaxUtil.post(url,{},function(data){
		//MsgUtil.unLoading();
		//MsgUtil.showInfo("待办标记成功！");
		if(success){
			success.call(this,data);
		}
	});
}
/**
 * 
 * @parma processDefinitionId 流程定义ID
 * 
 * @parma formData 流程定义ID
 * 
 */
bpm.selectNodeUser = function (processDefinitionKey,formData,option){	
	if(formData == null){
		formData = {};
	}	
	if(option == null){
		option = {};
	}
	formData.processDefinitionKey = processDefinitionKey;
	formData.selectPersonTypes = "all";	
	parent.window.selectUserGlobalVar= formData;
	
	WindowUtil.open(contextPath + '/p/bpm/popSetChecker', '选择审核人',{
		width:option.width?option.width:500,
		height:option.height?option.height:400
	});
	
}
/**
 * 获取开始节点，下一个用户节点用户
 */
bpm.getStartNextNodeEmps = function(processDefinitionId,businessData,success){
	MsgUtil.loading();
	AjaxUtil.post(contextPath+"/bpm/form/getStartNextNodeEmps?processDefinitionId="+processDefinitionId,businessData,function(data){
		MsgUtil.unLoading();
		MsgUtil.showInfo("获取下一个用户节点用户成功！");
		if(success){
			success.call(this,data);
		}
	}, function(e) {
		MsgUtil.unLoading();
		MsgUtil.showError("获取下一个用户节点用户失败！",e);
	});
}
/**
 * 获取流程启动表单
 * @param processDefinitionId 流程定义ID
 * @param businessData 业务数据
 * @param success(流程定义id,输出html,返回的form信息) 请求成功后回调方法
 */
bpm.getStartForm = function(processDefinitionId,businessData,success){
	MsgUtil.loading();
	AjaxUtil.post(contextPath + '/bpm/form/get-form/start/' + processDefinitionId,businessData,function(data){
		MsgUtil.unLoading();
		var html = "";
		if(data.outForm){
			html = data.form;
		}else{
			html = bpm.renderDynamicForm(data.dynamicFormProperties);
		}
		success.call(this,processDefinitionId,html,data);
	},function(e){
		MsgUtil.unLoading();
		MsgUtil.showError("获取流程表单信息失败!");
	});
};
/**
 * 获取流程任务表单
 * @param processDefinitionId 流程定义ID
 * @param success(流程定义id,输出html,返回的form信息) 请求成功后回调方法
 */
bpm.getTaskForm = function(taskId,success){
	MsgUtil.loading();
	AjaxUtil.get(contextPath + '/bpm/form/get-form/task/' + taskId,function(data){
		MsgUtil.unLoading();
		var html = "";
		if(data.outForm){
			//html = data.form;
		}else{
			//html = bpm.renderDynamicForm(data.dynamicFormProperties);
		}
		success.call(this,taskId,html,data);
	},function(e){
		MsgUtil.unLoading();
		MsgUtil.showError("获取流程表单信息失败!");
	});
};

bpm.getProcessComments = function(processInstanceId,success){
	AjaxUtil.post(contextPath + '/bpm/history/getProcessComments?processInstanceId='+processInstanceId,{},function(data){
		if(success){
			success.call(this,data);
		}		
	},function(e){
		MsgUtil.showError("获取流程审核信息失败!");
	});
}
bpm.getHistoryTaskForm = function(param,success){
	MsgUtil.loading();
	AjaxUtil.post(contextPath + '/bpm/form/getHistoryStartForm',param,function(data){
		MsgUtil.unLoading();
		var html = "";
		if(data.outForm){
			html = data.form;
		}else{
			html = bpm.renderDynamicForm(data.dynamicFormProperties);
		}
		success.call(this,html,data);
	},function(e){
		MsgUtil.unLoading();
		MsgUtil.showError("获取流程表单信息失败!");
	});
};
/**
 * 用户签收
 * @param taskId 任务id
 */
bpm.claim = function(taskId,success){
	MsgUtil.loading();
	AjaxUtil.get(contextPath + '/bpm/userTask/task/claim/' + taskId,function(data){
		MsgUtil.unLoading();
		MsgUtil.showInfo('签收成功!');
		success.call(this,data);
	},function(e){
		MsgUtil.unLoading();
		MsgUtil.showError('签收成功!');
	});
}
/**
 * 办理任务
 * @param taskId 任务id
 * @param formData 表单数据
 * @param success 办理成功后调用
 */
bpm.handle = function(taskId,formData,success){
	MsgUtil.loading();
	AjaxUtil.post(contextPath + '/bpm/form/completeTask/',formData,function(data){
		MsgUtil.unLoading();
		MsgUtil.showInfo('办理成功!');
		if(success){
			success.call(this,taskId,data);
		}
	},function(){
		MsgUtil.unLoading();
		MsgUtil.showError('办理失败!');
	})
}
bpm.reject = function(processInstanceId,taskId,rejectMessage,formData,success){
	var data_ = {
		processInstanceId:processInstanceId,
		taskId:taskId,
		rejectMessage:rejectMessage,
		formData:formData
	}
	MsgUtil.loading();
	AjaxUtil.post(contextPath + '/bpm/userTask/rejectTask/',data_,function(data){
		MsgUtil.unLoading();
		MsgUtil.showInfo('办理成功!');
		if(success){
			success.call(this,taskId,data);
		}
	},function(){
		MsgUtil.unLoading();
		MsgUtil.showError('办理失败!');
	})
}
/**
 * 激活流程实例
 */
bpm.activeProcessInstance=function(processInstanceId,success){
	AjaxUtil.get(contextPath + '/bpm/processinstance/update/active/' + processInstanceId,function(data){
		MsgUtil.unLoading();
		MsgUtil.showInfo('激活成功!');
		if(success){
			success.call(this,processInstanceId,data);
		}
	},function(){
		MsgUtil.unLoading();
		MsgUtil.showError('激活失败!');
	})
}
/**
 * 挂起流程实例
 */
bpm.suspendProcessInstance=function(processInstanceId,success){
	AjaxUtil.get(contextPath + '/bpm/processinstance/update/suspend/' + processInstanceId,function(data){
		MsgUtil.unLoading();
		MsgUtil.showInfo('挂起成功!');
		if(success){
			success.call(this,processInstanceId,data);
		}
	},function(){
		MsgUtil.unLoading();
		MsgUtil.showError('挂起失败!');
	})
}
/**
 * 根据流程实例id查询流程历史任务
 * @param processInstanceId
 * @param success 查询成功后调用
 */
bpm.queryHistoryTaskByProcessInstanceId = function(processInstanceId,success){
	MsgUtil.loading();
	AjaxUtil.get(contextPath + '/bpm/history/queryHistoryTaskByProcessInstanceId/' + processInstanceId,function(data){
		MsgUtil.unLoading();
		if(success){
			success.call(this,data);
		}
	},function(){
		MsgUtil.unLoading();
		MsgUtil.showError('查询历史数据失败!');
	})
}

/**
 * 渲染动态表单
 */
bpm.renderDynamicForm = function(dynamicFormPropertie){
	if(!dynamicFormPropertie || dynamicFormPropertie.length < 1){
		return "";
	}
	var trs = "<table>";
	$.each(dynamicFormPropertie, function() {
		var className = this.required === true ? "required" : "";
		this.value = this.value ? this.value : "";
		trs += "<tr>" + bpm.createFieldHtml(this, dynamicFormPropertie, className)
		if (this.required === true) {
			trs += "<span style='color:red'>*</span>";
		}
		trs += "</td></tr>";
	});
	trs +="</table>";
	return trs;
}
/**
 * 创建字段
 */
bpm.createFieldHtml = function(prop, className) {
	return bpm.formFieldCreator[prop.type.name](prop, className);
}
/**
 * 表单字段创建
 */
bpm.formFieldCreator = {
		'string': function(prop, datas, className) {
			var result = "<td width='120'>" + prop.name + "：</td>";
			if (prop.writable === true) {
				result += "<td><input type='text' id='" + prop.id + "' name='" + prop.id + "' class='easyui-textbox " + className + "' value='" + prop.value + "' />";
			} else {
				result += "<td>" + prop.value;
			}
			return result;
		},
		'date': function(prop, datas, className) {
			var result = "<td width='120'>" + prop.name + "：</td>";
			if (prop.writable === true) {
				result += "<td><input type='text' id='" + prop.id + "' name='" + prop.id + "' class='easyui-datebox " + className + "' value='" + prop.value + "' ";
				
				if(prop.datePattern){
					result +="data-options=\"formatter:function(date){ return bpm.formatDate(date,'"+prop.datePattern+"')} \" ";
				}
				
				result += '/>';
				
			} else {
				result += "<td>" + prop.value;
			}
			return result;
		},
		'enum': function(prop, datas, className) {
			var result = "<td width='120'>" + prop.name + "：</td>";
			if (prop.writable === true) {
				result += "<td><select id='" + prop.id + "' name='" + prop.id + "' class='easyui-combobox " + className + "'>";
				$.each(prop.enums, function(k, v) {
					result += "<option value='" + k + "'>" + v + "</option>";
				});
				result += "</select>";
			} else {
				result += "<td>" + prop.value;
			}
			return result;
		},
		'users': function(prop, datas, className) {
			var result = "<td width='120'>" + prop.name + "：</td>";
			if (prop.writable === true) {
				result += "<td><input type='text' id='" + prop.id + "' name='" + prop.id + "' class='" + className + "' value='" + prop.value + "' />";
			} else {
				result += "<td>" + prop.value;
			}
			return result;
		}
};
bpm.formatDate = function(date, format) {
	if(!date) return '';
	
		if(!(format && format != '')) {
			format = 'yyyy-MM-dd HH:mm:ss';
		}
		
		var year = date.getFullYear();
		var month = StringUtil.numLeftPadZore(date.getMonth()+1, 2);
		var day = StringUtil.numLeftPadZore(date.getDate(), 2);
		var hour = StringUtil.numLeftPadZore(date.getHours(), 2);
		var minute = StringUtil.numLeftPadZore(date.getMinutes(), 2);
		var second = StringUtil.numLeftPadZore(date.getSeconds(), 2);
		//var millis = date.getMilliseconds();// 毫秒
		format = format.toUpperCase();
		return format.replace('YYYY', year).replace('MM', month).replace('DD', day)
				.replace('HH', hour).replace('MM', minute).replace('SS', second);
};
/**
 * 显示流程跟踪图
 * @param pid 流程实例id
 * @param pdid 流程定义id
 * @param title 标题
 */
bpm.showProcessTraceWindow = function(pid,pdid,title){
	var $window = $('#dg_process_trace_window_');
	var width_ = 800;
	var height_ = 600;
	if($window.length < 1){
		$window = $("<div id='dg_process_trace_window_' class='easyui-window' />");
		$("body").append($window);
		$window.window({
			closed:true,
			title:'当前运行节点['+title + "]",
		    modal:true,
		    tools:[{
		        iconCls:'icon-add',
		        handler:diagramViewer
		    }]
		});
	
	}
	showTraceWindow(pid,pdid,title);
	$(window).resize(function(){
		$window.window("center");
	})
	//自定义跟踪图
	function showTraceWindow(pid,pdid,title){
		$window.window({
			title:'当前运行节点['+title + "]",
		    width:width_,
		    height:height_,
		    minimizable:false,
		    maximizable:false,
		    collapsible:false
		}).data("selectData",{pid:pid,pdid:pdid}); 
		// contextPath + '/p/bpm/component/processTraceView?pid=' + pid + "&pdid="+pdid
		$window.window('refresh',bpm.getProcessTraceUrl('custom',pid,pdid)).window('center').window('open');
	}
	//插件流程图
	function diagramViewer(){
		var data = $window.data("selectData");
		//contextPath + '/p/bpm/component/diagramViewer?pid=' + pid + "&pdid="+pdid
		$window.window('refresh', bpm.getProcessTraceUrl('plugIn',pid,pdid));
	}
	
}
bpm.getProcessTraceUrl = function(type,pid,pdid){
	if(type === 'custom'){
		return contextPath + '/p/bpm/component/processTraceView?pid=' + pid + "&pdid="+pdid;
	}else{
		return contextPath + '/p/bpm/component/diagramViewer?pid=' + pid + "&pdid="+pdid;
	}
}
/**
 * easygrid 是否formatter
 */
bpm.booleanFormatter = function(value,row,index){
	return value ? "是":"否";
}
/**
 * 流程定义转模型
 */
bpm.convertToModel = function(processDefinitionId,success){
	MsgUtil.loading();
	AjaxUtil.get(contextPath+"/bpm/process/convert-to-model/"+processDefinitionId,function(data){
		MsgUtil.unLoading();
		MsgUtil.showInfo("转换成功！");
		if(success){
			success.call(this,data);
		}
	}, function(e) {
		MsgUtil.unLoading();
		MsgUtil.showError("转换失败！",e);
	});
}
/**
 * 删除流程部署
 */
bpm.deleteDeployment = function(deploymentId,success){
	MsgUtil.loading();
	AjaxUtil.get(contextPath+"/bpm/process/delete?deploymentId="+deploymentId,function(data){
		MsgUtil.unLoading();
		MsgUtil.showInfo("删除成功！");
		if(success){
			success.call(this,data);
		}
	}, function(e) {
		MsgUtil.unLoading();
		MsgUtil.showError("删除失败!",e);
	});
}
/**
 * 删除模型
 */
bpm.deleteModel = function(modelId){
	MsgUtil.loading();
	AjaxUtil.get(contextPath+"/bpm/model/delete?modelId="+deploymentId,function(data){
		MsgUtil.unLoading();
		MsgUtil.showInfo("删除成功！");
		if(success){
			success.call(this,data);
		}
	}, function(e) {
		MsgUtil.unLoading();
		MsgUtil.showError("删除失败!",e);
	});
}
bpm.showHandWindow = function(row,onClose){
	var title = '办理[' + row.name+'----'+row.id+']';
	
	var mapping = urlMapping4Check[row.procDefKey];
	if(!mapping){
		MsgUtil.showError('没有配置相关页面，请联系管理员....');
		return ;
	}			
	var url = contextPath + mapping.url;	
	
	if(!onClose){
		onClose = function (){
			if(window.queryEvent){
				queryEvent();
			}else{
				var tab = top.$("#pageContainerTabs").tabs("getTab","首页");
				if(tab.length > 0){
					var iframes =tab.find('iframe');
					if(iframes.length > 0){
						iframes[0].contentWindow.queryEvent();
					}
				}
				$("#dg_userTaskList").datagrid("reload");
			}
		}
	}
	var width  = 950;
	var maximized = false;
	if("ALLOWANCE_COST_KEY" == row.procDefKey){
		width = 1230;
	}
	
	if(url == (contextPath + '/p/ps/issue/psPpIssueCheck')) {
		toPage('ps/issue/psPpIssueAddAndEdit?fromType=daiban&ppiUuid=' + row.id);
	} else {
		var from = '';
		var isFirstTask = '';
		if('usertask001' == row.taskDefKey) {
			from = 'applyerAudit';//退回到申请人后，申请人再次提交
			isFirstTask = 'Y'
		} else {
			from = 'audit';//其他人员审批
			isFirstTask = 'N';
		}
		var params = '?taskId=' + row.id + '&taskDefKey=' + row.taskDefKey 
					+ '&procInstId=' + row.procInstId + '&procDefId=' + row.procDefId
					+ '&isFirstTask=' + isFirstTask + '&businessKey=' + row.businessKey + '&from=' + from;
		toPage(mapping.url + params);
		
		/*var winId = WindowUtil.open(url,title,{
			width:width,
			load:function(){
				bpmUserTask.taskWindowLoad(row,winId);
			},
			maximized : maximized,
			close:onClose
		});
		return winId;*/
	}
}

bpm.showHistoryWindow = function(row){
	var title = '查看[' + row.proDefName+']';
	var mapping = urlMapping4Check[row.procDefKey];
	if(!mapping){
		MsgUtil.showError('未配置流程查看页面:[' + row.taskDefKey + ']');
		return ;
	}
	
	var url = contextPath + mapping.url;
	var width  = 950;
	var maximized = false;
	if("ALLOWANCE_COST_KEY" == row.procDefKey){
		width = 1230;
	}
	
	if(url == (contextPath + '/p/ps/issue/psPpIssueCheck')) {
		toPage('ps/issue/psPpIssueAddAndEdit?fromType=yiban&ppiUuid=' + row.businessKey);
	} else {
		var params = '?taskId=' + row.id + '&taskDefKey=' + row.taskDefKey 
				+ '&procInstId=' + row.procInstId + '&procDefId=' + row.procDefId
				+ '&businessKey=' + row.businessKey + '&from=yiban&currTaskId='+row.currTaskId+'&assignee='+row.assignee;
		toPage(mapping.url + params);
		/*var winId = WindowUtil.open(url,title,{
			width:width,
			load:function(){
				bpmUserTask.taskWindowLoad(row,winId,"OVER");
			},
			maximized : maximized
		});*/
	}
}

bpm.isReadTagRowStyler = function(index,row){
	if (row.readTag != 'Y'){
		return 'font-weight: 600';
	}
}
bpm.formatProcessWindow = function(gridId,val,row){
	if(row && row.processId){
		var grid = $('#'+gridId);
    	var index= grid.datagrid('getRowIndex',row); 
		return '<a href="javascript:bpm.showProcessWindow(' + index+ ',\''+gridId+'\')" style="color:blue;">点击查看</a>';
	}
}

bpm.showProcessWindow = function(gridId){
	DatagridUtil.singleRowOps(gridId,function(row){		
		if(row.processId){
			row.businessKey = row.billNo;
			var title = '查看流程';
			AjaxUtil.get(contextPath+'/bpm/userTask/task/getProcDefKeyByProcessId/'+row.processId,function(data){
				var mapping = urlMapping4Check[data];
				if(!mapping){
					MsgUtil.showError('没有配置相关页面，请联系管理员....');
					return ;
				}			
				var url = contextPath + mapping.url;	
				var width = 930;
				if("ALLOWANCE_COST_KEY" == data){
					width = 1230;
				}
				var winId = WindowUtil.open(url,title,{
					width:width,
					load:function(){
						bpmUserTask.taskWindowLoad(row,winId,"OVER");
					}
				});
			});
		}else{
			MsgUtil.showError("只能查看已发起流程的数据!");
		}
	});	
}

bpm.lookBpmInfo = function(procInstId){
	WindowUtil.open(contextPath+'/p/bpm/bpmInfo?procInstId='+procInstId, '查看流程审核信息',{height:"360px"});
}

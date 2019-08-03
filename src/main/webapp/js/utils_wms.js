//扩展utils
if("undefined" == typeof DatagridUtil) {
	var DatagridUtil={};
}
if("undefined" == typeof DomUtil) {
	var DomUtil={};
}

/**
 * 添加1行
 * 
 * @parm data 默认放入的值
 */
DatagridUtil.addRow=function(dgId,data){
	var idField=$('#'+dgId).datagrid("options").idField;
	var row = data || {};
	row[idField]='TMP_'+(new Date().getTime());
	$('#'+dgId).datagrid('appendRow', row);
}
/**
 * 删除选中的行
 */
DatagridUtil.delSelectedRow=function(dgId) {
	var rows = $('#'+dgId).datagrid("getSelections");
	if(rows&&rows.length>0) {
		MsgUtil.confirm ("确认删除","已选中"+rows.length+"行，确认删除选中的行？",function(r){
			if(!r){
				return;
			}
			for(var i=rows.length-1;i>=0;i--){
				var rowIndex=$('#'+dgId).datagrid("getRowIndex",rows[i]);
				$('#'+dgId).datagrid('deleteRow',rowIndex);
			}
		});
	} else {
		MsgUtil.showError("请选择需要删除的数据！");
	}
}

/**
 * 删除未选中的行
 * @param {} dgId
 */
DatagridUtil.delUnSelectedRow=function(dgId) {
	var idField=$('#'+dgId).datagrid("options").idField;
	var rows = $('#'+dgId).datagrid("getRows");
	var selectedRows = $('#'+dgId).datagrid("getSelections");
	if(rows&&rows.length>0) {
		var selected = false;
		for(var i=rows.length-1;i>=0;i--){
			selected = false;
			for(var j=0;j<selectedRows.length;j++) {
				if(selectedRows[j][idField]==rows[i][idField]) {
					selected = true;
					break;
				}
			}
			if(!selected) {
				var rowIndex=$('#'+dgId).datagrid("getRowIndex",rows[i]);
				$('#'+dgId).datagrid('deleteRow',rowIndex);
			}
		}
	} 
}
/**
 * 结束编辑
 */
DatagridUtil.endEditing=function(dgId,delBlank,keyField){	
	var editIndexVarName=dgId+"EditingIndex";
	var rowIndex = DatagridUtil[editIndexVarName];
	
	if (rowIndex == undefined){
		return true
	}
	if ($('#'+dgId).datagrid('validateRow', rowIndex)){
		$('#'+dgId).datagrid('endEdit', rowIndex);
		DatagridUtil[editIndexVarName] = undefined;
		return true;
	} else {
		if(delBlank) {
			var editor = $("#"+dgId).datagrid('getEditor', {index:rowIndex,field:keyField}); 
			var value = $(editor.target).textbox('getValue');
			if(!value) {
				$("#"+dgId).datagrid("deleteRow",rowIndex);
				return true;
			} else {
				MsgUtil.showError("此行内容未填写完整，请根据提示将本行填写完整！");
				return false;
			}
		} else {
			MsgUtil.showError("此行内容未填写完整，请根据提示将本行填写完整！");
			return false;
		}
	}
}
/**
 * 开始编辑
 * @param dgId
 * @param rowIndex
 * @param data 默认放入行里的数据
 * @isAddRow 是否添加一行新行
 * 
 */
DatagridUtil.beginEditing=function(dgId,rowIndex,data,isAddRow){
	if (DatagridUtil.endEditing(dgId)){
		$('#'+dgId).datagrid('beginEdit', rowIndex);
		DatagridUtil[dgId+"EditingIndex"] = rowIndex;
		var gridData = $('#'+dgId).datagrid('getData');
		if(isAddRow){
			if(rowIndex==gridData.total-1){//自动加一行
				DatagridUtil.addRow(dgId,data);
			}
		}		
	}
}

/**
 * 编辑当前选中的行
 * @param dgId
 * @param rowIndex
 * @param data 默认放入行里的数据
 * 
 */
DatagridUtil.editing=function(dgId,rowIndex,data){
	if (DatagridUtil.endEditing(dgId)){
		$('#'+dgId).datagrid('beginEdit', rowIndex);
		DatagridUtil[dgId+"EditingIndex"] = rowIndex;
		var gridData = $('#'+dgId).datagrid('getData');
	}
}
/**
 * 取当前编辑的行
 */
DatagridUtil.getEditingRowIndex=function (dgId) {
	return DatagridUtil[dgId+"EditingIndex"];
}


/**
 * 显示子对象的数据
 */
DatagridUtil.formatterSubObj=function(value,row,index){
	var field = this.field;
	var subFields = field.split(".");
	var resultValue=row;
	for (var i = 0; i < subFields.length; i++) {
		resultValue = resultValue[subFields[i]];
		if(!resultValue) {
			resultValue="";
			break;
		}
	}
	return resultValue;
}

/**
 * 删除空白行
 */
DatagridUtil.deleteBlankRow=function(dgId,field){
	var rows = $("#"+dgId).datagrid("getRows");
	if(!rows) {
		return;
	}
	for(var i=rows.length-1;i>=0;i--) {
		if(!rows[i][field]) {
			$("#"+dgId).datagrid("deleteRow",i);
		}
	}
}

/**
 * 复制属性值，当目标属性存在值的时候不替换
 */
DomUtil.noReplaceCopy=function(targetObj,fieldName,value){
	if(targetObj==undefined) {
		return;
	}
	if(!targetObj[fieldName]) {
		targetObj[fieldName] = value;
	}
}

/**
 * 判断是否有重复行，如果有则用颜色标识
 */
DatagridUtil.checkHasRepeatRow=function(dgId,field,autoSelected){
	var hasRepeat=false;
	var rows = $("#"+dgId).datagrid("getRows");
	if(!rows) {
		return;
	}
	var values = new Array();
	var repeatIndexs = new Array();
	for(var i= 0;i<rows.length;i++) {
		if(rows[i][field]&&$.inArray(rows[i][field],values)!=-1) {
			hasRepeat = true;
			repeatIndexs.push(i);
		}else {
			values.push(rows[i][field]);
		}
	}
	if(hasRepeat) {
		$("#"+dgId).datagrid({   
	        rowStyler:function(index,row){
	            if ($.inArray(index,repeatIndexs)!=-1){
	            	return 'background-color:#f6b26b!important;';     
	            }     
	        }     
		}); 
		if(autoSelected) {//如果自动选中重复的行，则将其他的已经选择的取消选中
			$("#"+dgId).datagrid('uncheckAll');
		}
		for(var i=0;i<repeatIndexs.length;i++) {
			$("#"+dgId).datagrid('refreshRow',repeatIndexs[i]);
			if(autoSelected) {
				$("#"+dgId).datagrid('checkRow',repeatIndexs[i]);
			}
		}
	}
	return hasRepeat;
}

/**
 * 在数组中查找对象
 */
DatagridUtil.findObjectInArray=function(rows,field,value){
	for(var i=0;i<rows.length;i++){
		if(rows[i][field]==value){
			return rows[i];
		}
	}
	return null;
}

/**
 * 判断是否存在
 */
DatagridUtil.checkExists=function(rows,field,value){
	if(DatagridUtil.findObjectInArray(rows,field,value)!=null) {
		return true;
	} else {
		return false;
	}
}


/*
 * 物料相关操作
 */
var SkuUtil={
	//下拉选择
	onSelectSkuDefault:function(dgId,selectIndex,rowData){	
		try{
			var rowIndex = DatagridUtil.getEditingRowIndex(dgId);
			var skuNameEdit = $("#"+dgId).datagrid('getEditor', {index:rowIndex,field:"skuName"});
			$(skuNameEdit.target).textbox('setValue',rowData.skuName);
			var unitEdit = $("#"+dgId).datagrid('getEditor', {index:rowIndex,field:"unit"});
			$(unitEdit.target).combobox('setValue',rowData.outUnit);
			var rows = $("#"+dgId).datagrid("getRows");
			rows[rowIndex].skuUuid=rowData.skuUuid;
		}catch(e){
			console.log(e);
		}
	},
	//下拉选择框change事件
	onChangeSkuDefault:function onChangeSku(dgId,newValue,oldValue) {
		try{
			var rowIndex = DatagridUtil.getEditingRowIndex(dgId);
			var rows = $("#"+dgId).datagrid("getRows");
		
			if(rowIndex!=undefined&&rows) {
				rows[rowIndex].skuUuid="";
			}
		}catch(e){
			console.log(e);
		}
	}
}

/*
 * 项目相关操作
 */
var projectUtil={
	//下拉选择
	onSelectProjectDefault:function(dgId,selectIndex,rowData){	
		try{
			var rowIndex = DatagridUtil.getEditingRowIndex(dgId);
			var projectName = $("#"+dgId).datagrid('getEditor', {index:rowIndex,field:"projectName"});
			$(projectName.target).textbox('setValue',rowData.projectName);
			var projectUuid = $("#"+dgId).datagrid('getEditor', {index:rowIndex,field:"projectUuid"});
			$(projectUuid.target).textbox('setValue',rowData.projectUuid);
		}catch(e){
			console.log(e);
		}
	},
	//下拉选择框change事件
	onChangeProjectDefault:function onChangeProject(dgId,newValue,oldValue) {
		try{
			var rowIndex = DatagridUtil.getEditingRowIndex(dgId);
			var rows = $("#"+dgId).datagrid("getRows");
			if(rowIndex!=undefined&&rows) {
				//rows[rowIndex].projectUuid="";
				var projectUuid = $("#"+dgId).datagrid('getEditor', {index:rowIndex,field:"projectUuid"});
				$(projectUuid.target).textbox('setValue',newValue.projectUuid);
			}
		}catch(e){
			console.log(e);
		}
	}
}


var PrintUtil={
	print:function(el,divId) {
		 $("#"+divId).printArea(); 
	}
}

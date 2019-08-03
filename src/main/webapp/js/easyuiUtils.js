//$.fn.combo.defaults.panelHeight = 'auto';
$.fn.combobox.defaults.panelHeight = 'auto';
$.fn.combobox.defaults.panelMaxHeight = 200;
/*$.fn.combobox.defaults.panelWidth = 250;*/
//$.fn.combobox.defaults.panelWidthHeight = 300;
$.fn.filebox.defaults.buttonText="选择文件";
$.fn.datebox.defaults.height = 32;


$.fn.calendar.defaults.showWeek = true;
$.fn.calendar.defaults.weekNumberHeader = "周";
$.fn.calendar.defaults.firstDay = 0;

//$.fn.pagination.defaults.nav.after.title='之后';
$.fn.pagination.defaults.nav.first.title='第一页';
$.fn.pagination.defaults.nav.last.title='最后一页';
$.fn.pagination.defaults.nav.next.title='下一页';
$.fn.pagination.defaults.nav.prev.title='上一页';
$.fn.pagination.defaults.nav.refresh.title='刷新列表';



$.fn.calendar.defaults.getWeekNumber = function(date) {
	return moment(date).week();
}
$.fn.treegrid.defaults.onLoadSuccess = function(){
	$(this).treegrid('renderTooltip');
}
/**
 * datagrid默认值
 */
$.fn.datagrid.defaults.pageSize = 100;
$.fn.datagrid.defaults.pageList = [10,20,50,100,500,1000];

$.fn.combogrid.defaults.pageSize = 100;
$.fn.combogrid.defaults.pageList = [10,20,50,100,500];

/**
 * 自定义控件ctfilebox 放入easyui表单列表赋值中
 */
if($.fn.form.defaults.fieldTypes.indexOf("ctfilebox") < 1){
	$.fn.form.defaults.fieldTypes.splice(0, 0,"ctfilebox");
}
/**
 * 自定义控件ctfilebox 放入easyui表单控件渲染列表中
 */
if($.parser.plugins.indexOf('ctfilebox') < 1){
	$.parser.plugins.push('ctfilebox');
}

/**
 * 自定义控件usercombo 放入easyui表单列表赋值中
 */
if($.fn.form.defaults.fieldTypes.indexOf("usercombo") < 1){
	$.fn.form.defaults.fieldTypes.splice(0, 0,"usercombo");
}
/**
 * 自定义控件usercombo 放入easyui表单控件渲染列表中
 */
if($.parser.plugins.indexOf('usercombo') < 1){
	$.parser.plugins.push('usercombo');
}

/**
 * 用于datagrid编辑获取行序号
 */
$.fn.datagrid.methods.getParentIndex = function(jq){
	var data = jq.data("data-parent-grid");
	if(!data){
		console.log("未绑定编辑列表行数据!");
		return -1;
	}
	return data.grid.datagrid("getRowIndex",data.row)
}

$.fn.datagrid.methods.getRow = function(jq, rowIndex){
	var data = jq.datagrid('getRows');
	if(data){
		return data[rowIndex];
	} else {
		return null;
	}
}
$.fn.datagrid.methods.rendEditTag = function(jq, filter){
	filter = filter ? filter : function(){return true};
	var rows = jq.datagrid('getRows');
	var allFields = [],hasEditorFields = [];
	allFields = allFields.concat(jq.datagrid('getColumnFields', true));
	allFields = allFields.concat(jq.datagrid('getColumnFields'));
	
	allFields.forEach(function(field){
		var opts = jq.datagrid('getColumnOption',field );
		if(opts.editor){
			hasEditorFields.push(opts.field);
		}
	});
	var titleFields = [];
	for(var i = 0 ;i < rows.length ; i++){
		hasEditorFields.forEach(function(field){
			if(filter(i,field,rows[i])){
				jq.parent().find('.datagrid-btable').find('td[field='+field+']').css('border', '1px solid #7bb8f5');
				titleFields.push(field);
			}
		})
	}
	if(titleFields.length > 0){
		jq.parent().find('.datagrid-btable').css('border-collapse', 'collapse');
	}
	titleFields.forEach(function(field){
		var picUrl = contextPath + '/Insdep/themes/insdep/images/edit.png';
		jq.parent().find('.datagrid-htable').find('td[field='+field+']').find('span:first').css({"padding-left":'20px', 'background':'url('+picUrl+') no-repeat left', 'background-size':'14px'});
	});
}
$.fn.treegrid.methods.rendEditTag = function(jq, filter){
	filter = filter ? filter : function(){return true};
	var allFields = [],hasEditorFieldsFilter = [];
	allFields = allFields.concat(jq.treegrid('getColumnFields', true));
	allFields = allFields.concat(jq.treegrid('getColumnFields'));
	allFields.forEach(function(field){
		var opts = jq.treegrid('getColumnOption',field );
		if(opts.editor){
			hasEditorFieldsFilter.push('td[field=' + opts.field + ']');
		}
	});
	//jq.find('.datagrid-btable').css('border-collapse', 'collapse');
	jq.parent().find('.datagrid-view1 > *,.datagrid-view2 > *').each(function(i,item){
		//头
		if($(item).hasClass('datagrid-header')){
			var picUrl = contextPath + '/Insdep/themes/insdep/images/edit.png';
			$(item).find(hasEditorFieldsFilter.join(',')).find('span:first').css({"padding-left":'20px', 'background':'url('+picUrl+') no-repeat left', 'background-size':'14px'});
		//数据
		}else if($(item).hasClass('datagrid-body')){
			$(item).find(hasEditorFieldsFilter.join(',')).each(function(i2,item2){
				var row = jq.treegrid('find',$(item2).parent().attr('node-id'));
				if(filter(i,$(item2).attr('field'),row) !== false){
					/*var width = $(item2).width() - 2;
					var height = $(item2).height() - 2;
					//$(item2).css('border', '1px solid #7bb8f5').css({width:(width +'px'),height:(height+'px')});*/
					//$(item2).css({'border-color':'#7bb8f5','border-style':'solid'});
					//var t = $(item2).css('border-left')
				//	debugger
					
					///$(item2).css({'border-color':'#7bb8f5','border-style':'solid'});
					$(item2).css('border', '1px solid #7bb8f5')
				}
			});
		}
	});
	return;
	
	var titleFields = [];
	for(var i = 0 ;i < rows.length ; i++){
		hasEditorFields.forEach(function(field){
			if(filter(i,field,rows[i])){
				jq.find('.datagrid-btable').find('td[field='+field+']').css('border', '1px solid #7bb8f5');
				titleFields.push(field);
			}
		})
	}
	if(titleFields.length > 0){
		jq.find('.datagrid-btable').css('border-collapse', 'collapse');
	}
	titleFields.forEach(function(field){
		var picUrl = contextPath + '/Insdep/themes/insdep/images/edit.png';
		jq.find('.datagrid-htable').find('td[field='+field+']').find('span').css({"padding-left":'20px', 'background':'url('+picUrl+') no-repeat left', 'background-size':'14px'});
	});
}

$.fn.datagrid.methods.endAllEdit = function(jq){
	var rows = jq.datagrid('getRows');
	for(var i = 0; i < rows.length; i++) {
		jq.datagrid("endEdit",i);
	}
}

$.extend($.fn.datagrid.methods, {    
    addEditor : function(jq, param) {    
        if (param instanceof Array) {    
            $.each(param, function(index, item) {    
                var e = $(jq).datagrid('getColumnOption', item.field);    
                e.editor = item.editor;    
            });    
        } else {    
            var e = $(jq).datagrid('getColumnOption', param.field);    
            e.editor = param.editor;    
        }    
    },    
    removeEditor : function(jq, param) {    
        if (param instanceof Array) {    
            $.each(param, function(index, item) {    
                var e = $(jq).datagrid('getColumnOption', item);    
                e.editor = {};    
            });    
        } else {    
            var e = $(jq).datagrid('getColumnOption', param);    
            e.editor = {};    
        }    
    },
    // 清空指定行
    cleanRow: function(jq, rowIndex) {
    	var row = $(jq).datagrid('getRow', rowIndex);
    	for(field in row) {
			row[field] = null;
		}
    }
});
/**
 * 默认选中第一行数据
 */
$.fn.combobox.defaults.onLoadSuccess = function(){
	var data = $(this).combobox("getData");
	if(data && data.length == 1){
		var opts = $(this).combobox('options');
		$(this).combobox("select", data[0][opts.valueField]);
	}
}
/**
 * 日期控件默认转换
 */
$.fn.datebox.defaults.parser = function(s){
	var r = moment(s, ["YYYY-MM-DD", "YYYY/MM/DD", "YYYY.MM.DD"]);
	if(r.isValid()){
		return r.toDate();
	}
	return new Date();
}
/**
 * 获取到行的TD元素
 * @param param 参数  可以为number类型和对象{index：行序列,fieldName:字段名称(可以为空)}
 */
$.fn.datagrid.methods.getRowTds = function(jq,param){
	 var grid = jq[0];
	 var opts = $.data(grid, "datagrid").options;
	 var index  = -1;
	 var fieldName = "";
	 if(isNaN(param)){
		 index = param.index;
		 fieldName = param.fieldName;
	 }else{
		 index = param;
	 }
     var tr = opts.finder.getTr(grid, index);
     var tds = [];
     var tdExp = StringUtil.isEmpty(fieldName) ?  "td[field]" : ("td[field='"+ fieldName +"']");
     tr.children(tdExp).each(function() {
    	 tds.push(this);
     });
     return tds;
}
/**
 * 校验datagrid所有列 {nullable:'true:false , 是否可以为空','  nullableMsg: '为空错误信息'}
 */
$.fn.datagrid.methods.validateGrid = function(jq,params){
	var params  = params ? params : {};
	var grid = jq;
	var rows = grid.datagrid('getRows');
	
	if(params.nullable == true){
		if(rows.length < 1){
			MsgUtil.showError(params.nullableMsg ? params.nullableMsg : '数据列表不能为空!');
			return false;
		}
	}
	
	for(var  i = 0 ;i < rows.length ; i ++){
		if(grid.datagrid("validateRow",i) == false){
			return false;
		}
	}
    return true;
}
/**
 * 绘制tooltip
 */
$.fn.datagrid.methods.renderTooltip = function(jq){
	jq.siblings('.datagrid-view1,.datagrid-view2').find('.datagrid-btable').find('.datagrid-cell').each(function(){
		var htmlText = $(this).text();
		if(StringUtil.isEmpty(htmlText)){
			return ;
		}
		$(this).tooltip({
			position: 'bottom',
			content: ('<span style="color:#fff">' + htmlText+'</span>'),
			onShow: function(){
				$(this).tooltip('tip').css({
					backgroundColor: '#999',
					borderColor: '#999'
				});
			}
		});
	});
    return true;
}
$.fn.treegrid.methods.renderTooltip = $.fn.datagrid.methods.renderTooltip;

$.fn.treegrid.methods.clearAllChecked = function(treegrid){
	var ops = treegrid.treegrid('options');
	var nodes = treegrid.treegrid("getCheckedNodes");
	$(nodes).each(function(i,node){
		var node = treegrid.treegrid('find',node[ops.idField]);
		if(node){
			treegrid.treegrid('uncheckNode',node[ops.idField]);
		}
	});
	treegrid.data("treegrid").checkedRows = [];
}
/**
 * datagrid添加行并且开启当前编辑
 */
$.fn.datagrid.methods.addRowEditable = function(jq,params){
	var grid = jq;
	params = params ? params : {};
	var isSingle = params.isSingle;
	if(isSingle){
		if(!grid.datagrid('validateGrid')){
			return false;
		}
	}
	var row =params.defaults || {};
	grid.datagrid('appendRow', row);
	var rowIndex = grid.datagrid('getRowIndex',row);
	grid.datagrid('beginEdit',rowIndex);
	//dgId,rowIndex,row,isSingle
	
	
}
/**
 * 用于datagrid编辑获取行序号
 */
$.fn.tree.methods.getParentIndex = function(jq){
	var data = jq.data("data-parent-grid");
	if(!data){
		console.log("未绑定编辑列表行数据!");
		return -1;
	}
	return data.grid.datagrid("getRowIndex",data.row)
}
/**
 * 用于datagrid编辑获取行序号
 */
$.fn.textbox.methods.getParentIndex = function(jq){
	var data = jq.data("data-parent-grid");
	if(!data){
		console.log("未绑定编辑列表行数据!");
		return -1;
	}
	return data.grid.datagrid("getRowIndex",data.row)
}
$.fn.combogrid.defaults.onSetValue = function(newVal){
	var grid = $(this).combogrid("grid");
	var options =  $(this).combogrid("options");
	if(StringUtil.isEmpty(options.url) || options.url.indexOf('/rest/hr/hrEmployees/getHrEmp') < 0 ){
		return ;
	}
	var rows = grid.datagrid('getRows');
	var find = false;
	$.each(rows, function(index, value) {
         if (newVal == value[options.idField]) { 
             find = true;
             return false;
         }
    });
	if(!find){
		var grid = $(this).combogrid("grid");
		grid.datagrid({
			queryParams: {
				order:newVal
			}
		});
	}
}

var easyUi = {};
/**
 * 开始编辑
 * @param dgId gridId
 * @param rowIndex 开始编辑的index
 * @param row 绑定的行数据 或是idField
 * @param 当前grid是否是单行编辑模式
 */
easyUi.beginEditing = function(dgId,rowIndex,row,isSingle){
	var grid = $('#'+dgId);
	var rows = grid.datagrid("getRows");
	var opts = grid.datagrid("options");
	function isBeginEdit(){
		if(!isSingle){
			return true;
		}
		for(var i = 0 ;i < rows.length ;i ++){
			var row = rows[i] , isValidate = false;
			var tr = opts.finder.getTr(grid.get(0),i, false);
			//如果开启了编辑就需要验证
			if(!tr.hasClass("datagrid-row-editing")){
				continue;
			}
			if(!grid.datagrid('validateRow',i)){
				return false;
			}
		}
		return true;
	}
	if(!isBeginEdit()){
		return false;
	}else{
		if(isSingle){
			for(var i = 0 ;i < rows.length ;i ++){
				grid.datagrid("endEdit",i);
			}
		}
	}
	grid.datagrid('beginEdit', rowIndex);
	grid.datagrid('getEditors',rowIndex).forEach(function(item){
		if(item.type == 'combogrid'){
			if(!StringUtil.isEmpty(row[item.field])){
				var ops = $(item.target).combogrid("options");
				var param = ops.url.indexOf("?") > 0 ? "&initialValue=" : "?initialValue=";
				var newUrl = ops.url + param + $(item.target).combogrid("getValue");
				$(item.target).combogrid({url:newUrl});
				$(item.target).combogrid("setValue",row[item.field]);
			}
			$($(item.target).combogrid("grid")).data("data-parent-grid",{grid:grid,row:row});
		}else if(item.type == 'combotree'){
			$($(item.target).combotree("tree")).data("data-parent-grid",{grid:grid,row:row});
		}else{
			var target = item.target;
			if(!item.target){
				target = item;
			}
			$(target).data("data-parent-grid",{grid:grid,row:row});
		}
	});
}
/**
 * 添加一行
 */
easyUi.addRow=function(dgId,data){
	var grid = $('#'+dgId);
	var row = data || {};
	row._record = true;
	grid.datagrid('appendRow', row);
	return row;
}
/**
 * 删除行
 */
easyUi.deleteRow = function(dgId,_this){
	if(!_this){
		_this = this;
	}
	var index = easyUi.getRowIndex(_this);
	var grid = $('#' + dgId);
	if(grid.length < 1){
		throw Error('dgId错误，无法删除!');
	}
	grid.datagrid('deleteRow', index); 
}
/**
 * 获取行index 
 * @param _this 行内对象
 */
easyUi.getRowIndex = function(_this){
	var index = parseInt($(_this).parents("tr[datagrid-row-index]").attr("datagrid-row-index"));
	if(isNaN(index)){
		throw Error('_this 对象执行错误!');
	}
	return index;
}
/**
 * 获取列表数据，并且结束行编辑、校验数据是否合法
 * @param dgId datagridId
 * @returns 返回列表数据
 */
easyUi.getRows = function(dgId){
	var grid = $('#'+dgId);
	var opts = grid.datagrid("options");
	var rows = grid.datagrid("getRows");
	for(var i = 0 ;i < rows.length ;i ++){
		var row = rows[i] , isValidate = false;
		var tr = opts.finder.getTr(grid.get(0),i, false);
		//如果开启了编辑就需要验证
		if(!tr.hasClass("datagrid-row-editing")){
			continue;
		}
		if(!grid.datagrid('validateRow',i)){
			return false;
		}
		grid.datagrid('endEdit',i);
	}
	return rows;
}

/**
 * 控件获取值
 */
easyUi.getVal=function(id){
	var $_ = $("#" + id),clazz =$_.attr("class");
	if(clazz){
		if(clazz.indexOf('textbox') > 0){
			return $_.textbox("getValue");
		}else if(clazz.indexOf('combobox') > 0){
			return $_.combobox("getValue");
		}else if(clazz.indexOf('combotree') > 0){
			return $_.combotree("getValue");
		}else if(clazz.indexOf('combogrid') > 0){
			return $_.combogrid("getValue");
		}else if(clazz.indexOf('combotreegrid') > 0){
			return $_.combotreegrid("getValue");
		}else if(clazz.indexOf('tagbox') > 0){
			return $_.tagbox("getValue");
		}else if(clazz.indexOf('numberbox') > 0){
			return $_.numberbox("getValue");
		}else if(clazz.indexOf('datebox') > 0){
			return $_.datebox("getValue");
		}else if(clazz.indexOf('datetimebox') > 0){
			return $_.datetimebox("getValue");
		}else if($_.get(0).type == 'checkbox'){
			if($_.is(':checked')){
				return $_.val();
			}else{
				return "";
			}
		}else{
			return  $_.val();
		}
	}
	return "";
}
/**
 * 开启编辑后，修复列错误
 */
easyUi.calculaRowWidth = function(param){
	var grid = param.get(0);
    var dc = $.data(grid, "datagrid").dc;
    dc.view.find("div.datagrid-editable").each(function() {
        var cell = $(this);
        var field = cell.parent().attr("field");
        var col = $(grid).datagrid("getColumnOption", field);
        cell._outerWidth(col.boxWidth + col.deltaWidth + 1);
        var ed = $.data(this, "datagrid.editor");
        if (ed.actions.resize) {
            ed.actions.resize(ed.target, cell.width());
        }
    });
}
$.extend($.fn.datagrid.defaults.editors, {
	checkBoxExt:{
		 init: function(td, ops) {
			 var checkBox = $("<input type=\"checkbox\">");
			 if(ops.onChange){
				 checkBox.change(ops.onChange);
			 }else if(ops.onClick){
				 checkBox.click(ops.onClick);
			 }
			 
	         var $html = checkBox.appendTo(td);
	         $html.val(ops.on);
	         $html.attr("offval", ops.off);
	         return $html;
	     },
	     getValue: function(_7d4) {
	         if ($(_7d4).is(":checked")) {
	             return $(_7d4).val();
	         } else {
	             return $(_7d4).attr("offval");
	         }
	     },
	     setValue: function(_7d5, _7d6) {
	         var _7d7 = false;
	         if ($(_7d5).val() == _7d6) {
	             _7d7 = true;
	         }
	         $(_7d5)._propAttr("checked", _7d7).change();
	     },
	     getParentIndex : function(jq){
	    	var data = jq.data("data-parent-grid");
	    	if(!data){
	    		console.log("未绑定编辑列表行数据!");
	    		return -1;
	    	}
	    	return data.grid.datagrid("getRowIndex",data.row)
	     } 
	}
});
$.extend($.fn.tabs.methods,{
    allTabs:function(jq){
        var tabs = $(jq).tabs('tabs');
        var all = [];
        all = $.map(tabs,function(n,i){
             return $(n).panel('options')
        });
        return all;
    },
    closeCurrent: function(jq){ // 关闭当前
        var currentTab = $(jq).tabs('getSelected'),
            currentTabIndex = $(jq).tabs('getTabIndex',currentTab);
         $(jq).tabs('close',currentTabIndex);
    },
    closeAll:function(jq){ //关闭全部
        var tabs = $(jq).tabs('allTabs');
        $.each(tabs,function(i,n){
            $(jq).tabs('close', n.title);
        })
    },
    closeOther:function(jq){ //关闭除当前标签页外的tab页
        var tabs =$(jq).tabs('allTabs');
        var currentTab = $(jq).tabs('getSelected'),
            currentTabIndex = $(jq).tabs('getTabIndex',currentTab);
 
        $.each(tabs,function(i,n){
            if(currentTabIndex != i) {
                $(jq).tabs('close', n.title);
            }
        })
    },
    closeLeft:function(jq){ // 关闭当前页左侧tab页
        var tabs = $(jq).tabs('allTabs');
        var currentTab = $(jq).tabs('getSelected'),
            currentTabIndex = $(jq).tabs('getTabIndex',currentTab);
        var i = currentTabIndex-1;
 
        while(i > -1){
            $(jq).tabs('close', tabs[i].title);
            i--;
        }
    },
    closeRight: function(jq){ //// 关闭当前页右侧tab页
        var tabs = $(jq).tabs('allTabs');
        var currentTab = $(jq).tabs('getSelected'),
            currentTabIndex = $(jq).tabs('getTabIndex',currentTab);
        var i = currentTabIndex+ 1,len = tabs.length;
        while(i < len){
            $(jq).tabs('close', tabs[i].title);
            i++;
        }
    }
});
/**
 * 校验从下拉中
 */
$.extend($.fn.validatebox.defaults.rules, {
    fromSelect: {
        validator: function(value,param){
        	var combo = $(this).closest('.combo').siblings('.combo-f'),
	            data = combo.data();
	        if (data && data.combogrid) {
	            var options = combo.combogrid('options');
	            if (_.trim(options.value) === _.trim(value) || _.trim(options.text) === _.trim(value)) {
	                return true;
	            }
	            return combo.combogrid('grid').datagrid('getSelected');
	        } else if (data && data.combobox) {
	            var options = data.combobox.options;
	            if (options.value === value || options.text === value || options.editable === false) {
	                return true;
	            }
	            var listData = options.data;
	            if (listData && !listData.length) {
	                listData = data.combobox.data;
	            } else if (data.combobox) {
	                listData = data.combobox.data;
	            }
	            var matched = _.find(listData, function(d){
	            	return d[options.textField] === value;
	            });
	            return matched;
	        }
            return false;
        },
        message: '只能从下拉中选取数据！'
    },
    isLetter:{
    	validator: function(value) {
    		return ValidationUtil.isLetter(value);
    	},
    	message : '只能输入字母' 
    },
    isNumber:{
    	validator: function(value) {
    		return ValidationUtil.isNumber(value);
    	},
    	message : '只能输入数字' 
    },
    isLetterOrNumber:{
    	validator: function(value) {
    		return ValidationUtil.isLetterOrNumber(value);
    	},
    	message : '只能输入数字或字母' 
    },
    isInt:{
    	validator: function(value) {
    		return ValidationUtil.isInt(value);
    	},
    	message : '只能输入整数' 
    },
    isIntGeZero:{
    	validator: function(value) {
    		return ValidationUtil.isIntGeZero(value);
    	},
    	message : '只能输入大于等于0的整数' 
    },
    isIntGtZero:{
    	validator: function(value) {
    		return ValidationUtil.isIntGtZero(value);
    	},
    	message : '只能输入大于0的整数' 
    },
    isFloat:{
    	validator: function(value) {
    		return ValidationUtil.isFloat(value);
    	},
    	message : '只能输入浮点数' 
    },
    isChinese:{
    	validator: function(value) {
    		return ValidationUtil.isChinese(value);
    	},
    	message : '只能输入中文' 
    },
    isPhoneOrTel:{
    	validator: function(value) {
    		return ValidationUtil.isPhoneOrTel(value);
    	},
    	message : '请输入正确的电话号码' 
    },
    isDate:{
   	 validator : function(value) {  
           return moment(value).isValid();  
        },  
        message : '无效的日期格式!'  
   },
   isMobilePhone:{
   	validator: function(value) {
   		return ValidationUtil.isMobilePhone(value);
   	},
   	message : '请输入正确的手机号码' 
   }
});


$.fn.combobox.methods.setReadonly = function(jq){
	jq.next('.combo').find('.textbox-text').attr('readonly', true);
	jq.next('.combo').find('.combo-arrow').unbind();
	jq.next('.combo').find('.combo-arrow').attr('disabled', 'disabled');
	jq.next('.combo').find('.textbox-addon').unbind();
	jq.next('.combo').find('.textbox-addon').attr('disabled', 'disabled');
}


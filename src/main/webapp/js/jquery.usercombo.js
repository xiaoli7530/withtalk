/**
 
 * EasyUI for jQuery 1.5.3
 
 * 
 
 * Copyright (c) 2009-2017 www.jeasyui.com. All rights reserved.
 
 *
 
 * Licensed under the freeware license: http://www.jeasyui.com/license_freeware.php
 
 * To use it on other terms please contact us: info@jeasyui.com
 *
 */

(function($) {
	var selectedRows;
	function init(target) {
		var _3 = $.data(target, "usercombo");
		var callback;
		if(target){
			callback = $(target).attr("callback");			
		}		
		var _4 = _3.options;
		var id = StringUtil.uuid32();
		$(target).addClass("usercombo-f").combo($.extend({}, _4, {
			onShowPanel: function() {
				var p = $(this).usercombo("panel");
				
				var _5 = p.outerHeight() - p.height();
				var _6 = p._size("minHeight");
				var _7 = p._size("maxHeight");
				var url =_4.url;
				var method=_4.method||'get';
				var multiple=_4.multiple||false;//多选控制
				
				if(!url){
					url = contextPath+'/rest/wf/wfOverview/querySelPerson?roleCode=VSE&'
				}else{
					if(url.indexOf("?")>0){
						url = url+"&";
					}else{
						url = url+"?";
					}
				}
				
				var selectUserPopup = $(p).find('#'+id);
				selectUserPopup.find('#seachUser').textbox({
					onChange:function (newValue,oldValue){
						if($.trim(newValue)){
							selectUserPopup.find('.empsGrid').datagrid({
								method:method,
								url:url+'q='+encodeURI(newValue),
								columns:[[
								    {field:'accountUuid',title:'id',width:10,hidden:true},
									{field:'empName',title:'姓名',width:100},
									{field:'empCode',title:'工号',width:60},
									{field:'empEmail',title:'email',width:150},
									{field:'empPhone',title:'电话',width:110}
								]],
								onDblClickRow:function(index,row){
									moveRow(selectUserPopup.find('.empsGrid'), selectUserPopup.find('.selectedEmpsGrid'),row,index,multiple);
								}
							});
						}
					},
					onClickIcon:function (index){
						selectUserPopup.find('.empsGrid').datagrid({
							method:method,
							url:url+'q='+encodeURI(newValue),
							columns:[[
								{field:'accountUuid',title:'id',width:10,hidden:true},
								{field:'empName',title:'姓名',width:100},
								{field:'empCode',title:'工号',width:60},
								{field:'empEmail',title:'email',width:150},
								{field:'empPhone',title:'电话',width:110}
							]],
							onDblClickRow:function(index,row){
								moveRow(selectUserPopup.find('.empsGrid'), selectUserPopup.find('.selectedEmpsGrid'),row,index,multiple);
							}
						});
					}
				});
				selectUserPopup.find('.saveBtn').on({
					'click':function(e){
						var rows =  selectUserPopup.find('.selectedEmpsGrid').datagrid('getRows');
						if(multiple){
							var values = [];
							var text ='';
							$.each(rows,function(index,row){
								values.push(row.accountUuid);
								if(index < rows.length-1){
									text += row.empName +",";
								}else{
									text += row.empName;
								}
							});
							$(target).combo("setValues",values);
							$(target).textbox("setText",text);
						}else{
							if(rows.length > 0){
								var row  = rows[0];
								$(target).combo("setValue",row.accountUuid);
								$(target).textbox("setText",row.empName);
								
								var  options =$(target).combo("options");
								if(options.onSelectUser){
									options.onSelectUser.call(this,rows);
								}														
								$(target).combo("hidePanel");
							}else{
								//MsgUtil.showError("请选择一个人！");
								//$(target).combo("hidePanel");
							}
						}
						$(target).combo("hidePanel");
					}
				});
				selectUserPopup.find('.clearBtn').on({
					'click':function(e){
						$(target).textbox("setValue",'');
						$(target).textbox("setText",'');
						selectUserPopup.find('.empsGrid').datagrid('reload');
						selectUserPopup.find('.selectedEmpsGrid').datagrid('loadData',[]);
					}
				});
				
				selectUserPopup.find('.closeBtn').on({
					'click':function(e){
						$(target).combo("hidePanel");
					}					
				});				
				
				selectUserPopup.find('.deptTree').tree({
					height:280,
					url:contextPath+'/rest/hr/hrDepartment/loadDeparmentByTree',
					onSelect: function(node){
						if(node.id){		
							selectUserPopup.find('.empsGrid').datagrid({
								method:method,
								url:url+'deptId='+node.id,
								columns:[[
									{field:'accountUuid',title:'id',width:10,hidden:true},
									{field:'empName',title:'姓名',width:100},
									{field:'empCode',title:'工号',width:60},
									{field:'empEmail',title:'email',width:150},
									{field:'empPhone',title:'电话',width:110}
								]],
								onDblClickRow:function(index,row){
									moveRow(selectUserPopup.find('.empsGrid'), selectUserPopup.find('.selectedEmpsGrid'),row,index,multiple);	
								}
							});
						}
					}
				});

				selectUserPopup.find('.empsGrid').datagrid({
					columns:[[
						{field:'accountUuid',title:'id',width:10,hidden:true},
						{field:'empName',title:'姓名',width:100},
						{field:'empCode',title:'工号',width:60},
						{field:'empEmail',title:'email',width:150},
						{field:'empPhone',title:'电话',width:110}
					]],
					onDblClickRow:function(index,row){
						moveRow(selectUserPopup.find('.empsGrid'), selectUserPopup.find('.selectedEmpsGrid'),row,index,multiple);
					}
				});

				selectUserPopup.find('.selectedEmpsGrid').datagrid({
					columns:[[    
					    {field:'accountUuid',title:'id',width:10,hidden:true},
						{field:'empName',title:'姓名',width:100},
						{field:'empCode',title:'工号',width:60},
						{field:'empEmail',title:'email',width:150},
						{field:'empPhone',title:'电话',width:110}
					]],
					onDblClickRow:function(index,row){
						selectUserPopup.find('.selectedEmpsGrid').datagrid('deleteRow',index);	
						if(!existedRow(selectUserPopup.find('.empsGrid').datagrid('getRows'),row)){
							selectUserPopup.find('.empsGrid').datagrid('appendRow',row);
						}
					}
				});
			
				_4.onShowPanel.call(this);
				if(selectedRows&&(selectedRows.length>0)){
					selectUserPopup.find('.selectedEmpsGrid').datagrid('loadData',selectedRows);
				}
			}
		}));
		if (!_3.user) {
			var _9 = $(target).combo("panel");	
			
			var html_popup = '';
			html_popup += '<div id="'+ id +'" class="ass33" style="display: block;overflow: auto; border:1px solid #bbb;border-bottom:none">';						
			html_popup += '	<div style="border-bottom: 1px solid #ccc;">';
			html_popup += '	<div style="width:200px;float: left;margin:5px 0 0 5px">';
			html_popup += '		<input class="easyui-textbox" style="width:99%;" type="text" id="seachUser" name="seachUser" data-options="prompt:\'输入关键字搜索\',iconCls:\'icon-backSearch\',iconAlign:\'right\'"></input>';
			html_popup += '	</div>';			
			html_popup += '	<div style="float: right;margin:3px 15px 3px 0">';
			html_popup += '		<a class="easyui-linkbutton saveBtn" data-options="iconCls:\'icon-ok\'" style="border-radius:5px;">确定</a>';
			html_popup += '		<a class="easyui-linkbutton clearBtn" data-options="iconCls:\'icon-remove\'" style="margin-left:5px;border-radius:5px;">清除</a>';
			html_popup += '		<a class="easyui-linkbutton closeBtn" style="margin-left:5px;border-radius:5px;" data-options="iconCls:\'icon-cancel\'" >返回</a>';
			html_popup += '	</div>';
			html_popup += '	<div class="clearfix">';
			html_popup += '	</div>';
			html_popup += '	</div>';
			html_popup += '	<div style="width:200px;height: 305px; float:left; overflow: auto; margin-left:5px;">';			
			html_popup += '		<ul class="deptTree" style="height:300px"></ul>';
			html_popup += '	</div>';
			html_popup += '	<div style="width:330px;height: 305px;float:left;margin-left: 5px; ">';
			html_popup += '		待选人员（<b style="color:red">双击选择</b>）';
			html_popup += '		<table class="empsGrid" style="height:285px;"></table>';
			html_popup += '	</div>	';
			html_popup += '	<div style="width:330px;height: 305px;float:left; margin-left: 5px; ">';
			html_popup += '		已选人员（<b style="color:red">双击取消</b>）';
			html_popup += '		<table class="selectedEmpsGrid"  style="height:285px"></table>';
			html_popup += '	</div>';
			html_popup += '</div>';
			
			_9.panel({content:html_popup});
			_9.panel("refresh");
			$("#"+id+" .textbox-button-right").addClass("searchBoxIcon");
		}
	};
	
	function existedRow(rows,row){
		for(var i=0;i<rows.length;i++){
			if(rows[i].accountUuid==row.accountUuid){
				return true;
			}
		}
		return false;
	}
	
	function moveRow(_from,_to,row,rowIndex,multiple){
		var rows = _to.datagrid('getRows');	

		if(!multiple){
			$.each(rows,function(index,item){
				_to.datagrid('deleteRow',index);
				_from.datagrid('appendRow',item);
			});
			_to.datagrid('appendRow',row);
			_from.datagrid('deleteRow',rowIndex);	
		}else{
			//去重
			if(!existedRow(rows,row)){
				_to.datagrid('appendRow',row);
				_from.datagrid('deleteRow',rowIndex);
			}
		}
	}

	function _11(_12) {
		var _13 = $.data(_12, "usercombo");
		var _14 = _13.options;
		var _15 = _13.grid;
		var vv = [];
		if (_14.multiple) {
			vv = $.map(_15.treegrid("getCheckedNodes"), function(row) {
				return row[_14.idField];
			});
		} else {
			var row = _15.treegrid("getSelected");
			if (row) {
				vv.push(row[_14.idField]);
			}
		}
		vv = vv.concat(_14.unselectedValues);
		_16(_12, vv);
	};

	function _16(_17, _18) {
		var _19 = $.data(_17, "usercombo");
		var _1a = _19.options;
		var _1b = _19.grid;
		
		if (!$.isArray(_18)) {
			_18 = _18.split(_1a.separator);
		}
		if (!_1a.multiple) {
			_18 = _18.length ? [_18[0]] : [""];
		}
		var vv = $.map(_18, function(_1c) {
			return String(_1c);
		});
		vv = $.grep(vv, function(v, _1d) {
			return _1d === $.inArray(v, vv);
		});
		
		var ss = [];
		_1a.unselectedValues = [];
		
		if(!$.isArray(_18)){
			if (!_19.remainText) {
				AjaxUtil.get(contextPath+"/rest/hr/hrEmployees/get.do?empUuid="+_18,function(data){				
					$(_17).combo("setText", data.empName);
					selectedRows = [];
					selectedRows.push(data);
				});
			}
			$(_17).combo("setValue", vv);
		}else{
			AjaxUtil.get(contextPath+"/rest/hr/hrEmployees/getByUuids.do?empUuids="+_18,function(data){				
				var text ='';
				$.each(data,function(index,row){
					if(index < data.length-1){
						text += row.empName +",";
					}else{
						text += row.empName;
					}
					selectedRows = data;
				});
				$(_17).combo("setText", text);
			});
		}

		function _20(_21, a) {
			var _22 = $.easyui.getArrayItem(a, _1a.idField, _21);
			return _22 ? _1f(_22) : undefined;
		};

		function _1f(row) {
			return row[_1a.textField || ""] || row[_1a.treeField];
		};
	};
	
	function _23(_24, q) {
		var _25 = $.data(_24, "usercombo");
		var _26 = _25.options;
		var _27 = _25.grid;
		_25.remainText = true;
		_27.treegrid("clearSelections").treegrid("clearChecked").treegrid("highlightRow", -1);
		if (_26.mode == "remote") {
			$(_24).usercombo("clear");
			_27.treegrid("load", $.extend({}, _26.queryParams, {
				q: q
			}));
		} else {
			if (q) {
				var _28 = _27.treegrid("getData");
				var vv = [];
				var qq = _26.multiple ? q.split(_26.separator) : [q];
				$.map(qq, function(q) {
					q = $.trim(q);
					if (q) {
						var v = undefined;
						$.easyui.forEach(_28, true, function(row) {
							if (q.toLowerCase() == String(row[_26.treeField]).toLowerCase()) {
								v = row[_26.idField];
								return false;
							} else {
								if (_26.filter.call(_24, q, row)) {
									_27.treegrid("expandTo", row[_26.idField]);
									_27.treegrid("highlightRow", row[_26.idField]);
									return false;
								}
							}
						});
						if (v == undefined) {
							$.easyui.forEach(_26.mappingRows, false, function(row) {
								if (q.toLowerCase() == String(row[_26.treeField])) {
									v = row[_26.idField];
									return false;
								}
							});
						}
						if (v != undefined) {
							vv.push(v);
						}
					}
				});
				_16(_24, vv);
				_25.remainText = false;
			}
		}
	};

	function _29(_2a) {
		_11(_2a);
	};
	$.fn.usercombo = function(options, param) {

		if (typeof options == "string") {
			var _2d = $.fn.usercombo.methods[options];
			if (_2d) {
				return _2d(this, param);
			} else {
				return this.combo(options, param);
			}
		}
		options = options || {};
		return this.each(function() {
			var _2e = $.data(this, "usercombo");
			if (_2e) {
				$.extend(_2e.options, options);
			} else {
				_2e = $.data(this, "usercombo", {
					options: $.extend({}, $.fn.usercombo.defaults, $.fn.usercombo.parseOptions(this), options)
				});
			}
			init(this);
		});
	};
	$.fn.usercombo.methods = {
		options: function(jq) {
			var _2f = jq.combo("options");
			return $.extend($.data(jq[0], "usercombo").options, {
				width: _2f.width,
				height: _2f.height,
				originalValue: _2f.originalValue
			});
		},
		selectUserPopup: function(jq) {
			return $.data(jq[0], "usercombo").selectUserPopup;
		},
		setValues: function(jq, _30) {
			return jq.each(function() {
				var _31 = $(this).usercombo("options");
				if ($.isArray(_30)) {
					_30 = $.map(_30, function(_32) {
						if (_32 && typeof _32 == "object") {
							$.easyui.addArrayItem(_31.mappingRows, _31.idField, _32);
							return _32[_31.idField];
						} else {
							return _32;
						}
					});
				}
				_16(this, _30);
			});
		},
		setValue: function(jq, _33) {
			return jq.each(function() {				
				$(this).usercombo("setValues", $.isArray(_33) ? _33 : [_33]);
			});
		},
		clear: function(jq) {
			return jq.each(function() {
				$(this).usercombo("setValues", []);
			});
		},
		reset: function(jq) {
			return jq.each(function() {
				var _34 = $(this).usercombo("options");
				if (_34.multiple) {
					$(this).usercombo("setValues", _34.originalValue);
				} else {
					$(this).usercombo("setValue", _34.originalValue);
				}
			});
		}
	};
	$.fn.usercombo.parseOptions = function(_35) {
		var t = $(_35);
		return $.extend({}, $.fn.combo.parseOptions(_35), $.fn.treegrid.parseOptions(_35), $.parser.parseOptions(_35, ["mode", {
			limitToGrid: "boolean"
		}]));
	};
	$.fn.usercombo.defaults = $.extend({}, $.fn.combo.defaults, $.fn.treegrid.defaults, {
		editable: false,
		singleSelect: true,
		limitToGrid: false,
		unselectedValues: [],
		mappingRows: [],
		mode: "local",
		textField: null,
		keyHandler: {
			up: function(e) {},
			down: function(e) {},
			left: function(e) {},
			right: function(e) {},
			enter: function(e) {
				_29(this);
			},
			query: function(q, e) {
				_23(this, q);
			}
		},
		inputEvents: $.extend({}, $.fn.combo.defaults.inputEvents, {
			blur: function(e) {
				var _36 = e.data.target;
				var _37 = $(_36).usercombo("options");
				if (_37.limitToGrid) {
					_29(_36);
				}
			}
		}),
		filter: function(q, row) {
			var _38 = $(this).usercombo("options");
			return (row[_38.treeField] || "").toLowerCase().indexOf(q.toLowerCase()) >= 0;
		},
		onSelectUser:function(e){}
	});
})(jQuery);
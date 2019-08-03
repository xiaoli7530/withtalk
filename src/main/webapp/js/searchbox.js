(function($) {
    function init(_2) {
        var _3 = $.data(_2, "tagbox");
        var _4 = _3.options;
        $(_2).addClass("tagbox-f").combo($.extend({}, _4, {
            cls: "tagbox white_style",
            reversed: true,
            onChange: function(_5, _6) {
            	renderTagBox(_2);
                $(this).combo("hidePanel");
                _4.onChange.call(_2, _5, _6);
            },
            onResizing: function(_8, _9) {
                var _a = $(this).combo("textbox");
                _a.css({
                    borderBottom:'none'
                });
                
                var tb = $(this).data("textbox").textbox;
                tb.css({
                    height: "",
                    paddingLeft: _a.css("marginLeft"),
                    paddingRight: _a.css("marginRight"),
                });
                _a.css("margin", 0);
                tb._size({
                    width: _4.width
                }, $(this).parent());
                tb.click(function(){
                	_a.focus()
                })
                _23(_2);
                _12(this);
                _4.onResizing.call(_2, _8, _9);
            },
            onLoadSuccess: function(_b) {
            	renderTagBox(_2);
                _4.onLoadSuccess.call(_2, _b);
            }
        }));
        $.fn.ctSearcher.methods.flushTagBox = function(instance){
        	renderTagBox(instance);
        }
        initDropList();
        initQueryBtn();
        initExtInputsEvent();
        _23(_2);
        //绑定其他空间，回车事件
        function initExtInputsEvent(){
        	var extInputs = _4.extInputs;
        	if(extInputs){
        		$.map(extInputs,function(obj,index){
        			var $object = $("#" + obj.id);
        			var objectClass = $object.attr("class");
        			if(!objectClass){
        				return true;
        			}
        			if(objectClass.indexOf("easyui-combobox") > -1){
        				$object.combobox({onChange:function(){
        					queryGrid(_2);
        				}});
        			}else if(objectClass.indexOf("easyui-datebox") > -1){
        				$object.datebox({onChange:function(){
        					queryGrid(_2);
        				}});
        			}else if(objectClass.indexOf("easyui-combotree") > -1){
        				$object.combotree({onChange:function(){
        					queryGrid(_2);
        				}});
        			}else if(objectClass.indexOf("easyui-datetimebox") > -1){
        				$object.datetimebox({onChange:function(){
        					queryGrid(_2);
        				}});
        			}else if(objectClass.indexOf('textbox')>-1){
        				$object.textbox('textbox').bind('keydown', function(e){
        					if (e.keyCode == 13){
        						queryGrid(_2);
        					}
        				});
        			}else if(objectClass.indexOf('textbox')>-1){
        				$object.textbox('textbox').bind('keydown', function(e){
        					if (e.keyCode == 13){
        						queryGrid(_2);
        					}
        				});
        			}else if(objectClass.indexOf('ct-checkbox')>-1){
        				$object.bind('change', function(e){
        					queryGrid(_2);
        				});
        			}else{
        				$object.combo({
                			keyHandler:{
                    			enter:function(jq){
                    				queryGrid(_2);
                    			}
                    		},
                    		onChange : function(){
                    			queryGrid(_2);
                    		}
        				});
        			}
        			
        		});
        	}
        }
        function strOverDeal(str){
        	if(!StringUtil.isEmpty(str)){
        		if(str.length > 15){
        			return str.substr(0,12) + "...";
        		}
        		return str;
        	}
        	return "";
        }
        function initDropList(){//初始化下拉内容
        	var panel$ = $(_2).combo('panel');
        	panel$.html('');// 清空
            var _4 = $(_2).combo('options');
        	
        	var queryFields = _4.queryFields;
        	if(queryFields && queryFields.length > 0){
        		 $.map(queryFields,function(item,index){
        			 var copyItem = _.cloneDeep(item);
        			 var isDict = copyItem.type=='dict';
        			 var div = $( "<div class='combobox-item "+(isDict?"fa-caret-right":"")+"' style='margin-left: 10px; font-family: FontAwesome;' ><span>搜索 &nbsp;"+copyItem.label+"</span>：<span class='flush_span'></span><div><div></div>")
        			 div.appendTo(panel$);
        			 div.data($.fn.ctSearcher.defaults.selectItemData,copyItem);
        			 
        			 //字典
        			 if(isDict){
        				 div.css({marginLeft: "5px"});
        				 var dicts = window.dicts[copyItem.dict];
        				 var contain = div.find('div');
        				 
        				 var html = "<ul style='display:none;margin-bottom: 0px'>";
        				 for(i = 0 ;i < dicts.length ; i++){//code
        					 html += "<li style='margin-left: 50px;margin-bottom: 2px;font-size: 14px;font-style: italic;' data-value="+dicts[i].code+" data-name="+dicts[i].name+">"+dicts[i].name+"</li>";
        				 }
        				 html += "</ul>";
    					 contain.html(html);
    					 
    					 contain.find("li").click(function(){
    						 var item_ = copyItem;
    						 var inputVal = $(this).attr('data-value');
            				 item_.value = inputVal;
            				 item_.dictName = $(this).attr('data-name');
            				 item_.isDictItem = true;
            				 $(_2).ctSearcher("setSelectOption",item_);
            				 renderTagBox(_2);
            				 $(_2).combo("hidePanel");
            				 queryGrid(_2);
    					 });
    					 
    					 div.click(function(){
    						 var ul = $(this).find("ul");
    						 if(ul.css("display") === 'none'){
    							 ul.css({"display":"inline"})
    							 div.removeClass('fa-caret-right').addClass('fa-caret-down').css({marginLeft: "1px"});
    						 }else{
    							 ul.css({"display":"none"})
    							 div.removeClass('fa-caret-down').addClass('fa-caret-right').css({marginLeft: "5px"});
    						 }
    					 })
        				 
        			 }else{
        				 div.click(function(){
            				 var item_ = copyItem;
            				 var inputVal = div.find('.flush_span').attr('data-option-value');
            				 if(item_.type == 'date'){
            					 if(!ValidationUtil.isYYYY_MM_DD(inputVal)){
            						 MsgUtil.showError("请正确输入日期格式!");
            						 return ;
            					 }
            				 }
            				 item_.value = inputVal;
            				 $(_2).ctSearcher("setSelectOption",item_);
            				 renderTagBox(_2);
            				 $(_2).combo("hidePanel");
            				 queryGrid(_2);
            			 });
        			 }
        			 
        		 });
        	}else{
        		//alert('无效的查询字段!');
        	}
        }
        function initQueryBtn(){
        	var textbox = $(_2).ctSearcher("textbox");
        	var options = $(_2).ctSearcher("options");
        	   
        	var btn = $('<span class="textbox-addon textbox-addon-right" style="right: 20px; top: 0px;"><a href="javascript:;" class="textbox-icon searchbox-button" title="查询" icon-index="0" tabindex="-1" style="width: 18px; height: 31px;"></a></span>');
        	btn.insertBefore(textbox);
        	
        	var btn2 = $('<span class="textbox-addon textbox-addon-right" style="right: 0px; top: 0px;"><a href="javascript:;" class="textbox-icon icon-clear" title="重置" icon-index="0" tabindex="-1" style="width: 18px; height: 31px;"></a></span>');
        	btn2.insertBefore(textbox);
        	btn.click(queryGrid.bind(this,_2));
        	btn2.click(function(){
        		$(_2).ctSearcher("clearQuery");
        	});
        }
        
        ;function renderTagBox(_2){ // _7代替方法
        	$(_2).next().find(".tagbox-label").remove();
            var _c = $(_2).ctSearcher("textbox");
            var ss = [];
            // TODO
            var selectOptions = $(_2).ctSearcher("getSelectOptions");
            $.map(selectOptions, function(_d, _e) {
            	var _f = null;
                var cs = {};
                var css = _4.tagStyler.call(_2, _d, _f) || "";
                if (typeof css == "string") {
                    cs = {
                        s: css
                    };
                } else {
                    cs = {
                        c: css["class"] || "",
                        s: css["style"] || ""
                    };
                }
                var labelSpan = "<span>" + _d.label + ":</span>" ;
                
                var displayValue = _d.isDictItem ? _d.dictName : _d.value;
                
                var valueSpan = "<span>" + strOverDeal(displayValue) +"</span>"  ;
                
                if( _d.isDictItem){
                	_d.type = 'string';
                }
                var _11 = $("<span class=\"tagbox-label\"></span>").insertBefore(_c).html(labelSpan+valueSpan);
                _11.attr("tagbox-index", _e);
                _11.attr("data-option-field",_d.field);
                _11.attr("style", cs.s).addClass(cs.c);
                $("<a href=\"javascript:;\" class=\"tagbox-remove\"></a>").appendTo(_11);
            });
            _12(_2);
            $(_2).combo("setText", "");
        };
    }
    
    ;function _12(_13, _14) {
        var _15 = $(_13).next();
        var _16 = _14 ? $(_14) : _15.find(".tagbox-label");
        if (_16.length) {
            var _17 = $(_13).ctSearcher("textbox");
            var _18 = $(_16[0]);
            var _19 = _18.outerHeight(true) - _18.outerHeight();
            var _1a = _17.outerHeight() - _19 * 2;
            _16.css({
                height: _1a + "px",
                lineHeight: _1a + "px"
            });
            var _1b = _15.find(".textbox-addon").css("height", "100%");
            _1b.find(".textbox-icon").css("height", "100%");
            _15.find(".textbox-button").linkbutton("resize", {
                height: "100%"
            });
        }
    }
    ;function _1c(_1d) {
        var _1e = $(_1d).next();
        _1e.unbind(".tagbox").bind("click.tagbox", function(e) {
            var _1f = $(_1d).ctSearcher("options");
            // 删除标签调用
            if ($(e.target).hasClass("tagbox-remove")) {
                var fieldName = $(e.target).parent().attr("data-option-field");
                var selOpts = $(_1d).ctSearcher("getSelectOptions");
              
                var newSelOpts = [];
                var delSelOpts = [];
                
                for(i = 0;i< selOpts.length;i++){
                	if(selOpts[i].field != fieldName){
                		newSelOpts.push(selOpts[i]);
                	}else{
                		delSelOpts.push(selOpts[i]);
                	}
                }
                
                if (_1f.onBeforeRemoveTag.call(_1d, delSelOpts) == false) {
                    return;
                }
                _1f.onRemoveTag.call(_1d, delSelOpts);
                $(_1d).ctSearcher('setSelectOptions',newSelOpts);
                $(_1d).ctSearcher("flushTagBox");
                if(newSelOpts.length < 1){
                	$(_1d).combo("hidePanel");
                }
                queryGrid(_1d)
            } else {
                var _22 = $(e.target).closest(".tagbox-label");
                if (_22.length) {
                    var _20 = parseInt(_22.attr("tagbox-index"));
                    var _21 = $(_1d).ctSearcher("getValues");
                    _1f.onClickTag.call(_1d, _21[_20]);
                }
            }
            $(_1d).find(".textbox-text").focus();
        }).bind("keyup.tagbox", function(e) {
            _23(_1d);
        }).bind("mouseover.tagbox", function(e) {
            if ($(e.target).closest(".textbox-button,.textbox-addon,.tagbox-label").length) {
                $(this).triggerHandler("mouseleave");
            } else {
                $(this).find(".textbox-text").triggerHandler("mouseenter");
            }
        }).bind("mouseleave.tagbox", function(e) {
            $(this).find(".textbox-text").triggerHandler("mouseleave");
        });
    }
    ;function _23(_24) {
        var _25 = $(_24).ctSearcher("options");
        var _26 = $(_24).ctSearcher("textbox");
        var _27 = $(_24).next();
        var tmp = $("<span></span>").appendTo("body");
        tmp.attr("style", _26.attr("style"));
        tmp.css({
            position: "absolute",
            top: -9999,
            left: -9999,
            width: "auto",
            fontFamily: _26.css("fontFamily"),
            fontSize: _26.css("fontSize"),
            fontWeight: _26.css("fontWeight"),
            whiteSpace: "nowrap"
        });
        var _28 = _29(_26.val());
        var _2a = _29(_25.prompt || "");
        tmp.remove();
        var _2b = Math.min(Math.max(_28, _2a) + 20, _27.width());
        _26._outerWidth(_2b);
        _27.find(".textbox-button").linkbutton("resize", {
            height: "100%"
        });
        function _29(val) {
            var s = val.replace(/&/g, "&amp;").replace(/\s/g, " ").replace(/</g, "&lt;").replace(/>/g, "&gt;");
            tmp.html(s);
            return tmp.outerWidth();
        }
        ;
    }
    //回车处理
    ;function enterHandler(_this) { 
    	queryGrid(_this);
    }
    ;function _33(_34, _35) {
        $(_34).combo("setText", "");
        _23(_34);
        $(_34).combo("setValues", _35);
        $(_34).combo("setText", "");
        $(_34).ctSearcher("validate");
    }
    ;function flushInputVal(val,target){// 下拉刷新
    	var panel$ = $(target).combo('panel');
        panel$.find('.combobox-item').find('.flush_span').attr('data-option-value',val).html(val);
    }
    ;function queryGrid(_this){
    	var options = $(_this).ctSearcher('options');
    	if(options.clearing === true){
    		console.log('正在清空，忽视查询....');
    		return ;
    	}
    
    	//拼接查询数据
    	var queryBody = {filter:{ logic:'and',filters:[]},pageSize:100,sortField:'',sortOrder:''};
    	var extInputs = options.extInputs;
    	// 自定义的输入框
    	for(i = 0;i<extInputs.length;i++){
    		var cfg = extInputs[i],operator = cfg.operator,value='';
    		var $ext = $("#" + cfg.id);
    		value = easyUi.getVal( cfg.id);
    		if(value != ''  && !_.isNil(value)){
    			queryBody.filter.filters.push(buildQueryFilter(cfg,value));
    		}
    	}
    	
    	var selectOptions = $(_this).ctSearcher('getSelectOptions');
    	for(i = 0 ;i < selectOptions.length ; i++){
    		var cfg = selectOptions[i],operator = cfg.operator,value='';
    		queryBody.filter.filters.push(buildQueryFilter(cfg,cfg.value));
    	}
	   	if (options.onBeforeQuery.call(_this,$(_this),queryBody) == false) {
	         return;
	    }

	  	var options1 = $('#' + options.gridId).datagrid('options');
	   	if(options1.isTree){
	   		$('#' + options.gridId).treegrid('load',queryBody);
	   	}else{
	   		$('#' + options.gridId).datagrid('load',queryBody);
	   	}
    	
    }
    function buildQueryFilter(cfg,value){
    	var operator = cfg.operator;
    	if(!cfg.operator){
			if(cfg.type == 'date'){
				operator = 'eq';
			}else{
				operator = 'contains';
			}
		}
    	if(_.isNil(value)){
    		value = '';
    	}
		return {
			field:cfg.field,
			operator:operator,
			type:cfg.type,
			value:value.trim(),
			format:cfg.format
		};
    }
    ;$.fn.ctSearcher = function(_36, _37) {
        if (typeof _36 == "string") {
            var _38 = $.fn.ctSearcher.methods[_36];
            if (_38) {
                return _38(this, _37);
            } else {
                return this.combo(_36, _37);
            }
        }
        _36 = _36 || {};
        return this.each(function() {
            var _39 = $.data(this, "tagbox");
            if (_39) {
                $.extend(_39.options, _36);
            } else {
                $.data(this, "tagbox", {
                    options: $.extend({}, $.fn.ctSearcher.defaults, $.fn.ctSearcher.parseOptions(this), _36)
                });
            }
            init(this);
            _1c(this);
        });
    }
    ;
    $.fn.ctSearcher.methods = {
        options: function(jq) {
            var _3a = jq.combo("options");
            return $.extend($.data(jq[0], "tagbox").options, {
                width: _3a.width,
                height: _3a.height,
                originalValue: _3a.originalValue,
                disabled: _3a.disabled,
                readonly: _3a.readonly
            });
        },
        setValues: function(jq, _3b) {
            return jq.each(function() {
                _33(this, _3b);
            });
        },
        getSelectOptions:function(jq){
        	var selectOpts = jq.data($.fn.ctSearcher.defaults.selectOptionsKey);
        	if(selectOpts){
        		return selectOpts;
        	}
        	return [];
        },
        setSelectOption:function(jq,dataOpt){
        	var selectOpts = jq.data($.fn.ctSearcher.defaults.selectOptionsKey);
        	if(selectOpts){
        		var isSetVal = true;
        		$.map(selectOpts,function(item){
        			if(item.field === dataOpt.field && item.type != 'date'){
        				isSetVal = false;
            			return false;
        			}
        		});
        		if(isSetVal){
        			selectOpts.push(dataOpt);
        			jq.data($.fn.ctSearcher.defaults.selectOptionsKey,selectOpts);
        		}
        	}else{
        		var t = [dataOpt];
    			jq.data($.fn.ctSearcher.defaults.selectOptionsKey,t);
        	}
        },
        setSelectOptions:function(jq,dataOpts,isConcat){
        	var selectOpts = [];
        	if(isConcat){
        		var r = jq.data($.fn.ctSearcher.defaults.selectOptionsKey);
        		selectOpts = selectOpts.concat(r);
        	}
        	selectOpts.concat(jq.data($.fn.ctSearcher.defaults.selectOptionsKey));
        	jq.data($.fn.ctSearcher.defaults.selectOptionsKey,dataOpts);
        },
        //手动调用查询方法
        doSearch : function(jq){
        	queryGrid(jq);
        },
        
        clearQuery : function(jq){
        	var options = $(jq).ctSearcher('options');
        	
        	//触发清空事件
            if(options.onClear(jq) === false){
            	return;
            }
        	//拼接查询数据
        	var queryBody = {filter:{ logic:'and',filters:[]},pageSize:100,sortField:'',sortOrder:''};
        	var extInputs = options.extInputs;
        	options.clearing = true;
        	try{
	        	// 自定义的输入框
	        	for(var ii = 0;ii<extInputs.length;ii++){
	        		var cfg = extInputs[ii];
	        		var $ext = $("#" + cfg.id);
	        		console.log("查询控件清空中：" + cfg.id);
	        		if($ext.data("combo")){
	        			$ext.combo("clear");
	        		}else if($ext.data("textbox")){
	        			$ext.textbox("clear")
	        		}else if($ext.get(0).type == 'checkbox'){
	        			$ext.removeAttr("checked");
	        		}else{
	        			$ext.val("");
	        		}
	        	}
        	}catch(e){
        		console.error(e);
        	}
        	options.clearing = false;
        	$(jq).ctSearcher('setSelectOptions',[]);
            $(jq).ctSearcher("flushTagBox");
            queryGrid(jq);
           
        }
    };
    $.fn.ctSearcher.parseOptions = function(_3c) {
        return $.extend({}, $.fn.combo.parseOptions(_3c), $.parser.parseOptions(_3c, []));
    }
    ;
    $.fn.ctSearcher.defaults = $.extend({}, $.fn.combo.defaults, {
        hasDownArrow: false,
        multiple: true,
        reversed: true,
        selectOptionsKey:'search-box-data',
        selectItemData:'search-item-data',
        selectOnNavigation: false,
        tipOptions: $.extend({}, $.fn.textbox.defaults.tipOptions, {
            showDelay: 200
        }),
        val: function(_3d) {
            var vv = $(_3d).parent().prev().ctSearcher("getValues");
            if ($(_3d).is(":focus")) {
                vv.push($(_3d).val());
            }
            return vv.join(",");
        },
        inputEvents: $.extend({}, $.fn.combo.defaults.inputEvents, {
            blur: function(e) {
                var _3e = e.data.target;
                var _3f = $(_3e).ctSearcher("options");
                if (_3f.limitToList) {
                    _2c(_3e);
                }
            }
        }),
        keyHandler: $.extend({}, $.fn.combo.defaults.keyHandler, {
            enter: function(e) {
            	enterHandler(this);
            },
            query: function(q, e) {
                var _40 = $(this).ctSearcher("options");
                if(!_.isNil(q) && q != ""){
                	flushInputVal(q,e.data.target);
                }else{
                	$(this).combo("hidePanel");
                }
            }
        }),
        tagFormatter: function(_41, row) {
            var _42 = $(this).ctSearcher("options");
            return row ? row[_42.textField] : _41;
        },
        tagStyler: function(_43, row) {
            return "";
        },
        onClickTag: function(_44) {},
        onBeforeRemoveTag: function(_45) {},
        onRemoveTag: function(_46) {},
        onBeforeQuery:function(jq){},//在查询之前调用，如果返回false 终止查询
        onClear:function(jq){}//清空事件
    });
  
})(jQuery);

$(function(){
	$('.easyui-ctSearcher').ctSearcher();
});
/**
 * eg：
 //$('.easyui-ctSearcher').ctSearcher('doSearch');  //执行查询
 //$('.easyui-ctSearcher').ctSearcher({onBeforeQuery:function(jq){alert(1)}})//绑定查询前事件，返回false 取消查询
 */

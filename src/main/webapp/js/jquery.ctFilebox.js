(function($) {
    var _1 = 0;

    function init(_3) {
        var _4 = $.data(_3, "ctfilebox");
        var _5 = _4.options;
        
        var isInit = false;
        //如果出示过，则不进行变量更改
        if(!_5.ctfileboxId){
	        _5.ctfileboxId = "ctfilebox_file_id_" + (++_1);
	        _5.downloadATagId = "ctfilebox_a_tag_id_" + _1;
	        //formid
	        _5.ctfileboxformId = "ctfilebox_file_form_id_" + (_1);
	        _5.attId = "att_id_" + (_1);
	        var downAtag = $('<a style="margin-left: 10px;color:blue;display:none;"></a>')
	        downAtag.attr("id",_5.downloadATagId);
	        $(_3).after(downAtag);
        }
        
        $(_3).addClass("ctfilebox-f").textbox(_5).removeClass("textbox-f");
        $(_3).textbox("textbox").attr("readonly", "readonly");
        _4.ctfilebox = $(_3).next().addClass("filebox");
        var _6 = _7(_3);
        var _8 = $(_3).ctfilebox("button");
        if (_8.length) {
            var label$ = $("<label class=\"filebox-label\" for=\"" + _5.ctfileboxId + "\"></label>");
           /* label$.click(function(){
            	$("#" + _5.ctfileboxId).click();
            })*/
            label$.appendTo(_8);
            if (_8.linkbutton("options").disabled) {
                _6.attr("disabled", "disabled");
            } else {
                _6.removeAttr("disabled");
            }
        }
        var icon = $(_3).textbox('getIcon',0);
        icon.css('visibility','hidden');
        icon1 = $(_3).textbox('getIcon',1);
        icon1.css('visibility','hidden');
    };

    function _7(_9) {
        var _a = $.data(_9, "ctfilebox");
        var _b = _a.options;
        _a.ctfilebox.find(".textbox-value").remove();
        _b.oldValue = "";
        
        //移除原有
        $("#"+_b.ctfileboxformId).remove();
        
        var from$ = $("<form method=\"post\" action=\""+_b.url+"\" enctype=\"multipart/form-data\" />");
        from$.attr("id",_b.ctfileboxformId);
        
        var hiddenComp = $("<input type=\"hidden\" class=\"textbox-value\" />").attr("name", $(_9).attr("textboxName") || "").attr("id",_b.attId).appendTo(_a.ctfilebox);
        
        from$.appendTo(_a.ctfilebox);
        var _c = $("<input type=\"file\" >").appendTo(from$);
        _c.attr("id", _b.ctfileboxId).attr("name", "file");
        _c.attr("accept", _b.accept);
        _c.attr("capture", _b.capture);
        //_c.css("display","none");
        
        _c.css({position:'absolute',verticalAlign: "top",top:0,left:"-5000px"});
        if (_b.multiple) {
            _c.attr("multiple", "multiple");
        }
       // _b.fileSuffix
        _c.change(function() {
            var _d = this.value;
            if (this.files) {
                _d = $.map(this.files, function(_e) {
                    return _e.name;
                }).join(_b.separator);
            }
            
          //  var isPass = true;
        	/*$.each(this.files,function(i,item){
        		 if(!checkFileSuffix(_b,item.name)){
        			 isPass = false;
        			 return false;
        		 }
        	});*/
            
        	if(!checkFileSuffix(_b,_d)){
        		$(_9).ctfilebox("clear");
        		MsgUtil.showError("仅支持后缀名为" + _b.fileSuffix.join(',') + "的文件!");
        		return false;
        	}
            
            $(_9).ctfilebox("setText", _d);
            _b.onChange.call(_9, _d, _b.oldValue);
            _b.oldValue = _d;
            
            if(_b.autoUpload){
            	$(_9).ctfilebox("upload");
            }
            iconsViewChange(_9);
        });
        
       /* $("#" + _b.ctfileboxId).change(function(){
        	iconsViewChange(_9);
        });*/
        //add by dais
        from$.form({
		    url:_b.url,
		    iframe: false,
		    ajax:true,
		    onSubmit: function(){
		    	
		    },
		    success:function(data){
		    	MsgUtil.unLoading();
		    	if(_b.isComm){
			    	try{
				    	data = $.parseJSON(data);
				    	//hiddenComp.val(data[_b.attachmentId]);
				    	_b.rAttachmentId = data[_b.attachmentId];
				    	 $(_9).ctfilebox("setValue", data[_b.attachmentId]);
				    	_b.uploadSuccess.call(_9,data);
			    	}catch(e){
			    		console.log(data);
			    		MsgUtil.showError("文件上传失败，请联系管理员!");
			    	}
		    	}else{
		    		_b.uploadSuccess.call(_9,data);
		    	}
		    },
		    onLoadError:function(e){
		    	MsgUtil.unLoading();
		    	console.log(e);
		    	MsgUtil.showError("文件上传失败，请联系管理员!");
		    	_b.uploadFailed.call(_9,e);
		    }
		});
        return _c;
    };
    function checkFileSuffix(ops,fileName){
    	var result = false;
    	if(ops.fileSuffix && ops.fileSuffix.length > 0){
    		$.each(ops.fileSuffix,function(index,item){
    			if(fileName.toLocaleLowerCase().endsWith("."+item.toLocaleLowerCase())){
    				result = true;
    				return false;
    			}
    		});
    	} else {
    		result = true;
    	}
    	return result;
    }
    function iconsViewChange(downDoc){
    	var icon = $(downDoc).textbox('getIcon',0);
    	var icon1 = $(downDoc).textbox('getIcon',1);
    	
    	var options = $(downDoc).ctfilebox("options");
    	if($(downDoc).ctfilebox("getValue")){
    		if(options.clearBtn){
    			icon.css('visibility','visible');
    		}
    		if(options.downBtn){
    			icon1.css('visibility','visible');
    		}
    	}else{
    		icon.css('visibility','hidden');
    		icon1.css('visibility','hidden');
    	}
    }
    $.fn.ctfilebox = function(_f, _10) {
        if (typeof _f == "string") {
            var _11 = $.fn.ctfilebox.methods[_f];
            if (_11) {
                return _11(this, _10);
            } else {
                return this.textbox(_f, _10);
            }
        }
        _f = _f || {};
        return this.each(function() {
            var _12 = $.data(this, "ctfilebox");
            if (_12) {
                $.extend(_12.options, _f);
            } else {
                $.data(this, "ctfilebox", {
                    options: $.extend({}, $.fn.ctfilebox.defaults, $.fn.ctfilebox.parseOptions(this), _f)
                });
            }
            init(this);
        });
    };
    $.fn.ctfilebox.methods = {
        options: function(jq) {
            var _13 = jq.textbox("options");
            return $.extend($.data(jq[0], "ctfilebox").options, {
                width: _13.width,
                value: _13.value,
                originalValue: _13.originalValue,
                disabled: _13.disabled,
                readonly: _13.readonly
            });
        },
        clear: function(jq) {
            return jq.each(function() {
                $(this).textbox("clear");
                iconsViewChange(this);
                _7(this);
            });
        },
        reset: function(jq) {
            return jq.each(function() {
                $(this).ctfilebox("clear");
            });
        },
        setValue: function(jq,value) {
        	  return jq.each(function() {
        		  if(value){
	        		  var options = $(this).ctfilebox("options");
	        		  $("#" + options.attId).val(value);
	        		  if(options.isComm){
		        		  var _this = this;
		        		  AjaxUtil.get(contextPath+"/rest/base/baseAttachment/get.do?attachmentUuid="+value,function(data){			
		        			  $(_this).ctfilebox("setText", data.name);
		        			  iconsViewChange(_this);
		      			  });
	        		  }
        		  }
              });
        }/*,
        getValue:function(jq){
//        	/return jq.data("textbox").textbox.find(".textbox-value").val();
        	
        	return $("#" + jq.ctfilebox("options").attId).val();
        }*/,
        setValues: function(jq) {
            return jq;
        },
		upload:function(jq,su ,fail){
			MsgUtil.loading();
			return jq.each(function() {
			    var _12 = $.data(this, "ctfilebox");
			    $("#"+_12.options.ctfileboxformId).form('options').url = _12.options.url;
			    //TODO
				$("#"+_12.options.ctfileboxformId).submit();
	        });
		},
		toView : function(jq){
			return jq.each(function(){
				var text = $(this).ctfilebox("getText");
				var value = $(this).ctfilebox("getValue");
				$(this).ctfilebox("textbox").parent().hide();
				if(StringUtil.isEmpty(value)){
					return;
				}
				var options = $(this).ctfilebox("options");
				var aTag = $("#" + options.downloadATagId);
				aTag.attr("data-href" , options.downloadUrl + value);
				aTag.click(function(){
					window.open($(this).attr("data-href"));
				})
				aTag.text("正在加载中....");
				aTag.show();
				if(StringUtil.isEmpty(text)){
	        		  if(options.isComm){
		        		  var _this = this;
		        		  AjaxUtil.get(contextPath+"/rest/base/baseAttachment/get.do?attachmentUuid="+value,function(data){			
		        			  $(_this).ctfilebox("setText", data.name);
		        			  aTag.text(data.name);
		      			  });
	        		  }
				}else{
					aTag.text(text);
				}
			});
		}
    };
    $.fn.ctfilebox.parseOptions = function(_14) {
        var t = $(_14);
        return $.extend({}, $.fn.textbox.parseOptions(_14), $.parser.parseOptions(_14, ["accept", "capture", "separator"]), {
            multiple: (t.attr("multiple") ? true : undefined)
        });
    };
    $.fn.ctfilebox.defaults = $.extend({}, $.fn.textbox.defaults, {
        buttonIcon: null,
        buttonText: "选择文件",
        iconWidth: 22,
        autoUpload:true,
        attachmentName:"file",
        attachmentId:'attachmentUuid',
        url : contextPath + '/rest/base/baseAttachment/uploadFile',
        downloadUrl:contextPath+"/rest/base/baseAttachment/downloadFiles?attachmentUuid=",
        icons: [{
            iconCls:'icon-file-download',
            handler: function(e){
            	var options = $(e.data.target).ctfilebox("options");
            	var aid = $(e.data.target).ctfilebox('getValue');
            	window.open(options.downloadUrl + aid);// options.rAttachmentId);
            }
        },{
            iconCls:'icon-clear',
            handler: function(e){
            	$(e.data.target).ctfilebox('clear')
            }
        }],
        buttonAlign: "right",
        inputEvents: {},
        accept: "",
        capture: "",
        separator: ",",
        multiple: false,
        uploadSuccess : function(data){},
        uploadFailed:function(data){},
        isComm : true,
        clearBtn : true,
        downBtn : true,
        fileSuffix:[]
    });
    $.parser.plugins.push('ctfilebox');
})(jQuery);
/******************************************************************************

 *	util类
 *目录：
 *	BreadcrumbUtil：菜单面包屑工具类
 *	DictUtil：数据字典工具类
 *	AjaxUtil：Ajax工具类
 *	ExcelUtil：Excel工具类
 *	WindowUtil：弹出窗口工具类
 *	StringUtil：字符串工具类
 *	ArrayUtil：数组工具类
 *	DateUtil：日期工具类
 *	MsgUtil：消息提示框工具类
 *	ValidationUtil：校验工具类
 *	DomUtil：操作DOM的工具类
 *	RoleManager：权限按钮控制工具类
 *	DatagridUtil：easyUi datagrid工具类
 *	TreegridUtil：easyUi treegrid工具类
 *  FilterUtil: easyUi 列筛选工具类
 * ****************************************************************************/

/**
 * 菜单面包屑工具类
 */
var BreadcrumbUtil = {
	breadcrumb: null,//key=url, value=text
	initData:function(menuTree) {
		this.breadcrumb = {};
		var bc = this.breadcrumb;
		if(menuTree) {
			$.each(menuTree, function(i, val) {
				var subMenu = val.children;
				if(subMenu) {
					$.each(subMenu, function(i2, val2) {
						bc[val2.url] = val.text + ' / ' + val2.text;
					});
				}
			});
			
			buildBreadcrumb(menuTree,"");
		}
		function buildBreadcrumb(items,paretTxt){
			items.forEach(function(item){
				var subMenu = item.children;
				if(subMenu) {
					buildBreadcrumb(subMenu,item.text);
				}
				
				//bc[item.url] = breadcrumbText;
				item.breadcrumbText = item.breadcrumbText + "/" + paretTxt +"/"+ item.text;
				
			});
		}
		
	},
	getByUrl:function(url) {
		if(url.indexOf('#') > -1) {
			url = url.substring(0, url.indexOf('#'));
		}
		if(url.indexOf('?') > -1) {
			url = url.substring(0, url.indexOf('?'));
		}
		var text = this.breadcrumb[url];
		if(!text) {
			url = url.substring(url.indexOf('/p/')+2, url.length);
			text = this.breadcrumb[url];
		}
		if(!text) {
			url = url.substring(0, url.length-4);
			text = this.breadcrumb[url];
		}
		return text;
	}
};

/**
 * 数据字典工具类
 */
var DictUtil = {
	getDictName : function(code, value){
		var dicts = null;
		if(window.dicts) {
			dicts = window.dicts[code];
		} else {
			dicts = appDicts[code];
		}
		if(dicts){
			for(var i = 0; i < dicts.length; i++) {
				if(value == dicts[i].code) {
					value = dicts[i].name
					break;
				}
			}
		}
		return value;
	},
	dictCombobox:function(id, dictCode, otherOptions) {
		var options = DictUtil.buildComboboxOptions(dictCode, otherOptions);
		$("#" + id).combobox(options);
	},
	dictCombogrid : function(id,dictCode,initialValue,value){
		var options = DictUtil.buildCombogridOptions(dictCode,initialValue,value);
		$("#"+ id).combogrid(options);
	},
	buildComboboxOptions : function(dictCode, initialValue,value, otherOptions){
		var dict = window.dicts[dictCode];
		if(!dict){
			throw Error('错误的字典[' + dictCode + ']!');
		}
		if(!initialValue){
			initialValue = '';
		}
		if(!value){
			value = '';
		}
		var dictJson = dict[0];
		var url = contextPath + '/rest/base/baseDictTable/data/' + dictJson.bdtUuid + '?dictCode=' + dictCode + '&initialValue=' + initialValue + '&value='+value
		var r = {
			url:url,
			method:'get',
			valueField:dictJson.columnKey,
			textField:dictJson.columnDisplay
		};
		if(otherOptions){
			r = _.merge(r,otherOptions);
		}
		return r;
		
	}
	,
	buildCombogridOptions : function(dictCode,initialValue,value,otherOptions){
		var dict = window.dicts[dictCode];
		if(!dict){
			throw Error('错误的字典[' + dictCode + ']!');
		}
		if(!initialValue){
			initialValue = '';
		}
		if(!value){
			value = '';
		}
		
		var url = contextPath + '/rest/base/baseDictTable/data/';
		var dictJson = dict[0];
		var options =  (new Function("return " + dictJson.columnsJson))();//$.parseJSON(dictJson.columnsJson);
		options.idField = dictJson.columnKey;
		options.textField = dictJson.columnDisplay;
		options.method = 'get';
		options.url = url + '/' + dictJson.bdtUuid + '?dictCode=' + dictCode + '&initialValue=' + initialValue + '&value='+value
		if(otherOptions){
			options = _.merge(options,otherOptions);
		}
		return options;
	},
	loadTableDicts: function(dictCode){
		var dictsList = window.dicts[dictCode];
		if(dictsList) {
			return contextPath+"/rest/base/baseDictTable/dataPost/"+dictsList[0].bdtUuid;
		} else {
			console.log("error,dictCode is not exists:"+dictCode);
			return "";
		}
	},
	getTableDictName : function(code,value){
		var dictsList = window.dicts[code];
		if(dictsList){
			var url = contextPath+"/rest/base/baseDictTable/dataPost/"+dictsList[0].bdtUuid;
			var dictN = value;
			$.ajax({
				url : url,
				type : 'post',
				async: false,
				contentType : 'application/json; charset=utf-8',
				dataType : 'json',
				timeout: 35000,
				success : function(data) {
					var dictL = data.map(function (item) {
						if(item.IP_ID==value)
					     return item.PROJECT;
					});
					dictN = dictL[0];
				}
			});
			return dictN;
		}
		return value;
	}
};

/**
 * Ajax工具类
 */
var AjaxUtil = {
	//property: '', // 定义属性
	get:function(url, success, error) {
		return this.ajax(url, 'GET', null, success, error);
	},
	
	post:function(url, data, success, error) {
		return this.ajax(url, 'POST', data, success, error);
	},
	
	put:function(url, data, success, error) {
		return this.ajax(url, 'PUT', data, success, error);
	},
	
	del:function(url, data, success, error) {
		return this.ajax(url, 'POST', data, success, error);
	},
	
	ajax:function(url, type, data, successCallback, errorCallback) {
		if(!type) {
			type = "POST";
		} 
		var option = {url: url,
				type: type,
				cache:false,
				contentType: 'application/json; charset=utf-8',
				dataType: 'text',
				success: function(data) {
					if(successCallback){
						if(data){
							try{
								data = $.parseJSON(data);
							}catch(e){
								data = data;
								console.log(e);
							}
						}
						successCallback(data);
					}
				},
				error: function(jqXHR, textStatus, errorMsg) {
					MsgUtil.unLoading();
					if(errorMsg=='Unauthorized'){
						MsgUtil.showError('会话已过期，需要重新登录');
						//loadPage('login.html','#mainbody');
						//$("#mainbody").attr("class","login-bg");
						return a;
					}
					if(errorCallback) {
						var msg = AjaxUtil.extractErrorMsg(jqXHR.responseText);
						errorCallback(jqXHR, textStatus, errorMsg,msg);
					} else {
						// 默认处理：提示错误信息
						if(!StringUtil.isEmpty(jqXHR.responseText)) {
							var msg = jqXHR.responseText;
							MsgUtil.showError(msg.substring(msg.lastIndexOf("message")+10, msg.lastIndexOf('"}}')));
						}
					}
				}
		};
		
		if(type != 'GET') {
			option.data = typeof(data) == 'string' ? data : JSON.stringify(data);
		}
		var a = $.ajax(option);
		return a;
	},
	extractErrorMsg : function(responseText){
		try{
			return $.parseJSON(responseText).exception.message;
		}catch(e){
			console.error(e);
		}
	}
};

/**
 * Excel工具类
 */
var ExcelUtil = {
	exportExcel: function(title,frozenColumns,columns,url,data,queryParams) {
		var exportUrl = contextPath+'/rest/common/excelExport/datagrid'; 
		if (url && url.indexOf('?') != -1) {
	        var index = url.indexOf('?');
	        var params = url.substring(index);
	        exportUrl += params;
	    }
		var cols = [];
	    for (var i = 0; i < Math.max(columns.length, frozenColumns.length); i++) {
	        cols[i] = (frozenColumns[i] || []).concat(columns[i] || []);
	    }
	    
	    for(var i= 0; i < cols[0].length; i++) {
	    	// 去掉title里面的html标签
	    	if(!StringUtil.isEmpty(cols[0][i].title)) {
	    		cols[0][i].title = $('<span>'+cols[0][i].title+'</span>').text();
	    	}
	    	
	    	// 若设置了export=true，即使是隐藏的也导出
	    	if(cols[0][i].export) {
	    		cols[0][i].hidden = false;
	    	}
	    }
	    var param = new Object();
	    param.title = title;
	    param.columns = cols;
	    param.data = url ? [] : data;
	    param.url = url;
	    param.queryParams = queryParams;
	    AjaxUtil.post(exportUrl,param).then(function(data){
	    	var url = contextPath +'/rest/common/excelExport/download?uuid='+data;
	    	var iframe = $('#download_iframe');
	        if (!iframe.length) {
	            iframe = $('<iframe id="download_iframe" ref="iframe" width=0 height=0 style="display:none" />');
	            $(document.body).append(iframe);
	        }
	        iframe.prop('src', url);
		}, function(jqXHR, textStatus, errorMsg) {
			
		});
	}
};

/**
 * 弹出窗口工具类
 */
var WindowUtil = {
	getParentContext : function(){
		if(self != top){
			return window.top;
		}
		return window;
	},
	/**
	 * url：地址
	 * title：标题
	 * option：配置项，示例：
	 * 	{
	 * 		params:{},				//业务参数		
	 * 		width:800,				//默认值为800
	 * 		height:490,				//默认值为490
	 * 		modal:true,				//默认值为true
	 * 		collapsible:false,		//默认值为false
	 * 		iconCls:icon-add | icon-edit | icon-remove,
	 * 		load:function() {},	加载成功回调函数
	 * 		close:function() {}	关闭窗口回调函数
	 * 	}
	 */	
	open: function(url, title, option) {
		var winId = StringUtil.uuid32();
	//	var par = WindowUtil.getParentContext();
		$('body').append('<div class="easyui-window" id="' + winId + '"></div>');
		if(option == null) {
			option = {};
		}
		if(!option.height){
			option.height = $('body').height();
		}
		$('#'+winId).window({
			href: url,
			title: title,
			top:0,
		    width: (option.width == null) ? 890 : option.width,
		    height: (option.height == null) ? 490 : option.height,
		    modal: (option.modal == null) ? true : option.modal,
		    collapsible: (option.collapsible == null) ? false : option.collapsible,
		    iconCls: option.iconCls,
		    minimizable:false,
		    maximized:option.maximized,
		    maximizable:option.maximizable,
		    onMaximize:function() {
		    	if(option.onMaximize) {
		    		option.onMaximize();
		    	}
		    },
		    onRestore: function() {
		    	if(option.onRestore) {
		    		option.onRestore();
		    	}
		    },
		    onBeforeLoad: function() {
		    	console.log(winId);
		    	$('#'+winId).window("center");
		    	$($('#'+winId).closest('.window')).ctopMask('show');//增加遮罩
		    	$('#'+winId).data('params', option.params);
		    },
		    onLoad: function() {
		    	$($('#'+winId).closest('.window')).ctopMask('hide');//去掉遮罩
		    	if(option.load) {
		    		option.load(winId);
		    	}
		    },
		    onOpen : function(){
		    	$($('#'+winId).closest('.window')).ctopMask('hide');//去掉遮罩
		    	if(option.opened) {
		    		option.opened();
		    	}
		    },
		    onClose: function() {
		    	$('#' + this.id).data('params', null);
		    	$('#' + this.id).window('destroy');
		    	if(option.close) {
		    		option.close();
		    	}
		    }
		});
		
		return winId;
	},		
	close: function(el) {
		var windowId = $(el).closest('.window-body').attr('id');
		$('#' + windowId).window('close');		
	},
	getParams: function(el) {
		var windowId = $(el).closest('.window-body').attr('id');
		return $('#' + windowId).data('params');
	},
	getParentEle : function(winId){
		var par = WindowUtil.getParentContext();
		return par.$('#' + winId);
	},
	getTabWindow :function() {
		var curTabWin = null;
		var curTab = WindowUtil.getParentContext().$('#pageContainerTabs').tabs('getSelected');
		if (curTab && curTab.find('iframe').length > 0) {
		    curTabWin = curTab.find('iframe')[0].contentWindow;
		}
		return curTabWin;
	}
};


/**
 * 数字工具类
 */
var NumberUtil = {
	
	/**
	 * 数字转为百分比格式
	 * @param value		数字
	 * @param digits	保留小数位数，默认为两位
	 * @returns
	 */
	percentage: function (value, digits) {
		var result = '';
		if(StringUtil.isEmpty(value)){
			result = '0.00%';
		}
		if($.isNumeric(value)){
			var num = parseFloat(value);
			if($.isNumeric(digits)) {
				result = (num * 100).toFixed(digits) + '%';
			} else {
				result = (num * 100).toFixed(2) + '%';
			}
		}
		return result;
	},
	
	/**
	 * s数字转成货币格式，1900->1,900
	 * @param value		数字
	 * @param prefix	前缀:如￥
	 * @param suffix	后缀：元
	 */
	currencyFormat: function(value, prefix, suffix) {
		if(StringUtil.isEmpty(value)) {
			return '0';
		}
		var number = new Number(value);
	    var str = number.toString();
	    //正则匹配
	    var newstr = str.replace(/\d{1,3}(?=(\d{3})+$)/g,function(s){
	    	return s+','
	    })
	    if(!StringUtil.isEmpty(prefix)) {
	    	newstr = prefix + newstr;
	    }
	    if(!StringUtil.isEmpty(suffix)) {
	    	newstr = newstr + suffix;
	    }
	    return newstr;
	},
	
	/**
	 * 转换为数字，若为非数则转为0
	 * @param value
	 */
	toNumber: function(value) {
		if(isNaN(value)) {
			return 0;
		} else {
			return new Number(value);
		}
	}
	
	
};

/**
 * 字符串工具类
 */
var StringUtil = {
	/**
	 * 判断字符串是否为null或空字符串
	 */
	isEmpty:function(str) {
		if ((typeof(str) == "undefined") || ($.trim(str) == '') || (str == 'null')|| str == null) {
			return true;
		}
		return false;
	},
	/**
	 * 去掉所有的html标记
	 * @param str
	 * @returns
	 */
	delHtmlTag:function(str){
		if(StringUtil.isEmpty(str)){
			return "";
		}
		if(typeof str=="string"){
			return str.replace(/<[^>]+>/g,"");
		}
		return str;
	},
	/**
	 * 字符串超出指定长度后显示为XXX（默认为...）
	 */
	fomatLongerStr:function(str, size, replaceChar) {
		if(str && str!='' && str.length>size) {
			str = str.substring(0, size);
			if(replaceChar) {
				return str += (replaceChar+replaceChar+replaceChar);
			} else {
				return str += '...';
			}
		} else {
			return '';
		}
	},
	
	/**
	 * 去掉字符串前面的空格
	 */
	trimLeft:function(str) {
		return str.replace(/(^\s*)/g, '');
	},
	
	/**
	 * 去掉字符串后面的空格
	 * @param str
	 * @returns
	 */
	trimRight:function(str) {
		return str.replace(/(\s*$)/g, '');
	},
	
	/**
	 * 去掉字符串前后的空格
	 * @param str
	 * @returns
	 */
	trim:function(str) {
		if(this.isEmpty(str)) {
			return '';
		} else {
			return str.replace(/(^\s+)/, '').replace(/(\s+$)/, '');
		}
	},
	
	/**
	 * 去掉字符串中所有的空格
	 * @param str
	 * @returns
	 */
	trimAll:function(str) {
		if(this.isEmpty(str)) {
			return '';
		} else {
			return str.replace(/(\s+)/g, '');
		}
	},
	
	/**
	 * 字符串是否以prefix开头
	 * @param str
	 * @param prefix
	 * @returns
	 */
	startsWith:function(str, prefix) {
		if(this.isEmpty(str)) {
			return false;
		} else {
			var reg = new RegExp('^'+prefix);     
			return str.match(reg) ? true : false;
		}
	},
	
	/**
	 * 字符串是否以suffix结尾
	 * @param str
	 * @param suffix
	 * @returns
	 */
	endsWith:function(str, suffix) {
		if(this.isEmpty(str)) {
			return false;
		} else {
			var reg = new RegExp(suffix+'$');     
			return str.match(reg) ? true : false;
		}
	},
	
	/**
	 * 字符串替换
	 * @param str
	 * @param fixStr
	 * @param replaceChar
	 * @returns
	 */
	replaceAll:function(str, fixStr, replaceChar) {
		var reg = new RegExp(fixStr, 'g'); 
		return str.replace(reg, replaceChar);
	},
	
	/**
	 * 生成标准的guid（36位有"-"）
	 */
	guid:function() {
		return (this.S4()+this.S4()+"-"+this.S4()+"-"+this.S4()+"-"+this.S4()+"-"+this.S4()+this.S4()+this.S4());
	},
	
	/**
	 * 生成uuid（32位无"-"）
	 */
	uuid32:function() {
		return (this.S4()+this.S4()+this.S4()+this.S4()+this.S4()+this.S4()+this.S4()+this.S4());			
	},
	
	/**
	 * 生成四个随机十六进制数字
	 */
	S4:function() {
		return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
	},
	
	/**
	 * 数字左边补零
	 * @param  {number}	number待补零的数字
	 * @param  {string} length补零后的长度
	 * @return {string} 补零后的数字字符串
	 */
	numLeftPadZore:function(number, length) {
		var nLength = ('' + number).length;
		if(length < nLength) return ('' + number);
		
		var padLength = length - nLength;
		var result = '';
		for(var i = 0; i < padLength; i++) {
			result += '0';
		}
		return (result + number);
	}
	
};

var ArrayUtil = {
	/**
	 * 判断元素是否存在于数组中，适用于简单数组
	 * 例：	var arr = ['a', 'b', 12]; 
	 * 		ArrayUtil.contains(arr, 'a');//true
	 * 		ArrayUtil.contains(arr, 'c');//false
	 * 		ArrayUtil.contains(arr, 12);//true
	 * @param array
	 * @param element
	 * @returns {Boolean}
	 */
	contains: function(array, element){
		for(var i = 0; i < array.length; i++) {
			if(array[i] == element) {
				return true;
			}
		}
		return false;
	},
	
	/**
	 * 删除数组中的某个元素，适用于简单数组
	 * 例：	var arr = ['a', 'b', 12]; 
	 * 		ArrayUtil.remove(arr, 'a');//['b', 12]; 
	 * 		ArrayUtil.remove(arr, 12);//['a', 'b'];
	 * 		ArrayUtil.remove(arr, 1);//['a', 'b', 12]; 
	 * @param array
	 * @param element
	 */
	remove: function(array ,element) {
		var index = array.indexOf(element);
		if (index > -1) {
			array.splice(index, 1);
		}
	},
	
	/**
	 * 数组去重复，适用于简单数组
	 * @param array
	 * @returns {Array}
	 */
	distinct: function(array) {
		temp = new Array();
	    for(var i = 0; i < array.length; i ++){
	        if(!contains(temp, array[i])){
	        	temp.push(array[i]);
	        }
	    }
	    return temp;
	}
};

/**
 * 日期工具类
 */
var DateUtil = {
  	/**
  	 * 根据日期得到对应是星期几
  	 * @param  {date}	date 日期
  	 * @param  {int}	type 格式，1（默认）-星期X；2-周X
  	 * @return {string} 星期几
  	 */
  	getWeek:function(date, type) {
  		var weekArray = new Array("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六");;
  		if(type && type == 2) {
  			weekArray = new Array("周日", "周一", "周二", "周三", "周四", "周五", "周六");
  		}
  		return weekArray[date.getDay()]; 
  	},
  	  	
   	
  	/**
	 * 把日期格式化成字符串，默认格式为："yyyy-MM-dd HH:mm:ss"
	 * @param  {date}  		date 待格式化的日期
	 * @param  {string}		format 格式化字符串。例如："yyyy-MM-dd HH:mm:ss"	
	 * @return {string}     格式后的日期字符串
	 */
	formatDate:function(date, format) {
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
  		
  		return format.replace('yyyy', year).replace('MM', month).replace('dd', day)
  				.replace('HH', hour).replace('mm', minute).replace('ss', second);
  	},
	formatDateStr:function(format, dateStr){
  		var newDate = DateUtil.parseDate(dateStr);
  		return DateUtil.formatDate(newDate,format);
  	},
  	/**
	 * 把日期字符串转为日期
	 * @param  {string}		日期字符串，支持的格式有：2016-07-10
	 * 										 2016/07/10	
	 * 										 2016-07-10T23:23:59	
	 * 										 2016/07/10T23:23:59
	 * @return {date}     	日期
	 */
  	parseDate:function(dateStr) {
  		if(!(dateStr && dateStr != '')) return '';
  		if(dateStr.indexOf('T') == -1) {	// 不带时分秒
  			if(dateStr.indexOf('-') != -1) {
  				var dateArr = dateStr.split('-');
  				var date = new Date();
  				date.setFullYear(parseInt(dateArr[0])||0, parseInt(dateArr[1])?parseInt(dateArr[1])-1:0, parseInt(dateArr[2])||0);
  				date.setHours(0, 0, 0, 0);
  				return date;
  			}
  			
  			if(dateStr.indexOf('/') != -1) {
  				var dateArr = dateStr.split('/');
  				var date = new Date();
  				date.setFullYear(parseInt(dateArr[0])||0, parseInt(dateArr[1])?parseInt(dateArr[1])-1:0, parseInt(dateArr[2])||0);
  				date.setHours(0, 0, 0, 0);
  				return date;
  			}
  		} else {	// 带时分秒
  			if(dateStr.indexOf('-') != -1) {
  				var dateArr = dateStr.split('T');
  				var dateArr2 = dateArr[0].split('-');
  				var dateArr3 = dateArr[1].split(':');
  				var date = new Date();
  				date.setFullYear(parseInt(dateArr2[0]), parseInt(dateArr2[1])?parseInt(dateArr2[1])-1:0, parseInt(dateArr2[2])||0);
  				date.setHours(parseInt(dateArr3[0])||0, parseInt(dateArr3[1])||0, parseInt(dateArr3[2])||0, 0);
  				return date;
  			}
  			
  			if(dateStr.indexOf('/') != -1) {
  				var dateArr = dateStr.split('T');
  				var dateArr2 = dateArr[0].split('/');
  				var dateArr3 = dateArr[1].split(':');
  				var date = new Date();
  				date.setFullYear(parseInt(dateArr2[0])||0, parseInt(dateArr2[1]) ? parseInt(dateArr2[1])-1: 0, parseInt(dateArr2[2])||0);
  				date.setHours(parseInt(dateArr3[0])||0, parseInt(dateArr3[1])||0, parseInt(dateArr3[2])||0, 0);
  				return date;
  			}
  		}
  	},
  	
  	/**
  	 * 把指定的日期加（减）年数
  	 * @param date	指定日期
  	 * @param day	年数，若为负数则是减
  	 */
  	addYear: function(date, year) {
  		if(date == null) {
  			return null;
  		}
  		if(typeof(date) != 'object') {
  			throw Error('date对象不是日期格式!');
  		}
  		
  		date.setFullYear(date.getFullYear() + NumberUtil.toNumber(year));
  		return date;
  	},
  	
  	/**
  	 * 把指定的日期加（减）月数
  	 * @param date	指定日期
  	 * @param day	月数，若为负数则是减
  	 */
  	addMonth: function(date, month) {
  		if(date == null) {
  			return null;
  		}
  		if(typeof(date) != 'object') {
  			throw Error('date对象不是日期格式!');
  		}
  		
  		date.setMonth(date.getMonth() + NumberUtil.toNumber(month));
  		return date;
  	},
  	
  	/**
  	 * 把指定的日期加（减）天数
  	 * @param date	指定日期
  	 * @param day	天数，若为负数则是减
  	 */
  	addDay: function(date, day) {
  		if(date == null) {
  			return null;
  		}
  		if(typeof(date) != 'object') {
  			throw Error('date对象不是日期格式!');
  		}
  		
  		date.setDate(date.getDate() + NumberUtil.toNumber(day));
  		return date;
  	},
  	
  	/**
  	 * 根据数字得到对应是星期几
  	 * @param  {num}	num 数字
  	 * @param  {int}	type 格式，1（默认）-星期X；2-周X
  	 * @return {string} 星期几
  	 */
  	getWeekByNum:function(num, type) {
  		var weekArray = new Array("星期一", "星期二", "星期三", "星期四", "星期五", "星期六","星期日");
  		if(type && type == 2) {
  			weekArray = new Array("周一", "周二", "周三", "周四", "周五", "周六","周日");
  		}
  		return weekArray[num]; 
  	},
  	
  	

};

/**
 * 消息提示框工具类
 */
var MsgUtil ={
	showInfo : function(content, timeout){
		content = '<div class="messager-icon messager-info"></div><div>'+content+'</div><div style="clear:both;"></div>';
		this.buildMssagerShow('信息',content,'fade', timeout);
	},
	showInfoCom: function(title, content, callback, timeout) {
		content = '<div class="messager-icon messager-info"></div><div>'+content+'</div><div style="clear:both;"></div>';
		this.buildMssagerShow(title,content,'fade', timeout, callback);
	},
	showSuccess: function(content, callback, timeout) {
		content = '<div class="messager-icon messager-success"></div><div>'+content+'</div><div style="clear:both;"></div>';
		this.buildMssagerShow('成功',content,'fade', timeout, callback);
	},
	showError : function(content, timeout){
		content = '<div class="messager-icon messager-error"></div><div>'+content+'</div><div style="clear:both;"></div>';
		this.buildMssagerShow('错误',content,'fade', timeout);
	},
	showWarning: function(content, callback, timeout) {
		content = '<div class="messager-icon messager-warning"></div><div>'+content+'</div><div style="clear:both;"></div>';
		this.buildMssagerShow('警告',content,'fade', timeout, callback);
	},
	alert : function(title,msg,icon){
		$.messager.alert({
			title: title,
			icon:icon,
			msg: msg,
			fn: function(){
				//...
			}
		});
	},
	confirm : function(title,msg,callback){
		$.messager.confirm(title,msg,callback);
	},
	buildMssagerShow : function(title,content,showType, timeout, callback){
		timeout = isNaN(timeout) ? 3000 : timeout;
		$.messager.show({
            title:title+ "<span style='color:red;margin-left:10px;' id='timeOut'>3s</span>",
            msg:content,
            showType:showType,
            showSpeed:1000,
            timeout:timeout,
            width:'370px',
            height:'',
            showType:null,
            modal:true,
            onClose: function() {
            	if(callback) {
            		callback();
            	}
            },
            style:{
            	zIndex:999999
            }
        });
		var interval;
		var s=3;
		interval = setInterval(function() {
			s--;
			WindowUtil.getParentContext().$("#timeOut").html(s + "s");
			if (s == 0) {
				clearInterval(interval);
			}
		}, 1000);
	},
	loading: function() {
		$.messager.progress({text:''});
	},
	
	unLoading: function() {
		$.messager.progress('close');
	}
}

/**
 * 校验工具类
 */
var ValidationUtil = {
	/**
	 * 是否为英文字母
	 */
	isLetter:function(str) {
		str += '';
		var reg = new RegExp('^[a-zA-Z]+$');
	    if(reg.test(str)) return true;
	    return false;
	},
	
	/**
	 * 是否为数字
	 */
	isNumber:function(str) {
		str += '';
		var reg = new RegExp('^[0-9]+$');
	    if(reg.test(str)) return true;
	    return false;
	},
	
	/**
	 * 是否为数字或英文
	 * @param str
	 */
	isLetterOrNumber:function(str) {
		//var reg = new RegExp('^(\\d{2,4}-\\d{7,8})|(\\d{2,4}\\d{7,8})$');
		str += '';
		var reg = new RegExp('^[a-zA-Z0-9]+$');
	    if(reg.test(str)) return true;
	    return false;
	},
	
	/**
	 * 整数
	 * @param str
	 * @returns
	 */
	isInt:function(str) {
		str += '';
		var reg = /^-?\d+$/;
		return str.match(reg)?true:false;
	},
	
	/**
	 * 大于等于0的整数
	 * @param str
	 * @returns {Boolean}
	 */
	isIntGeZero:function(str) {
		if(this.isInt(str)){
			if(parseInt(str) >= 0){
				return true;
			}
		}
		return false;
	},
	
	/**
	 * 大于0的整数
	 * @param str
	 * @returns {Boolean}
	 */
	isIntGtZero:function(str) {
		if(this.isInt(str)){
			if(parseInt(str) > 0){
				return true;
			}
		}
		return false;
	},
	
	isFloat:function(str) {
		str += '';
		var reg = /^(-?\d+)(\.\d+)?$/;
		return str.match(reg)?true:false;
	},
	
	isEmail:function(str) {
		str += '';
		var reg = new RegExp('^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$');
		if(reg.test(str)) return true;
	    return false;
	},
	
	/**
	 * 是否为中文
	 */
	isChinese:function(str) {
		str += '';
		var reg = new RegExp('[\\u4E00-\\u9FFF]+','g');
	    if(reg.test(str)) return true;
	    return false;
	},
	/**
	 * 是否为手机号码或者电话号码
	 */
	isPhoneOrTel:function(str) {		
		if(this.isMobilePhone(str) || this.isPhone(str)) {
			return true;
		}
		return false;
	},
	
	/**
	 * 是否为手机号码，支持86、+86，支持13、14、15、17、18开头
	 */
	isMobilePhone:function(str) {
		str += '';
		var reg = new RegExp('^((\\+86)|(86))?[1][34578][0-9]{9}$');
	    if(reg.test(str)) return true;
	    return false;
	},
	
	/**
	 * 是否为固定电话，需要带区号
	 */
	isPhone:function(str) {
		str += '';
		var reg = new RegExp('^(\\d{2,4}-\\d{7,8})|(\\d{2,4}\\d{7,8})$');
	    if(reg.test(str)) return true;
	    return false;
	},
	
	isYYYY_MM_DD:function(date){  
	    var result = date.match(/((^((1[8-9]\d{2})|([2-9]\d{3}))(-)(10|12|0?[13578])(-)(3[01]|[12][0-9]|0?[1-9])$)|(^((1[8-9]\d{2})|([2-9]\d{3}))(-)(11|0?[469])(-)(30|[12][0-9]|0?[1-9])$)|(^((1[8-9]\d{2})|([2-9]\d{3}))(-)(0?2)(-)(2[0-8]|1[0-9]|0?[1-9])$)|(^([2468][048]00)(-)(0?2)(-)(29)$)|(^([3579][26]00)(-)(0?2)(-)(29)$)|(^([1][89][0][48])(-)(0?2)(-)(29)$)|(^([2-9][0-9][0][48])(-)(0?2)(-)(29)$)|(^([1][89][2468][048])(-)(0?2)(-)(29)$)|(^([2-9][0-9][2468][048])(-)(0?2)(-)(29)$)|(^([1][89][13579][26])(-)(0?2)(-)(29)$)|(^([2-9][0-9][13579][26])(-)(0?2)(-)(29)$))/);
	    if(result==null){
            return false;
        }
	    return true;
	}
};

/**
 * 操作DOM的工具类
 */
var DomUtil = {
	postToPage:function(url, args){
        var form = $("<form method='post'></form>"), input;
        form.attr({"action":contextPath + url});
        $.each(args,function(index,obj){
            input = $("<input type='hidden'>");
            input.attr({"name":obj.name});
            input.val(obj.value);
            form.append(input);
        });
        
        $('body').append(form);
        form.trigger("submit");
    },
	/**
	 * 获取form表单里面元素（input、select、textare，必须要加name属性）的数据
	 * @param formId form的id
	 */
	getFormData:function(formId) {
		var result = {};
		var inputs = $('#' + formId + ' :input[name]');
		$.each(inputs, function(i, item) {
			if(item.name && item.name != '') {
				// hidden、text、select、textare
				if(item.type != 'checkbox' && item.type != 'radio') {
					if(!result[item.name]) {//如果没有值，则设值
						result[item.name] = item.value;
					}
				}
				if(item.type == 'checkbox' && !result[item.name]) {
					result[item.name] = DomUtil.getCheckboxVals(item.name, formId);
				}
				if(item.type == 'radio' && !result[item.name]) {
					result[item.name] = DomUtil.getRadioVal(item.name, formId);
				}
			}
		});
		
		return result;
	},
	/**
	 * 获取form表单里面元素（input、select、textare，必须要加name属性）的数据
	 * @param formId form的id
	 */
	getFormDataIgnoreNull:function(formId) {
		var result = {};
		var inputs = $('#' + formId + ' :input[name]');
		$.each(inputs, function(i, item) {
			if(item.name && item.name != '') {
				// hidden、text、select、textare
				if(item.type != 'checkbox' && item.type != 'radio') {
					if(!StringUtil.isEmpty(item.value) && !result[item.name]) {
						result[item.name] = item.value;
					}
				}
				if(item.type == 'checkbox' && !result[item.name]) {
					result[item.name] = DomUtil.getCheckboxVals(item.name, formId);
				}
				if(item.type == 'radio' && !result[item.name]) {
					result[item.name] = DomUtil.getRadioVal(item.name, formId);
				}
			}
		});
		
		return result;
	},
	/**
	 * 设置form表单里面的元素（input、select、textare，必须要加name属性）的值
	 * @param formId form的id
	 * @param data
	 */
	setFormData:function(formId, data) {
		if(data && typeof(data) == 'object') {
			var inputs = $('#' + formId + ' :input[name]');
			$.each(inputs, function(i, item) {
				if(item.name && item.name != '') {
					// hidden、text、select、textare
					if(item.type != 'checkbox' && item.type != 'radio') {
						if(typeof(data[item.name]) == "undefined" || data[item.name] == null) {
							item.value = '';
						} else {
							item.value = data[item.name];
						}
					}
					if(item.type == 'checkbox') {
						item.checked = false;
						if(data[item.name] && data[item.name].indexOf(item.value) > -1) {
							item.checked = true;
						}
					}
					if(item.type == 'radio') {
						if(item.value == data[item.name]) {
							item.checked = true;
						}
					}
				}
			});
		}
	},
	
	/**
	 * 得到checkbook选中的值
	 * @param name checkbook的name属性
	 */
	getCheckboxVals:function(name, fromId) {
		var checkboxs = null;
		if(StringUtil.isEmpty(fromId)) {
			checkboxs = $('input[name='+name+'][type=checkbox]');
		} else {
			checkboxs = $('#' + fromId + ' input[name='+name+'][type=checkbox]');
		}
		var result = [];
		$.each(checkboxs, function(i, item) {
			if(item.checked && item.checked == true) result.push(item.value);
		});
		return result.join(',');
	},
	
	/**
	 * 得到radio选中的值
	 * @param name radio的name属性
	 */
	getRadioVal:function(name, fromId) {
		var radios = null;
		if(StringUtil.isEmpty(fromId)) {
			radios = $('input[name='+name+'][type=radio]');
		} else {
			radios = $('#' + fromId + ' input[name='+name+'][type=radio]');
		}
		var result = '';
		$.each(radios, function(i, item) {
			if(item.checked && item.checked == true) {
				result = item.value;
				return false;
			}
		});
		return result;
	},
	/**
	 * 设置区块的EasyUi控件为只读，
	 * @param selector
	 */
	setReadOnly4Easyui: function(selector, excludeNames) {
		if(selector) {
			$(selector + ' input[name]').each(function() {
				if(!DomUtil.isExcludeName(excludeNames, this)) {
					//console.log(this);
					if(this.type && this.type=='checkbox') {
						this.disabled = 'disabled';
					} else {
						this.readOnly = 'readOnly';
						$(this).parent('.textbox ').addClass('textbox-readonly');
						
						// 隐藏下拉框的箭头
						$(this).parent('.textbox').find('.combo-arrow').css('visibility', 'hidden');
						
						$(this).siblings().attr('readOnly', 'readOnly');
					}
				}
			});
			
			$(selector + ' textarea[name]').each(function() {
				if(!DomUtil.isExcludeName(excludeNames,this)) {
					this.readOnly = 'readOnly';
					$(this).parent('.textbox ').addClass('textbox-readonly');
				}
			});
			
			$(selector + ' .easyui-ctfilebox').ctfilebox('toView');
		}
	},
	
	/**
	 * 是否为被排除的元素
	 * @param excludeNames	被排除元素的name列表
	 * @param element		被排除的元素（JS对象）
	 */
	isExcludeName: function(excludeNames, element) {
		if(excludeNames) {
			if(ArrayUtil.contains(excludeNames, element.name)) {
				return true
			} else {
				return false;
			}
		}
	},
	
	/**
	 * 设置区域块为只读
	 * @param selector
	 */
	setReadOnly:function(selector) {
		if(selector) {
			$(selector + ' input').each(function() {
				if(this.type && (this.type=='file' || this.type=='checkbox')) {
					this.disabled = 'disabled';
				} else {
					this.readOnly = 'readOnly';
				}
				$(this).parent('.textbox ').addClass('textbox-readonly');
			});
			$(selector + ' select').each(function(){
				this.disabled = 'disabled';
		  	});
		  	$(selector+" textarea").attr("readonly","readonly");
		  	// 移除选择控件的事件
		  	$(selector + ' input').removeAttr("onmouseover");
		  	$(selector + ' input').removeAttr("onkeyup");
		  	$(selector + ' input').removeAttr("placeholder");
		} else {
			$('.readOnly input').each(function() {
				if(this.type && this.type=='file') {
					this.disabled = 'disabled';
				} else {
					this.readOnly = 'readOnly';
				}
			});
			$(".readOnly textarea").attr("readonly","readonly");
			$('.readOnly select').each(function(){
				this.disabled = 'disabled';
		  	});
		  	$('.readOnly input').removeAttr("onmouseover");
		  	$('.readOnly input').removeAttr("onkeyup");
			$('.readOnly input').removeAttr("placeholder");
		}
	},
	checkSiblingLabelExclude:function(excludeNames,obj) {//判断元素是否在被排除的范围内
		if(excludeNames) {
			var nextObj = $(obj).siblings("input:hidden").first();
			if(nextObj&&nextObj[0]&&excludeNames.indexOf($(nextObj[0]).attr("name"))>=0) {
			
				return true;
			}
		}
		return false;
	},
	checkChildLabelExclude:function(excludeNames,obj) {//判断元素是否在被排除的范围内
		if(excludeNames) {
			var nextObj = $(obj).children("input:hidden").first();
			if(nextObj&&nextObj[0]&&excludeNames.indexOf($(nextObj[0]).attr("name"))>=0) {
				
				return true;
			}
		}
		return false;
	},
	/**
	 * 设置区块为label显示
	 * @param selector
	 */
	setAsLabel:function(selector,excludeNames) {
		var css = {"border":"0", "background":"#none"};
		if(selector) {
			$(selector + ' input').each(function() {
				if(!DomUtil.checkSiblingLabelExclude(excludeNames,this)) {
					this.readOnly = 'readOnly';
				}
			});
			
			$(selector + ' .textbox').each(function() {
				if(!DomUtil.checkChildLabelExclude(excludeNames,this)) {
					$(this).css(css);
					$(this).removeClass('textbox-readonly');
					$(this).find('.textbox-addon-right').hide();
				}
			});
			
			$(selector + ' textarea').each(function() {
				this.readOnly = 'readOnly';
				$(this).closest('.easyui-fluid').css('box-shadow', 'none');
				
			});
			$(selector + '.textbox-icon').each(function() {
				$(this).css({"display":"none"});
			});
		}
	},
	changeEdit2View:function(formId) {
		var inputs = $('#' + formId + ' :input');
		$.each(inputs, function(i, item) {
			if($.data(item, "ctfilebox")){
				$(item).ctfilebox('toView');
				return true;
			}
			if($(item).attr("render") && $(item).attr("render") =='uploadfile'){
				$(item).parent().remove();
			}else if(item.type && item.type != 'hidden' && item.style.display !="none" && $(item).attr("render") !='uploadfile') {
				var val = item.value;
				$(item).parent().find("a").remove();
				if(val){
					$(item).parent().append("<p style='width:100%;text-align:left;white-space: pre-wrap;'>"+val+"</p>");
				}
				$(item).parent().removeClass("textbox");
				$(item).remove();
			}
		});
	},
	autoResizeForm : function (container){
		container.find(".form_div > table").each(function(index,item){
			var table$ = $(item);
			if(table$.hasClass("no_resize")){
				return true;
			}
			/*if(StringUtil.isEmpty(table$.css("width"))){
			}*/
			table$.css({"width" : "100%"});
			table$.css({tableLayout : "fixed"});
			
			//table$.find("td:has(input)").css({});
			//$("td:has(input, check ,select)")
			table$.find("*:has(input,check,select,textarea,radio)").children("input,textarea,select").each(function(i,ii){
				$(ii).css({width:"100%"});
			})
		});
	}
};

/**
 * 权限按钮控制工具类
 */
var RoleManager = {
	// 控制界面权限按钮
	authRole:function (){
		var rcMap ={};
		AjaxUtil.get(contextPath+"/rest/sys/sysAccount/currentUser.do",function(data){
			var user = data;
			$.each(user.sysResources || [],function(n,sr){
				var arr =[];
				$.each(user.sysPermissions || [],function(n,p){
					 if(sr.resourceUuid == p.resourceUuid){
						 arr.push(p.operationCode);
					 }
					 if (!rcMap[sr.url]) {
			                rcMap[sr.url] = arr;
		            } else {
		                rcMap[sr.url] = rcMap[sr.url].concat(arr);
		            }
					var parent = sr;
				    if(parent.parentUuid && rcMap[sr.url].length) {
				    	$.each(user.sysResources || [],function(n,srr){
				    		if(srr.resourceUuid == parent.parentUuid){
					    		parent = srr;
				    		}
			                if (parent) {
			                    var arr2 = rcMap[parent.url];
			                    if (parent.url && (!arr2 || !arr2.length)) {
			                        rcMap[parent.url] = ['show'];
			                    }
			                }
				    	});
		            }
				});
		     });
			// TODO /p or /p/
			var url = (window.location.pathname).replace(contextPath+"/p/","").replace(contextPath+"/pages","");
			$(".operation").find("a").each(function(){
				   var opCode = $(this).attr("opCode");
				   if(!StringUtil.isEmpty(opCode)){
					   if(user.sysAccount && user.sysAccount.loginName=="admin"){
						   $(this).css("visibility","visible");
						   return;
					   }
					   if(rcMap && url && rcMap[url] && rcMap[url].indexOf(opCode) != -1){
						   $(this).css("visibility","visible");
					   }else{
						   $(this).hide();
					   }
				   }else{
					   $(this).show();
					   $(this).css("visibility","visible");
				   }
			});
		});
	}
};

/**
 * easyUi datagrid工具类
 */
var DatagridUtil = {
	/**
	 * 单行操作
	 * @param id
	 * @param sucess
	 */
	singleRowOps : function(id,callBack,lessMsg,overMsg){
		var rows = $("#" + id).datagrid("getSelections");
		var len = rows.length;
		if(len == 1){
			callBack(rows[0]);
			return ;
		}
		overMsg = overMsg || '只能选择一条数据!';
		lessMsg = lessMsg || '请先选择一条数据';
		if(len > 1){
			MsgUtil.showError(overMsg);
		}else{
			MsgUtil.showError(lessMsg);
		}
	},
	
	/**
	 * easy ui datagrid 的loader
	 */
	searchLoader:function(params, success, error) {
		
		var options = $(this).datagrid('options');
	    var url = options.url;
	    if(!url){
	    	$(this).datagrid('loaded');
	    	return;
	    }
	    $(this).datagrid("clearSelections");
		params.pageIndex = (params.page || 1) - 1;
		params.pageSize = params.rows;
		params.sortField = params.sort;
		params.sortOrder = params.order;
		delete params.page;
		delete params.rows;
		delete params.sort;
		delete params.order;
		
		if(params.filterRules &&  params.filterRules.length > 0){
			var filterRules = params.filterRules;
			filterRules = JSON.parse(params.filterRules);
			if(params.filter){
				var filterIsOpen = options.filterIsOpen;
				for(var i = 0; i < filterRules.length; i++){
					var item = filterRules[i];
					for(var j = 0; j < params.filter.filters.length; j++){
						var filter = params.filter.filters[j];
						if(item.field == filter.field){
							 params.filter.filters.splice(j, 1); 
							 break;
						}
					}
					if(ValidationUtil.isYYYY_MM_DD(item.value)){//日期
						item.type='date';
						item.op='eq';
					}
					item.operator = item.op;
					delete item.op;
					if(!StringUtil.isEmpty(item.value) && !StringUtil.isEmpty(filterIsOpen) && filterIsOpen == 'Y'){
						params.filter.filters.push(item);
					}
				}
			}else{
				var filter = {};
				var filters = [];
				for(var j = 0; j < filterRules.length; j++){
					var item = filterRules[j];
					filters.push(item);
				}
				filter.filters = filters;
				filter.logic = 'and';
				params.filter = filter;
			}
			delete params.filterRules;
		}
		if (this.xhr) {
			//this.xhr.abort();
		}
		var _this = this;
		
		this.xhr = AjaxUtil.post(url, JSON.stringify(params), function(data) {
			success({
				rows : data.data,
				total : data.total,
				footer : data.footer
			});
		}, function(e) {
			console.log(e);
			MsgUtil.showError('查询列表失败!');
			$(_this).datagrid('loaded');
		});
	},
	//根据cell里面的元素得到所在行的行号
	getRowIndex: function(el){
		var index = parseInt($(el).parents("tr[datagrid-row-index]").attr("datagrid-row-index"));
		if(isNaN(index)){
			throw Error('_this 对象执行错误!');
		}
		return index;
	},
	/**
	 * 得到列表的所有改变行的数据
	 */
	getDataGridByOnChanger:function(dgId){
		var details = [];
		//新增行
		var addRows = $('#'+dgId).datagrid('getChanges','inserted');
		
		addRows.forEach(function(item){
			item._status='add';
			details.push(item);
		});
		
		//修改行
		var updatedRows = $('#'+dgId).datagrid('getChanges','updated');
		updatedRows.forEach(function(item){
			item._status='updated';
			details.push(item);
		});
		
		//删除行
		var delRows = $('#'+dgId).datagrid('getChanges','deleted');
		delRows.forEach(function(item){
			item._status='del';
			details.push(item);
		});
		return details;
	},
	/**
	 * 按照属性过滤空行
	 * @param rows
	 * @param field
	 */
	filterEmptyRow : function(rows,field){
		if(rows){
			if(rows.length > 0){
				var details = [];
				rows.forEach(function(item){
					if(item[field]){
						details.push(item);
					}					
				});
				return details;
			}else{
				return rows;
			}
		}else{
			return [];
		}
	},
	/**
	 * 行单选 复选框多选
	 */
	rowSingleSelect:function(rowIndex,rowData) {
		$(this).datagrid('unselectAll');
		$(this).datagrid('selectRow', rowIndex);
	},
	/**
	 * 初始化datagrid的导出等分页按钮
	 */
	initPagination:function(datagridId) {
		//TODO 待实现
		var pager = $('#'+datagridId).datagrid('getPager');	// get the pager of datagrid
		pager.pagination({
			buttons:[
			   {
				iconCls:'icon-excel',
				title:'列表下载',
				handler:function(){
					var options = $('#'+datagridId).datagrid('options');
					var exportData =[];
					if($('#'+datagridId).datagrid('getSelections').length > 0){
						exportData = $('#'+datagridId).datagrid('getSelections');
						ExcelUtil.exportExcel(options.exportTitle || '列表数据导出',
								options.frozenColumns,
								options.columns,
								'',
								exportData,
								options.queryParams);
					}else{
						exportData = $('#'+datagridId).datagrid('getRows');
						ExcelUtil.exportExcel(options.exportTitle || '列表数据导出',
								options.frozenColumns,
								options.columns,
								options.url,
								options.url ? [] : exportData,
								options.queryParams);
					}					
				}
			}]
		});
		//pagination-first pagination-prev pagination-next pagination-last pagination-refresh
		//pager.find('.pagination-load').attr('title','刷新列表');
		
		/*var pagerData = $.data(pager.get(0), "pagination");
		pagerData.bb.after.attr('title','之后');
		pagerData.bb.first.attr('title','第一页');
		pagerData.bb.last.attr('title','最后一页');
		pagerData.bb.next.attr('title','下一页');
		pagerData.bb.prev.attr('title','上一页');
		pagerData.bb.refresh.attr('title','刷新列表');*/
		
	},
	fmtNumber: function(value,row,index) {
		return NumberUtil.currencyFormat(value);
	}
};

/**
 * easyUi treegrid工具类
 */
var TreegridUtil = {
	/**
	 * easy ui datagrid 的loader
	 */
	searchLoader:function(params, success, error) {
		var options = $(this).treegrid('options');
		var idField = options.id;
		var parentField = options.parentId;
		var textField = options.name;
	    var url = options.url;
	    if(!url){
	    	$(this).treegrid('loaded');
	    	return;
	    }
		params.pageIndex = (params.page || 1) - 1;
		params.pageSize = params.rows;
		params.sortField = params.sort;
		params.sortOrder = params.order;
		delete params.page;
		delete params.rows;
		delete params.sort;
		delete params.order;
		if (this.xhr) {
			//this.xhr.abort();
		}
		this.xhr = AjaxUtil.post(url, JSON.stringify(params), function(data) {
			var row = TreegridUtil.buildCascadeList4EasyTree(data.data,idField,parentField,textField);
			success({
				rows : row,
				total : data.total
			});
		}, function(e) {
	
		});
	},
	/**
	 * 行单选 复选框多选
	 */
	rowSingleSelect:function(rowData) {
		var ops = $(this).treegrid('options');
		
		
		$(this).treegrid('unselectAll');
		$(this).treegrid('clearAllChecked');
		$(this).treegrid('checkNode', rowData[ops.idField]);
		$(this).treegrid('select', rowData[ops.idField]);
	},
	/**
	 * 初始化datagrid的导出等分页按钮
	 */
	initPagination:function(treegridId) {
		//TODO 待实现
		var pager = $('#'+treegridId).treegrid('getPager');	// get the pager of datagrid
		//console.log(pager);
		pager.pagination({
			buttons:[
			   {
				iconCls:'icon-excel',
				title:'列表下载',
				handler:function(){
					var options = $('#'+treegridId).treegrid('options');
					ExcelUtil.exportExcel(options.exportTitle || '列表数据导出',
							options.frozenColumns,
							options.columns,
							options.url,
							options.url ? [] : $('#'+treegridId).treegrid('getRows'),
							options.queryParams);
				}
			},{
				iconCls:'icon-tool-shrink',
				title:'展开或收缩',
				handler:function(){
					var isExpandAll = true;
					if($(this).data("gridState")){
						$('#'+treegridId).treegrid('expandAll');
						isExpandAll = false;
					}else{
						$('#'+treegridId).treegrid('collapseAll');
						isExpandAll = true;
					}
					
					$(this).data("gridState",isExpandAll)
					$(this).linkbutton({
		                iconCls: (isExpandAll? "icon-tool-expand" : "icon-tool-shrink")
		            });
				}
			},{
				iconCls:'tree-checkbox0',
				title:'全选或取消全选',
				handler:function(){
					var isChecked = false,treegrid = $('#'+treegridId);
					var options = treegrid.treegrid('options');
					var roots = treegrid.treegrid('getRoots');
					function updateGridState(isCheck){
						roots.forEach(function(root){
							if(isCheck){
								treegrid.treegrid('checkNode',root[options.idField]);
							}else{
								treegrid.treegrid('uncheckNode',root[options.idField]);
							}
						});
					}
					if($(this).data("checkAllGridState")){
						isChecked = false;
					}else{
						isChecked = true;
					}
					$(this).data("checkAllGridState",isChecked)
					updateGridState(isChecked);
					$(this).linkbutton({
		                iconCls: (isChecked? "tree-checkbox1" : "tree-checkbox0")
		            });
				}
			}]
		});	
		var pagerData = $.data(pager.get(0), "pagination");
		/*pagerData.bb.after.attr('title','之后');
		pagerData.bb.first.attr('title','第一页');
		pagerData.bb.last.attr('title','最后一页');
		pagerData.bb.next.attr('title','下一页');
		pagerData.bb.prev.attr('title','上一页');
		pagerData.bb.refresh.attr('title','刷新列表');*/
	},
	
     buildCascadeList4EasyTree:function(list, idField, parentField,textField){
		var list1 = list;
		var list2 = list;
		var list3 = [];
		list2.forEach(function(item2){
	        var matchedList = list1.filter(function(item1){
	        	if(item1[idField] == item2[parentField])return true;
	        	});
	        item2.id = item2[idField];
	        if (parentField) {
	            item2.parentId = item2[parentField];
	        }
	        if (textField) {
	            item2.text = item2[textField];
	        }
	        if (matchedList.length > 0) {
	            var item1 = matchedList[0];
	            item1.children = item1.children || [];
	            item1.children.push(item2);
	            item1.state = StringUtil.isEmpty(item1.state) ? 'closed' : item1.state;
	        } else {
	            list3.push(item2);
	        }
	    });
	    return list3;
	 }
};

var CombogridUtil = {
	searchLoader:function(params, success, error) {
		var options = $(this).datagrid('options');
		// 拼接查询条件
		if(!params.filter) {
			var textField = options.textField;
			var filter = {"logic":"and","filters":[{"field":textField,"operator":"contains","type":"string","value":params.q}]};
			params.filter = filter;
		}
	    var url = options.url;
	    if(!url){
	    	$(this).datagrid('loaded');
	    	return;
	    }
	    $(this).datagrid("clearSelections");
		params.pageIndex = (params.page || 1) - 1;
		params.pageSize = params.rows;
		params.sortField = params.sort;
		params.sortOrder = params.order;
		delete params.page;
		delete params.rows;
		delete params.sort;
		delete params.order;
		if (this.xhr) {
			//this.xhr.abort();
		}
		var _this = this;
		this.xhr = AjaxUtil.post(url, JSON.stringify(params), function(data) {
			success({
				rows : data.data,
				total : data.total,
				footer : data.footer
			});
		}, function(e) {
			console.log(e);
			MsgUtil.showError('查询列表失败!');
			$(_this).datagrid('loaded');
		});
	}
};

/**
 * 列筛选工具方法
 * 示范：
 * 1.需要为列表添加 type 属性,时间类型的 type 为 date,有数据字典的 type 为 dict，其他类型的可以不写，如下：
 * <th data-options="field:'launchTime',width:'180px',type:'date'">Launch时间</th>
 * <th data-options="field:'coldTest',width:'80px',type:'dict'">冷试</th>
 * 2.以下方法中添加 FilterUtil.addIconFilter(datagridId)，参数为 table 的 id
 * $(function(){
 * 	FilterUtil.addIconFilter(datagridId);
 * });
 * 
 */
var FilterUtil = {
	//开启列筛选
	startFilter: function(datagridId){
		var $dg = $('#'+datagridId);
		var filters = FilterUtil.initFilter(datagridId);
		var opts = $dg.datagrid("options");
		opts.filterIsOpen = 'Y';
		$dg.datagrid('enableFilter', filters);
	},
	//关闭列筛选
	closeFilter: function(datagridId){
		var $dg = $('#'+datagridId);
		var opts = $dg.datagrid("options");
		opts.filterIsOpen = 'N';
		$dg.datagrid('disableFilter');
	},
	//初始化数据
	initFilter: function(datagridId){
		var $datagrid = $('#'+ datagridId);
		if($datagrid && $datagrid.length>0){
			var opts =$datagrid.datagrid("getColumnFields");
			var filters = [];
			var filter = {};
			if(opts && opts.length>0){
				for(var i=0;i<opts.length;i++){
					var field = opts[i];
					var fileAttr = $datagrid.datagrid('getColumnOption', field);
					if(fileAttr && fileAttr.type){
						if('numberbox' == fileAttr.type){
							filter = FilterUtil.creatFilter($datagrid, field, 'numberbox', null);
							filters.push(filter);
						}else if('date' == fileAttr.type){
							filter = FilterUtil.creatFilter($datagrid, field, 'datebox', null);
							filters.push(filter);
						}else if('dict' == fileAttr.type){
							var data = [];
							var dicts = window.dicts[fileAttr.dict];
							if(dicts && dicts.length > 0 ){
								data.push({value:'',text:'全部'});
								for(var j = 0 ; j < dicts.length; j++){
									var item = {};
									item.value=dicts[j].code;
									item.text=dicts[j].name;
									data.push(item);
								};
							}
							filter = FilterUtil.creatFilter($datagrid, field, 'combobox', data);
							filters.push(filter);
						}else {
							filter = FilterUtil.creatFilter($datagrid, field, 'text', null);
							filters.push(filter);
						}
					}
				}
				return filters;
			}
		}
	},
	//创建Filter对象
	creatFilter: function(object, field, type, data){
		var filter = {};
		filter.field = field;
		filter.type = type;
		var options = {
			 onChange:function(value){
	               if (value == ''){
	                  // dg.datagrid('removeFilterRule', this.name);
	            	   object.datagrid('addFilterRule', {
	                       field: this.name,
	                       op: 'eq',
	                       value: '',
	                       type:'string'
	                   });
	               } else {
	            	   object.datagrid('addFilterRule', {
	                       field: this.name,
	                       op: 'eq',
	                       value: value,
	                       type:'string'
	                   });
	               }
	               object.datagrid('doFilter');
	           }
		};
		options.panelHeight = 'auto';
		if(data && data.length > 0){
			options.data = data;
		}
		filter.options = options;
		return filter;
	},
	//增加列筛选按钮
	addIconFilter: function(datagridId){
		var str = '<div class="icon-filter"  onclick = FilterUtil.onclickIconFilter("' + datagridId + '")></div>';
		$(".datagrid-header-rownumber").html(str);
		var $dgtd = $(".datagrid-header-row").find("td");
		if($dgtd && $dgtd.length > 0){
			$dgtd[0].setAttribute("rowspan",'1');
		}
	},
	//列筛选按钮点击事件
	onclickIconFilter: function(datagridId){
		var options = $('#'+datagridId).datagrid("options");
		var filterIsOpen = options.filterIsOpen;
		if(StringUtil.isEmpty(filterIsOpen)){
			filterIsOpen = 'N';
		}
		if(filterIsOpen == 'N'){
			FilterUtil.startFilter(datagridId);
		}else if(filterIsOpen == 'Y'){
			FilterUtil.closeFilter(datagridId);
			FilterUtil.addIconFilter(datagridId);
		}
	},
	//清除列筛选参数
	clearFilter: function(datagridId){
		var $dg = $('#'+datagridId);
		var opts = $dg.datagrid("options");
		opts.filterIsOpen = 'N';
		$dg.datagrid('doFilter');
		$dg.datagrid('removeFilterRule');
		opts.filterIsOpen = 'Y';
	}
};

/**	util类 end *****************************************************************/


/******************************************************************************
 *	自定义控件
 * ****************************************************************************/
/**
 * 遮罩，
 * 用法：$(seletor).ctopMask('show')
 * 	   $(seletor).ctopMask('hide')
 * @param $
 */
(function($) {
	var methods = {
		init:function(options) {
			this.show();
		},
		show:function(options) {
			var $this = $(this);
	    	var width = $this.css('width');
	    	var height = $this.css('height');
	    	var top = $this.css('top');
	    	var left = $this.css('left');
	    	var html = '<div class="ctop-mask" style="width:'+ width +'; height:'+ height +'; top:'+ top +'; left:'+ left +'"><div class="ctop-mask-bar"><img src="data:image/gif;base64,R0lGODlhDwAPAKUAAEQ+PKSmpHx6fNTW1FxaXOzu7ExOTIyOjGRmZMTCxPz6/ERGROTi5Pz29JyanGxubMzKzIyKjGReXPT29FxWVGxmZExGROzq7ERCRLy6vISChNze3FxeXPTy9FROTJSSlMTGxPz+/OTm5JyenNTOzGxqbExKTAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACH/C05FVFNDQVBFMi4wAwEAAAAh+QQJBgAhACwAAAAADwAPAAAGd8CQcEgsChuTZMNIDFgsC1Nn9GEwDwDAoqMBWEDFiweA2YoiZevwA9BkDAUhW0MkADYhiEJYwJj2QhYGTBwAE0MUGGp5IR1+RBEAEUMVDg4AAkQMJhgfFyEIWRgDRSALABKgWQ+HRQwaCCEVC7R0TEITHbmtt0xBACH5BAkGACYALAAAAAAPAA8AhUQ+PKSmpHRydNTW1FxWVOzu7MTCxIyKjExKTOTi5LSytHx+fPz6/ERGROTe3GxqbNTS1JyWlFRSVKympNze3FxeXPT29MzKzFROTOzq7ISGhERCRHx6fNza3FxaXPTy9MTGxJSSlExOTOTm5LS2tISChPz+/ExGRJyenKyqrAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAZ6QJNQeIkUhsjkp+EhMZLITKgBAGigQgiiCtiAKJdkBgNYgDYLhmDjQIbKwgfF9C4hPYC5KSMsbBBIJyJYFQAWQwQbI0J8Jh8nDUgHAAcmDA+LKAAcSAkIEhYTAAEoGxsdSSAKIyJcGyRYJiQbVRwDsVkPXrhDDCQBSUEAIfkECQYAEAAsAAAAAA8ADwCFRD48pKKkdHZ01NLUXFpc7OrsTE5MlJKU9Pb03N7cREZExMbEhIKEbGpsXFZUVFZU/P78tLa0fH583NrcZGJk9PL0VE5MnJ6c/Pb05ObkTEZEREJErKqsfHp81NbUXF5c7O7slJaU5OLkzMrMjIaEdG5sVFJU/Pr8TEpMAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABndAiHA4DICISCIllBQWQgSNY6NJJAcoAMCw0XaQBQtAYj0ANgcE0SwZlgSe04hI2FiFAyEFRdQYmh8AakIOJhgQHhVCFQoaRAsVGSQWihAXAF9EHFkNEBUXGxsTSBxaGx9dGxFJGKgKAAoSEydNIwoFg01DF7oQQQAh+QQJBgAYACwAAAAADwAPAIVEPjykoqR0cnTU0tRUUlSMiozs6uxMSkx8fnzc3txcXlyUlpT09vRcWlxMRkS0trR8enzc2txcVlSUkpRUTkyMhoTk5uScnpz8/vxEQkR8dnTU1tRUVlSMjoz08vRMTkyEgoTk4uRkYmSclpT8+vy8urwAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAGc0CMcEgsGo9Gw6LhkHRCmICFODgAAJ8M4FDJTIUGCgCRwIQKV+9wMiaWtIAvRqOACiMKwucjJzFIJEN+gEQiHAQcJUMeBROCBFcLRBcAEESQAB0GGB4XGRkbghwCnxkiWhkPRRMMCSAfABkIoUhCDLW4Q0EAIfkECQYAGQAsAAAAAA8ADwCFRD48pKKkdHJ01NLU7OrsXFZUjIqMvLq8TEpM3N7c9Pb0lJaUxMbErK6sfH58bGpsVFJUTEZE3Nrc9PL0XF5clJKUxMLEVE5M5Obk/P78nJ6ctLa0hIaEREJE1NbU7O7sXFpcjI6MvL68TE5M5OLk/Pr8nJqczM7MtLK0hIKEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABnPAjHBILBqPRsICFCmESMcBAgAYdQAIi9HzSCUyJEOnAx0GBqUSsQJwYFAZyTiFGZZEgHGlJKACQBIZEwJXVR8iYwANE0MTAVMNGSISHAAhRSUYC2pCJFMhH4IaEAdGDGMdFFcdG0cJKSNYDoFIQgqctblBADs=">加载中...</div></div>'
	    	$this.append(html);
		},
		hide:function(options) {
			$(this).children('.ctop-mask').remove();
		}
	};
	
	$.fn.ctopMask = function() {
		var method = arguments[0];
		if(methods[method]) {
			method = methods[method];
			arguments = Array.prototype.slice.call(arguments, 1);
		} else if(typeof(method) == 'object' || !method) {
			method = methods.init;
		} else {
			$.error( 'Method ' +  method + ' does not exist on jQuery.autocomplete' );
			return this;
		}
		return method.apply(this, arguments)
	};
	
	$.ajaxSetup({
	    statusCode: { 
	        401: function() { 
	        	window.parent.location.href = contextPath + '/oAuthLogin.jsp';
	        }
	    } 
	})
})(jQuery);

/**
 * 多文件上传
 */
(function($) {
    var privateFunction = {
    	
    	bindFileChangeEvent: function(e, pulgin) {
    		var file = e.target;
    		var $file = $(file);
    		var settings = pulgin.data('settings');
    		
    		if($file.val()){
    			MsgUtil.loading();
    		    var files = file.files; // 获取文件对象 
    		    // FormData 对象  
    		    var form = new FormData();
    		    //form.append("field", "val"); // 可以增加表单数据 
    		    for(var i=0; i< files.length; i++){  
    	           form.append("files",files[i]); // 文件对象    
    	        } 
    		    // XMLHttpRequest 对象  
    		    var xhr = new XMLHttpRequest();  
    		    xhr.open("post", settings.uploadUrl, true);  
    		    xhr.onload = function () {
    		    	MsgUtil.unLoading();
    		    	if(JSON.parse(xhr.responseText).exception){
    					MsgUtil.showError(JSON.parse(xhr.responseText).exception.code);
    					$file.val('');
    				}else{
    					privateFunction.setValue(JSON.parse(xhr.responseText), pulgin, 'append');
    					
    					MsgUtil.showSuccess("附件上传成功！");
    				}
    		    }; 
    		    xhr.onerror = function(){
    				if (!StringUtil.isEmpty(jqXHR.responseText)) {
    					var msg = jqXHR.responseText;
    					MsgUtil.showError('上传附件失败,'
    							+ msg.substring(msg.lastIndexOf("message") + 10,msg.lastIndexOf('"}}')));
    				} else {
    					MsgUtil.showError('上传附件失败');
    				}
    				MsgUtil.unLoading();
    		    }
    		    xhr.send(form);
    		}
    	},
    	
    	// 删除文件
    	delAttachment: function(e, pulgin) {
    		/*AjaxUtil.get(pulgin.data('settings').delUrl + "?attachmentUuid=" + attachmentUuid,function(data){				
    			var attachmentUuidList = pulgin.data('attachmentUuidList');
    			ArrayUtil.remove(attachmentUuidList, attachmentUuid)
    			pulgin.data('attachmentUuidList', attachmentUuidList);
    			$('#' + pulgin.data('settings').fileboxId).find('input[type=hidden]').val(attachmentUuidList.join(','));
    			$(e.target).closest('.ct-mulfilebox-item').remove();
    		});*/
    		var attachmentUuidList = pulgin.data('attachmentUuidList');
			ArrayUtil.remove(attachmentUuidList, $(e.target).attr('attachmentUuid'));
			pulgin.data('attachmentUuidList', attachmentUuidList);
			$('#' + pulgin.data('settings').fileboxId).find('input[type=hidden]').val(attachmentUuidList.join(','));
			
			$(e.target).closest('.ct-mulfilebox-container').find('.ct-file-input').val('');
			
			$(e.target).closest('.ct-mulfilebox-item').remove();
    	},
    	
    	// 设或追加值
    	setValue: function(data, pulgin, operate) {
    		if(data && data.length > 0) {
    			var settings = pulgin.data('settings');
    			var itemCss = '';
    			if(settings.orient == 'horizontal') {
    				itemCss = 'style="float:left;width:'+ settings.itemWidth +';height:'+ settings.itemHeight +'; margin-right:15px;"';
                }
    			
    			var attachmentUuidList = [];
    			if(operate == 'append') {
    				if(pulgin.data('attachmentUuidList')) {
    					attachmentUuidList = pulgin.data('attachmentUuidList');
    				}
    			} else {
    				$('#' + settings.fileboxId + ' .ct-mulfilebox-content').html('');
    			}
    		
    			for (var i = 0; i < data.length; i++) {
    				var attachmentUuid = data[i].attachmentUuid;
    				var html = '<div class="ct-mulfilebox-item" ' + itemCss + ' id="'+ attachmentUuid +'_uploadfile">'
    					+		'<span class="ct-mulfilebox-text" title="'+ data[i].name +'">' + data[i].name + '</span>'
    					+		'<a class="textbox-icon icon-file-download" href="' + settings.downloadUrl+'?attachmentUuid=' + attachmentUuid +'">&nbsp;</a>'
    					+		'<a class="textbox-icon icon-clear" attachmentUuid="'+ attachmentUuid +'">&nbsp;</a>';
    				// 判断是否显示图片预览
    				if(settings.showImgFile == "Y" && privateFunction.isImg(data[i].name)) {
    					html += '<img class="ct-mulfilebox-img" src=" '+ settings.downloadUrl+'?attachmentUuid=' + attachmentUuid +' " alt="图片未找到" />';
    				}
    				html +=	'</div>';
    				
    				$('#' + settings.fileboxId + ' .ct-mulfilebox-content').append(html);
    				// 绑定删除事件
    				$('#' + attachmentUuid + '_uploadfile').find('.icon-clear').bind('click', function(e) {
    					privateFunction.delAttachment(e, pulgin);
    				});
    				// 新增文件
    				attachmentUuidList.push(attachmentUuid);
    			}
    			
    			pulgin.data('attachmentUuidList', attachmentUuidList);
    			$('#' + settings.fileboxId).find('input[type=hidden]').val(attachmentUuidList.join(','));
    		}
    	},
    	
    	// 判断是否为图片
    	isImg: function(fileName) {
    		if(StringUtil.endsWith(fileName, '.jpg')
    				|| StringUtil.endsWith(fileName, '.jpeg')
    				|| StringUtil.endsWith(fileName, '.gif')
    				|| StringUtil.endsWith(fileName, '.png')
    				|| StringUtil.endsWith(fileName, '.bmp')) {
    			return true;
    		} else {
    			return false;
    		}
    	}
    	
    };
 
    var methods = {
    	/**
    	 * 初始控件
    	 */
        init: function(options) {
 
            // 在每个元素上执行方法
            return this.each(function() {
                var $this = $(this);
                var fileboxId = StringUtil.uuid32();
                
                // 读取配置
                var settings = {
                	fileboxId: fileboxId,
                	name: $this.attr('name'),
                	width: $this.attr('width') || $.fn.ctMulFilebox.defaults.width,
                	
                	showImgFile: $this.attr('showImgFile') || $.fn.ctMulFilebox.defaults.showImgFile,
                	
                	orient: $this.attr('orient') || $.fn.ctMulFilebox.defaults.orient,
                	itemWidth: $this.attr('itemWidth') || $.fn.ctMulFilebox.defaults.itemWidth,
                	itemHeight: $this.attr('itemHeight') || $.fn.ctMulFilebox.defaults.itemHeight,
                	
                	uploadUrl: $this.attr('uploadUrl') || $.fn.ctMulFilebox.defaults.uploadUrl,
                	downloadUrl: $this.attr('downloadUrl') || $.fn.ctMulFilebox.defaults.downloadUrl,
                	delUrl: $this.attr('delUrl') || $.fn.ctMulFilebox.defaults.delUrl
                };
                $this.data('settings', settings);
                
                // 初始化HTML
                var html = '<div id="'+ fileboxId +'" class="ct-mulfilebox-container" style="width:'+ settings.width +'">'
                		+		'<input type="hidden" name="'+ settings.name +'"/>'
                		+		'<a class="ct-mulfilebox-btn">文件上传<input class="ct-file-input" type="file" multiple="true" /></a>'
                		+		'<div class="ct-mulfilebox-content"></div>'
                		+	'</div>';
                $this.after(html);
                $this.removeAttr('name');
                $this.attr('ctMulFileboxName', settings.name);
                $this.css("display", "none");
                
                // 绑定事件
                $('#' + fileboxId).find('.ct-file-input').bind('change', function(e) {
                	privateFunction.bindFileChangeEvent(e, $this, fileboxId);
                });
                
            });
        },
        
        /**
         * 销毁控件
         */
        destroy: function(options) {
            // 在每个元素中执行代码
            return $(this).each(function() {
                var $this = $(this);
                var settings = $this.data('settings');
                var fileboxId =  settings.fileboxId;
                // 删除元素对应的数据
                $this.removeData('attachmentUuidList');
                $this.removeData('settings');
                // 删除控件
                $('#' + fileboxId).remove();
                $this.attr('name', settings.name);
                $this.removeAttr('ctMulFileboxName');
            });
        },
        
        /**
         * 取值
         * @return 返回文件UUID数组
         */
        getValue: function(options) {
            return $(this).data('attachmentUuidList');
        },
        
        /**
         * 设值
         * @param 文件数组[{attachmentUuid: 文件UUID, name:文件名称},{attachmentUuid: 文件UUID2, name:文件名称2},...]
         */
        setValue: function(attachmentList) {
        	privateFunction.setValue(attachmentList, $(this));
        },
        
        /**
         * 取值，字符串形式
         * @return 返回文件UUID字符串，多个用逗号隔开
         */
        getValueText: function(options) {
        	var uuids = $(this).data('attachmentUuidList');
        	if(uuids) {
        		return uuids.join(',');
        	} else {
        		return null;
        	}
        	
        },
        
        /**
         * 设成只读模式
         */
        setReadonly: function(options) {
        	 var $this = $(this);
             var settings = $this.data('settings');
             var fileboxId =  settings.fileboxId;
             $('#' + fileboxId).find('.ct-mulfilebox-btn').hide();
             $('#' + fileboxId).find('.icon-clear').hide();
        }
        
    };
 
    $.fn.ctMulFilebox = function() {
        var method = arguments[0];
        if (methods[method]) {
            method = methods[method];
            arguments = Array.prototype.slice.call(arguments, 1);
        } else if (typeof(method) == 'object' || !method) {
            method = methods.init;
        } else {
            $.error('Method ' + method + ' does not exist on jQuery.ctMulFilebox');
            return this;
        }
        return method.apply(this, arguments);
    };
    
    $.fn.ctMulFilebox.defaults = {
    	width: '200px',// 控件的宽度	
    		
    	showImgFile: 'N',// 是否显示预览图片附件
    	
    	orient: 'vertical', // 布局方向vertical（垂直）、horizontal（水平）
    	itemWidth: '200px', // 每个文件区域的宽度
    	itemHeight: '200px', // 每个文件区域的高度
    	
		uploadUrl: contextPath + '/rest/base/baseAttachment/uploadFiles',
    	downloadUrl: contextPath + '/rest/base/baseAttachment/downloadFiles',
    	delUrl: contextPath + '/rest/base/baseAttachment/delFile'
	}
 
})(jQuery);


/**	自定义控件 end *****************************************************************/


/******************************************************************************
 *	公共方法 start 
 * ****************************************************************************/

/**
 * 跳转页面
 * @param url
 */
function toPage(url){
	$('body').ctopMask('show');
	window.location.href = contextPath + '/p/' + url;
}

/**
 * 回到上一个页面
 * @param url
 */
function toPrePage() {
	$('body').ctopMask('show');
	//history.go(-1);
	history.back();
}

/**
 * 打开一个新页面
 * @param url
 */
function openPage(url){
	window.open(contextPath + '/p/' + url);
}

//防止console.log 代码IE报错
if("undefined" == typeof console) {
	var console = {log:function(){}, info: function(){}, error: function(){}};
}

if (!Array.prototype.map) {
	Array.prototype.map = function(fun) {
		var len = this.length;
		if (typeof fun != "function")
			throw new TypeError();
		var res = new Array(len);
		var thisp = arguments[1];
		for (var i = 0; i < len; i++) {
			if (i in this)
				res[i] = fun.call(thisp, this[i], i, this);
		}
		return res;
	 };
}

if(!Array.prototype.filter){
	Array.prototype.filter = function(func) {
	    var arr = this;
	    var r = [];
	    for (var i = 0; i < arr.length; i++) {
	        if (func(arr[i])) {
	            r.push(arr[i]);
	        }
	    }
	    return r;
	}
}
if(!Array.prototype.indexOf){
	Array.prototype.indexOf = function(val) {
	    var arr = this;
	    for (var i = 0; i < arr.length; i++) {
	        if(arr[i] == val){
	        	return i;
	        }
	    }
	    return -1;
	}
}

if(!Array.prototype.forEach){
	Array.prototype.forEach =  function(fun) {
		var len = this.length;
		if (typeof fun != "function"){
			throw new TypeError();
		}
		for (var i = 0; i < len; i++) {
			fun.call(this, this[i]);
		}
	 };
}

if (!Function.prototype.bind) {
	Function.prototype.bind = function(oThis) {
		if (typeof this !== "function") {
			throw new TypeError(
					"Function.prototype.bind - what is trying to be bound is not callable");
		}
		var aArgs = Array.prototype.slice.call(arguments, 1), fToBind = this, fNOP = function() {
		}, fBound = function() {
			return fToBind.apply(this instanceof fNOP && oThis ? this : oThis,
					aArgs.concat(Array.prototype.slice.call(arguments)));
		};
		fNOP.prototype = this.prototype;
		fBound.prototype = new fNOP();
		return fBound;
	};
}  

if (!String.prototype.trim) {
	String.prototype.trim =function(){
		return $.trim(this);
	};
}  

if(!String.prototype.startsWith){
	String.prototype.startsWith = function(str){
		 return StringUtil.startsWith(this,str);       
	}
}

if(!String.prototype.endsWith){
	String.prototype.endsWith = function (str){
		return StringUtil.endsWith(this,str); 
	}
}

function encryptStr(str){
	var r = "";
	for(var i=0; i<str.length; i++){
		var code = str.charCodeAt(i);
	     var encryptCode = code + 8;
	     r += String.fromCharCode(encryptCode);
	 }
	return r;
}


/**	公共方法 end *****************************************************************/



/**
 * JS打开新的页面
 */
WindowUtil.openNewWin =  function(url,params){
	var divid = StringUtil.uuid32();
	var div = $("<div style='display: none;' id='"+divid+"'> </div>");
	var form = $("<form  action='#' method='post' target='_blank'></form>");
	form.attr("action",url);
	if(params) {
		$.each(params, function(i, param){
			form.append("<input name='"+param.name+"' value='"+param.value+"' type='text'></input");
		});
	}
	div.append(form);
	$(document.body).append(div);
	form.submit();
	div.remove();
}

/**
 * 消息提示框
 */
MsgUtil.showMsg4App = function(msg, title, callback) {
	if(StringUtil.isEmpty(title)) {
		title = '信息';
	}
	var dialogId = StringUtil.uuid32();
	var btnId = StringUtil.uuid32();
	var dialogHtml = '<div id="'+ dialogId +'" class="easyui-dialog" style="padding:20px 6px;width:80%;">'
				+		'<p>'+ msg +'</p>'
				+		'<div class="dialog-button">'
				+			'<a href="javascript:void(0)" id="'+ btnId +'" class="easyui-linkbutton" style="width:100%;height:35px" onclick="$(\'#'+ dialogId +'\').dialog(\'close\')">OK</a>'
				+		'</div>'
				+	 '</div>';
	$('body').append(dialogHtml);
	$('#' + dialogId).dialog({
		inline:true,
		modal:true,
		closed:true,
		title:title,
		onClose: function() {
	    	$('#' + this.id).window('destroy');
	    	if(callback) {
	    		callback();
	    	}
	    }
	});
	$('#' + dialogId).dialog('open').dialog('center');
	$('#' + btnId).linkbutton();
}


/**
 * 滚动刷新控件初始化
 * @param {} id 滑动控件id
 * @param {} pullDownAction 下拉响应事件
 * @param {} pullUpAction 上拉响应事件
 * @return {}
 */
function loadScroll(id, pullDownAction, pullUpAction) {
	var pullDownEl = $("#"+id+' .pullDown').get(0);
	var pullDownOffset = pullDownEl.offsetHeight;
	var pullUpEl = $("#"+id+' .pullUp').get(0);
	var pullUpOffset = pullUpEl.offsetHeight;
	var myScroll=null;
	myScroll = new iScroll(id, {
		scrollbarClass: 'myScrollbar', /* 重要样式 */
		useTransition: false, /* 此属性不知用意，本人从true改为false */
		topOffset: pullDownOffset,
		onRefresh: function () {
			if (pullDownEl.className.match('loading')) {
				pullDownEl.className = '';
			} else if (pullUpEl.className.match('loading')) {
				pullUpEl.className = '';
			}
		},
		onScrollMove: function () {
			if (this.y > 10 && !pullDownEl.className.match('flip')) {
				pullDownEl.className = 'flip';
			} else if (this.y < 10 && pullDownEl.className.match('flip')) {
				pullDownEl.className = '';
			} else if (this.y < (this.maxScrollY - 10) && !pullUpEl.className.match('flip')) {
				pullUpEl.className = 'flip';
			} else if (this.y > (this.maxScrollY + 10) && pullUpEl.className.match('flip')) {
				pullUpEl.className = '';
			}
		},
		onScrollEnd: function () {
			if (pullDownEl.className.match('flip')) {
				pullDownAction();	// Execute custom function (ajax call?)
				myScroll.refresh();
			} else if (pullUpEl.className.match('flip')) {
				pullUpAction();	// Execute custom function (ajax call?)
				myScroll.refresh();
			}
		}
	});
	return myScroll;
}
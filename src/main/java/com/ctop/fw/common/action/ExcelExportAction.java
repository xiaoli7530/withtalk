package com.ctop.fw.common.action;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.ctop.fw.common.excelexport.Datagrid;
import com.ctop.fw.common.excelexport.DatagridRenderer;
import com.ctop.fw.common.model.NuiPageRequestData;
import com.ctop.fw.common.model.PageResponseData;
import com.ctop.fw.common.utils.AppContextUtil;
import com.ctop.fw.common.utils.StringUtil;

@RestController
@RequestMapping(path = "/rest/common/excelExport")
public class ExcelExportAction {

	@Autowired
	RequestMappingHandlerMapping requestMappingHandlerMapping;
	ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

	@RequestMapping(value = "/download", method = RequestMethod.GET)
	public void download(@RequestParam("uuid") String uuid, HttpServletRequest request, HttpServletResponse response,
			HttpSession session) {
		try {
			Object obj = session.getAttribute(uuid);
			if (obj instanceof DatagridRenderer) {
				DatagridRenderer excel = (DatagridRenderer) obj;
				excel.download(excel.getTitle() + ".xlsx", request, response);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		session.removeAttribute(uuid);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/datagrid", method = RequestMethod.POST, produces = "text/plain")
	public String buildDatagridExcel(@RequestBody Datagrid datagrid, HttpServletRequest request,
			HttpServletResponse response) { 
		DatagridRenderer builder = new DatagridRenderer(datagrid);
		HandlerMethod handlerMethod = null;
		// 根据URL从spring的元数据中取handlerMethod;
		if (StringUtil.isNotEmpty(datagrid.getUrl())) {
			String contextPath = request.getContextPath();
			String url = datagrid.getUrl();
			int index = url.indexOf(contextPath);
			if (index != -1) {
				url = url.substring(index + contextPath.length());
				datagrid.setUrl(url);
			}
			index = url.indexOf("?");
			if(index != -1) {
				url = url.substring(0, index);
			}
			Map<RequestMappingInfo, HandlerMethod> map = this.requestMappingHandlerMapping.getHandlerMethods();
			
			for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : map.entrySet()) {
				RequestMappingInfo mappingInfo = entry.getKey();
				
				List<String> list = mappingInfo.getPatternsCondition().getMatchingPatterns(url);
				if (list.size() > 0) {
					handlerMethod = entry.getValue();
					break;
				}
			}
		}  

		builder.renderTitleRows("A1"); 
		// 前台传来数据
		if (StringUtil.isEmpty(datagrid.getUrl()) && datagrid.getData() != null) {
			builder.appendRowDatas(datagrid.getData());
		}
		// 调用后台分页逻辑取数据；这个方法的声明 PageResponseData<Object>
		// methodName(NuiPageRequestData data);
		if (handlerMethod != null) {
			NuiPageRequestData data = datagrid.getQueryParams(); 
			data.setPageIndex(0);
			data.setPageSize(1000);
			try {
				Object bean = handlerMethod.getBean();
				MethodParameter[] params = handlerMethod.getMethodParameters();
				Object[] paramValues = new Object[params.length]; 
				//处理分页方法有多个参数的情况；
				for(MethodParameter param : params) {
					if(param.hasParameterAnnotation(RequestParam.class)) {
						RequestParam a = param.getParameterAnnotation(RequestParam.class);
						String value = request.getParameter(a.name());
						paramValues[param.getParameterIndex()] = value;
					}
					if(param.hasParameterAnnotation(RequestBody.class)
							&& param.getParameterType().equals(NuiPageRequestData.class)) {
						paramValues[param.getParameterIndex()] = data;
					}
				}
				if (bean instanceof String) {
					bean = AppContextUtil.getContext().getBean((String) bean);
				}
				PageResponseData<Object> page = (PageResponseData<Object>) handlerMethod.getMethod().invoke(bean, paramValues);
				builder.appendRowDatas(page.getData());
				for (int i = 1; i < page.getTotalPages(); i++) {
					data.setPageIndex(i);
					page = (PageResponseData<Object>) handlerMethod.getMethod().invoke(bean, paramValues);
					builder.appendRowDatas(page.getData());
				}
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// 将结果放入session, 前台会发个普通请求下载文件；
		String uuid = UUID.randomUUID().toString();
		HttpSession session = request.getSession();
		session.setAttribute(uuid, builder);
		//几分钟后，自动清除；
		this.executor.schedule(new ClearTask(uuid, session), 2, TimeUnit.MINUTES);
		return uuid;
	}
	
	static class ClearTask implements Runnable {
		private String uuid;
		private HttpSession session;
		ClearTask(String uuid, HttpSession session) {
			this.uuid = uuid;
			this.session = session;
		}
		
		@Override
		public void run() {
			session.removeAttribute(uuid);
		}
		
	}
}

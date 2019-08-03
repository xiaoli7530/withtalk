package com.ctop.fw.config;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import com.ctop.fw.common.utils.AppContextUtil;
import com.ctop.fw.common.utils.ListUtil;
import com.ctop.fw.common.utils.StringUtil;
import com.ctop.fw.common.utils.UserContextUtil;
import com.ctop.fw.sys.service.SysAccessLogService;

public class LogFilter extends OncePerRequestFilter {
	private static Logger logger = LoggerFactory.getLogger(OncePerRequestFilter.class); 

	private static final int DEFAULT_MAX_PAYLOAD_LENGTH = 50;
 
	private boolean includeRequestPayload = true;
	private boolean includeResponsePayload = true;
	
	private int maxPayloadLength = DEFAULT_MAX_PAYLOAD_LENGTH; 

	public void setMaxPayloadLength(int maxPayloadLength) {
		Assert.isTrue(maxPayloadLength >= 0, "'maxPayloadLength' should be larger than or equal to 0");
		this.maxPayloadLength = maxPayloadLength;
	}

	@Override
	protected boolean shouldNotFilterAsyncDispatch() {
		return false;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		boolean isFirstRequest = !isAsyncDispatch(request);
		HttpServletRequest requestToUse = request;
		HttpServletResponse responseToUse = response;

		if (isIncludeRequestPayload() && isFirstRequest && !(request instanceof ContentCachingRequestWrapper)) {
			requestToUse = new ContentCachingRequestWrapper(request);
		}
		if (isIncludeResponsePayload() && isFirstRequest && !(response instanceof ContentCachingResponseWrapper)) {
			responseToUse = new ContentCachingResponseWrapper(response);
		}

		boolean shouldLog = shouldLog(requestToUse);
		AccessLog log = null;
		if (shouldLog && isFirstRequest) { 
			log = new AccessLog();
			log.readRequestInfo(requestToUse);
		}
		try {
			filterChain.doFilter(requestToUse, responseToUse);
		} finally {
			if (shouldLog && !isAsyncStarted(requestToUse)) { 
				if(this.isIncludeRequestPayload()) {
					log.readRequestPayload(requestToUse, maxPayloadLength);
				}
				
				if(this.isIncludeResponsePayload()) {
					log.readResponsePayload(responseToUse, maxPayloadLength);
				}
			}
			if (responseToUse instanceof ContentCachingResponseWrapper) {
				ContentCachingResponseWrapper res = (ContentCachingResponseWrapper) responseToUse;
				res.copyBodyToResponse();
			}
			if(log != null) {
				HttpSession session = requestToUse.getSession();
				String loginName = UserContextUtil.getLoginName(session);
				log.setLoginName(loginName);
				logger.info(log.toString());
				this.saveAccessLog(request, log);
			}
		}
	}
 
	protected boolean shouldLog(HttpServletRequest request) {
		String requestUri = request.getRequestURI();
		String contextPath = request.getContextPath();
		String apiPath = requestUri.replaceFirst(contextPath + "/rest", ""); 
		return apiPath.startsWith("/rf/") || "/sys/sysAccount/login".equals(apiPath);
//		return true;
	}
	
	private void saveAccessLog(HttpServletRequest request, AccessLog log) { 
		SysAccessLogService service = AppContextUtil.getBean(SysAccessLogService.class);
		service.saveAccessLog(log);
	}

	// ================ getter setter =============================== 
	public boolean isIncludeRequestPayload() {
		return includeRequestPayload;
	}

	public void setIncludeRequestPayload(boolean includeRequestPayload) {
		this.includeRequestPayload = includeRequestPayload;
	}

	public boolean isIncludeResponsePayload() {
		return includeResponsePayload;
	}

	public void setIncludeResponsePayload(boolean includeResponsePayload) {
		this.includeResponsePayload = includeResponsePayload;
	}

	protected int getMaxPayloadLength() {
		return this.maxPayloadLength;
	} 
	
	public static class AccessLog implements Serializable{
		
		private static final long serialVersionUID = -7166537582001599751L;
		
		private Date requestTime;
		private Date responseTime;
		private String requestUri;
		private String queryString;
		private String client;
		private String session;
		private String user;
		private String requestMethod;
		private String params;
		private String requesHeaders;
		private String requestPayload;
		
		private String responseStatus;
		private String responseHeaders;
		private String responsePayload;
		private String loginName;
		
		private String rstCode;
		public AccessLog() {
			this.requestTime = new Date();
		}
		
		/**
		 * 读参数数据；
		 * @param request
		 */
		public void readRequestInfo(HttpServletRequest request) {
			this.requestUri = request.getRequestURI();
			this.queryString = request.getQueryString();
			this.client = request.getRemoteAddr();
			this.requestMethod = request.getMethod();

			Map<String, String[]> paramsMap = request.getParameterMap();
			List<String> paramTexts = new ArrayList<String>();
			for(Map.Entry<String, String[]> entry : paramsMap.entrySet()) {
				paramTexts.add("    " + entry.getKey() + ": " + ListUtil.join(Arrays.asList(entry.getValue()), ",") + ", \n");
			}
			this.params = "{\n" + ListUtil.join(paramTexts, "") + "]";
			this.rstCode = request.getHeader("rstcode");
			HttpSession session = request.getSession(false);
			if (session != null) {
				this.session = session.getId();
			}
			this.user = request.getRemoteUser();
			
			Enumeration<String> it = request.getHeaderNames();
			List<String> list = new ArrayList<String>();
			while(it.hasMoreElements()) {
				String name = it.nextElement();
				String text = "    " + name + ": " + request.getHeader(name) + ";\n";
				list.add(text);
			}
			this.requesHeaders = "[\n" + ListUtil.join(list, "") + "]";
		}
		
		/**
		 * 读请求体数据；
		 * @param request
		 * @param maxPayloadLength
		 */
		public void readRequestPayload(HttpServletRequest request, int maxPayloadLength) {
			ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(request,
					ContentCachingRequestWrapper.class);
			if (wrapper != null) {
				byte[] buf = wrapper.getContentAsByteArray();
				if (buf.length > 0) {
					int length = Math.min(buf.length, maxPayloadLength);
					String payload;
					try {
						payload = new String(buf, 0, length, wrapper.getCharacterEncoding());
					} catch (UnsupportedEncodingException ex) {
						payload = "[unknown]";
					}
					this.requestPayload = payload;
				}
			}
		}
		
		/**
		 * 读response体数据；
		 * @param request
		 * @param maxPayloadLength
		 */
		public void readResponsePayload(HttpServletResponse response, int maxPayloadLength) {
			this.responseTime = new Date();
			Collection<String> names = response.getHeaderNames();
			List<String> list = new ArrayList<String>();
			for(String name : names) {
				String text = "    " + name + ": " + response.getHeader(name) + ";\n";
				list.add(text);
			}
			this.responseHeaders = "[\n" + ListUtil.join(list, "") + "]"; 
			this.responseStatus = String.valueOf(response.getStatus());
			
			ContentCachingResponseWrapper wrapper = WebUtils.getNativeResponse(response,
					ContentCachingResponseWrapper.class);
			if (wrapper != null) {
				byte[] buf = wrapper.getContentAsByteArray();
				if (buf.length > 0) {
					int length = Math.min(buf.length, maxPayloadLength);
					String payload;
					try {
						payload = new String(buf, 0, length, wrapper.getCharacterEncoding());
					} catch (UnsupportedEncodingException ex) {
						payload = "[unknown]";
					}
					this.responsePayload = payload;
				}
			}
		}
		
		public String toString() {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			StringBuilder buff = new StringBuilder();
			
			buff.append("\n==========request==============\n");
			buff.append("\nmethod:").append(this.requestMethod);
			buff.append("\nuri:").append(this.requestUri);
			if(StringUtil.isNotEmpty(this.queryString)) {
				buff.append("?").append(this.queryString);
			}
			buff.append("\nrequestTime:").append(format.format(this.requestTime));
			buff.append("\nsession:").append(this.session);
			buff.append("\nclient:").append(this.client);
			buff.append("\nuser:").append(this.user);
			if(StringUtil.isNotEmpty(this.requesHeaders)) {
				buff.append("\nheaders:\n").append(this.requesHeaders);
			}
			if(StringUtil.isNotEmpty(this.params)) {
				buff.append("\nparams:\n").append(this.params);
			}
			if(StringUtil.isNotEmpty(this.requestPayload)) {
				buff.append("\npayload:\n").append(this.requestPayload);
			}
			
			buff.append("\n==========response==============");
			if(this.responseTime != null) {
				buff.append("\nresponseTime:").append(format.format(this.responseTime));
				buff.append("\ntime spent:").append(this.responseTime.getTime() - this.requestTime.getTime());
			}
			buff.append("\nstatus:").append(this.responseStatus);
			if(StringUtil.isNotEmpty(this.responseHeaders)) {
				buff.append("\nheaders:\n").append(this.responseHeaders);
			}
			if(StringUtil.isNotEmpty(this.responsePayload)) {
				buff.append("\npayload:\n").append(this.responsePayload);
			}
			return buff.toString();
		}
		
		public String getRequestUri() {
			return requestUri;
		}
		public void setRequestUri(String requestUri) {
			this.requestUri = requestUri;
		}
		public String getQueryString() {
			return queryString;
		}
		public void setQueryString(String queryString) {
			this.queryString = queryString;
		}
		public String getClient() {
			return client;
		}
		public void setClient(String client) {
			this.client = client;
		}
		public String getUser() {
			return user;
		}
		public void setUser(String user) {
			this.user = user;
		}
		public String getRequstPayload() {
			return requestPayload;
		}
		public void setRequstPayload(String requstPayload) {
			this.requestPayload = requstPayload;
		}
		public String getResponsePayload() {
			return responsePayload;
		}
		public void setResponsePayload(String responsePayload) {
			this.responsePayload = responsePayload;
		}

		public Date getRequestTime() {
			return requestTime;
		}

		public void setRequestTime(Date requestTime) {
			this.requestTime = requestTime;
		}

		public Date getResponseTime() {
			return responseTime;
		}

		public void setResponseTime(Date responseTime) {
			this.responseTime = responseTime;
		}

		public String getSession() {
			return session;
		}

		public void setSession(String session) {
			this.session = session;
		}

		public String getRequesHeaders() {
			return requesHeaders;
		}

		public void setRequesHeaders(String requesHeaders) {
			this.requesHeaders = requesHeaders;
		}

		public String getResponseHeaders() {
			return responseHeaders;
		}

		public void setResponseHeaders(String responseHeaders) {
			this.responseHeaders = responseHeaders;
		}

		public String getRequestPayload() {
			return requestPayload;
		}

		public void setRequestPayload(String requestPayload) {
			this.requestPayload = requestPayload;
		}

		public String getLoginName() {
			return loginName;
		}

		public void setLoginName(String loginName) {
			this.loginName = loginName;
		}

		public String getRstCode() {
			return rstCode;
		}

		public void setRstCode(String rstCode) {
			this.rstCode = rstCode;
		}

		public String getRequestMethod() {
			return requestMethod;
		}

		public void setRequestMethod(String requestMethod) {
			this.requestMethod = requestMethod;
		}

		public String getParams() {
			return params;
		}

		public void setParams(String params) {
			this.params = params;
		}

		public String getResponseStatus() {
			return responseStatus;
		}

		public void setResponseStatus(String responseStatus) {
			this.responseStatus = responseStatus;
		}
	}

}

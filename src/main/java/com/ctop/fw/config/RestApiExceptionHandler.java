package com.ctop.fw.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.WebUtils;

import com.ctop.fw.common.utils.BusinessException;

/**
 * 统一处理后台REST接口中抛出的异常
 */
@RestControllerAdvice
public class RestApiExceptionHandler extends ResponseEntityExceptionHandler {

	private static Logger logger = LoggerFactory.getLogger(RestApiExceptionHandler.class);
	
	@ExceptionHandler({Exception.class})
	public final ResponseEntity<Object> handleAllException(Exception ex, WebRequest request) {
		logger.error("", ex);
		
		//ex.printStackTrace();
		return this.handleException(ex, request);
	}
	
	/**
	 * A single place to customize the response body of all Exception types.
	 * <p>The default implementation sets the {@link WebUtils#ERROR_EXCEPTION_ATTRIBUTE}
	 * request attribute and creates a {@link ResponseEntity} from the given
	 * body, headers, and status.
	 * @param ex the exception
	 * @param body the body for the response
	 * @param headers the headers for the response
	 * @param status the response status
	 * @param request the current request
	 */
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		logger.error("", ex);
		if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
			request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
		}
		headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
		Map<String, Map<String, String>> bodyMap = new HashMap<String, Map<String, String>>();
		bodyMap.put("exception", buildExceptionData(ex)); 
		return new ResponseEntity<Object>(bodyMap, headers, status);
	}
	
	public static Map<String, String> buildExceptionData(Exception ex) {
		Throwable root = ex;
		BusinessException bizEx = null;
		if (root instanceof BusinessException) {
			bizEx = (BusinessException) root;
		}
		while(root != null && root.getCause() != null && root.getCause() != root) {
			root = root.getCause();
			if (root instanceof BusinessException) {
				bizEx = (BusinessException) root;
			}
		}
		Map<String, String> exception = new HashMap<String, String>();
		if (root != null) {
			exception.put("message", root.getMessage());
			exception.put("stack", ExceptionUtils.getFullStackTrace(ex));
		}
		if(bizEx != null) {
			exception.put("code", bizEx.getCode());
			exception.put("message", bizEx.getFullMessage());
			logger.error(exception.get("message"));
		} 
		if (root instanceof MethodArgumentNotValidException) {
			MethodArgumentNotValidException ve = (MethodArgumentNotValidException) root;
			BindingResult br = ve.getBindingResult();
			List<FieldError> fieldErrors = br.getFieldErrors();
			logger.debug("REST 请求参数绑定失败！");
		}
		return exception;
	}
}

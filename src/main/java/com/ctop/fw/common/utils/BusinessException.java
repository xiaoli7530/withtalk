package com.ctop.fw.common.utils;

import java.io.IOException;
import java.text.MessageFormat;

import org.apache.commons.io.IOUtils;
import org.springframework.http.client.ClientHttpResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BusinessException extends RuntimeException {
	private static final long serialVersionUID = 6426776357073008575L;
	private String code;
	private String template;
	private Object[] params;
	private String remoteStack;
	
	public BusinessException(String message) {
		super(message);
		this.code = message;
		this.template = message;
		this.params = new Object[]{};
	}
	
	public BusinessException(String message, Object[] params) {
		super(message);
		this.code = message;
		this.template = message;
		this.params = params;
	}
	
	public BusinessException(Throwable ex, String message, Object[] params) {
		super(ex);
		this.code = message;
		this.template = message;
		this.params = params;
	}

	private BusinessException(String code, String template, Object[] params) { 
		super(code);
		this.code = code;
		this.template = template;
		this.params = params;
	}
	
	public BusinessException(ClientHttpResponse response) {
		ObjectMapper om = new ObjectMapper();
		JsonNode node;
		try {
			node = om.readTree(response.getBody());
			this.remoteStack = node.findValue("stack").asText();
			this.code = node.findValue("code").asText();
			this.template = node.findValue("message").asText();
		} catch (Exception e) {
			try {
				this.code = ListUtil.join(IOUtils.readLines(response.getBody()), "");
				this.template = this.code;
			} catch (IOException e1) {
				//
			}
		}
	}
	
	
	public static BusinessException template(String template, Object... params) {
		return new BusinessException(template, template, params);
	}
	
	public static BusinessException code(String code, Object... params) {
		return new BusinessException(code, null, params);
	}
	
	public String getFullMessage() {
		if(!StringUtil.isEmpty(this.code)) {
			return MessageUtil.getMessage(this.code, this.params);
		} else {
			MessageFormat message = new MessageFormat(this.template);
			return message.format(message);
		}
	}
	
	@Override
	public String getMessage() {
		return getFullMessage();
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public Object[] getParams() {
		return params;
	}

	public void setParams(Object[] params) {
		this.params = params;
	}
	
	
}

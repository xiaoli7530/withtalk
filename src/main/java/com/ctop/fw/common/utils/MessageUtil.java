package com.ctop.fw.common.utils;

import java.text.MessageFormat;
import java.util.Locale;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

@Component
public class MessageUtil{ 
	private static MessageSource messageSource;

	@Autowired
	public void setMessageSource(MessageSource messageSource) throws BeansException {
		MessageUtil.messageSource = messageSource;
	}
	

	public static String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
		String msg = messageSource.getMessage(code, args, defaultMessage, locale);
		return msg != null ? msg.trim() : msg;
	}

	public static String getMessage(String code, Object[] args, Locale locale) {
		String msg = messageSource.getMessage(code, args, locale);
		return msg != null ? msg.trim() : msg;
	}
	
	public static String getMessage(String code, Object[] args) { 
		ServletRequestAttributes ra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		Locale locale = RequestContextUtils.getLocale(ra.getRequest());
		String msg = messageSource.getMessage(code, args, locale);
		if(msg != null && msg.equals(code) && args != null && args.length > 0) {
			return new MessageFormat(code).format(args);
		}
		return msg != null ? msg.trim() : msg;
	}


}

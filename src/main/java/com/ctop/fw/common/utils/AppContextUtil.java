package com.ctop.fw.common.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class AppContextUtil implements ApplicationContextAware {
	
	private static ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		AppContextUtil.context = applicationContext;
	}

	public static <T> T getBean(Class<T> clz) {
		return context.getBean(clz);
	}
	
	public static ApplicationContext getContext() {
		return context;
	}
}

package com.ctop.fw.sys.rest;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DispatcherController {
	/**
	 * 模块页面跳转
	 * @param module
	 * @param page
	 * @return
	 */
	@RequestMapping("/p/**")
	public String jumpPage(HttpServletRequest  request){
		String servletPath = request.getServletPath();
		
		return servletPath.substring(3);
	}
	
}

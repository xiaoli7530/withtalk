package com.ctop.fw.common.action;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ctop.fw.common.constants.Constants;
import com.ctop.fw.common.model.UserDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class PageAction {

	@RequestMapping("/index.do")
	public ModelAndView index(HttpSession session) throws JsonProcessingException {
		UserDto user = (UserDto) session.getAttribute(Constants.SESSION_KEY_USER);
		ModelAndView mav = new ModelAndView();
		ObjectMapper mapper = new ObjectMapper();
		mav.addObject("user", mapper.writeValueAsString(user));
		mav.setViewName("index"); 
		return mav;
		/*** 当返回index字符串，会自动去WEB-INF JSP路径寻找index.jsp */
	}
	
	@RequestMapping("/")
	public ModelAndView root(HttpSession session) throws JsonProcessingException {
		UserDto user = (UserDto) session.getAttribute(Constants.SESSION_KEY_USER);
		ModelAndView mav = new ModelAndView();
		ObjectMapper mapper = new ObjectMapper();
		mav.addObject("user", mapper.writeValueAsString(user));
		if(user!=null){
			mav.setViewName("index");
		}else{
			mav.setViewName("login");
		}
		return mav;
		/*** 当返回index字符串，会自动去WEB-INF JSP路径寻找index.jsp */
	}

}

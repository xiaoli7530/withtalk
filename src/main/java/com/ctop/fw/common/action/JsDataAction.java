package com.ctop.fw.common.action;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ctop.fw.schedule.JavascriptDataSchedule;
import com.ctop.fw.schedule.JavascriptDataSchedule.CachedJsData;

@RestController
@RequestMapping(path = "/jsdata")
public class JsDataAction {
	@Autowired
	JavascriptDataSchedule javascriptDataSchedule;

	@RequestMapping(value = "/{name}.js", method = RequestMethod.GET)
	public void getJsData(
				@PathVariable(name="name", required=true) String name, 
				HttpServletRequest request, 
				HttpServletResponse response) throws IOException { 
		long ifModifiedSince = request.getDateHeader("If-Modified-Since");
		CachedJsData data = javascriptDataSchedule.getJsData(name);
		long gap = data.getLastModifiedDate().getTime() - ifModifiedSince;
		if (Math.abs(gap) > 1000 * 10) {
			response.getWriter().write(data.getContent());
			response.setStatus(200);
			response.setDateHeader("Last-Modified", data.getLastModifiedDate().getTime());
		} else {
			response.setStatus(304);
		}
	}
}

package com.ctop.fw.config;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

import com.ctop.fw.common.utils.StringUtil;

public class NoCacheFilter extends OncePerRequestFilter {
	private List<String> pathPrefixes;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		filterChain.doFilter(request, response);
		if (shouldNoCache(request)) {
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Expires", "-1");
		}
	}
	
	private boolean shouldNoCache(HttpServletRequest request) {
		//XmlHttpRequest
		String xhr = request.getHeader("X-Requested-With");
		if (StringUtil.isNotEmpty(xhr)) {
			String contextPath = request.getContextPath();
			String requestUri = request.getRequestURI();
			String apiPath = requestUri.replaceFirst(contextPath, ""); 
			if (this.pathPrefixes != null) {
				for(String prefix : this.pathPrefixes) {
					if (apiPath.startsWith(prefix)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public List<String> getPathPrefixes() {
		return pathPrefixes;
	}

	public void setPathPrefixes(List<String> pathPrefixes) {
		this.pathPrefixes = pathPrefixes;
	}
}

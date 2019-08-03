package com.ctop.fw.config;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.ctop.fw.common.utils.StringUtil;

// UserSecurityInterceptor代码
public class UserSecurityInterceptor extends HandlerInterceptorAdapter {
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String contextPath = request.getContextPath();
		String requestUri = request.getRequestURI();
		if("OPTIONS".equals(request.getMethod())) {
			response.setHeader("Access-Control-Allow-Credentials", "true");
			response.setHeader("Access-Control-Allow-Headers", "accept, content-type, File-Name");
			response.setHeader("Access-Control-Allow-Methods", "GET,PUT,DELETE,POST,DELETE,OPTIONS");
			response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
			response.setHeader("Access-Control-Max-Age", "3600");
			return true;
		}
		if (Authenticator.isAuthenticated(request)) {
			//本地开发时是跨域请求，需要这些header;
			response.setHeader("Access-Control-Allow-Credentials", "true");
			response.setHeader("Access-Control-Allow-Headers", "accept, content-type");
			response.setHeader("Access-Control-Allow-Methods", "GET,PUT,DELETE,POST,DELETE,OPTIONS");
			response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
			response.setHeader("Access-Control-Max-Age", "3600");
			response.setStatus(401);
			response.setHeader("ERROR_CODE", "401");
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html");
			//response.getWriter().write("{\"exception\": {\"code\": \"\", \"message\": \"会话已过期，请重新登陆!\"}}");
			//response.sendRedirect(request.getContextPath() + "/p/login");
			//response.getWriter().print("<html><script>window.parent.location.href ='"+request.getContextPath()+"/p/login'</script></html>");
			
			// 把请求的url存入cookie，用于登录成功后再跳转到此url
			String queryString = request.getQueryString();
			String requestUrl = request.getRequestURL().toString();
			if(StringUtil.isNotEmpty(queryString)) {
				requestUrl = requestUrl + "?" + queryString;
			}
			Cookie cookie = new Cookie("oauthRequestUrl", requestUrl);
			cookie.setMaxAge(3600);
			cookie.setPath("/");
			response.addCookie(cookie);
			response.getWriter().print("<html><script>window.parent.location.href ='"+request.getContextPath()+"/oAuthLogin.jsp'</script></html>");
			response.flushBuffer();
			
			/* 作用同上
			String queryString = request.getQueryString();
			String requestUrl = request.getRequestURL().toString();
			Cookie cookie = new Cookie("oauthRequestUrl", requestUrl + "?" + queryString);
			cookie.setMaxAge(3600);
			cookie.setPath("/");
			response.addCookie(cookie);
			request.getRequestDispatcher("/oAuthLogin.jsp").forward(request, response);*/
			
			return false;
		}
		return true;
	}
}
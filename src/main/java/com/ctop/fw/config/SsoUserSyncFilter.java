package com.ctop.fw.config;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ctop.fw.common.utils.StringUtil;
import com.ctop.fw.common.utils.UserContextUtil;

public class SsoUserSyncFilter implements Filter{
	private static Logger logger = LoggerFactory.getLogger(SsoUserSyncFilter.class);
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest r, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request  = (HttpServletRequest) r;
		// SSO时，容器提供的已登录的登录名
		String remoteUser = request.getRemoteUser();
		String accountUuid = UserContextUtil.getAccountUuid();
		// 检查Session中是否有登录用户，开发环境需单机登录，生产环境RF需通过EPMS登录，通过LDAP登录
		if (StringUtil.isNotEmpty(remoteUser) && StringUtil.isEmpty(accountUuid)) {
			logger.debug("REMOTE USER:" + remoteUser + ", 同步登录信息!");
			// 根据SSO传过来的loginName取登录信息；
			Authenticator.syncUserContext(request);
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
	}
	
}

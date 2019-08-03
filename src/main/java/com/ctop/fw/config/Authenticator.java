package com.ctop.fw.config;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.query.LdapQueryBuilder;

import com.ctop.fw.common.constants.Constants.LoginType;
import com.ctop.fw.common.model.UserDto;
import com.ctop.fw.common.utils.AppContextUtil;
import com.ctop.fw.common.utils.BusinessException;
import com.ctop.fw.common.utils.StringUtil;
import com.ctop.fw.common.utils.UserContextUtil;
import com.ctop.fw.sys.dto.SysAccountDto;
import com.ctop.fw.sys.entity.SysLoginLog;
import com.ctop.fw.sys.service.SysAccountService;
import com.ctop.fw.sys.service.SysLoginLogService;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Authenticator {
	public static final String AUTHENTICATE_WITH_LDAP = "epms.authenticate.with.ldap";
	private static Logger logger = LoggerFactory.getLogger(Authenticator.class);

	/**
	 * 判断请求是否是已登录用户发出的请求；
	 * @param request
	 * @return
	 */
	public static boolean isAuthenticated(HttpServletRequest request) {		
		String remoteUser = request.getRemoteUser();
		// SSO时，容器提供的已登录的登录名
		if (StringUtil.isNotEmpty(remoteUser)) {
			return true;
		}
		// 检查Session中是否有登录用户，开发环境需单机登录，生产环境RF需通过EPMS登录，通过LDAP登录
		String accountUuid = UserContextUtil.getAccountUuid();
		if (StringUtil.isNotEmpty(accountUuid)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 同步SSO登录的用户信息到session中
	 * @param request
	 */
	public static void syncUserContext(HttpServletRequest request) {
		// Session中已有用户，不需同步
		String accountUuid = UserContextUtil.getAccountUuid();
		if (StringUtil.isNotEmpty(accountUuid)) {
			return;
		}
		// session中没有，则判断容器是否提供，将容器提供的用户信息放入session
		String remoteUser = request.getRemoteUser();
		// 没登录
		if (StringUtil.isEmpty(remoteUser)) {
			return;
		}
		// 项目需要的用户信息放入session;
		SysAccountService service = AppContextUtil.getBean(SysAccountService.class);
		try {
			UserDto userDto = service.retrieveUserInfo(remoteUser);
			userDto.setLoginFromSso(true);
			UserContextUtil.setUser(userDto);
			// 保存登录日志；
			SysLoginLogService logService = AppContextUtil.getBean(SysLoginLogService.class);
			SysLoginLog log = SysLoginLog.buildSuccessLog(LoginType.EPMSWMS_WEBSSO, userDto.getSysAccount(), request);
			logService.addSysLoginLog(log);
		} catch (RuntimeException ex) {
			Map<String, String> exceptionData = RestApiExceptionHandler.buildExceptionData(ex);
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
			mapper.setSerializationInclusion(Include.NON_NULL);
			try {
				request.setAttribute("ssoUserSyncException", mapper.writeValueAsString(exceptionData));
			} catch (JsonProcessingException e) {
				request.setAttribute("ssoUserSyncException", "{'message': '不能同步用户信息, 自动登录系统失败!'}");
			}
			// 保存登录日志；
			SysLoginLogService logService = AppContextUtil.getBean(SysLoginLogService.class);
			SysLoginLog log = SysLoginLog.buildErrorLog(LoginType.EPMSWMS_WEBSSO, remoteUser, request, ex);
			logService.addSysLoginLog(log);
		}
	}
	
	//	Ldap://10.203.24.216:389
	//		BaseDN:  dc=tam,dc=sgmam,dc=com
	//		Bind: uid=epmsbind,cn=users,dc=tam,dc=sgmam,dc=com
	//		PWD: Pass1234
	/**
	 * 根据使用的环境判断，采用域登录，或本地数据库校验登录；
	 * @param loginName
	 * @param password
	 */
	public static void authenticate(String loginName, String password) {
		// 有ldap, 使用ldap的登录 (登录名不能是admin, admin则采用数据库校验登录；)
		if (shouldAuthenciateWithLdap(loginName)) {
			authenticateWithLdap(loginName, password);
		} else {
			authenticateWithLocal(loginName, password);
		}
	}
	
	/** 判断是否采用ldap验证用户登录 */
	public static boolean shouldAuthenciateWithLdap(String loginName) {
		Environment env = AppContextUtil.getBean(Environment.class);
		Boolean authenticateWithLdap = env.getProperty(AUTHENTICATE_WITH_LDAP, Boolean.class);
		if (!Boolean.TRUE.equals(authenticateWithLdap)) {
			return false;
		}
		// admin 不是ldap登录
		if (UserContextUtil.isSuperAdmin(loginName)) {
			return false;
		}
		// 启用了LDAP校验登录的话， 也只有账号的authType = ldap的才取LDAP校验；
		SysAccountService service = AppContextUtil.getBean(SysAccountService.class);
		SysAccountDto account = service.findSysAccountByLoginName(loginName);
		return account != null && "LDAP".equals(account.getAuthType());
	}
	
	/** 判断用户在LDAP中是否有效 */
	public static boolean isValidUserInLdap(String loginName, String password) {
		try {
			authenticateWithLdapInternal(loginName, password);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}
	
	private static void authenticateWithLdapInternal(String loginName, String password) {
		logger.debug("使用LDAP验证登录用户" + loginName);
		LdapTemplate template = AppContextUtil.getBean(LdapTemplate.class);
		LdapQuery query = LdapQueryBuilder.query()
				.where("objectclass").is("inetOrgPerson")
				.and("uid").is(loginName);
		try {
			template.authenticate(query, password);
		} catch(RuntimeException ex) {
			logger.debug("LDAP验证[" + loginName + "]登录失败!", ex); 
			throw ex;
		}
	}
	
	private static void authenticateWithLdap(String loginName, String password) {
		try {
			authenticateWithLdapInternal(loginName, password);
		} catch (Exception ex) {
			throw new BusinessException(ex, "用户名{0}域登录失败!", new Object[]{loginName});
		}
	}
	
	private static void authenticateWithLocal(String loginName, String password) {
		SysAccountService service = AppContextUtil.getBean(SysAccountService.class);
		service.authenticate(loginName, password);
	}
	
}

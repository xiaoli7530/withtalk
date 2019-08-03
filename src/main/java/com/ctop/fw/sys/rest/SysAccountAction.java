package com.ctop.fw.sys.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import com.ctop.base.service.BaseCompanyService;
import com.ctop.base.utils.DateUtil;
import com.ctop.fw.common.constants.Constants;
import com.ctop.fw.common.constants.Constants.LoginType;
import com.ctop.fw.common.constants.Constants.YesNo;
import com.ctop.fw.common.model.NuiPageRequestData;
import com.ctop.fw.common.model.PageResponseData;
import com.ctop.fw.common.model.UserDto;
import com.ctop.fw.common.utils.BusinessException;
import com.ctop.fw.common.utils.CommonAssembler;
import com.ctop.fw.common.utils.UserContextUtil;
import com.ctop.fw.config.Authenticator;
import com.ctop.fw.hr.service.HrDepartmentService;
import com.ctop.fw.hr.service.HrEmployeesService;
import com.ctop.fw.sys.dto.SysAccountDto;
import com.ctop.fw.sys.dto.SysAccountRoleParamDto;
import com.ctop.fw.sys.dto.SysRoleDto;
import com.ctop.fw.sys.entity.SysLoginLog;
import com.ctop.fw.sys.service.SysAccountService;
import com.ctop.fw.sys.service.SysLoginLogService;
import com.ctop.fw.sys.service.SysPermissionService;
import com.ctop.fw.sys.service.SysResourceService;

@RestController
@RequestMapping(path = "/rest/sys/sysAccount")
public class SysAccountAction {
	@Autowired
	SysAccountService sysAccountService;
	@Autowired
	SysResourceService sysResourceService;
	@Autowired
	SysPermissionService sysPermissionService;
	@Autowired
	BaseCompanyService baseCompanyService;
	@Autowired
	HrDepartmentService hrDepartmentService;
	@Autowired
	HrEmployeesService hrEmployeesService;
	@Autowired
	SysLoginLogService sysLoginLogService;
	@Autowired  
    private SessionLocaleResolver localeResolver;
	//@Autowired
	//OAuthClientService oAuthClientService;
	
	/**
	 * 国际化语言切换
	 * @param language
	 * @param request
	 * @param response
	 */
    @RequestMapping(value = "/changeLanguage", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void language(String language, HttpServletRequest request, HttpServletResponse response){
    	if(language == null || "".equals(language)) {
    		localeResolver.setLocale(request, response, Locale.CHINA);
        } else {
        	language = language.toLowerCase();
        	if(language.equals("zh_cn")){
        		localeResolver.setLocale(request, response, Locale.CHINA);
            }else if(language.equals("en")){
            	localeResolver.setLocale(request, response, Locale.US );
            }else{
            	localeResolver.setLocale(request, response, Locale.CHINA );
            }
        }
    }
	
	/**
	 * https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Access_control_CORS
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.POST, headers = { "content-type=text/*",
			"content-type=application/*" }, produces = { "application/json" })
	public PageResponseData<SysAccountDto> getSysAccountsList(@RequestBody NuiPageRequestData request) {
		return sysAccountService.sqlPageQuery(request);
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	@ResponseBody
	public SysAccountDto getSysAccount(@RequestParam("accountUuid") String id) {
		return sysAccountService.getById(id);
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseStatus(HttpStatus.CREATED)
	public SysAccountDto createSysAccount(@RequestBody SysAccountDto sysAccountDto) {
		sysAccountDto.setCompanyUuid(UserContextUtil.getCompanyUuid());
		return this.sysAccountService.addSysAccount(sysAccountDto);
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseStatus(HttpStatus.OK)
	public SysAccountDto updateSysAccount(@RequestBody SysAccountDto sysAccountDto) {
		//从requst中获取有效的参数进行修改，确保不会修改到无关的字段
		Set<String> httpParams =  CommonAssembler.getExistsRequestParam(sysAccountDto);
		return this.sysAccountService.updateSysAccount(sysAccountDto,httpParams);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void deleteSysAccount(@RequestParam("accountUuid") String id) {
		sysAccountService.deleteSysAccount(id);
	}

	@RequestMapping(value = "/deleteList", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void deleteSysAccounts(@RequestBody List<String> accountUuids) {
		sysAccountService.deleteSysAccounts(accountUuids);
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseBody
	public UserDto login(@RequestBody SysAccountDto sysAccountDto, HttpSession session, HttpServletRequest request) throws Exception {
		try {
			Authenticator.authenticate(sysAccountDto.getLoginName(), sysAccountDto.getPassword());
			UserDto user = sysAccountService.retrieveUserInfo(sysAccountDto.getLoginName());
			SysLoginLog log = SysLoginLog.buildSuccessLog(LoginType.EPMS_WEB, user.getSysAccount(), request);
			sysLoginLogService.addSysLoginLog(log);
			// 如果是初次登录，且登录成功，则将密码设置为这次登录的域密码
			if (YesNo.YES.equals(user.getSysAccount().getPasswordInitFlag())) {
				sysAccountDto.setOriPassword(Constants.DEFAULT_PASSWORD);
				sysAccountDto.setAccountUuid(user.getSysAccount().getAccountUuid());
				sysAccountService.changePassword(sysAccountDto);
				sysAccountDto.setPasswordInitFlag(YesNo.NO);
			}
			
			session.setAttribute(Constants.SESSION_KEY_USER, user);
			return user;
		} catch (RuntimeException ex) {
			SysLoginLog log = SysLoginLog.buildErrorLog(LoginType.EPMS_WEB, sysAccountDto.getLoginName(), request, ex);
			sysLoginLogService.addSysLoginLog(log);
			throw ex;
		}
	}

	/**
	 * 设置当前项目
	 * @param projectUuid
	 * @return
	 */
	@RequestMapping(value = "/setCurrentProjectUuid", method = RequestMethod.GET)
	public UserDto setCurrentProjectUuid(String projectUuid) {
		UserDto user = UserContextUtil.getUser();
		user.setCurrentProjectUuid(projectUuid);
		UserContextUtil.setUser(user);// 设置session
		
		this.sysAccountService.updateCurrentProjectUuid(user.getSysAccount().getAccountUuid(), projectUuid);
		
		return user;
	}
	
	@RequestMapping(value = "/currentUser", method = RequestMethod.GET, produces = { "application/json" })
	@ResponseBody
	public UserDto getCurrentUser(HttpSession session) {
		UserDto dto = (UserDto) session.getAttribute(Constants.SESSION_KEY_USER);
		if (dto == null) {
			return new UserDto();
		}
		return dto;
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public Map<String, String> logout() {
		HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest()
				.getSession();
		session.invalidate();
		
		Map<String, String> map = new HashMap<String, String>();
	//	map.put("sgmLogoutUrl", this.oAuthClientService.getLogoutUrl());
		return map;
	}


	@RequestMapping(value = "/assignRole", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void assignRole(@RequestParam("accountUuid") String accountUuid, @RequestBody List<String> roles) {
		this.sysAccountService.assignAccountRole(accountUuid, roles);
	}


	@RequestMapping(value = "/get/roleUuids", method = RequestMethod.GET)
	@ResponseBody
	public List<String> getAccountRoleUuids(@RequestParam("accountUuid") String accountUuid) {
		return this.sysAccountService.findRoleUuidByAccountUuid(accountUuid);
	}

	@RequestMapping(value = "/changePassword", method = RequestMethod.POST)
	public void changePassword(@RequestBody SysAccountDto accountDto) {
		this.sysAccountService.changePassword(accountDto);
	}

	@RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
	public void resetPassword(@RequestBody List<String> accountUuids) {
		this.sysAccountService.resetPassword(accountUuids);
	}

	@RequestMapping(value = "/verifiyPassword", method = RequestMethod.POST)
	public void verifiyPassword(@RequestBody String password) {
		String loginName = UserContextUtil.getLoginName();
		UserDto user = UserContextUtil.getUser();
		boolean result = false;
		if (Authenticator.shouldAuthenciateWithLdap(loginName)) {
			result = Authenticator.isValidUserInLdap(loginName, password);
		} else {
			result = this.sysAccountService.verifiyPassword(UserContextUtil.getAccountUuid(), password);
		}
		user.setScreenLocked(!result);
		if (!result) { 
			throw new BusinessException("密码验证失败！");
		}
	}

	@RequestMapping(value = "/flushStatus", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Boolean> flushStatus(@RequestParam("username") String username) {
		String sLoginName = UserContextUtil.getUser().getSysAccount().getLoginName();
		if (!sLoginName.equals(username)) {
			throw new BusinessException("前台session与后台不一致!");
		}
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		map.put("ok", true);
		return map;
	}

	@RequestMapping(value = "/lockScreen", method = RequestMethod.POST)
	public void lockScreen() {
		// 集群session处理
		UserDto user = UserContextUtil.getUser();
		user.setScreenLocked(Boolean.TRUE);
	}
	
	@RequestMapping(value = "/addSysAccountRole", method = RequestMethod.POST)
	public void addSysAccountRole(@RequestBody SysAccountRoleParamDto dtp) {
		List<String> roleUuids = dtp.getRoleUuids();
		String accountUuid = dtp.getAccountUuid();
		this.sysAccountService.addSysAccountRole(roleUuids,accountUuid);
	}
	
	@RequestMapping(value = "/get/allRoles", method = RequestMethod.GET)
	@ResponseBody
	public List<String> getAccountAllRoles(@RequestParam("accountUuid") String accountUuid) {
		String companyUuid = UserContextUtil.getCompanyUuid();
		return this.sysAccountService.getAccountAllRoles(companyUuid,accountUuid);
	}
	
	@RequestMapping(value = "findAccountByRoleId", method = RequestMethod.GET)
	@ResponseBody
	public List<SysAccountDto> findAccountByRoleId(@RequestParam("roleUuid") String roleUuid) {
		return this.sysAccountService.findAccountByRoleId(roleUuid);
	}
	
	@RequestMapping(value = "findAccountByNotRoleId", method = RequestMethod.GET)
	@ResponseBody
	public List<SysAccountDto> findAccountByNotRoleId(@RequestParam("roleUuid") String roleUuid) {
		return this.sysAccountService.findAccountByNotRoleId(roleUuid);
	}
	
	@RequestMapping(value = "/findEmpsByNotRole", method = RequestMethod.POST, produces = { "application/json" })
		public PageResponseData<SysAccountDto> findEmpsByNotRole(@RequestParam("roleUuid") String roleUuid,@RequestBody NuiPageRequestData request) {
		return sysAccountService.findEmpsByNotRole(roleUuid,request);
	}
	
	@RequestMapping(value = "/addSysAccountByRole", method = RequestMethod.POST)
	public SysRoleDto addSysAccountByRole(@RequestBody SysAccountRoleParamDto dtp) {
		List<String> accountUuids = dtp.getRoleUuids();
		String roleUuid = dtp.getAccountUuid();
		return this.sysAccountService.addSysAccountByRole(accountUuids,roleUuid);
	}
	
	@RequestMapping(value = "/sso", method = RequestMethod.GET)
	@ResponseBody
	public SysRoleDto sso() {
		System.out.println("connect sso success! "+DateUtil.getNowTime());
		return new SysRoleDto();
	}
	
}
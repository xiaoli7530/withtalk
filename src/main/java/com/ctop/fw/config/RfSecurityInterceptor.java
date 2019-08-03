package com.ctop.fw.config;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.ctop.base.dto.BaseCompanyDto;
import com.ctop.base.service.BaseCompanyService;
import com.ctop.fw.common.constants.Constants.AccountRefType;
import com.ctop.fw.common.model.UserDto;
import com.ctop.fw.common.utils.AppContextUtil;
import com.ctop.fw.common.utils.StringUtil;
import com.ctop.fw.common.utils.UserContextUtil;
import com.ctop.fw.hr.dto.HrDepartmentDto;
import com.ctop.fw.hr.dto.HrEmployeesDto;
import com.ctop.fw.hr.service.HrDepartmentService;
import com.ctop.fw.hr.service.HrEmployeesService;
import com.ctop.fw.sys.dto.SysAccountDto;
import com.ctop.fw.sys.dto.SysPermissionSimpleDto;
import com.ctop.fw.sys.dto.SysResourceDto;
import com.ctop.fw.sys.service.SysAccountService;
import com.ctop.fw.sys.service.SysPermissionService;
import com.ctop.fw.sys.service.SysResourceService;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class RfSecurityInterceptor extends HandlerInterceptorAdapter {
	
	private static LoadingCache<String, Optional<UserDto>> cache = CacheBuilder.newBuilder()
	.maximumSize(1000)
	.expireAfterAccess(4, TimeUnit.HOURS)
	.build(new CacheLoader<String, Optional<UserDto>> () {
		@Override
		public Optional<UserDto> load(String key) throws Exception {;
			SysAccountService sysAccountService = AppContextUtil.getBean(SysAccountService.class);
			BaseCompanyService baseCompanyService = AppContextUtil.getBean(BaseCompanyService.class);
		//	WmWarehouseService wmWarehouseService = AppContextUtil.getBean(WmWarehouseService.class);
			SysResourceService sysResourceService = AppContextUtil.getBean(SysResourceService.class);
			SysPermissionService sysPermissionService = AppContextUtil.getBean(SysPermissionService.class);
			HrEmployeesService hrEmployeesService = AppContextUtil.getBean(HrEmployeesService.class);
			HrDepartmentService hrDepartmentService = AppContextUtil.getBean(HrDepartmentService.class);
			SysAccountDto sysAccount = sysAccountService.getByToken(key);
			if(sysAccount !=null){ 
				String companyUuid = sysAccount.getCompanyUuid();
				CompletableFuture<BaseCompanyDto> companyFuture = null;
				//用异步任务 取数据
				if(companyUuid != null) {
					companyFuture = CompletableFuture.supplyAsync(() -> {
						return baseCompanyService.getById(companyUuid);
					});
				}
									
				
				//查询货主的菜单资源 
				CompletableFuture<List<SysResourceDto>> sysResourcesFuture = CompletableFuture.supplyAsync(() -> {
					return sysResourceService.findSysResourcesByCompanyUuid(companyUuid,"rf");
				});
				//查询登陆用户拥有的权限
				CompletableFuture<List<SysPermissionSimpleDto>> sysPermissionsFuture = CompletableFuture.supplyAsync(() -> {
					if("admin".equals(sysAccount.getLoginName())) {
						return Collections.emptyList();
					} else {
						return sysPermissionService.findSysPermissionAvailableToAccountUuid(companyUuid, sysAccount.getAccountUuid());
					}
				});
				
				UserDto user = new UserDto();
				HrDepartmentDto depart = null;
				if(AccountRefType.EMPLOYEE.equals(sysAccount.getRefType())) {
					HrEmployeesDto emp = hrEmployeesService.getById(sysAccount.getRefId());
					if(emp != null) {
						user.setEmpCode(emp.getEmpCode());
						user.setEmpName(emp.getEmpName());
						user.setEmpUuid(emp.getEmpUuid());
						depart = hrDepartmentService.getById(emp.getDepartmentUuid());
					}
				}
				//WmWarehouseDto warehouse = null;
				if(depart != null) {
					user.setDepartmentName(depart.getDepartmentName());
					user.setDepartmentNo(depart.getDepartmentNo());
					user.setDepartmentUuid(depart.getDepartmentUuid());
					//warehouse = wmWarehouseService.findDefaultWareHouseByDeptUuid(depart.getDepartmentUuid());
				}
			//	List<WmWarehouseDto> wmWarehouses = new ArrayList<WmWarehouseDto>();
				//wmWarehouses.add(warehouse);
				List<SysResourceDto> sysResources = sysResourcesFuture.get();
				user.setSysAccount(sysAccount);
				user.setBaseCompany(companyFuture != null ? companyFuture.get() : null);
				//user.setWmWarehouse(warehouse);
			//	user.setWmWarehouses(wmWarehouses);
				user.setSysResources(sysResources);
				user.setSysPermissions(sysPermissionsFuture.get());

				return Optional.of(user);
			}
			return Optional.empty();
		}
	});
	
	public static void clearCache(String token) {
		if (StringUtil.isNotEmpty(token)) {
			cache.invalidate(token);
		}
	}
			 
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String contextPath = request.getContextPath();
		String requestUri = request.getRequestURI();
		String apiPath = requestUri.replaceFirst(contextPath + "/rest", "");
		if (apiPath.indexOf("/rf/sysAccount/login") != -1) {
			return true;
		}
		WebConfig.logger.debug("apiPath:" + apiPath);
		UserDto user = UserContextUtil.getUser();
		String sign = request.getHeader("sign");
		if (user == null){
			if(StringUtil.isEmpty(sign)){	
				response.setCharacterEncoding("UTF-8");
				response.setContentType("application/json");
				response.getWriter().write("{\"turnState\": \"1\", \"infoCode\": \"1001003\"}");
				response.flushBuffer();
				return false;
			} else {
				// 从缓存中取， 如果缓存中不存在，缓存会从数据库中取；
				Optional<UserDto> userOptional = cache.getUnchecked(sign);
				if (userOptional.isPresent() ) {
					UserContextUtil.setUser(userOptional.get());
					return true;
				} else{
					response.setCharacterEncoding("UTF-8");
					response.setContentType("application/json");
					response.getWriter().write("{\"turnState\": \"1\", \"infoCode\": \"1001003\"}");
					response.flushBuffer();
					return false;
				}
			}
		}
		return true;
	}
}
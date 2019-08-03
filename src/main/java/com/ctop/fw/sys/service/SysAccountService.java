package com.ctop.fw.sys.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ctop.base.entity.HrEmployeesOrgPositionRlt;
import com.ctop.base.repository.HrEmployeesOrgPositionRltRepository;
import com.ctop.base.service.BaseCompanyService;
import com.ctop.fw.common.constants.BizErrors.SysAccountErrors;
import com.ctop.fw.common.constants.Constants;
import com.ctop.fw.common.constants.Constants.AccountRefType;
import com.ctop.fw.common.constants.Constants.SysAccountStatus;
import com.ctop.fw.common.constants.ErrorInfoConstants.ErrorCode;
import com.ctop.fw.common.model.Filter;
import com.ctop.fw.common.model.PageRequestData;
import com.ctop.fw.common.model.PageResponseData;
import com.ctop.fw.common.model.UserDto;
import com.ctop.fw.common.utils.BusinessException;
import com.ctop.fw.common.utils.CommonAssembler;
import com.ctop.fw.common.utils.ListUtil;
import com.ctop.fw.common.utils.SqlBuilder;
import com.ctop.fw.common.utils.SqlBuilderFactory;
import com.ctop.fw.common.utils.StringUtil;
import com.ctop.fw.common.utils.UserContextUtil;
import com.ctop.fw.hr.dto.HrDepartmentDto;
import com.ctop.fw.hr.dto.HrEmployeesDto;
import com.ctop.fw.hr.service.HrDepartmentService;
import com.ctop.fw.hr.service.HrEmployeesService;
import com.ctop.fw.sys.dto.RtnDto;
import com.ctop.fw.sys.dto.SysAccountDto;
import com.ctop.fw.sys.dto.SysPermissionSimpleDto;
import com.ctop.fw.sys.dto.SysResourceDto;
import com.ctop.fw.sys.dto.SysRoleDto;
import com.ctop.fw.sys.entity.SysAccount;
import com.ctop.fw.sys.entity.SysAccountRole;
import com.ctop.fw.sys.repository.SysAccountRepository;
import com.ctop.fw.sys.repository.SysAccountRoleRepository;
import com.ctop.fw.sys.repository.SysRoleRepository;

/**
 * <pre>
 * 功能说明：
 * 示例程序如下：
 * 创建者：${user}
 * 创建时间：${date}
 * 版本：${version}
 * </pre>
 */
@Service
@Transactional
public class SysAccountService {

	@Autowired
	private EntityManager entityManager;
	@Autowired
	private SysAccountRepository sysAccountRepository;
	@Autowired
	private SysAccountRoleRepository sysAccountRoleRepository;
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
	private HrEmployeesService hrEmployeesService;
	@Autowired
	private SysRoleRepository sysRoleRepository;
	@Autowired
	private HrEmployeesOrgPositionRltRepository hrEmployeesOrgPositionRltRepository;
	@Autowired
	private SysRoleService sysRoleService;
	

	public SysAccountDto getById(String id) {
		SysAccount sysAccount = this.sysAccountRepository.findOne(id);
		return CommonAssembler.assemble(sysAccount, SysAccountDto.class);
	}
	
	public SysAccountDto getByToken(String token) {
		SysAccount sysAccount= this.sysAccountRepository.findSysAccountByToken(token);
		if(sysAccount!=null){
			return CommonAssembler.assemble(sysAccount, SysAccountDto.class);
		}else{
			return null; 
		}
	}

	@Transactional
	public SysAccountDto addSysAccount(SysAccountDto sysAccountDto) {
		SysAccount sysAccount = CommonAssembler.assemble(sysAccountDto, SysAccount.class);
		sysAccount.setRefType("employee");
		// 新增账号时，设置固定密码，这个系统要加个常量表，配置在常量表中；
		String initPassword = Constants.DEFAULT_PASSWORD;
		String text = sysAccount.getLoginName() + "_" + initPassword;
		String md5Password = StringUtil.encodeMd5(text, "UTF-8");
		sysAccount.setPassword(md5Password);
		validateLoginName(sysAccountDto);
		sysAccount.setPasswordInitFlag("Y");
		sysAccount.setAccountUuid(StringUtil.getUuid());
		sysAccount = this.sysAccountRepository.save(sysAccount);
		return CommonAssembler.assemble(sysAccount, SysAccountDto.class);
	}
	
	@Transactional
	public SysAccountDto addSyncSysAccount(SysAccountDto sysAccountDto) {
		SysAccount sysAccount = CommonAssembler.assemble(sysAccountDto, SysAccount.class);
		if(StringUtil.isEmpty(sysAccount.getAccountUuid())) {
			sysAccount.setAccountUuid(StringUtil.getUuid());
		}
		sysAccount = this.sysAccountRepository.save(sysAccount);
		return CommonAssembler.assemble(sysAccount, SysAccountDto.class);
	}
	
	
	/**
	 * 
	 * @param sysAccountDto
	 * @param type 
	 * @return
	 */
	@Transactional
	public SysAccountDto addWmsSysAccount(SysAccountDto sysAccountDto,String type) {
		SysAccount sysAccount = CommonAssembler.assemble(sysAccountDto, SysAccount.class);
		// 新增账号时，设置固定密码，这个系统要加个常量表，配置在常量表中；
		sysAccount.resetPassword();
		sysAccount.setStatus(Constants.SysAccountStatus.COMMON);
		if(StringUtil.isEmpty(sysAccountDto.getName())){
			HrEmployeesDto  hrEmployeesDto =hrEmployeesService.getById(sysAccountDto.getRefId()); 
			if(hrEmployeesDto !=null  && StringUtil.isNotEmpty(hrEmployeesDto.getEmpName())){
				sysAccount.setName(hrEmployeesDto.getEmpName());
			}
		}
		validateLoginName(sysAccountDto);
		checkEmployeeAccoutOnly(sysAccountDto);
		sysAccount = this.sysAccountRepository.save(sysAccount);
		return CommonAssembler.assemble(sysAccount, SysAccountDto.class);
	}
	
	
	@Transactional
	public SysAccountDto updateSysAccount(SysAccountDto sysAccountDto,Set<String> updatedProperties) {
		SysAccount sysAccount = this.sysAccountRepository.findOne(sysAccountDto.getAccountUuid());
		// 更新时，不更新密码；密码要写重置的专门方法；
		sysAccountDto.setPassword(sysAccount.getPassword());
		validateLoginName(sysAccountDto);
		//CommonAssembler.assemble(sysAccountDto, sysAccount);
		CommonAssembler.assemble(sysAccountDto, sysAccount,updatedProperties, CommonAssembler.DEFAULT_IGNORE_PROPS);
		sysAccount.setRefType("employee");
		sysAccount = this.sysAccountRepository.save(sysAccount);
		return CommonAssembler.assemble(sysAccount, SysAccountDto.class);
	}

	/** 校验同级节点的序号必须不同 */
	private void validateLoginName(SysAccountDto sysAccount) {
		// 校验同级节点的序号必须不同
		String excludeAcctUuid = sysAccount.getAccountUuid();
		if (StringUtil.isEmpty(sysAccount.getAccountUuid())) {
			excludeAcctUuid = "_";
		}
		long sameCount = this.sysAccountRepository.countSameLoginName(excludeAcctUuid, sysAccount.getLoginName());
		if (sameCount > 0) {
			throw new BusinessException("登陆名已存在!");
		}
	}
	
	
	/**
	 * 员工是否有已经生成账号
	 * @param sysAccountDto
	 */
	public void checkEmployeeAccoutOnly(SysAccountDto sysAccountDto){
		if(StringUtil.isNotEmpty(sysAccountDto.getRefId())){
			long sameCount = this.sysAccountRepository.countSameLoginNameByEmployee(sysAccountDto.getRefId());
			if (sameCount > 0) {
				throw new BusinessException("该员工已经生成登录账号，请重新选择!");
			}
		}
	}
	
	

	
	@Transactional
	public void deleteSysAccount(String id) {
		SysAccount sysAccount = this.sysAccountRepository.findOne(id);
		if(Constants.SUPER_ADMIN.equals(sysAccount.getLoginName())) {
			//不能删除超级管理员账号!
			throw new BusinessException("sys.sysAccount.cannotDeleteAdmin");
		}
		sysAccount.setIsActive("N");
		this.sysAccountRepository.save(sysAccount);
	}

	@Transactional
	public void deleteSysAccounts(List<String> accountUuids) {
		Iterable<SysAccount> sysAccounts = this.sysAccountRepository.findAll(accountUuids);
		Iterator<SysAccount> it = sysAccounts.iterator();
		while (it.hasNext()) {
			SysAccount sysAccount = it.next();
			if(Constants.SUPER_ADMIN.equals(sysAccount.getLoginName())) {
				//不能删除超级管理员账号!
				throw new BusinessException("sys.sysAccount.cannotDeleteAdmin");
			}
			sysAccount.setIsActive("N");
			this.sysAccountRepository.save(sysAccount);
			sysAccountRoleRepository.deleteByAccount(sysAccount.getAccountUuid());
		}
	}

	/** 校验登陆 */
	@Transactional(noRollbackFor = BusinessException.class)
	public SysAccountDto authenticate(String loginName, String password) {
		// 前置条件校验
		SysAccount account = this.sysAccountRepository.findSysAccountByLoginName(loginName);
		if (account == null) {
			throw new BusinessException(SysAccountErrors.loginNameInvalid);
		}
		if ("lock".equals(account.getStatus())) {
			// 账号已锁定，不能登陆!
			throw new BusinessException(SysAccountErrors.accountLocked4Login);
		}
		// 校验密码
		if (!account.checkMatchWithUserInputPassword(password)) {
			Integer checkErrorTime = account.getCheckErrorTime() == null ? 0 : account.getCheckErrorTime();
			checkErrorTime ++ ;
			account.setCheckErrorTime(checkErrorTime);
			if(checkErrorTime >= 6){
				account.setStatus("lock");
				account.setCheckErrorTime(0);
			}
			this.sysAccountRepository.save(account);
			throw new BusinessException(SysAccountErrors.passwordInvalid,new Object[]{ 6 - checkErrorTime});
		}
		// 更新数据
		account.setLastLoginTime(new Date());
		account.setCheckErrorTime(0);
		this.sysAccountRepository.save(account);
		SysAccountDto result = CommonAssembler.assemble(account, SysAccountDto.class);
		result.setPassword(null);
		return result;
	}
	
	/** 校验登陆 rf   -- by pengzb*/
	@Transactional
	public RtnDto loginValidate(SysAccountDto accountDto) {
		RtnDto result = new RtnDto();
		result.setTurnState(0); 
		// 前置条件校验
		SysAccount account = this.sysAccountRepository.findSysAccountByLoginName(accountDto.getLoginName());
		if (account == null) {
			throw new BusinessException(ErrorCode.CODE_1001001);
		}
		if (SysAccountStatus.LOCKED.equals(account.getStatus())) {
			// 账号已锁定，不能登陆!
			throw new BusinessException(ErrorCode.CODE_1001006);
		} 
		// 校验密码
		if (!account.checkMatchWithUserInputPassword(accountDto.getPassword())) {
			throw new BusinessException(ErrorCode.CODE_1001002);
		}
		// 更新数据
		account.setLastLoginTime(new Date());
		String token =(UUID.randomUUID().toString()).replace("-", "");
		account.setToken(token);
		result.setSign(token);
		this.sysAccountRepository.save(account);
		SysAccountDto dto = CommonAssembler.assemble(account, SysAccountDto.class);
		dto.setPassword(null);
		result.setSysAccountDto(dto);
		return result;
	}
	
	@Transactional
	public RtnDto updateNewToken4RF(SysAccountDto accountDto) {
		RtnDto result = new RtnDto();
		result.setTurnState(0); 
		// 前置条件校验
		SysAccount account = this.sysAccountRepository.findOne(accountDto.getAccountUuid());
		// 更新数据
		account.setLastLoginTime(new Date());
		String token =(UUID.randomUUID().toString()).replace("-", "");
		account.setToken(token);
		result.setSign(token);
		this.sysAccountRepository.save(account);
		SysAccountDto dto = CommonAssembler.assemble(account, SysAccountDto.class);
		dto.setPassword(null);
		result.setSysAccountDto(dto);
		return result;
	}

	/**
	 * 通过登录名查找用户
	 * @param loginName
	 * @return
	 */
	public SysAccountDto findSysAccountByLoginName(String loginName){
		SysAccountDto sysAccountDto = null;
		SysAccount sysAccount = this.sysAccountRepository.findSysAccountByLoginName(loginName);
		if (sysAccount != null){
			sysAccountDto = CommonAssembler.assemble(sysAccount,SysAccountDto.class);
		} 
		return sysAccountDto;
	}
	
	/**
	 * 给用户分派权限 1. 删除用户原有权限 2. 分派新的权限
	 * 
	 * @param accountUuid
	 * @param roles
	 */
	@Transactional
	public void assignAccountRole(String accountUuid, List<String> roleUuids) {
		// 1. 删除用户原有权限
		this.sysAccountRoleRepository.deleteByAccountUuid(accountUuid);
		// 2. 分派新的权限
		List<SysAccountRole> ars = roleUuids.stream().map(roleUuid -> {
			SysAccountRole ar = new SysAccountRole();
			ar.setAccountUuid(accountUuid);
			ar.setRoleUuid(roleUuid);
			return ar;
		}).collect(Collectors.toList());
		this.sysAccountRoleRepository.save(ars);
	}

	/**
	 * 修改密码时，要校验输入的原来的密码，有效才改变密码
	 * @param accountDto
	 */
	@Transactional
	public void changePassword(SysAccountDto accountDto) {
		// 前置条件校验
		SysAccount sysAccount = this.sysAccountRepository.findOne(accountDto.getAccountUuid()); 
		sysAccount.changePassword(accountDto.getOriPassword(), accountDto.getPassword());
		this.sysAccountRepository.saveAndFlush(sysAccount);
	}

	@Transactional
	public void resetPassword(List<String> accountUuids) {
		List<SysAccount> sysAccounts = this.sysAccountRepository.findAll(accountUuids);
		for(SysAccount sysAccount : sysAccounts) {
			// 新增账号时，设置固定密码，这个系统要加个常量表，配置在常量表中；
			sysAccount.resetPassword();
			this.sysAccountRepository.save(sysAccount);
		}
		this.sysAccountRepository.flush();
	}

	/**
	 * 取分派给用户的角色
	 * 
	 * @param accountUuid
	 * @return
	 */
	public List<String> findRoleUuidByAccountUuid(String accountUuid) {
		return this.sysAccountRoleRepository.findRoleUuidByAccountUuid(accountUuid);
	}

	/**
	 * 分页查询
	 * 
	 * @param request
	 * @return
	 */
	public PageResponseData<SysAccount> pageQuery(PageRequestData request) {
		Specification<SysAccount> spec = request.toSpecification(SysAccount.class);
		Page<SysAccount> page = this.sysAccountRepository.findAll(spec, request.buildPageRequest());
		return new PageResponseData<SysAccount>(page);
	}

	/**
	 * 分页查询
	 * 
	 * @param request
	 * @return
	 */
	public PageResponseData<SysAccountDto> sqlPageQuery(PageRequestData request) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendField("h.emp_name", "empName",",");
		sql.appendField("h.emp_email", "empEmail",",");
		sql.appendField("h.emp_code", "empCode",",");
		sql.appendTableColumns(SysAccount.class, "t");
		sql.append(" from SYS_ACCOUNT t ");
		sql.append(" left join hr_employees h on t.ref_id = h.emp_uuid and h.IS_ACTIVE='Y'");
		sql.append(" where t.IS_ACTIVE='Y'");
		//sql.andEqual("t.COMPANY_UUID", UserContextUtil.getCompanyUuid());
		return sql.pageQuery(entityManager, request, SysAccountDto.class);
	}
	
	public boolean verifiyPassword(String accountUuid, String password){
		SysAccount sysAccount = sysAccountRepository.findOne(accountUuid);
		return sysAccount.checkMatchWithUserInputPassword(password);
	}

	
	public UserDto retrieveUserInfo(String loginName) {
		SysAccountDto sysAccount = sysAccountService.findSysAccountByLoginName(loginName);
		if (sysAccount == null) {
			throw new BusinessException("用户{0}在EPMS中不存在，请联系系统管理员!", new Object[]{loginName});
		}
		HrDepartmentDto depart = null;
		UserDto user = new UserDto();
		user.setSysAccount(sysAccount);
		if (AccountRefType.EMPLOYEE.equals(sysAccount.getRefType())) {
			HrEmployeesDto emp = hrEmployeesService.getById(sysAccount.getRefId());
			if (emp != null) {
				user.setEmpCode(emp.getEmpCode());
				user.setEmpName(emp.getEmpName());
				user.setEmpUuid(emp.getEmpUuid());
				user.setEmpPhone(emp.getEmpPhone());
				user.setAvatar(emp.getAvatar());
				user.setShowTips(emp.getShowTips());
				user.setEmpEmail(emp.getEmpEmail());
				
				depart = hrDepartmentService.getById(emp.getDepartmentUuid());
				user.setHrDepartment(depart); //设置员工对应的部门对象
			}
		}
		if (depart != null) {
			//根据部门ID查询父节点一级部门
			HrDepartmentDto deptDTO = hrDepartmentService.findLevelOne(depart.getDepartmentUuid());
			if(null != deptDTO) {
				user.setDepartmentName(deptDTO.getDepartmentName());//设置一级部门的名称
				user.setDepartmentNo(deptDTO.getDepartmentNo());//设置一级部门的部门编码
				user.setDepartmentUuid(deptDTO.getDepartmentUuid());//设置一级部门的部门ID
				user.setDepartmentType(deptDTO.getDeptType());//一级部门类型  GS(公司) DW（单位）
			}
		} 
		String companyUuid = sysAccount.getCompanyUuid();
		if (companyUuid != null) {
			user.setBaseCompany(baseCompanyService.getById(companyUuid));
		} 
		
		final String resourceOrigin = "ppobuild";
		CompletableFuture<List<SysResourceDto>> sysResourcesFuture = CompletableFuture.supplyAsync(() -> {
			return sysResourceService.findSysResourcesByCompanyUuid(companyUuid, resourceOrigin);
		});
		
		// 查询登陆用户拥有的权限
		CompletableFuture<List<SysPermissionSimpleDto>> sysPermissionsFuture = CompletableFuture.supplyAsync(() -> {
			if (UserContextUtil.isSuperAdmin(sysAccount.getLoginName())) {
				return Collections.emptyList();
			} else {
				return sysPermissionService.findSysPermissionAvailableToAccountUuid(companyUuid,
						sysAccount.getAccountUuid());
			}
		});
	
		try {
			user.setSysResources(sysResourcesFuture.get());
			user.setSysPermissions(sysPermissionsFuture.get());
		} catch(Exception ex) {
			throw new BusinessException(ex, "", new Object[]{});
		}
	
		return user;
	}
	
	public void addSysAccountRole(List<String> roleUuids,String accountUuid){
		SysAccountRole accountRole = null;
		for (String roleUuid : roleUuids) {
		     accountRole = this.sysAccountRoleRepository.findRoleForAccount(accountUuid, roleUuid);
			if(accountRole != null){
				accountRole.setIsActive("N");
				sysAccountRoleRepository.saveAndFlush(accountRole);
			}else{
				accountRole = new SysAccountRole();
				accountRole.setAccountUuid(accountUuid);
				accountRole.setRoleUuid(roleUuid);
				sysAccountRoleRepository.saveAndFlush(accountRole);
			}
		}	
	}
	
	public List<String> getAccountAllRoles(String companyUuid,String accountUuid){
		List<String> roleUuids = this.sysRoleRepository.findRoleUuid(companyUuid);
		List<String> accountRoleUuids = this.sysAccountRoleRepository.findRoleUuidByAccountUuid(accountUuid);
		roleUuids.removeAll(accountRoleUuids);
		return roleUuids;
	}
	
	/**
	 * 得到用户
	 * @param empUUid
	 * @return
	 */
	public SysAccount getAccountByEmpUuid(String empUUid){
		SysAccount account = new SysAccount();
		SysAccount example = new SysAccount();
		example.setIsActive("Y");
		example.setRefId(empUUid);
		account = this.sysAccountRepository.findOne(Example.of(example));
		return account;
	}
	
	
	/**
	 * 通过角色ID得到有这个角色的所有用户
	 * @param roleUuid
	 * @return
	 */
	public List<SysAccountDto> findAccountByRoleId(String roleUuid){
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendField("e.emp_name", "empName",",");
		sql.appendField("e.emp_code", "empCode",",");
		sql.appendField("d.department_name", "deptName",",");
		sql.appendTableColumns(SysAccount.class, "a");
		sql.append(" from sys_account a ");
		sql.append(" join sys_account_role r");
		sql.append(" on ( r.account_uuid = a.account_uuid and r.is_active = 'Y' ");		
		sql.andEqualNotNull("r.role_uuid", roleUuid);
		sql.append(") ");
		sql.append(" join hr_employees e");
		sql.append(" on (e.emp_uuid = a.ref_id and e.is_active = 'Y')");
		sql.append(" left join hr_department d on (d.is_active='Y' and d.department_uuid = e.department_uuid)");
		sql.append(" where a.is_active = 'Y'");	
		return sql.query(entityManager, SysAccountDto.class); 
	}
	
	/**
	 * 通过角色ID得到没有这个角色的所有用户
	 * @param roleUuid
	 * @return
	 */
	public List<SysAccountDto> findAccountByNotRoleId(String roleUuid){
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendField("e.emp_name", "empName",",");
		sql.appendField("e.emp_code", "empCode",",");
		sql.appendTableColumns(SysAccount.class, "t");
		sql.append(" from sys_account t ");		
		sql.append(" join hr_employees e");
		sql.append(" on (e.emp_uuid = t.ref_id and e.is_active = 'Y')");
		sql.append(" where t.is_active = 'Y'");	
		sql.append(" and not exists (select 1 from sys_account_role r where r.is_active = 'Y'");
		sql.append("  and r.account_uuid = t.account_uuid ");
		sql.andEqualNotNull("r.role_uuid", roleUuid);
		sql.append(" ) ");
		return sql.query(entityManager, SysAccountDto.class); 
	}
	
	
	public PageResponseData<SysAccountDto> findEmpsByNotRole(String roleUuid,PageRequestData request) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendField("e.emp_name", "empName",",");
		sql.appendField("e.emp_code", "empCode",",");
		sql.appendField("d.department_name", "deptName",",");
		sql.appendTableColumns(SysAccount.class, "t");
		sql.append(" from sys_account t ");		
		sql.append(" join hr_employees e");
		sql.append(" on (e.emp_uuid = t.ref_id and e.is_active = 'Y')");
		sql.append(" left join hr_department d on (d.is_active='Y' and d.department_uuid = e.department_uuid)");
		sql.append(" where t.is_active = 'Y'");	
		sql.append(" and not exists (select 1 from sys_account_role r where r.is_active = 'Y'");
		sql.append(" and r.account_uuid = t.account_uuid ");
		sql.andEqualNotNull("r.role_uuid", roleUuid);
		sql.append(" ) ");
		
		List<Filter> filters = request.getFilter().getFilters();
		if(ListUtil.isNotNullOrEmpty(filters)){
			Filter filter = filters.get(0);
			String empName = filter.getField();
			String value = filter.getValue();
			if(StringUtil.isNotEmpty(empName) && StringUtil.isNotEmpty(value)){
				sql.append(sql.or(sql.contains("e.emp_name", value),sql.contains("t.login_name", value),sql.contains("e.emp_code", value),sql.contains("d.department_name", value)));
				request.getFilter().getFilters().remove(0);
			}
		}
		return sql.pageQuery(entityManager, request, SysAccountDto.class);
	}
	
	/**
	 * 根据角色id添加用户 角色 关系
	 * @param accountUuids
	 * @param roleUuid
	 * @return
	 */
	public SysRoleDto addSysAccountByRole(List<String> accountUuids,String roleUuid){
		this.sysAccountRoleRepository.deleteByRoleUuid(roleUuid);//先删除这个角色对应的所有用户
		//添加这个角色对应的用户
		if(!ListUtil.isEmpty(accountUuids)){
			List<SysAccountRole> entities = new ArrayList<SysAccountRole>();
			for(String accountUuid : accountUuids){
				SysAccountRole entitie = new SysAccountRole();
				entitie.setAccountUuid(accountUuid);
				entitie.setRoleUuid(roleUuid);
				entities.add(entitie);
			}			
			this.sysAccountRoleRepository.save(entities);
		}			
		return this.sysRoleService.getById(roleUuid);
	}
	
	/**
	 * 这个用户是否是在这些角色里的一个
	 * @param accountUuid
	 * @param roleCodes
	 * @return
	 */
	public boolean isRoleCodes(String accountUuid,String roleCodes){
		boolean result = false;
		if(StringUtil.isNotEmpty(roleCodes)){
			String[] codes = roleCodes.split(",");
			SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
			sql.append("select ");
			sql.appendTableColumns(SysAccount.class, "t");
			sql.append(" from sys_account t ");		
			sql.append(" join sys_account_role ar on (ar.is_active='Y' and ar.account_uuid = t.account_uuid) ");
			sql.append(" join sys_role r on (r.is_active='Y' and ar.role_uuid = r.role_uuid ");
			sql.append(sql.in("r.role_code", Arrays.asList(codes)));
			sql.append(" ) ");
			sql.append(" where t.is_active='Y' ");
			sql.andEqual("t.account_uuid", accountUuid);
			List<SysAccountDto> list = sql.query(entityManager, SysAccountDto.class); 
			if(!ListUtil.isNullOrEmpty(list)){
				result = true;
			}
		}
		return result;
	}
	
	public SysAccount queryRoleSys(String acctId,String role,String departmentUuid){
		if(StringUtil.isEmpty(departmentUuid)){
			SysAccountDto sysDto=getById(acctId);
			HrEmployeesDto heDto= hrEmployeesService.getById(sysDto.getRefId());
			departmentUuid=heDto.getDepartmentUuid();
		}
		HrEmployeesOrgPositionRlt rlt=new HrEmployeesOrgPositionRlt();
		rlt.setIsActive("Y");
		rlt.setDepartmentUuid(departmentUuid);
		rlt.setPositionNbrEpms(role);
		List<HrEmployeesOrgPositionRlt> rltList= hrEmployeesOrgPositionRltRepository.findAll(Example.of(rlt));
		if(rltList.size()>0){
			SysAccount account=new SysAccount();
			account.setRefId(rltList.get(0).getEmpUuid());
			account.setIsActive("Y");
			List<SysAccount> sysList=sysAccountRepository.findAll(Example.of(account));
			if(sysList.size()>0){
				return sysList.get(0);
			}else{
				HrDepartmentDto departmentDto=hrDepartmentService.getById(departmentUuid);
				if(StringUtil.isNotEmpty(departmentDto.getParentUuid())){
					return queryRoleSys(acctId,role, departmentDto.getParentUuid());
				}else{
					return null;
				}
			}
		}else{
			HrDepartmentDto departmentDto=hrDepartmentService.getById(departmentUuid);
			if(StringUtil.isNotEmpty(departmentDto.getParentUuid())){
				return queryRoleSys(acctId, role, departmentDto.getParentUuid());
			}else{
				return null;
			}
		}
	}
	
	/**
	 * 更新当前项目
	 * @param accountUuid
	 * @param projectUuid
	 * @return
	 */
	public SysAccountDto updateCurrentProjectUuid(String accountUuid, String projectUuid) {
		SysAccount sysAccount = this.sysAccountRepository.findOne(accountUuid);
		sysAccount.setCurrentProjectUuid(projectUuid);
		sysAccount = this.sysAccountRepository.save(sysAccount);
		return CommonAssembler.assemble(sysAccount, SysAccountDto.class);
	}
}
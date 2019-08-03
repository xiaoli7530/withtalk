package com.ctop.fw.sys.repository;
 
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.ctop.fw.sys.entity.SysAccount;
 

/** 
 */
public interface SysAccountRepository extends  JpaRepository<SysAccount, String>, JpaSpecificationExecutor<SysAccount> {

	@Query("select o from SysAccount o where o.loginName = ?1 and o.isActive='Y' ")
	public SysAccount findSysAccountByLoginName(String loginName);
	
	@Query("select o from SysAccount o where o.token = ?1 and o.isActive='Y' ")
	public SysAccount findSysAccountByToken(String token);
	
	@Query("select o from SysAccount o where o.companyUuid = ?1 and o.isActive='Y' ")
	public SysAccount findByCompanyUuid(String companyUuid);
	
	/**
	 * 查询登陆名是否唯一(在货主下唯一)
	 * @param companyUuid
	 * @param accountUuid
	 * @param loginName
	 * @return
	 */
	@Query(nativeQuery = true, value = "select count(1) from SYS_ACCOUNT a where a.ACCOUNT_UUID != ?1 and a.LOGIN_NAME = ?2 and a.IS_ACTIVE='Y' ")
	public long countSameLoginName(String accountUuid, String loginName);
	
	
	/**
	 * 员工信息是否生成账号信息
	 * @param refUuid
	 * @return
	 */
	@Query(nativeQuery = true, value = "select count(1) from SYS_ACCOUNT a where a.ref_id = ?1   and a.IS_ACTIVE='Y' ")
	public long countSameLoginNameByEmployee(String refUuid);
	
	/**
	 * 
	 * 功能说明:统计用户是否有对应菜单，对应的操作的权限
	 * 创建人:cinny
	 * 最后修改日期:2016年8月31日
	 * @param accountId
	 * @param operatorCode
	 * @param resourceCode
	 * @return
	 * long
	 */
	@Query(nativeQuery = true, value = " SELECT count(1) FROM sys_account a  "
			+ " join sys_account_role b on a.account_uuid = b.account_uuid"
			+ " join sys_role_permission c on b.role_uuid = c.role_uuid"
			+ " join sys_permission d on c.permission_uuid = d.permission_uuid"
			+ " join sys_operation e on e.operation_uuid = d.operation_uuid"
			+ " join sys_resource f on d.resource_uuid = f.resource_uuid"
			+ " where a.account_uuid = ?1 and e.operation_code = ?2 and f.resource_code = ?3 "
			+ " and a.is_active = 'Y' and b.is_active = 'Y' and c.is_active = 'Y' and d.is_active = 'Y' and e.is_active = 'Y' and f.is_active = 'Y' ")
	public long countAccountIsPermission(String accountId,String operatorCode,String resourceCode);
	
	
	/**
	 * @param 根据员工邮箱查询登录信息
	 * @return
	 */
	@Query(nativeQuery=true,value="select s.* from sys_account s  left join hr_employees h  on s.ref_id=h.emp_uuid   where lower(h.emp_email) in (?1) and h.is_active='Y'  and s.is_active='Y'")
	List<SysAccount> queryAccountByEmail(List<String> email);
	/**
	 * @param 根据员工ID查询登录信息
	 * @return
	 */
	@Query(nativeQuery=true,value="select s.* from sys_account s where  s.ref_id=(?1) and s.is_active='Y'")
	List<SysAccount> queryAccountByRefid(String refId);
	
}

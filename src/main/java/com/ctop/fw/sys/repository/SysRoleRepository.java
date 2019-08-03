package com.ctop.fw.sys.repository;
 
import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.ctop.fw.sys.entity.SysRole;
 

/** 
 */
public interface SysRoleRepository  extends JpaRepository<SysRole, String>, JpaSpecificationExecutor<SysRole> {

	@Query("select o from SysRole o where o.companyUuid = ?1 and o.isActive = 'Y' ")
	public List<SysRole> findAllAvailable(String companyUuid);
	
	@Query("select o.roleCode from SysRole o where o.companyUuid = ?1 and o.isActive = 'Y' ")
	public List<String> findRoleCode(String companyUuid);
	
	@Query("select o.roleUuid from SysRole o where o.companyUuid = ?1 and o.isActive = 'Y' ")
	public List<String> findRoleUuid(String companyUuid);
	
	@Query("select o from SysRole o where o.roleUuid in (?1) and o.isActive = 'Y' and o.fromType ='ppobuild' ")
	public List<SysRole> findAllRoles(List<String> roleUuids);
	
	/**
	 * 查询角色代码是否唯一(在货主下唯一)
	 * @param companyUuid
	 * @param roleUuid
	 * @param roleCode
	 * @return
	 */
	@Query(nativeQuery = true, value = "select count(1) from SYS_ROLE a where a.COMPANY_UUID = ?1 and a.ROLE_UUID != ?2 and a.ROLE_CODE = ?3 and a.IS_ACTIVE='Y' ")
	public long countSameRoleCode(String companyUuid, String roleUuid, String roleCode);
	
	@Query(nativeQuery = true, value = "select distinct r.ROLE_UUID from SYS_ACCOUNT_ROLE ar join SYS_ROLE r on ar.ROLE_UUID = r.ROLE_UUID where r.ROLE_UUID in (?1) and ar.IS_ACTIVE='Y' and r.IS_ACTIVE='Y' ")
	public List<String> listUsedRoleUuids(List<String> roleUuids);
	
	@Query("select o from SysRole o where o.roleCode in (?1) and o.isActive = 'Y' ")
	public List<SysRole> queryRoleByRoleCodes(Collection<String> roleCodes);
}

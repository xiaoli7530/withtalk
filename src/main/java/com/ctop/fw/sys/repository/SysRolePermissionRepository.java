package com.ctop.fw.sys.repository;
 
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.ctop.fw.sys.entity.SysRolePermission;
 

/** 
 */
public interface SysRolePermissionRepository  extends JpaRepository<SysRolePermission, String>, JpaSpecificationExecutor<SysRolePermission> {
	/**取角色的权限*/
	@Query("select o.permissionUuid from SysRolePermission o where o.roleUuid = ?1 and o.isActive='Y' ")
	public List<String> findPermissionUuidByRoleUuid(String roleUuid);
	
	/**取用户的权限*/
	@Query(
			"select o.permissionUuid "
			+ "from SysRolePermission o, SysRole sr, SysAccountRole sar "
			+ "where o.roleUuid = sr.roleUuid and o.isActive='Y' "
			+ "  and sar.roleUuid = o.roleUuid and sar.isActive = 'Y' "
			+ "  and sr.isActive='Y' and sar.accountUuid = ?1 ")
	public List<String> findPermissionUuidByAccountUuid(String accountUuid);
	
	/**物理删除分派给角色的权限*/
	@Modifying
	@Query("delete from SysRolePermission o where o.roleUuid = ?1 ")
	public void deleteByRoleUuid(String roleUuid);
	

	/**物理删除功能相关已分配的权限*/
	@Modifying
	@Query("delete from SysRolePermission o where o.permissionUuid = ?1 ")
	public void deleteByPermissionUuid(String permissionUuid);
}

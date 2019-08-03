package com.ctop.fw.sys.repository;
 
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.ctop.fw.sys.entity.SysPermission;
 

/** 
 */
public interface SysPermissionRepository  extends JpaRepository<SysPermission, String>, JpaSpecificationExecutor<SysPermission> {

	@Query("select b from SysPermission b where b.resourceUuid = ?1 and b.isActive='Y' ")
	public List<SysPermission> findByResourceUuid(String resourceUuid);
	@Query("select b from SysPermission b where b.operationUuid = ?1 and b.isActive='Y' ")
	public List<SysPermission> findSysPermissionsByOperationUuid(String operationUuid);
	
	/**取用户所拥有的权限*/
	@Query(
			"select distinct p "
			+ "from SysPermission p, SysRolePermission o, SysRole sr, SysAccountRole sar "
			+ "where sar.accountUuid = ?1 "
			+ "  and sar.roleUuid = sr.roleUuid and sr.isActive='Y' "
			+ "  and o.roleUuid = sr.roleUuid and o.isActive='Y' "
			+ "  and p.permissionUuid = o.permissionUuid and p.isActive = 'Y'  " )
	public List<SysPermission> findSysPermissionByAccountUuid(String accountUuid);
	
	/**物理删除资源下所有的功能*/
	@Modifying
	@Query("delete from SysPermission o where o.resourceUuid = ?1 ")
	public void deleteByResourceUuid(String resourceUuid);
	
	/**
	 * 
	 * 功能说明:根据permission表中的id联合查询sys_role_permission
	 * 创建人:cinny
	 * 最后修改日期:2016年8月18日
	 * @param permissionIds
	 * @return
	 * List<SysPermission>
	 */
	@Query("select distinct p from SysPermission p, SysRolePermission o where p.permissionUuid = o.permissionUuid "
			+ " and o.isActive = 'Y' and p.isActive = 'Y' and p.permissionUuid in (?1)")
	public List<SysPermission> findSysPermissionByPermissionIds(List<String> permissionIds);
}

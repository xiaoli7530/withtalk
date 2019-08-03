package com.ctop.fw.sys.repository;
 
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.ctop.fw.sys.entity.SysAccountRole;
import com.ctop.fw.sys.entity.SysRole;
 

/** 
 */
public interface SysAccountRoleRepository  extends JpaRepository<SysAccountRole, String>, JpaSpecificationExecutor<SysAccountRole> {

	/**
	 * 查用户分派的角色
	 * @param accountUuid
	 * @return
	 */
	@Query("select o.roleUuid from SysAccountRole o where o.accountUuid = ?1 and o.isActive = 'Y' ")
	public List<String> findRoleUuidByAccountUuid(String accountUuid);
	
	/**
	 * 删除用户的角色
	 * @param accountUuid
	 */
	@Modifying
	@Query("delete from SysAccountRole o where o.accountUuid = ?1 ")
	public void deleteByAccountUuid(String accountUuid);
	
	@Query("select o from SysAccountRole o where o.accountUuid = ?1 and o.roleUuid =?2 and o.isActive = 'Y' ")
	public SysAccountRole findRoleForAccount(String accountUuid,String roleUuids);
	

	@Modifying
	@Query("update SysAccountRole o set o.isActive='N' where o.accountUuid = ?1 ")
	public void deleteByAccount(String accountUuid);
	
	/**
	 * 根据角色删除用户角色关系
	 * @param roleUuid
	 */
	@Modifying
	@Query("delete from SysAccountRole o where o.roleUuid = ?1 ")
	public void deleteByRoleUuid(String roleUuid);
	
}

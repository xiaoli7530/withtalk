package com.ctop.fw.sys.repository;
 

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.ctop.fw.sys.entity.SysAccountWm;
 

/** 
 */
public interface SysAccountWmRepository  extends JpaRepository<SysAccountWm, String>, JpaSpecificationExecutor<SysAccountWm> {
	@Query(nativeQuery = true, value = " select t.* from SYS_ACCOUNT_WM t where t.warehouse_uuid = ?1 and t.account_uuid = ?2  and t.is_active = 'Y' and rownum=1")
	public SysAccountWm findHouseWm(String warehouseUuid,String accountUuid);
	
	@Query(nativeQuery = true, value = " select t.* from SYS_ACCOUNT_WM t where  t.account_uuid = ?1  and t.is_active = 'Y' ")
	public List<SysAccountWm> findAllHouseWm(String accountUuid);
}

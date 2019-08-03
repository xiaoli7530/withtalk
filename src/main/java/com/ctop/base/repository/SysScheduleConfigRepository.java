package com.ctop.base.repository;
 
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.ctop.base.entity.SysScheduleConfig;
 

/** 
 */
public interface SysScheduleConfigRepository  extends JpaRepository<SysScheduleConfig, String>, JpaSpecificationExecutor<SysScheduleConfig> {

	/**
	 * 加载任务调度配置
	 */
	//@Query("select b from SysScheduleConfig b where b.isActive = 'Y' and (lower(b.serverName) = ?1 or b.serverName = ?2)and b.status in (?3) ")
	//List<SysScheduleConfig> loadActivateScheduleConfig(String computerName,String computerAddress ,List<String> status);
	
	@Query("select b from SysScheduleConfig b where b.isActive = 'Y' "
			+ "and (lower(b.serverName) = ?1 or b.serverName = ?2) and b.status in (?3) "
			+ "and COALESCE(b.userDir,'NA') = COALESCE(?4,'NA')")
	List<SysScheduleConfig> loadActivateScheduleConfig(String computerName, String computerAddress,
			List<String> status,String userDir);
	
	
	@Query("select b from SysScheduleConfig b where b.isActive = 'Y' "
			+ "and scheduleId = ?1 "
			+ "and COALESCE(b.userDir,'NA') = COALESCE(?2,'NA')")
	SysScheduleConfig findActivateScheduleConfig(String scheduleId, String userDir);

}

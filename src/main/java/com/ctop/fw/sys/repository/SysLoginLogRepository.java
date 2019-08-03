package com.ctop.fw.sys.repository;
 
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ctop.fw.sys.entity.SysLoginLog;
 

/** 
 */
public interface SysLoginLogRepository  extends JpaRepository<SysLoginLog, String>, JpaSpecificationExecutor<SysLoginLog> {

}

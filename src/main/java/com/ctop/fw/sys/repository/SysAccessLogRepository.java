package com.ctop.fw.sys.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ctop.fw.sys.entity.SysAccessLog;

public interface SysAccessLogRepository extends JpaRepository<SysAccessLog, String>, JpaSpecificationExecutor<SysAccessLog> {

}

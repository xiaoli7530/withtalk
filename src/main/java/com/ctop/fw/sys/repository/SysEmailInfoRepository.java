package com.ctop.fw.sys.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ctop.fw.sys.entity.SysEmailInfo;
 

/** 
 */
public interface SysEmailInfoRepository  extends JpaRepository<SysEmailInfo, String>, JpaSpecificationExecutor<SysEmailInfo> {
}

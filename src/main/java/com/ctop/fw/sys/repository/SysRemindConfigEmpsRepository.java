package com.ctop.fw.sys.repository;
 
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.ctop.fw.sys.entity.SysRemindConfigEmps;
 

/** 
 */
public interface SysRemindConfigEmpsRepository  extends JpaRepository<SysRemindConfigEmps, String>, JpaSpecificationExecutor<SysRemindConfigEmps> {

}

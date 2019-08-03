package com.ctop.base.repository;
 
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.ctop.base.entity.BaseMonitorStandard;
 

/** 
 */
public interface BaseMonitorStandardRepository  extends JpaRepository<BaseMonitorStandard, String>, JpaSpecificationExecutor<BaseMonitorStandard> {

}

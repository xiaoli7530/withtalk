package com.ctop.fw.sys.repository;
 
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ctop.fw.sys.entity.SysExcelImportInstance;
 

/** 
 */
public interface SysExcelImportInstanceRepository  extends JpaRepository<SysExcelImportInstance, String>, JpaSpecificationExecutor<SysExcelImportInstance> {

}

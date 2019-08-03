package com.ctop.fw.sys.repository;
 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.ctop.fw.sys.entity.SysExcelImport;
 

/** 
 */
public interface SysExcelImportRepository  extends JpaRepository<SysExcelImport, String>, JpaSpecificationExecutor<SysExcelImport> {

	/**
	 * 查询同样importCode的导入配置
	 * @return
	 */
	@Query(nativeQuery = true, value = "select count(1) from SYS_EXCEL_IMPORT a where a.IMPORT_UUID != ?1 and a.IMPORT_CODE = ?2 and a.IS_ACTIVE='Y' ")
	public long countSameImportCode(String exImportUuid, String importCode);
	
	@Query(nativeQuery = true, value = "select a.* from SYS_EXCEL_IMPORT a where  a.IMPORT_CODE = ?1 and a.IS_ACTIVE= ?2 ")
	public SysExcelImport findByImportCodeAndIsActive(String importCode, String isActive);
}

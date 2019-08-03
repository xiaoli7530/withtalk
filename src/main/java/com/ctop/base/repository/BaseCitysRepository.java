package com.ctop.base.repository;
 
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.ctop.base.entity.BaseCitys;
import com.ctop.base.entity.BaseProvinces;
 

/** 
 */
public interface BaseCitysRepository  extends JpaRepository<BaseCitys, String>, JpaSpecificationExecutor<BaseCitys> {
	
	
	@Query("select b from BaseCitys b where b.isActive='Y' and b.countryUuid = '101' and b.provinceUuid = ?1")
	List<BaseCitys> findAllByIsActive(String provinceUuid);
	
	@Query("select b from BaseCitys b where b.isActive='Y' and b.countryUuid = '101'")
	List<BaseCitys> findAllByIsActive();
}

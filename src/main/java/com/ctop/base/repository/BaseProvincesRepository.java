package com.ctop.base.repository;
 
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.ctop.base.entity.BaseProvinces;
 

/** 
 */
public interface BaseProvincesRepository  extends JpaRepository<BaseProvinces, String>, JpaSpecificationExecutor<BaseProvinces> {

	@Query("select b from BaseProvinces b where b.isActive='Y' and b.countryUuid = '101'")
	List<BaseProvinces> findAllByIsActive();
}

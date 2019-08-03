package com.ctop.base.repository;
 
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.ctop.base.entity.BaseRegion;
 

/** 
 */
public interface BaseRegionRepository  extends JpaRepository<BaseRegion, String>, JpaSpecificationExecutor<BaseRegion> {
	@Query("select b from BaseRegion b where b.cityUuid = ?1 and b.isActive='Y' ")
	List<BaseRegion> findRegionByCityUuid(String cityUuid);
}

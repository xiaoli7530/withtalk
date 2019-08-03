package com.ctop.base.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.ctop.base.entity.BaseExpressRegion;

/** 
 */
public interface BaseExpressRegionRepository
		extends JpaRepository<BaseExpressRegion, String>, JpaSpecificationExecutor<BaseExpressRegion> {
	@Query("select b from BaseExpressRegion b where b.isActive='Y' and b.erCode=?1 ")
	BaseExpressRegion findByErCode(String erCode);

	@Query("select b from BaseExpressRegion b where b.isActive='Y' and b.erCode=?1 and b.erCode!=?2 ")
	BaseExpressRegion findByErCodes(String newErCode,String erCode);
}

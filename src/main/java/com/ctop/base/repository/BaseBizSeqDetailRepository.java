package com.ctop.base.repository;
 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.ctop.base.entity.BaseBizSeqDetail;
 

/** 
 */
public interface BaseBizSeqDetailRepository  extends JpaRepository<BaseBizSeqDetail, String>, JpaSpecificationExecutor<BaseBizSeqDetail> {

	@Query("select b from BaseBizSeqDetail b where b.bbsUuid=?1 and b.bizSeqHolder=?2 and b.isActive='Y'")
	public BaseBizSeqDetail findByBizSeqHolder(String bbsUuid, String bizSeqHolder);

}

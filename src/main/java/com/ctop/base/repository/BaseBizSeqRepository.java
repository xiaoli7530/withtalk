package com.ctop.base.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.ctop.base.entity.BaseBizSeq;

/** 
 */
public interface BaseBizSeqRepository extends JpaRepository<BaseBizSeq, String>, JpaSpecificationExecutor<BaseBizSeq> {

	@Query("select b from BaseBizSeq b where b.code = ?1 and b.isActive='Y'")
	public BaseBizSeq findByCode(String code);
	
	/**
	 * 查询同样code的单据号配置
	 * @return
	 */
	@Query(nativeQuery = true, value = "select count(1) from BASE_BIZ_SEQ a where a.BBS_UUID != ?1 and a.CODE = ?2 and a.IS_ACTIVE='Y' ")
	public long countSameCode(String exUuid, String code);
	
	@Query(nativeQuery = true, value = "select  count(*)  from BASE_BIZ_SEQ b where b.CODE=?1 and b.IS_ACTIVE='Y'")
	public int countForBizSeq(String code);
}

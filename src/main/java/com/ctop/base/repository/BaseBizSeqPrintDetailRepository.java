package com.ctop.base.repository;
 
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.ctop.base.entity.BaseBizSeqPrintDetail;
 

/** 
 */
public interface BaseBizSeqPrintDetailRepository  extends JpaRepository<BaseBizSeqPrintDetail, String>, JpaSpecificationExecutor<BaseBizSeqPrintDetail> {
	@Query(nativeQuery=true, value="select t.biz_seq_no from base_biz_seq_print_detail t where t.bbsp_uuid= ?1 and t.IS_ACTIVE='Y' order by t.biz_seq_no asc ")
	public List<String> selectSeqNoById(String bbspUuid);
	
	@Query(nativeQuery=true, value="select bbspd.* from base_biz_seq_print_detail bbspd left join base_biz_seq_print bbsp on(bbsp.bbsp_uuid=bbspd.bbsp_uuid and bbsp.is_active='Y') where bbsp.biz_seq_code= ?1 and bbspd.biz_seq_no= ?2 and bbspd.is_active='Y' ")
	public BaseBizSeqPrintDetail selectBbspdByNo(String bizSeqCode,String bizSeqNo);
	
}

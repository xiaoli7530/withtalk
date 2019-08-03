package com.ctop.base.repository;
 
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.ctop.base.entity.BaseBizSeqPrint;
 

/** 
 */
public interface BaseBizSeqPrintRepository  extends JpaRepository<BaseBizSeqPrint, String>, JpaSpecificationExecutor<BaseBizSeqPrint> {

	@Query(nativeQuery=true, value="select t.* from base_biz_seq_print t where t.bbsp_uuid=?1 and t.is_active='Y' ")
	public BaseBizSeqPrint selectBbspByUuid(String bbsUuid);
}

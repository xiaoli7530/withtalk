package com.ctop.fw.sys.repository;
 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.ctop.fw.sys.entity.SysOperation;
 

/** 
 */
public interface SysOperationRepository  extends JpaRepository<SysOperation, String>, JpaSpecificationExecutor<SysOperation> {
	/**
	 * 查询角色代码是否唯一(在货主下唯一)
	 * @param companyUuid
	 * @param roleUuid
	 * @param roleCode
	 * @return
	 */
	@Query(nativeQuery = true, value = "select count(1) from SYS_OPERATION a where  a.OPERATION_UUID != nvl(?1, '_') and (a.OPERATION_CODE = ?2 or a.SEQ_NO = ?3) and a.IS_ACTIVE='Y' ")
	public long countSameOperationCode(String operationUuid, String operationCode,Long seqNo);
}

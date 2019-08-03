package com.ctop.base.repository;
 
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ctop.base.entity.BaseDictDetail;
 

/** 
 */
public interface BaseDictDetailRepository extends JpaRepository<BaseDictDetail, String>, JpaSpecificationExecutor<BaseDictDetail> {

	@Query("select b from BaseDictDetail b where b.dictCode = ?1 and b.isActive=?2 order by b.seqNo asc ")
	public List<BaseDictDetail> findByDictCodeAndIsActive(String dictCode, String isActive);
	
	@Query("select b from BaseDictDetail b where b.isActive='Y' order by b.dictCode asc, b.seqNo asc ")
	List<BaseDictDetail> findAllAvailable();

	
	@Query(nativeQuery=true, value="select count(1) from BASE_DICT_DETAIL w where w.DICT_CODE = ?1  and w.BDL_UUID != ?2 and w.SEQ_NO = ?3 and w.IS_ACTIVE='Y' ")
	public long countSameSeqNo(String dictCode, String exBdlUuid, long seqNo);
	

	@Query(nativeQuery=true, value="select count(1) from BASE_DICT_DETAIL w where w.DICT_CODE = ?1  and w.BDL_UUID != ?2 and w.CODE = ?3 and w.IS_ACTIVE='Y' ")
	public long countSameCode(String dictCode, String exBdlUuid, String code);
	
	@Query(nativeQuery=true, value="select count(1) from BASE_DICT_DETAIL w where w.DICT_CODE = ?1   and w.CODE = ?2 and w.IS_ACTIVE='Y' ")
	public long countSameDcode(String dictCode, String code);
	
	@Query(nativeQuery=true, value="select * from BASE_DICT_DETAIL w where w.DICT_CODE =?1   and w.name= ?2 and w.IS_ACTIVE='Y'")
	List<BaseDictDetail> selectByName(String dictCode, String name);
	
	@Modifying
	@Query("update BaseDictDetail t set t.isActive = 'N' where t.dictCode = :name ")
	public void delDictDetail4Name(@Param(value = "name") String name);
	
}

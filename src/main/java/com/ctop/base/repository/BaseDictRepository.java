package com.ctop.base.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.ctop.base.entity.BaseDict;

/** 
 */
public interface BaseDictRepository extends JpaRepository<BaseDict, String>, JpaSpecificationExecutor<BaseDict> {

	@Query("select b from BaseDict b where b.isActive='Y' ")
	List<BaseDict> findAllAvailable();
	
	@Query("select b from BaseDict b where b.dictCode = ?1 and b.isActive='Y' ")
	BaseDict findByDictCode(String dictCode);
	
	@Query(nativeQuery=true, value="select count(1) from BASE_DICT b where b.DICT_UUID != ?1 and b.DICT_CODE = ?2 and b.IS_ACTIVE='Y' ")
	public long countSameDictCode(String exDictUuid, String dictCode);
	
	@Query("select b from BaseDict b where b.isActive='Y' and b.type = ?1 ")
	List<BaseDict> findAllAvailableByType(String type);
}

package com.ctop.base.repository;
 
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ctop.base.entity.BaseDictTable;
 

/** 
 */
public interface BaseDictTableRepository extends JpaRepository<BaseDictTable, String>, JpaSpecificationExecutor<BaseDictTable> {

	@Query("select b from BaseDictTable b where b.isActive='Y' ")
	List<BaseDictTable> findAllAvailable();
	
	@Query("select b from BaseDictTable b where b.isActive='Y' and b.type='dict'")
	List<BaseDictTable> findDictTableAvailable();
	
	@Query("select b from BaseDictTable b where b.dictCode = ?1 and b.isActive='Y' ")
	BaseDictTable findByDictCode(String dictCode);
	
	@Modifying
	@Query("update BaseDictTable t set t.isActive = 'N' where t.dictCode = :name ")
	public void delDictTable4Name(@Param(value = "name") String name);
}

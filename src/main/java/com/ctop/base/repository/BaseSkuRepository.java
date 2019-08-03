package com.ctop.base.repository;
 
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.ctop.base.entity.BaseSku;

/** 
 */
public interface BaseSkuRepository  extends JpaRepository<BaseSku, String>, JpaSpecificationExecutor<BaseSku> {

	/**
	 * 根据零件号查询
	 */
	@Query("select o from BaseSku o where o.isActive = 'Y' and o.skuCode = ?1")
	public List<BaseSku> selectBySkuCode(String skuCode);
	
	/**
	 * 查询零件号是否唯一
	 * @param skuCode
	 * @return
	 */
	@Query("select count(1) from BaseSku o where o.isActive = 'Y' and o.skuCode = ?1")
	public int selectCodeUnique(String skuCode);
}

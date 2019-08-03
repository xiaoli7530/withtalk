package com.ctop.base.repository;
 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ctop.base.entity.BasePinyin;
 

/** 
 */
public interface BasePinyinRepository  extends JpaRepository<BasePinyin, String>, JpaSpecificationExecutor<BasePinyin> {

}

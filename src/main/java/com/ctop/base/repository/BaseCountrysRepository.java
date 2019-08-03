package com.ctop.base.repository;
 
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ctop.base.entity.BaseCountrys;
 

/** 
 */
public interface BaseCountrysRepository  extends JpaRepository<BaseCountrys, String>, JpaSpecificationExecutor<BaseCountrys> {

}

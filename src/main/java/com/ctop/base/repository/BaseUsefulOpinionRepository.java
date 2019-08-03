package com.ctop.base.repository;
 
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.ctop.base.entity.BaseUsefulOpinion;
 

/** 
 */
public interface BaseUsefulOpinionRepository  extends JpaRepository<BaseUsefulOpinion, String>, JpaSpecificationExecutor<BaseUsefulOpinion> {

}

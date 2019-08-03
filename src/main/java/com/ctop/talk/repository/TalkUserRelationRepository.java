package com.ctop.talk.repository;
 
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.ctop.talk.entity.TalkUserRelation;
 

/** 
 */
public interface TalkUserRelationRepository  extends JpaRepository<TalkUserRelation, String>, JpaSpecificationExecutor<TalkUserRelation> {

}

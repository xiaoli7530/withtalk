package com.ctop.talk.repository;
 
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.ctop.talk.entity.TalkUserGroupRelation;
 

/** 
 */
public interface TalkUserGroupRelationRepository  extends JpaRepository<TalkUserGroupRelation, String>, JpaSpecificationExecutor<TalkUserGroupRelation> {

}

package com.ctop.talk.repository;
 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.ctop.talk.entity.TalkUser;
 

/** 
 */
public interface TalkUserRepository  extends JpaRepository<TalkUser, String>, JpaSpecificationExecutor<TalkUser> {
	
	@Query(nativeQuery = true, value="select t.* from talk_user t where t.is_active='Y' and t.login_name= ?1 and t.password= ?2")
	public TalkUser getByloginNameAndPassword(String loginName, String password);
	
	@Query(nativeQuery = true, value="select t.* from talk_user t where t.is_active='Y' and t.login_name= ?1")
	public TalkUser getByloginName(String loginName);
}

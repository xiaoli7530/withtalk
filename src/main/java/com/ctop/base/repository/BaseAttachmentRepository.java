package com.ctop.base.repository;
 
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.ctop.base.entity.BaseAttachment;
 

/** 
 */
public interface BaseAttachmentRepository  extends JpaRepository<BaseAttachment, String>, JpaSpecificationExecutor<BaseAttachment> {

	@Modifying
	@Query(nativeQuery = true, value = "update base_attachment t set t.is_active='N' where t.ref_uuid=?1")	
	public void delByRefUuid(String pbtpUuid);
}

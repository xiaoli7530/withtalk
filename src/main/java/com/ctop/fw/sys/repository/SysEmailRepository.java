package com.ctop.fw.sys.repository;
 
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.ctop.fw.sys.entity.SysEmail;
 

/** 
 */
public interface SysEmailRepository  extends JpaRepository<SysEmail, String>, JpaSpecificationExecutor<SysEmail> {
	@Query(nativeQuery=true, value="select d.email_uuid,d.TEMPLET_UUID,d.ATTACHMENT_UUID,d.INLINE_IMAGE_UUID,d.TITLE,d.SEND_TYPE,d.LAST_SEND_DATE, "
			+ "d.STATUS,d.EXT1,d.EXT2,d.EXT3,d.EXT4,d.EXT5,d.IS_ACTIVE,d.CREATED_DATE,d.UPDATED_DATE,d.CREATED_BY,d.UPDATED_BY,d.VERSION,d.CONTENT"
			+ "  from (select rownum as topNum,t.* from (select rownum as rownums,e.* from sys_email e "
			+ " where e.is_active = 'Y' and (e.status = '0' or e.status = '1') order by e.send_type desc,e.created_date asc "
			+ " ) t ) d where d.topNum = 1  ")
	public List<SysEmail> findFirstSendedMailData();
}

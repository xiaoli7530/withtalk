package com.ctop.base.repository;
 
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.ctop.base.entity.BasePageTemplate;
 

/** 
 */
public interface BasePageTemplateRepository  extends JpaRepository<BasePageTemplate, String>, JpaSpecificationExecutor<BasePageTemplate> {

	@Query(nativeQuery = true, value = "select t.* from BASE_PAGE_TEMPLATE t where t.template_name=?1 and t.is_active='Y'")
	public List<BasePageTemplate> findByTemplateName(String templateName);
}

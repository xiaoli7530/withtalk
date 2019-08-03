package com.ctop.base.service;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ctop.fw.common.model.PageRequestData;
import com.ctop.fw.common.model.PageResponseData;
import com.ctop.fw.common.utils.CommonAssembler;
import com.ctop.fw.common.utils.SqlBuilderFactory;
import com.ctop.fw.common.utils.SqlBuilder;

import com.ctop.base.entity.BasePageTemplate;
import com.ctop.base.repository.BasePageTemplateRepository;
import com.ctop.base.dto.BasePageTemplateDto;
import com.ctop.base.entity.BasePageTemplate;


/**
 * <pre>
 * 功能说明：
 * 示例程序如下：
 * 创建者：${user}
 * 创建时间：${date}
 * 版本：${version}
 * </pre>
 */
@Service
@Transactional
public class BasePageTemplateService {


	@Autowired
	private EntityManager entityManager;
	@Autowired
	private BasePageTemplateRepository basePageTemplateRepository; 
	
	@Transactional(readOnly=true)
	public BasePageTemplateDto getById(String id) {
		BasePageTemplate basePageTemplate = this.basePageTemplateRepository.findOne(id);
		return CommonAssembler.assemble(basePageTemplate, BasePageTemplateDto.class);
	}
	
	@Transactional
	public BasePageTemplateDto addBasePageTemplate(BasePageTemplateDto basePageTemplateDto) {
		BasePageTemplate basePageTemplate = CommonAssembler.assemble(basePageTemplateDto, BasePageTemplate.class);
		basePageTemplate = this.basePageTemplateRepository.save(basePageTemplate);
		return CommonAssembler.assemble(basePageTemplate, BasePageTemplateDto.class);
	} 

	@Transactional
	public BasePageTemplateDto updateBasePageTemplate(BasePageTemplateDto basePageTemplateDto,Set<String> updatedProperties) {
		BasePageTemplate basePageTemplate = this.basePageTemplateRepository.findOne(basePageTemplateDto.getPptUuid());
		CommonAssembler.assemble(basePageTemplateDto, basePageTemplate,updatedProperties, CommonAssembler.DEFAULT_IGNORE_PROPS);
		basePageTemplate = this.basePageTemplateRepository.save(basePageTemplate);
		return CommonAssembler.assemble(basePageTemplate, BasePageTemplateDto.class);
	}
	
	@Transactional
	public void deleteBasePageTemplate(String id) {
		BasePageTemplate basePageTemplate = this.basePageTemplateRepository.findOne(id);
		basePageTemplate.setIsActive("N");
		this.basePageTemplateRepository.save(basePageTemplate);
	}
		
	@Transactional
	public void deleteBasePageTemplates(List<String> pptUuids) {
		Iterable<BasePageTemplate> basePageTemplates = this.basePageTemplateRepository.findAll(pptUuids);
		Iterator<BasePageTemplate> it = basePageTemplates.iterator();
		while(it.hasNext()) {
			BasePageTemplate basePageTemplate = it.next();
			basePageTemplate.setIsActive("N");
			this.basePageTemplateRepository.save(basePageTemplate);
		}
	} 

 
	/**
	 * 分页查询
	 * @param request
	 * @return
	 */
	 /*
	@Transactional(readOnly=true)
	public PageResponseData<BasePageTemplate> pageQuery(PageRequestData request) {
		Specification<BasePageTemplate> spec = request.toSpecification(BasePageTemplate.class);
		Page<BasePageTemplate> page = basePageTemplateRepository.findAll(spec, request.buildPageRequest());
		return new PageResponseData<BasePageTemplate>(page);
	}*/
	
	/**
	 * 分页查询
	 * @param request
	 * @return
	 */
	@Transactional(readOnly=true)
	public PageResponseData<BasePageTemplateDto> sqlPageQuery(PageRequestData request) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendTableColumns(BasePageTemplate.class, "t");
		sql.append(" from BASE_PAGE_TEMPLATE t where t.IS_ACTIVE='Y' ");
		return sql.pageQuery(entityManager, request, BasePageTemplateDto.class);
	}
	
}


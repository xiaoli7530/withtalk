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

import com.ctop.base.entity.BaseMonitorStandard;
import com.ctop.base.repository.BaseMonitorStandardRepository;
import com.ctop.base.dto.BaseMonitorStandardDto;
import com.ctop.base.entity.BaseMonitorStandard;


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
public class BaseMonitorStandardService {


	@Autowired
	private EntityManager entityManager;
	@Autowired
	private BaseMonitorStandardRepository baseMonitorStandardRepository; 
	
	@Transactional(readOnly=true)
	public BaseMonitorStandardDto getById(String id) {
		BaseMonitorStandard baseMonitorStandard = this.baseMonitorStandardRepository.findOne(id);
		return CommonAssembler.assemble(baseMonitorStandard, BaseMonitorStandardDto.class);
	}
	
	@Transactional
	public BaseMonitorStandardDto addBaseMonitorStandard(BaseMonitorStandardDto baseMonitorStandardDto) {
		BaseMonitorStandard baseMonitorStandard = CommonAssembler.assemble(baseMonitorStandardDto, BaseMonitorStandard.class);
		baseMonitorStandard = this.baseMonitorStandardRepository.save(baseMonitorStandard);
		return CommonAssembler.assemble(baseMonitorStandard, BaseMonitorStandardDto.class);
	} 

	@Transactional
	public BaseMonitorStandardDto updateBaseMonitorStandard(BaseMonitorStandardDto baseMonitorStandardDto,Set<String> updatedProperties) {
		BaseMonitorStandard baseMonitorStandard = this.baseMonitorStandardRepository.findOne(baseMonitorStandardDto.getMsUuid());
		CommonAssembler.assemble(baseMonitorStandardDto, baseMonitorStandard,updatedProperties, CommonAssembler.DEFAULT_IGNORE_PROPS);
		baseMonitorStandard = this.baseMonitorStandardRepository.save(baseMonitorStandard);
		return CommonAssembler.assemble(baseMonitorStandard, BaseMonitorStandardDto.class);
	}
	
	@Transactional
	public void deleteBaseMonitorStandard(String id) {
		BaseMonitorStandard baseMonitorStandard = this.baseMonitorStandardRepository.findOne(id);
		baseMonitorStandard.setIsActive("N");
		this.baseMonitorStandardRepository.save(baseMonitorStandard);
	}
		
	@Transactional
	public void deleteBaseMonitorStandards(List<String> msUuids) {
		Iterable<BaseMonitorStandard> baseMonitorStandards = this.baseMonitorStandardRepository.findAll(msUuids);
		Iterator<BaseMonitorStandard> it = baseMonitorStandards.iterator();
		while(it.hasNext()) {
			BaseMonitorStandard baseMonitorStandard = it.next();
			baseMonitorStandard.setIsActive("N");
			this.baseMonitorStandardRepository.save(baseMonitorStandard);
		}
	} 

 
	/**
	 * 分页查询
	 * @param request
	 * @return
	 */
	 /*
	@Transactional(readOnly=true)
	public PageResponseData<BaseMonitorStandard> pageQuery(PageRequestData request) {
		Specification<BaseMonitorStandard> spec = request.toSpecification(BaseMonitorStandard.class);
		Page<BaseMonitorStandard> page = baseMonitorStandardRepository.findAll(spec, request.buildPageRequest());
		return new PageResponseData<BaseMonitorStandard>(page);
	}*/
	
	/**
	 * 分页查询
	 * @param request
	 * @return
	 */
	@Transactional(readOnly=true)
	public PageResponseData<BaseMonitorStandardDto> sqlPageQuery(PageRequestData request) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendTableColumns(BaseMonitorStandard.class, "t");
		sql.append(" from BASE_MONITOR_STANDARD t where t.IS_ACTIVE='Y' ");
		return sql.pageQuery(entityManager, request, BaseMonitorStandardDto.class);
	}
	
}


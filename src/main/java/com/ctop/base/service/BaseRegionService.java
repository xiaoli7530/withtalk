package com.ctop.base.service;

import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ctop.base.dto.BaseRegionDto;
import com.ctop.base.entity.BaseRegion;
import com.ctop.base.repository.BaseRegionRepository;
import com.ctop.fw.common.model.PageRequestData;
import com.ctop.fw.common.model.PageResponseData;
import com.ctop.fw.common.utils.CommonAssembler;
import com.ctop.fw.common.utils.SqlBuilder;
import com.ctop.fw.common.utils.SqlBuilderFactory;


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
public class BaseRegionService {


	@Autowired
	private EntityManager entityManager;
	@Autowired
	private BaseRegionRepository baseRegionRepository; 
	
	public BaseRegionDto getById(String id) {
		BaseRegion baseRegion = this.baseRegionRepository.findOne(id);
		return CommonAssembler.assemble(baseRegion, BaseRegionDto.class);
	}
	
	@Transactional
	public BaseRegionDto addBaseRegion(BaseRegionDto baseRegionDto) {
		BaseRegion baseRegion = CommonAssembler.assemble(baseRegionDto, BaseRegion.class);
		baseRegion = this.baseRegionRepository.save(baseRegion);
		return CommonAssembler.assemble(baseRegion, BaseRegionDto.class);
	} 

	@Transactional
	public BaseRegionDto updateBaseRegion(BaseRegionDto baseRegionDto) {
		BaseRegion baseRegion = this.baseRegionRepository.findOne(baseRegionDto.getRegionUuid());
		CommonAssembler.assemble(baseRegionDto, baseRegion);
		baseRegion = this.baseRegionRepository.save(baseRegion);
		return CommonAssembler.assemble(baseRegion, BaseRegionDto.class);
	}
	
	@Transactional
	public void deleteBaseRegion(String id) {
		BaseRegion baseRegion = this.baseRegionRepository.findOne(id);
		baseRegion.setIsActive("N");
		this.baseRegionRepository.save(baseRegion);
	}
		
	@Transactional
	public void deleteBaseRegions(List<String> regionUuids) {
		Iterable<BaseRegion> baseRegions = this.baseRegionRepository.findAll(regionUuids);
		Iterator<BaseRegion> it = baseRegions.iterator();
		while(it.hasNext()) {
			BaseRegion baseRegion = it.next();
			baseRegion.setIsActive("N");
			this.baseRegionRepository.save(baseRegion);
		}
	} 

 
	/**
	 * 分页查询
	 * @param request
	 * @return
	 */
	 /*
	public PageResponseData<BaseRegion> pageQuery(PageRequestData request) {
		Specification<BaseRegion> spec = request.toSpecification(BaseRegion.class);
		Page<BaseRegion> page = baseRegionRepository.findAll(spec, request.buildPageRequest());
		return new PageResponseData<BaseRegion>(page);
	}*/
	
	/**
	 * 分页查询
	 * @param request
	 * @return
	 */
	public PageResponseData<BaseRegionDto> sqlPageQuery(PageRequestData request) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendTableColumns(BaseRegion.class, "t");
		sql.append(" from BASE_REGION t where t.IS_ACTIVE='Y' ");
		return sql.pageQuery(entityManager, request, BaseRegionDto.class);
	}
	
	
	
	
	public List<BaseRegion> findRegionByCityUuid(String cityUuid) {
		return baseRegionRepository.findRegionByCityUuid(cityUuid);
		//return baseRegionRepository.findAll();
	}
	
}


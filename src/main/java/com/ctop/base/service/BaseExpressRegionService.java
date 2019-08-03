package com.ctop.base.service;

import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ctop.base.dto.BaseExpressRegionDto;
import com.ctop.base.entity.BaseExpressRegion;
import com.ctop.base.repository.BaseExpressRegionRepository;
import com.ctop.fw.common.model.PageRequestData;
import com.ctop.fw.common.model.PageResponseData;
import com.ctop.fw.common.utils.BusinessException;
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
public class BaseExpressRegionService {

	@Autowired
	private EntityManager entityManager;
	@Autowired
	private BaseExpressRegionRepository baseExpressRegionRepository;
	@Autowired
	private ModelMapper modelMapper;

	public BaseExpressRegionDto getById(String id) {
		BaseExpressRegion baseExpressRegion = this.baseExpressRegionRepository.findOne(id);
		return CommonAssembler.assemble(baseExpressRegion, BaseExpressRegionDto.class);
	}

	@Transactional
	public BaseExpressRegionDto addBaseExpressRegion(BaseExpressRegionDto baseExpressRegionDto) {
		BaseExpressRegion baseExpressRegion = CommonAssembler.assemble(baseExpressRegionDto, BaseExpressRegion.class);
		BaseExpressRegion baseExpressRegions = baseExpressRegionRepository
				.findByErCode(baseExpressRegionDto.getErCode());
		if (baseExpressRegions != null) {
			throw BusinessException.template("该区域号已存在！",
					this.modelMapper.map(baseExpressRegion, BaseExpressRegionDto.class));
		}
		baseExpressRegion = this.baseExpressRegionRepository.save(baseExpressRegion);
		return CommonAssembler.assemble(baseExpressRegion, BaseExpressRegionDto.class);
	}

	@Transactional
	public BaseExpressRegionDto updateBaseExpressRegion(BaseExpressRegionDto baseExpressRegionDto) {
		BaseExpressRegion baseExpressRegion = this.baseExpressRegionRepository
				.findOne(baseExpressRegionDto.getErUuid());
		BaseExpressRegion baseExpressRegions = this.baseExpressRegionRepository
				.findByErCodes(baseExpressRegionDto.getErCode(), baseExpressRegion.getErCode());
		if (baseExpressRegions != null) {
			throw BusinessException.template("该区域号已存在！",
					this.modelMapper.map(baseExpressRegion, BaseExpressRegionDto.class));
		}
		CommonAssembler.assemble(baseExpressRegionDto, baseExpressRegion);
		baseExpressRegion = this.baseExpressRegionRepository.save(baseExpressRegion);
		return CommonAssembler.assemble(baseExpressRegion, BaseExpressRegionDto.class);
	}

	@Transactional
	public void deleteBaseExpressRegion(String id) {
		BaseExpressRegion baseExpressRegion = this.baseExpressRegionRepository.findOne(id);
		baseExpressRegion.setIsActive("N");
		this.baseExpressRegionRepository.save(baseExpressRegion);
	}

	@Transactional
	public void deleteBaseExpressRegions(List<String> erUuids) {
		Iterable<BaseExpressRegion> baseExpressRegions = this.baseExpressRegionRepository.findAll(erUuids);
		Iterator<BaseExpressRegion> it = baseExpressRegions.iterator();
		while (it.hasNext()) {
			BaseExpressRegion baseExpressRegion = it.next();
			baseExpressRegion.setIsActive("N");
			this.baseExpressRegionRepository.save(baseExpressRegion);
		}
	}

	/**
	 * 分页查询
	 * 
	 * @param request
	 * @return
	 */
	/*
	 * public PageResponseData<BaseExpressRegion> pageQuery(PageRequestData
	 * request) { Specification<BaseExpressRegion> spec =
	 * request.toSpecification(BaseExpressRegion.class); Page<BaseExpressRegion>
	 * page = baseExpressRegionRepository.findAll(spec,
	 * request.buildPageRequest()); return new
	 * PageResponseData<BaseExpressRegion>(page); }
	 */

	/**
	 * 分页查询
	 * 
	 * @param request
	 * @return
	 */
	public PageResponseData<BaseExpressRegionDto> sqlPageQuery(PageRequestData request) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendField("emp.emp_name", "empName", ",");
		sql.appendTableColumns(BaseExpressRegion.class, "t");
		sql.append(" from base_express_region t ");
		sql.append("left join hr_employees emp on emp.emp_uuid=t.emp_uuid ");
		sql.append("where t.IS_ACTIVE='Y' ");
		return sql.pageQuery(entityManager, request, BaseExpressRegionDto.class);
	}

}

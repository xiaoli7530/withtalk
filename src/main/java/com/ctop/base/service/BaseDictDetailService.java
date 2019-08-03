package com.ctop.base.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ctop.base.dto.BaseBizSeqPrintDto;
import com.ctop.base.dto.BaseDictDetailDto;
import com.ctop.base.entity.BaseDictDetail;
import com.ctop.base.repository.BaseDictDetailRepository;
import com.ctop.fw.common.model.PageRequestData;
import com.ctop.fw.common.model.PageResponseData;
import com.ctop.fw.common.utils.BusinessException;
import com.ctop.fw.common.utils.CommonAssembler;
import com.ctop.fw.common.utils.SqlBuilder;
import com.ctop.fw.common.utils.SqlBuilderFactory;
import com.ctop.fw.common.utils.StringUtil;

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
public class BaseDictDetailService {

	@Autowired
	private EntityManager entityManager;
	@Autowired
	private BaseDictDetailRepository baseDictDetailRepository;
	@Autowired
	private ModelMapper modelMapper;

	public BaseDictDetailDto getById(String id) {
		BaseDictDetail baseDictDetail = this.baseDictDetailRepository.findOne(id);
		return modelMapper.map(baseDictDetail, BaseDictDetailDto.class);
	}

	@Transactional
	@CacheEvict(cacheNames="dicts", key="#baseDictDetailDto.dictCode")
	public BaseDictDetailDto addBaseDictDetail(BaseDictDetailDto baseDictDetailDto) {
		this.validateSeqNo(baseDictDetailDto);
		this.validateCode(baseDictDetailDto);
		BaseDictDetail baseDictDetail = modelMapper.map(baseDictDetailDto, BaseDictDetail.class);
		
		baseDictDetail = this.baseDictDetailRepository.save(baseDictDetail);
		//return modelMapper.map(baseDictDetail, BaseDictDetailDto.class);
		return CommonAssembler.assemble(baseDictDetail, BaseDictDetailDto.class);
	}

	@Transactional
	@CacheEvict(cacheNames="dicts", key="#baseDictDetailDto.dictCode")
	public BaseDictDetailDto updateBaseDictDetail(BaseDictDetailDto baseDictDetailDto) {
		this.validateSeqNo(baseDictDetailDto);
		this.validateCode(baseDictDetailDto);
		BaseDictDetail baseDictDetail = this.baseDictDetailRepository.findOne(baseDictDetailDto.getBdlUuid());
		modelMapper.map(baseDictDetailDto, baseDictDetail);
		baseDictDetail = this.baseDictDetailRepository.save(baseDictDetail);
		return modelMapper.map(baseDictDetail, BaseDictDetailDto.class);
	}

	private void validateSeqNo(BaseDictDetailDto dto) {
		// 校验序号必须不同
		String exUuid = StringUtil.isEmpty(dto.getBdlUuid()) ? "_" : dto.getBdlUuid();
		long sameCount = this.baseDictDetailRepository.countSameSeqNo(dto.getDictCode(), exUuid, dto.getSeqNo());
		if (sameCount > 0) {
			// base.baseDictDetail.duplicateSeqNo=数据字典明细的序号不能重复!
			throw new BusinessException("base.baseDictDetail.duplicateSeqNo");
		}
	}

	/** 校验明细代码必须不同 */
	private void validateCode(BaseDictDetailDto dto) {
		// 校验明细代码必须不同
		String exUuid = StringUtil.isEmpty(dto.getBdlUuid()) ? "_" : dto.getBdlUuid();
		long sameCount = this.baseDictDetailRepository.countSameCode(dto.getDictCode(), exUuid, dto.getCode());
		if (sameCount > 0) {
			// base.baseDictDetail.duplicateCode=同数据字典下明细不能重复!
			throw new BusinessException("base.baseDictDetail.duplicateCode");
		}
	}

	@Transactional
	@CacheEvict(cacheNames="dicts", allEntries=true)
	public void deleteBaseDictDetail(String id) {
		BaseDictDetail baseDictDetail = this.baseDictDetailRepository.findOne(id);
		baseDictDetail.setIsActive("N");
		this.baseDictDetailRepository.save(baseDictDetail);
	}

	@Transactional
	@CacheEvict(cacheNames="dicts", allEntries=true)
	public void deleteBaseDictDetails(List<String> bdlUuids) {
		List<BaseDictDetail> deleteList = this.baseDictDetailRepository.findAll(bdlUuids);
		for(BaseDictDetail dictDetail : deleteList){
			dictDetail.setIsActive("N");
			this.baseDictDetailRepository.save(dictDetail);
		}
	}

	public List<BaseDictDetailDto> findByDictCode(String dictCode) {
		List<BaseDictDetail> list = this.baseDictDetailRepository.findByDictCodeAndIsActive(dictCode, "Y");
		return list.stream().map((item) -> this.modelMapper.map(item, BaseDictDetailDto.class))
				.collect(Collectors.toList());
	}

	/**
	 * 分页查询
	 * 
	 * @param request
	 * @return
	 */
	public PageResponseData<BaseDictDetail> pageQuery(PageRequestData request) {
		Specification<BaseDictDetail> spec = request.toSpecification(BaseDictDetail.class);
		Page<BaseDictDetail> page = this.baseDictDetailRepository.findAll(spec, request.buildPageRequest());
		return new PageResponseData<BaseDictDetail>(page);
	}

	/**
	 * 分页查询
	 * 
	 * @param request
	 * @return
	 */
	public PageResponseData<BaseDictDetailDto> sqlPageQuery(PageRequestData request) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendTableColumns(BaseDictDetail.class, "t");
		sql.append(" from BASE_DICT_DETAIL t where 1=1 ");
		return sql.pageQuery(entityManager, request, BaseDictDetailDto.class);
	}

}

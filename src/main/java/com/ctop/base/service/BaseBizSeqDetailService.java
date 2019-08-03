package com.ctop.base.service;

import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ctop.base.dto.BaseBizSeqDetailDto;
import com.ctop.base.entity.BaseBizSeqDetail;
import com.ctop.base.repository.BaseBizSeqDetailRepository;
import com.ctop.fw.common.model.PageRequestData;
import com.ctop.fw.common.model.PageResponseData;
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
public class BaseBizSeqDetailService {


	@Autowired
	private EntityManager entityManager;
	@Autowired
	private BaseBizSeqDetailRepository baseBizSeqDetailRepository;
	@Autowired
	private ModelMapper modelMapper;
	
	public BaseBizSeqDetailDto getById(String id) {
		BaseBizSeqDetail baseBizSeqDetail = this.baseBizSeqDetailRepository.findOne(id);
		return modelMapper.map(baseBizSeqDetail, BaseBizSeqDetailDto.class);
	}
	
	@Transactional
	public BaseBizSeqDetailDto addBaseBizSeqDetail(BaseBizSeqDetailDto baseBizSeqDetailDto) {
		BaseBizSeqDetail baseBizSeqDetail = modelMapper.map(baseBizSeqDetailDto, BaseBizSeqDetail.class);
		baseBizSeqDetail = this.baseBizSeqDetailRepository.save(baseBizSeqDetail);
		return modelMapper.map(baseBizSeqDetail, BaseBizSeqDetailDto.class);
	} 

	@Transactional
	public BaseBizSeqDetailDto updateBaseBizSeqDetail(BaseBizSeqDetailDto baseBizSeqDetailDto) {
		BaseBizSeqDetail baseBizSeqDetail = this.baseBizSeqDetailRepository.findOne(baseBizSeqDetailDto.getBbsdUuid());
		modelMapper.map(baseBizSeqDetailDto, baseBizSeqDetail);
		baseBizSeqDetail = this.baseBizSeqDetailRepository.save(baseBizSeqDetail);
		return modelMapper.map(baseBizSeqDetail, BaseBizSeqDetailDto.class);
	}
	
	@Transactional
	public void deleteBaseBizSeqDetail(String id) {
		BaseBizSeqDetail baseBizSeqDetail = this.baseBizSeqDetailRepository.findOne(id);
		baseBizSeqDetail.setIsActive("N");
		this.baseBizSeqDetailRepository.save(baseBizSeqDetail);
	}
		
	@Transactional
	public void deleteBaseBizSeqDetails(List<String> bbsdUuids) {
		Iterable<BaseBizSeqDetail> baseBizSeqDetails = this.baseBizSeqDetailRepository.findAll(bbsdUuids);
		Iterator<BaseBizSeqDetail> it = baseBizSeqDetails.iterator();
		while(it.hasNext()) {
			BaseBizSeqDetail baseBizSeqDetail = it.next();
			baseBizSeqDetail.setIsActive("N");
			this.baseBizSeqDetailRepository.save(baseBizSeqDetail);
		}
	} 

	/**
	 * 分页查询
	 * @param request
	 * @return
	 */
	public PageResponseData<BaseBizSeqDetail> pageQuery(PageRequestData request) {
		Specification<BaseBizSeqDetail> spec = request.toSpecification(BaseBizSeqDetail.class);
		Page<BaseBizSeqDetail> page = baseBizSeqDetailRepository.findAll(spec, request.buildPageRequest());
		return new PageResponseData<BaseBizSeqDetail>(page);
	}
	
	/**
	 * 分页查询
	 * @param request
	 * @return
	 */
	public PageResponseData<BaseBizSeqDetailDto> sqlPageQuery(PageRequestData request) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendTableColumns(BaseBizSeqDetail.class, "t");
		sql.append(" from BASE_BIZ_SEQ_DETAIL t where 1=1 ");
		return sql.pageQuery(entityManager, request, BaseBizSeqDetailDto.class);
	}
	
}


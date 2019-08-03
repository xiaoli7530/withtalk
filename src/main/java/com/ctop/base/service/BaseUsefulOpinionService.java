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
import com.ctop.fw.common.utils.UserContextUtil;
import com.ctop.fw.common.utils.SqlBuilder;

import com.ctop.base.entity.BaseUsefulOpinion;
import com.ctop.base.repository.BaseUsefulOpinionRepository;
import com.ctop.base.dto.BaseUsefulOpinionDto;
import com.ctop.base.entity.BaseUsefulOpinion;


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
public class BaseUsefulOpinionService {


	@Autowired
	private EntityManager entityManager;
	@Autowired
	private BaseUsefulOpinionRepository baseUsefulOpinionRepository; 
	
	@Transactional(readOnly=true)
	public BaseUsefulOpinionDto getById(String id) {
		BaseUsefulOpinion baseUsefulOpinion = this.baseUsefulOpinionRepository.findOne(id);
		return CommonAssembler.assemble(baseUsefulOpinion, BaseUsefulOpinionDto.class);
	}
	
	@Transactional
	public BaseUsefulOpinionDto addBaseUsefulOpinion(BaseUsefulOpinionDto baseUsefulOpinionDto) {
		baseUsefulOpinionDto.setAccountUuid(UserContextUtil.getAccountUuid());
		BaseUsefulOpinion baseUsefulOpinion = CommonAssembler.assemble(baseUsefulOpinionDto, BaseUsefulOpinion.class);
		baseUsefulOpinion = this.baseUsefulOpinionRepository.save(baseUsefulOpinion);
		return CommonAssembler.assemble(baseUsefulOpinion, BaseUsefulOpinionDto.class);
	} 

	@Transactional
	public BaseUsefulOpinionDto updateBaseUsefulOpinion(BaseUsefulOpinionDto baseUsefulOpinionDto,Set<String> updatedProperties) {
		BaseUsefulOpinion baseUsefulOpinion = this.baseUsefulOpinionRepository.findOne(baseUsefulOpinionDto.getUoId());
		CommonAssembler.assemble(baseUsefulOpinionDto, baseUsefulOpinion,updatedProperties, CommonAssembler.DEFAULT_IGNORE_PROPS);
		baseUsefulOpinion = this.baseUsefulOpinionRepository.save(baseUsefulOpinion);
		return CommonAssembler.assemble(baseUsefulOpinion, BaseUsefulOpinionDto.class);
	}
	
	@Transactional
	public void deleteBaseUsefulOpinion(String id) {
		BaseUsefulOpinion baseUsefulOpinion = this.baseUsefulOpinionRepository.findOne(id);
		baseUsefulOpinion.setIsActive("N");
		this.baseUsefulOpinionRepository.save(baseUsefulOpinion);
	}
		
	@Transactional
	public void deleteBaseUsefulOpinions(List<String> uoIds) {
		Iterable<BaseUsefulOpinion> baseUsefulOpinions = this.baseUsefulOpinionRepository.findAll(uoIds);
		Iterator<BaseUsefulOpinion> it = baseUsefulOpinions.iterator();
		while(it.hasNext()) {
			BaseUsefulOpinion baseUsefulOpinion = it.next();
			baseUsefulOpinion.setIsActive("N");
			this.baseUsefulOpinionRepository.save(baseUsefulOpinion);
		}
	} 

 
	/**
	 * 分页查询
	 * @param request
	 * @return
	 */
	 /*
	@Transactional(readOnly=true)
	public PageResponseData<BaseUsefulOpinion> pageQuery(PageRequestData request) {
		Specification<BaseUsefulOpinion> spec = request.toSpecification(BaseUsefulOpinion.class);
		Page<BaseUsefulOpinion> page = baseUsefulOpinionRepository.findAll(spec, request.buildPageRequest());
		return new PageResponseData<BaseUsefulOpinion>(page);
	}*/
	
	/**
	 * 分页查询
	 * @param request
	 * @return
	 */
	@Transactional(readOnly=true)
	public PageResponseData<BaseUsefulOpinionDto> sqlPageQuery(PageRequestData request) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendTableColumns(BaseUsefulOpinion.class, "t");
		sql.append(" from BASE_USEFUL_OPINION t where t.IS_ACTIVE='Y' ");
		return sql.pageQuery(entityManager, request, BaseUsefulOpinionDto.class);
	}
	
	public List<BaseUsefulOpinionDto> getBaseUsefulOpinionByUser() {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendTableColumns(BaseUsefulOpinion.class, "t");
		sql.append(" from BASE_USEFUL_OPINION t where t.IS_ACTIVE='Y' ");
		sql.andEqual("t.account_uuid", UserContextUtil.getAccountUuid());
		sql.append(" order by t.UPDATED_DATE desc");
		return sql.query(entityManager, BaseUsefulOpinionDto.class);
	}
	
}


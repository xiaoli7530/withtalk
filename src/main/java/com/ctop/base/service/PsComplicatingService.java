package com.ctop.base.service;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ctop.fw.common.model.PageRequestData;
import com.ctop.fw.common.model.PageResponseData;
import com.ctop.fw.common.utils.BusinessException;
import com.ctop.fw.common.utils.CommonAssembler;
import com.ctop.fw.common.utils.SqlBuilderFactory;
import com.ctop.fw.common.utils.SqlBuilder;

import com.ctop.base.entity.PsComplicating;
import com.ctop.base.repository.PsComplicatingRepository;
import com.ctop.base.dto.PsComplicatingDto;
import com.ctop.base.entity.PsComplicating;


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
public class PsComplicatingService {


	@Autowired
	private EntityManager entityManager;
	@Autowired
	private PsComplicatingRepository psComplicatingRepository; 
	
	@Transactional(readOnly=true)
	public PsComplicatingDto getById(String id) {
		PsComplicating psComplicating = this.psComplicatingRepository.findOne(id);
		return CommonAssembler.assemble(psComplicating, PsComplicatingDto.class);
	}
	
	@Transactional
	public PsComplicatingDto addPsComplicating(PsComplicatingDto psComplicatingDto) {
		PsComplicating psComplicating = CommonAssembler.assemble(psComplicatingDto, PsComplicating.class);
		psComplicating = this.psComplicatingRepository.save(psComplicating);
		return CommonAssembler.assemble(psComplicating, PsComplicatingDto.class);
	} 

	@Transactional
	public PsComplicatingDto updatePsComplicating(PsComplicatingDto psComplicatingDto,Set<String> updatedProperties) {
		PsComplicating psComplicating = this.psComplicatingRepository.findOne(psComplicatingDto.getPccUuid());
		CommonAssembler.assemble(psComplicatingDto, psComplicating,updatedProperties, CommonAssembler.DEFAULT_IGNORE_PROPS);
		psComplicating = this.psComplicatingRepository.save(psComplicating);
		return CommonAssembler.assemble(psComplicating, PsComplicatingDto.class);
	}
	
	@Transactional
	public void deletePsComplicating(String id) {
		PsComplicating psComplicating = this.psComplicatingRepository.findOne(id);
		psComplicating.setIsActive("N");
		this.psComplicatingRepository.save(psComplicating);
	}
		
	@Transactional
	public void deletePsComplicatings(List<String> pccUuids) {
		Iterable<PsComplicating> psComplicatings = this.psComplicatingRepository.findAll(pccUuids);
		Iterator<PsComplicating> it = psComplicatings.iterator();
		while(it.hasNext()) {
			PsComplicating psComplicating = it.next();
			psComplicating.setIsActive("N");
			this.psComplicatingRepository.save(psComplicating);
		}
	} 

 
	/**
	 * 分页查询
	 * @param request
	 * @return
	 */
	 /*
	@Transactional(readOnly=true)
	public PageResponseData<PsComplicating> pageQuery(PageRequestData request) {
		Specification<PsComplicating> spec = request.toSpecification(PsComplicating.class);
		Page<PsComplicating> page = psComplicatingRepository.findAll(spec, request.buildPageRequest());
		return new PageResponseData<PsComplicating>(page);
	}*/
	
	/**
	 * 分页查询
	 * @param request
	 * @return
	 */
	@Transactional(readOnly=true)
	public PageResponseData<PsComplicatingDto> sqlPageQuery(PageRequestData request) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendTableColumns(PsComplicating.class, "t");
		sql.append(" from PS_COMPLICATING t where t.IS_ACTIVE='Y' ");
		return sql.pageQuery(entityManager, request, PsComplicatingDto.class);
	}
	
	@Transactional
	public PsComplicating getPsComplicatingByRefUuid(String refUuid,String refType) {
		PsComplicating complicating=new PsComplicating();
		complicating.setRefUuid(refUuid);
		complicating.setRefType(refType);
		complicating.setIsActive("Y");
		List<PsComplicating> list=psComplicatingRepository.findAll(Example.of(complicating));
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	@Transactional
	public void addPsComplicatingByRefUuid(String refUuid,String refType) {
		PsComplicating complicating=getPsComplicatingByRefUuid(refUuid,refType);
		if(complicating==null){
			PsComplicatingDto dto=new PsComplicatingDto();
			dto.setRefUuid(refUuid);
			dto.setRefType(refType);
			addPsComplicating(dto);
		}else{
			throw new BusinessException("其他人正在操作，请稍后再试！");
		}
	}
	@Transactional
	public void deletePsComplicatingByRefUuid(String refUuid,String refType) {
		PsComplicating complicating=getPsComplicatingByRefUuid(refUuid,refType);
		if(complicating!=null){
			complicating.setIsActive("N");
			psComplicatingRepository.save(complicating);
		}
	}
}


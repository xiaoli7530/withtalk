package com.ctop.talk.service;

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

import com.ctop.talk.entity.TalkUserRelation;
import com.ctop.talk.repository.TalkUserRelationRepository;
import com.ctop.talk.dto.TalkUserRelationDto;
import com.ctop.talk.entity.TalkUserRelation;


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
public class TalkUserRelationService {


	@Autowired
	private EntityManager entityManager;
	@Autowired
	private TalkUserRelationRepository talkUserRelationRepository; 
	
	@Transactional(readOnly=true)
	public TalkUserRelationDto getById(String id) {
		TalkUserRelation talkUserRelation = this.talkUserRelationRepository.findOne(id);
		return CommonAssembler.assemble(talkUserRelation, TalkUserRelationDto.class);
	}
	
	@Transactional
	public TalkUserRelationDto addTalkUserRelation(TalkUserRelationDto talkUserRelationDto) {
		TalkUserRelation talkUserRelation = CommonAssembler.assemble(talkUserRelationDto, TalkUserRelation.class);
		talkUserRelation = this.talkUserRelationRepository.save(talkUserRelation);
		return CommonAssembler.assemble(talkUserRelation, TalkUserRelationDto.class);
	} 

	@Transactional
	public TalkUserRelationDto updateTalkUserRelation(TalkUserRelationDto talkUserRelationDto,Set<String> updatedProperties) {
		TalkUserRelation talkUserRelation = this.talkUserRelationRepository.findOne(talkUserRelationDto.getUserRelationUuid());
		CommonAssembler.assemble(talkUserRelationDto, talkUserRelation,updatedProperties, CommonAssembler.DEFAULT_IGNORE_PROPS);
		talkUserRelation = this.talkUserRelationRepository.save(talkUserRelation);
		return CommonAssembler.assemble(talkUserRelation, TalkUserRelationDto.class);
	}
	
	@Transactional
	public void deleteTalkUserRelation(String id) {
		TalkUserRelation talkUserRelation = this.talkUserRelationRepository.findOne(id);
		talkUserRelation.setIsActive("N");
		this.talkUserRelationRepository.save(talkUserRelation);
	}
		
	@Transactional
	public void deleteTalkUserRelations(List<String> userRelationUuids) {
		Iterable<TalkUserRelation> talkUserRelations = this.talkUserRelationRepository.findAll(userRelationUuids);
		Iterator<TalkUserRelation> it = talkUserRelations.iterator();
		while(it.hasNext()) {
			TalkUserRelation talkUserRelation = it.next();
			talkUserRelation.setIsActive("N");
			this.talkUserRelationRepository.save(talkUserRelation);
		}
	} 

 
	/**
	 * 分页查询
	 * @param request
	 * @return
	 */
	 /*
	@Transactional(readOnly=true)
	public PageResponseData<TalkUserRelation> pageQuery(PageRequestData request) {
		Specification<TalkUserRelation> spec = request.toSpecification(TalkUserRelation.class);
		Page<TalkUserRelation> page = talkUserRelationRepository.findAll(spec, request.buildPageRequest());
		return new PageResponseData<TalkUserRelation>(page);
	}*/
	
	/**
	 * 分页查询
	 * @param request
	 * @return
	 */
	@Transactional(readOnly=true)
	public PageResponseData<TalkUserRelationDto> sqlPageQuery(PageRequestData request) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendTableColumns(TalkUserRelation.class, "t");
		sql.append(" from TALK_USER_RELATION t where t.IS_ACTIVE='Y' ");
		return sql.pageQuery(entityManager, request, TalkUserRelationDto.class);
	}
	
}


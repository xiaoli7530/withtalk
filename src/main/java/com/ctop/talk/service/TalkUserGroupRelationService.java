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

import com.ctop.talk.entity.TalkUserGroupRelation;
import com.ctop.talk.repository.TalkUserGroupRelationRepository;
import com.ctop.talk.dto.TalkUserGroupRelationDto;
import com.ctop.talk.entity.TalkUserGroupRelation;


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
public class TalkUserGroupRelationService {


	@Autowired
	private EntityManager entityManager;
	@Autowired
	private TalkUserGroupRelationRepository talkUserGroupRelationRepository; 
	
	@Transactional(readOnly=true)
	public TalkUserGroupRelationDto getById(String id) {
		TalkUserGroupRelation talkUserGroupRelation = this.talkUserGroupRelationRepository.findOne(id);
		return CommonAssembler.assemble(talkUserGroupRelation, TalkUserGroupRelationDto.class);
	}
	
	@Transactional
	public TalkUserGroupRelationDto addTalkUserGroupRelation(TalkUserGroupRelationDto talkUserGroupRelationDto) {
		TalkUserGroupRelation talkUserGroupRelation = CommonAssembler.assemble(talkUserGroupRelationDto, TalkUserGroupRelation.class);
		talkUserGroupRelation = this.talkUserGroupRelationRepository.save(talkUserGroupRelation);
		return CommonAssembler.assemble(talkUserGroupRelation, TalkUserGroupRelationDto.class);
	} 

	@Transactional
	public TalkUserGroupRelationDto updateTalkUserGroupRelation(TalkUserGroupRelationDto talkUserGroupRelationDto,Set<String> updatedProperties) {
		TalkUserGroupRelation talkUserGroupRelation = this.talkUserGroupRelationRepository.findOne(talkUserGroupRelationDto.getUserGroupRelationUuis());
		CommonAssembler.assemble(talkUserGroupRelationDto, talkUserGroupRelation,updatedProperties, CommonAssembler.DEFAULT_IGNORE_PROPS);
		talkUserGroupRelation = this.talkUserGroupRelationRepository.save(talkUserGroupRelation);
		return CommonAssembler.assemble(talkUserGroupRelation, TalkUserGroupRelationDto.class);
	}
	
	@Transactional
	public void deleteTalkUserGroupRelation(String id) {
		TalkUserGroupRelation talkUserGroupRelation = this.talkUserGroupRelationRepository.findOne(id);
		talkUserGroupRelation.setIsActive("N");
		this.talkUserGroupRelationRepository.save(talkUserGroupRelation);
	}
		
	@Transactional
	public void deleteTalkUserGroupRelations(List<String> userGroupRelationUuiss) {
		Iterable<TalkUserGroupRelation> talkUserGroupRelations = this.talkUserGroupRelationRepository.findAll(userGroupRelationUuiss);
		Iterator<TalkUserGroupRelation> it = talkUserGroupRelations.iterator();
		while(it.hasNext()) {
			TalkUserGroupRelation talkUserGroupRelation = it.next();
			talkUserGroupRelation.setIsActive("N");
			this.talkUserGroupRelationRepository.save(talkUserGroupRelation);
		}
	} 

 
	/**
	 * 分页查询
	 * @param request
	 * @return
	 */
	 /*
	@Transactional(readOnly=true)
	public PageResponseData<TalkUserGroupRelation> pageQuery(PageRequestData request) {
		Specification<TalkUserGroupRelation> spec = request.toSpecification(TalkUserGroupRelation.class);
		Page<TalkUserGroupRelation> page = talkUserGroupRelationRepository.findAll(spec, request.buildPageRequest());
		return new PageResponseData<TalkUserGroupRelation>(page);
	}*/
	
	/**
	 * 分页查询
	 * @param request
	 * @return
	 */
	@Transactional(readOnly=true)
	public PageResponseData<TalkUserGroupRelationDto> sqlPageQuery(PageRequestData request) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendTableColumns(TalkUserGroupRelation.class, "t");
		sql.append(" from TALK_USER_GROUP_RELATION t where t.IS_ACTIVE='Y' ");
		return sql.pageQuery(entityManager, request, TalkUserGroupRelationDto.class);
	}
	
}


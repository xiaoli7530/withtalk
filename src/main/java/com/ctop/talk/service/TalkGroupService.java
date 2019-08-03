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

import com.ctop.talk.entity.TalkGroup;
import com.ctop.talk.repository.TalkGroupRepository;
import com.ctop.talk.dto.TalkGroupDto;
import com.ctop.talk.entity.TalkGroup;


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
public class TalkGroupService {


	@Autowired
	private EntityManager entityManager;
	@Autowired
	private TalkGroupRepository talkGroupRepository; 
	
	@Transactional(readOnly=true)
	public TalkGroupDto getById(String id) {
		TalkGroup talkGroup = this.talkGroupRepository.findOne(id);
		return CommonAssembler.assemble(talkGroup, TalkGroupDto.class);
	}
	
	@Transactional
	public TalkGroupDto addTalkGroup(TalkGroupDto talkGroupDto) {
		TalkGroup talkGroup = CommonAssembler.assemble(talkGroupDto, TalkGroup.class);
		talkGroup = this.talkGroupRepository.save(talkGroup);
		return CommonAssembler.assemble(talkGroup, TalkGroupDto.class);
	} 

	@Transactional
	public TalkGroupDto updateTalkGroup(TalkGroupDto talkGroupDto,Set<String> updatedProperties) {
		TalkGroup talkGroup = this.talkGroupRepository.findOne(talkGroupDto.getGroupUuid());
		CommonAssembler.assemble(talkGroupDto, talkGroup,updatedProperties, CommonAssembler.DEFAULT_IGNORE_PROPS);
		talkGroup = this.talkGroupRepository.save(talkGroup);
		return CommonAssembler.assemble(talkGroup, TalkGroupDto.class);
	}
	
	@Transactional
	public void deleteTalkGroup(String id) {
		TalkGroup talkGroup = this.talkGroupRepository.findOne(id);
		talkGroup.setIsActive("N");
		this.talkGroupRepository.save(talkGroup);
	}
		
	@Transactional
	public void deleteTalkGroups(List<String> groupUuids) {
		Iterable<TalkGroup> talkGroups = this.talkGroupRepository.findAll(groupUuids);
		Iterator<TalkGroup> it = talkGroups.iterator();
		while(it.hasNext()) {
			TalkGroup talkGroup = it.next();
			talkGroup.setIsActive("N");
			this.talkGroupRepository.save(talkGroup);
		}
	} 

 
	/**
	 * 分页查询
	 * @param request
	 * @return
	 */
	 /*
	@Transactional(readOnly=true)
	public PageResponseData<TalkGroup> pageQuery(PageRequestData request) {
		Specification<TalkGroup> spec = request.toSpecification(TalkGroup.class);
		Page<TalkGroup> page = talkGroupRepository.findAll(spec, request.buildPageRequest());
		return new PageResponseData<TalkGroup>(page);
	}*/
	
	/**
	 * 分页查询
	 * @param request
	 * @return
	 */
	@Transactional(readOnly=true)
	public PageResponseData<TalkGroupDto> sqlPageQuery(PageRequestData request) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendTableColumns(TalkGroup.class, "t");
		sql.append(" from TALK_GROUP t where t.IS_ACTIVE='Y' ");
		return sql.pageQuery(entityManager, request, TalkGroupDto.class);
	}
	
}


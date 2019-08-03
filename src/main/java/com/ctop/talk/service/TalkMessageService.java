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

import com.ctop.talk.entity.TalkMessage;
import com.ctop.talk.repository.TalkMessageRepository;
import com.ctop.talk.dto.TalkMessageDto;
import com.ctop.talk.entity.TalkMessage;


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
public class TalkMessageService {


	@Autowired
	private EntityManager entityManager;
	@Autowired
	private TalkMessageRepository talkMessageRepository; 
	
	@Transactional(readOnly=true)
	public TalkMessageDto getById(String id) {
		TalkMessage talkMessage = this.talkMessageRepository.findOne(id);
		return CommonAssembler.assemble(talkMessage, TalkMessageDto.class);
	}
	
	@Transactional
	public TalkMessageDto addTalkMessage(TalkMessageDto talkMessageDto) {
		TalkMessage talkMessage = CommonAssembler.assemble(talkMessageDto, TalkMessage.class);
		talkMessage = this.talkMessageRepository.save(talkMessage);
		return CommonAssembler.assemble(talkMessage, TalkMessageDto.class);
	} 

	@Transactional
	public TalkMessageDto updateTalkMessage(TalkMessageDto talkMessageDto,Set<String> updatedProperties) {
		TalkMessage talkMessage = this.talkMessageRepository.findOne(talkMessageDto.getMessageUuid());
		CommonAssembler.assemble(talkMessageDto, talkMessage,updatedProperties, CommonAssembler.DEFAULT_IGNORE_PROPS);
		talkMessage = this.talkMessageRepository.save(talkMessage);
		return CommonAssembler.assemble(talkMessage, TalkMessageDto.class);
	}
	
	@Transactional
	public void deleteTalkMessage(String id) {
		TalkMessage talkMessage = this.talkMessageRepository.findOne(id);
		talkMessage.setIsActive("N");
		this.talkMessageRepository.save(talkMessage);
	}
		
	@Transactional
	public void deleteTalkMessages(List<String> messageUuids) {
		Iterable<TalkMessage> talkMessages = this.talkMessageRepository.findAll(messageUuids);
		Iterator<TalkMessage> it = talkMessages.iterator();
		while(it.hasNext()) {
			TalkMessage talkMessage = it.next();
			talkMessage.setIsActive("N");
			this.talkMessageRepository.save(talkMessage);
		}
	} 

 
	/**
	 * 分页查询
	 * @param request
	 * @return
	 */
	 /*
	@Transactional(readOnly=true)
	public PageResponseData<TalkMessage> pageQuery(PageRequestData request) {
		Specification<TalkMessage> spec = request.toSpecification(TalkMessage.class);
		Page<TalkMessage> page = talkMessageRepository.findAll(spec, request.buildPageRequest());
		return new PageResponseData<TalkMessage>(page);
	}*/
	
	/**
	 * 分页查询
	 * @param request
	 * @return
	 */
	@Transactional(readOnly=true)
	public PageResponseData<TalkMessageDto> sqlPageQuery(PageRequestData request) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendTableColumns(TalkMessage.class, "t");
		sql.append(" from TALK_MESSAGE t where t.IS_ACTIVE='Y' ");
		return sql.pageQuery(entityManager, request, TalkMessageDto.class);
	}
	
}


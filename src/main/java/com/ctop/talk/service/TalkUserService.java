package com.ctop.talk.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ctop.fw.common.constants.Constants.UserStatus;
import com.ctop.fw.common.model.PageRequestData;
import com.ctop.fw.common.model.PageResponseData;
import com.ctop.fw.common.utils.CommonAssembler;
import com.ctop.fw.common.utils.SqlBuilderFactory;
import com.ctop.fw.common.utils.SqlBuilder;

import com.ctop.talk.entity.TalkUser;
import com.ctop.talk.repository.TalkUserRepository;
import com.ctop.talk.dto.TalkUserDto;
import com.ctop.talk.entity.TalkUser;


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
public class TalkUserService {


	@Autowired
	private EntityManager entityManager;
	@Autowired
	private TalkUserRepository talkUserRepository; 
	
	@Transactional(readOnly=true)
	public TalkUserDto getById(String id) {
		TalkUser talkUser = this.talkUserRepository.findOne(id);
		return CommonAssembler.assemble(talkUser, TalkUserDto.class);
	}
	
	@Transactional
	public TalkUserDto addTalkUser(TalkUserDto talkUserDto) {
		TalkUser talkUser = CommonAssembler.assemble(talkUserDto, TalkUser.class);
		talkUser = this.talkUserRepository.save(talkUser);
		return CommonAssembler.assemble(talkUser, TalkUserDto.class);
	} 

	@Transactional
	public TalkUserDto updateTalkUser(TalkUserDto talkUserDto,Set<String> updatedProperties) {
		TalkUser talkUser = this.talkUserRepository.findOne(talkUserDto.getUserUuid());
		CommonAssembler.assemble(talkUserDto, talkUser,updatedProperties, CommonAssembler.DEFAULT_IGNORE_PROPS);
		talkUser = this.talkUserRepository.save(talkUser);
		return CommonAssembler.assemble(talkUser, TalkUserDto.class);
	}
	
	@Transactional
	public void deleteTalkUser(String id) {
		TalkUser talkUser = this.talkUserRepository.findOne(id);
		talkUser.setIsActive("N");
		this.talkUserRepository.save(talkUser);
	}
		
	@Transactional
	public void deleteTalkUsers(List<String> userUuids) {
		Iterable<TalkUser> talkUsers = this.talkUserRepository.findAll(userUuids);
		Iterator<TalkUser> it = talkUsers.iterator();
		while(it.hasNext()) {
			TalkUser talkUser = it.next();
			talkUser.setIsActive("N");
			this.talkUserRepository.save(talkUser);
		}
	} 
	
	/**
	 * 分页查询
	 * @param request
	 * @return
	 */
	@Transactional(readOnly=true)
	public PageResponseData<TalkUserDto> sqlPageQuery(PageRequestData request) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendTableColumns(TalkUser.class, "t");
		sql.append(" from TALK_USER t where t.IS_ACTIVE='Y' ");
		return sql.pageQuery(entityManager, request, TalkUserDto.class);
	}
	
	@Transactional
	public Map<String, Object> login(TalkUserDto talkUserDto) {
		Map<String, Object> result = new HashMap<String, Object>();
		if(talkUserDto != null) {
			String loginName = talkUserDto.getLoginName();
			String password = talkUserDto.getPassword();
			TalkUser talkUser = this.talkUserRepository.getByloginName(loginName);
			if(talkUser != null) {
				TalkUser talkUser2 = this.talkUserRepository.getByloginNameAndPassword(loginName, password);
				if(talkUser2 != null) {
					talkUser2.setLastLoginTime(new Date());
					this.talkUserRepository.save(talkUser2);
					result.put("success", "Y");
					result.put("message", "登录成功！");
				}else {
					result.put("success", "Y");
					result.put("message", "密码不正确！");
				}
			}else {
				result.put("success", "N");
				result.put("message", "请先注册之后再来登录！");
			}
		}
		return result;
	}
	
	/**
	 * 注册
	 * @param talkUserDto
	 * @return
	 */
	@Transactional
	public Map<String, Object> register(TalkUserDto talkUserDto) {
		Map<String, Object> result = new HashMap<String, Object>();
		if(talkUserDto == null) {
			return result;
		}
		String loginName = talkUserDto.getLoginName();
		TalkUser talkUser = this.talkUserRepository.getByloginName(loginName);
		if(talkUser != null) {
			result.put("success", "Y");
			result.put("message", "该用户已经存在，请修改用户名！");
		}else {
			talkUserDto.setLastLoginTime(new Date());
			talkUserDto.setStatus(UserStatus.COMMON);
			talkUserDto.setType(UserStatus.COMMON);
			this.addTalkUser(talkUserDto);
		}
		return result;
	}
}


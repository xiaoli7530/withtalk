package com.ctop.fw.sys.service;

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
import com.ctop.fw.common.utils.BusinessException;
import com.ctop.fw.common.utils.CommonAssembler;
import com.ctop.fw.common.utils.ListUtil;
import com.ctop.fw.common.utils.SqlBuilderFactory;
import com.ctop.fw.common.utils.StringUtil;
import com.ctop.fw.common.utils.SqlBuilder;

import com.ctop.fw.sys.entity.SysRemindConfig;
import com.ctop.fw.sys.repository.SysRemindConfigRepository;
import com.ctop.fw.sys.dto.SysRemindConfigDto;
import com.ctop.fw.sys.dto.SysRemindConfigEmpsDto;
import com.ctop.fw.sys.entity.SysRemindConfig;


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
public class SysRemindConfigService {


	@Autowired
	private EntityManager entityManager;
	@Autowired
	private SysRemindConfigRepository sysRemindConfigRepository; 
	@Autowired
	private SysRemindConfigEmpsService sysRemindConfigEmpsService;
	
	@Transactional(readOnly=true)
	public SysRemindConfigDto getById(String id) {
		SysRemindConfigDto result = new SysRemindConfigDto();
		SysRemindConfig sysRemindConfig = this.sysRemindConfigRepository.findOne(id);
		SysRemindConfigEmpsDto dto = new SysRemindConfigEmpsDto();
		dto.setRcUuid(id);
		List<SysRemindConfigEmpsDto> epms = this.sysRemindConfigEmpsService.findByDto(dto);
		result = CommonAssembler.assemble(sysRemindConfig, SysRemindConfigDto.class);
		if(!ListUtil.isNullOrEmpty(epms)){
			result.setEmps(epms);
		}
		return result;
	}
	
	@Transactional
	public SysRemindConfigDto addSysRemindConfig(SysRemindConfigDto sysRemindConfigDto) {
		//个业务类型只能配置一个提醒配置信息
		SysRemindConfigDto dto = new SysRemindConfigDto();
		dto.setBizType(sysRemindConfigDto.getBizType());
		List<SysRemindConfigDto> list = this.findByDto(dto);
		if(!ListUtil.isNullOrEmpty(list)){
			throw new BusinessException("一个业务类型只能配置一个提醒配置信息!!!");
		}		
		SysRemindConfig sysRemindConfig = CommonAssembler.assemble(sysRemindConfigDto, SysRemindConfig.class);
		sysRemindConfig = this.sysRemindConfigRepository.save(sysRemindConfig);
		
		this.sysRemindConfigEmpsService.addSysRemindConfigEmps(sysRemindConfigDto.getEmps(),sysRemindConfig.getRcUuid());
		
		return CommonAssembler.assemble(sysRemindConfig, SysRemindConfigDto.class);
	} 

	@Transactional
	public SysRemindConfigDto updateSysRemindConfig(SysRemindConfigDto sysRemindConfigDto,Set<String> updatedProperties) {
		//个业务类型只能配置一个提醒配置信息
		SysRemindConfigDto dto = new SysRemindConfigDto();
		dto.setBizType(sysRemindConfigDto.getBizType());
		dto.setRcUuid(sysRemindConfigDto.getRcUuid());
		List<SysRemindConfigDto> list = this.findByDto(dto);		
		if(!ListUtil.isNullOrEmpty(list)){
			throw new BusinessException("一个业务类型只能配置一个提醒配置信息!!!");
		}
		
		SysRemindConfig sysRemindConfig = this.sysRemindConfigRepository.findOne(sysRemindConfigDto.getRcUuid());
		CommonAssembler.assemble(sysRemindConfigDto, sysRemindConfig,updatedProperties, CommonAssembler.DEFAULT_IGNORE_PROPS);
		sysRemindConfig = this.sysRemindConfigRepository.save(sysRemindConfig);
		this.sysRemindConfigEmpsService.addSysRemindConfigEmps(sysRemindConfigDto.getEmps(),sysRemindConfig.getRcUuid());
		return CommonAssembler.assemble(sysRemindConfig, SysRemindConfigDto.class);
	}
	
	@Transactional
	public void deleteSysRemindConfig(String id) {
		SysRemindConfig sysRemindConfig = this.sysRemindConfigRepository.findOne(id);
		sysRemindConfig.setIsActive("N");
		this.sysRemindConfigRepository.save(sysRemindConfig);
	}
		
	@Transactional
	public void deleteSysRemindConfigs(List<String> rcUuids) {
		Iterable<SysRemindConfig> sysRemindConfigs = this.sysRemindConfigRepository.findAll(rcUuids);
		Iterator<SysRemindConfig> it = sysRemindConfigs.iterator();
		while(it.hasNext()) {
			SysRemindConfig sysRemindConfig = it.next();
			sysRemindConfig.setIsActive("N");
			this.sysRemindConfigRepository.save(sysRemindConfig);
		}
	}  
	
	/**
	 * 分页查询
	 * @param request
	 * @return
	 */
	@Transactional(readOnly=true)
	public PageResponseData<SysRemindConfigDto> sqlPageQuery(PageRequestData request) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendTableColumns(SysRemindConfig.class, "t");
		sql.append(" from SYS_REMIND_CONFIG t  ");
		sql.append(" where t.IS_ACTIVE='Y' and t.in_system = 'ppobuild' ");
		return sql.pageQuery(entityManager, request, SysRemindConfigDto.class);
	}
	
	/**
	 * 查询
	 * @param dto
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<SysRemindConfigDto> findByDto(SysRemindConfigDto dto) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendTableColumns(SysRemindConfig.class, "t");
		sql.append(" from SYS_REMIND_CONFIG t  ");
		sql.append(" where t.IS_ACTIVE='Y' and t.in_system = 'ppobuild' ");
		sql.andEqual("t.biz_type", dto.getBizType());
		//没事不要传主键到这个查询来查询自己，有主键查自己findOne就好，这个地方传来主键是排除自己的意思
		if(StringUtil.isNotEmpty(dto.getRcUuid())){
			sql.append(sql.notEqual("t.rc_uuid", dto.getRcUuid()));
		}
		return sql.query(entityManager, SysRemindConfigDto.class);
	}
}


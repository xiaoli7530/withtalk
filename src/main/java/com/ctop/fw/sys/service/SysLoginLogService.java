package com.ctop.fw.sys.service;

import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ctop.fw.common.model.PageRequestData;
import com.ctop.fw.common.model.PageResponseData;
import com.ctop.fw.common.utils.SqlBuilder;
import com.ctop.fw.common.utils.SqlBuilderFactory;
import com.ctop.fw.sys.dto.SysLoginLogDto;
import com.ctop.fw.sys.entity.SysLoginLog;
import com.ctop.fw.sys.repository.SysLoginLogRepository;


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
public class SysLoginLogService {


	@Autowired
	private EntityManager entityManager;
	@Autowired
	private SysLoginLogRepository sysLoginLogRepository;
	@Autowired
	private ModelMapper modelMapper;
	
	public SysLoginLogDto getById(String id) {
		SysLoginLog sysLoginLog = this.sysLoginLogRepository.findOne(id);
		return modelMapper.map(sysLoginLog, SysLoginLogDto.class);
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public SysLoginLog addSysLoginLog(SysLoginLog sysLoginLog) {
		return this.sysLoginLogRepository.save(sysLoginLog);
	} 

	@Transactional
	public SysLoginLogDto updateSysLoginLog(SysLoginLogDto sysLoginLogDto) {
		SysLoginLog sysLoginLog = this.sysLoginLogRepository.findOne(sysLoginLogDto.getLogUuid());
		modelMapper.map(sysLoginLogDto, sysLoginLog);
		sysLoginLog = this.sysLoginLogRepository.save(sysLoginLog);
		return modelMapper.map(sysLoginLog, SysLoginLogDto.class);
	}
	
	@Transactional
	public void deleteSysLoginLog(String id) {
		SysLoginLog sysLoginLog = this.sysLoginLogRepository.findOne(id);
		sysLoginLog.setIsActive("N");
		this.sysLoginLogRepository.save(sysLoginLog);
	}
		
	@Transactional
	public void deleteSysLoginLogs(List<String> logUuids) {
		Iterable<SysLoginLog> sysLoginLogs = this.sysLoginLogRepository.findAll(logUuids);
		Iterator<SysLoginLog> it = sysLoginLogs.iterator();
		while(it.hasNext()) {
			SysLoginLog sysLoginLog = it.next();
			sysLoginLog.setIsActive("N");
			this.sysLoginLogRepository.save(sysLoginLog);
		}
	} 

	/**
	 * 分页查询
	 * @param request
	 * @return
	 */
	public PageResponseData<SysLoginLog> pageQuery(PageRequestData request) {
		Specification<SysLoginLog> spec = request.toSpecification(SysLoginLog.class);
		Page<SysLoginLog> page = sysLoginLogRepository.findAll(spec, request.buildPageRequest());
		return new PageResponseData<SysLoginLog>(page);
	}
	
	/**
	 * 分页查询
	 * @param request
	 * @return
	 */
	public PageResponseData<SysLoginLogDto> sqlPageQuery(PageRequestData request) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendTableColumns(SysLoginLog.class, "t");
		sql.append(" from SYS_LOGIN_LOG t where 1=1 ");
		return sql.pageQuery(entityManager, request, SysLoginLogDto.class);
	}
	
}


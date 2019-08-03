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
import com.ctop.fw.common.utils.CommonAssembler;
import com.ctop.fw.common.utils.SqlBuilderFactory;
import com.ctop.fw.common.utils.SqlBuilder;

import com.ctop.fw.sys.entity.SysAccountWm;
import com.ctop.fw.sys.repository.SysAccountWmRepository;
import com.ctop.fw.sys.dto.SysAccountWmDto;
import com.ctop.fw.sys.entity.SysAccountWm;


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
public class SysAccountWmService {


	@Autowired
	private EntityManager entityManager;
	@Autowired
	private SysAccountWmRepository sysAccountWmRepository; 
	
	@Transactional(readOnly=true)
	public SysAccountWmDto getById(String id) {
		SysAccountWm sysAccountWm = this.sysAccountWmRepository.findOne(id);
		return CommonAssembler.assemble(sysAccountWm, SysAccountWmDto.class);
	}
	
	@Transactional
	public SysAccountWmDto addSysAccountWm(SysAccountWmDto sysAccountWmDto) {
		SysAccountWm sysAccountWm = CommonAssembler.assemble(sysAccountWmDto, SysAccountWm.class);
		sysAccountWm = this.sysAccountWmRepository.save(sysAccountWm);
		return CommonAssembler.assemble(sysAccountWm, SysAccountWmDto.class);
	} 

	@Transactional
	public SysAccountWmDto updateSysAccountWm(SysAccountWmDto sysAccountWmDto,Set<String> updatedProperties) {
		SysAccountWm sysAccountWm = this.sysAccountWmRepository.findOne(sysAccountWmDto.getAwUuid());
		CommonAssembler.assemble(sysAccountWmDto, sysAccountWm,updatedProperties, CommonAssembler.DEFAULT_IGNORE_PROPS);
		sysAccountWm = this.sysAccountWmRepository.save(sysAccountWm);
		return CommonAssembler.assemble(sysAccountWm, SysAccountWmDto.class);
	}
	
	@Transactional
	public void deleteSysAccountWm(String id) {
		SysAccountWm sysAccountWm = this.sysAccountWmRepository.findOne(id);
		sysAccountWm.setIsActive("N");
		this.sysAccountWmRepository.save(sysAccountWm);
	}
		
	@Transactional
	public void deleteSysAccountWms(List<String> awUuids) {
		Iterable<SysAccountWm> sysAccountWms = this.sysAccountWmRepository.findAll(awUuids);
		Iterator<SysAccountWm> it = sysAccountWms.iterator();
		while(it.hasNext()) {
			SysAccountWm sysAccountWm = it.next();
			sysAccountWm.setIsActive("N");
			this.sysAccountWmRepository.save(sysAccountWm);
		}
	} 

 
	/**
	 * 分页查询
	 * @param request
	 * @return
	 */
	 /*
	@Transactional(readOnly=true)
	public PageResponseData<SysAccountWm> pageQuery(PageRequestData request) {
		Specification<SysAccountWm> spec = request.toSpecification(SysAccountWm.class);
		Page<SysAccountWm> page = sysAccountWmRepository.findAll(spec, request.buildPageRequest());
		return new PageResponseData<SysAccountWm>(page);
	}*/
	
	/**
	 * 分页查询
	 * @param request
	 * @return
	 */
	@Transactional(readOnly=true)
	public PageResponseData<SysAccountWmDto> sqlPageQuery(PageRequestData request) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendTableColumns(SysAccountWm.class, "t");
		sql.append(" from SYS_ACCOUNT_WM t where t.IS_ACTIVE='Y' ");
		return sql.pageQuery(entityManager, request, SysAccountWmDto.class);
	}
	
}


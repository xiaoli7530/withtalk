package com.ctop.fw.sys.service;

import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ctop.fw.common.model.PageRequestData;
import com.ctop.fw.common.model.PageResponseData;
import com.ctop.fw.common.utils.CommonAssembler;
import com.ctop.fw.common.utils.SqlBuilder;
import com.ctop.fw.common.utils.SqlBuilderFactory;
import com.ctop.fw.sys.dto.SysEmailInfoDto;
import com.ctop.fw.sys.entity.SysEmailInfo;
import com.ctop.fw.sys.repository.SysEmailInfoRepository;


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
public class SysEmailInfoService {


	@Autowired
	private EntityManager entityManager;
	@Autowired
	private SysEmailInfoRepository sysEmailInfoRepository; 
	
	@Transactional(readOnly=true)
	public SysEmailInfoDto getById(String id) {
		SysEmailInfo sysEmailInfo = this.sysEmailInfoRepository.findOne(id);
		return CommonAssembler.assemble(sysEmailInfo, SysEmailInfoDto.class);
	}
	
	@Transactional
	public SysEmailInfoDto addSysEmailInfo(SysEmailInfoDto sysEmailInfoDto) {
		SysEmailInfo sysEmailInfo = CommonAssembler.assemble(sysEmailInfoDto, SysEmailInfo.class);
		sysEmailInfo = this.sysEmailInfoRepository.save(sysEmailInfo);
		return CommonAssembler.assemble(sysEmailInfo, SysEmailInfoDto.class);
	} 

	@Transactional
	public SysEmailInfoDto updateSysEmailInfo(SysEmailInfoDto sysEmailInfoDto) {
		SysEmailInfo sysEmailInfo = this.sysEmailInfoRepository.findOne(sysEmailInfoDto.getEmailInfoUuid());
		CommonAssembler.assemble(sysEmailInfoDto, sysEmailInfo);
		sysEmailInfo = this.sysEmailInfoRepository.save(sysEmailInfo);
		return CommonAssembler.assemble(sysEmailInfo, SysEmailInfoDto.class);
	}
	
	@Transactional
	public void deleteSysEmailInfo(String id) {
		SysEmailInfo sysEmailInfo = this.sysEmailInfoRepository.findOne(id);
		sysEmailInfo.setIsActive("N");
		this.sysEmailInfoRepository.save(sysEmailInfo);
	}
		
	@Transactional
	public void deleteSysEmailInfos(List<String> emailInfoUuids) {
		Iterable<SysEmailInfo> sysEmailInfos = this.sysEmailInfoRepository.findAll(emailInfoUuids);
		Iterator<SysEmailInfo> it = sysEmailInfos.iterator();
		while(it.hasNext()) {
			SysEmailInfo sysEmailInfo = it.next();
			sysEmailInfo.setIsActive("N");
			this.sysEmailInfoRepository.save(sysEmailInfo);
		}
	} 

 
	/**
	 * 分页查询
	 * @param request
	 * @return
	 */
	 /*
	@Transactional(readOnly=true)
	public PageResponseData<SysEmailInfo> pageQuery(PageRequestData request) {
		Specification<SysEmailInfo> spec = request.toSpecification(SysEmailInfo.class);
		Page<SysEmailInfo> page = sysEmailInfoRepository.findAll(spec, request.buildPageRequest());
		return new PageResponseData<SysEmailInfo>(page);
	}*/
	
	/**
	 * 分页查询
	 * @param request
	 * @return
	 */
	@Transactional(readOnly=true)
	public PageResponseData<SysEmailInfoDto> sqlPageQuery(PageRequestData request) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendTableColumns(SysEmailInfo.class, "t");
		sql.append(" from SYS_EMAIL_INFO t where t.IS_ACTIVE='Y' ");
		return sql.pageQuery(entityManager, request, SysEmailInfoDto.class);
	}
	
	public List<SysEmailInfoDto> findByEmailUuid(String emailUuid) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendTableColumns(SysEmailInfo.class, "t");
		sql.append(" from SYS_EMAIL_INFO t where t.IS_ACTIVE='Y' and t.status <> '2' and t.EMAIL_UUID = :emailUuid ");
		sql.addParameter("emailUuid", emailUuid);
		return sql.query(entityManager, SysEmailInfoDto.class);
	}
	
}


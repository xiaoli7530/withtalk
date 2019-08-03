package com.ctop.fw.sys.service;

import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.collection.spi.PersistentCollection;
import org.hibernate.engine.spi.SessionImplementor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ctop.fw.common.model.PageRequestData;
import com.ctop.fw.common.model.PageResponseData;
import com.ctop.fw.common.utils.BusinessException;
import com.ctop.fw.common.utils.SqlBuilder;
import com.ctop.fw.common.utils.SqlBuilderFactory;
import com.ctop.fw.common.utils.StringUtil;
import com.ctop.fw.sys.entity.SysExcelImport;
import com.ctop.fw.sys.event.CreateTempTableEvent;
import com.ctop.fw.sys.event.DropTempTableEvent;
import com.ctop.fw.sys.repository.SysExcelImportRepository;


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
public class SysExcelImportService {


	@Autowired
	private EntityManager entityManager;
	@Autowired
	private SysExcelImportRepository sysExcelImportRepository;

	@Autowired 
    private ApplicationEventPublisher publisher;

	
	public SysExcelImport getById(String id) {
		SysExcelImport sysExcelImport = this.sysExcelImportRepository.findOne(id);
		SessionImplementor session = this.entityManager.unwrap(SessionImplementor.class);
		//强制加载lazyLoad的集合
		session.initializeCollection((PersistentCollection) sysExcelImport.getColumns(), true);
		return sysExcelImport;
	}
	
	public SysExcelImport findByImportCode(String importCode) {
		SysExcelImport sysExcelImport = this.sysExcelImportRepository.findByImportCodeAndIsActive(importCode, "Y");
		if(sysExcelImport == null) {
			return null;
		}
		SessionImplementor session = this.entityManager.unwrap(SessionImplementor.class);
		//强制加载lazyLoad的集合
		session.initializeCollection((PersistentCollection) sysExcelImport.getColumns(), true);
		return sysExcelImport;
	}
	
	public String getTemplateUrl(String importCode) {
		SysExcelImport sysExcelImport = this.sysExcelImportRepository.findByImportCodeAndIsActive(importCode, "Y");
		if(sysExcelImport == null) {
			return null;
		}
		return sysExcelImport.getTplUrl();
	}
	
	@Transactional
	public SysExcelImport addSysExcelImport(SysExcelImport sysExcelImport) {
		this.validateImportCode(sysExcelImport);
		Session session = this.entityManager.unwrap(Session.class);
		session.persist(sysExcelImport);
		//事务提交后执行
		this.publisher.publishEvent(new CreateTempTableEvent(this, sysExcelImport));
		return sysExcelImport;
	} 

	@Transactional
	public SysExcelImport updateSysExcelImport(SysExcelImport sysExcelImport) {
		this.validateImportCode(sysExcelImport);
		SysExcelImport old = this.getById(sysExcelImport.getImportUuid());
		//事务提交后执行
		this.publisher.publishEvent(new DropTempTableEvent(this, old.getTempTable()));
		//事务提交后执行
		sysExcelImport.setVersion(old.getVersion());
		this.publisher.publishEvent(new CreateTempTableEvent(this, sysExcelImport));
		return this.entityManager.merge(sysExcelImport);
	}
	
	private void validateImportCode(SysExcelImport sysExcelImport) {
		String exImportUuid  = StringUtil.isEmpty(sysExcelImport.getImportUuid()) ? "_" : sysExcelImport.getImportUuid();
		long sameCount = this.sysExcelImportRepository.countSameImportCode(exImportUuid, sysExcelImport.getImportCode());
		if(sameCount > 0) {
			throw new BusinessException("存在同样的导入配置编号!");
		}
	}
	
	@Transactional
	public void deleteSysExcelImport(String id) {
		SysExcelImport sysExcelImport = this.sysExcelImportRepository.findOne(id);
		this.sysExcelImportRepository.delete(sysExcelImport);
		//事务提交后执行  
		this.publisher.publishEvent(new DropTempTableEvent(this, sysExcelImport.getTempTable()));
	}
	 
	@Transactional
	public void deleteSysExcelImports(List<String> importUuids) {
		Iterable<SysExcelImport> sysExcelImports = this.sysExcelImportRepository.findAll(importUuids); 
		this.sysExcelImportRepository.delete(sysExcelImports);
		for(SysExcelImport sysExcelImport : sysExcelImports) { 
			this.publisher.publishEvent(new DropTempTableEvent(this, sysExcelImport.getTempTable()));
		}
	}
	
	@Transactional 
	public void deleteByImportCode(String importCode) {
		SysExcelImport excelImport = this.findByImportCode(importCode);
		if(excelImport == null) {
			return;
		}
		this.deleteSysExcelImport(excelImport.getImportUuid());
	}
	
	/**
	 * 分页查询
	 * @param request
	 * @return
	 */
	public PageResponseData<SysExcelImport> sqlPageQuery(PageRequestData request) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendTableColumns(SysExcelImport.class, "t", "processScript", "transferScript");
		sql.append(" from SYS_EXCEL_IMPORT t where t.IS_ACTIVE='Y' ");
		return sql.pageQuery(entityManager, request, SysExcelImport.class);
	}
	
}


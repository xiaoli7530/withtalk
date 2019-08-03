package com.ctop.fw.sys.listener;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.ctop.fw.sys.entity.SysExcelImportInstance;
import com.ctop.fw.sys.event.CreateTempTableEvent;
import com.ctop.fw.sys.event.DropTempTableEvent;
import com.ctop.fw.sys.event.TempTableDataTransferedEvent;
import com.ctop.fw.sys.rest.SysExcelImportAction;
import com.ctop.fw.sys.service.SysExcelImportInstanceService;

/**
 * Excel导入相关的一些事务提交后才能执行的回调，通过ApplicationListener的方式实现；
 */
@Component
@Transactional
public class SysExcelImportListener {
	private static Logger logger = LoggerFactory.getLogger(SysExcelImportAction.class);


	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	SysExcelImportInstanceService sysExcelImportInstanceService;

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT) 
	public void createTempTable(CreateTempTableEvent event) {
		logger.debug("创建excel导入用的辅佐临时表{}.", event.getSysExcelImport().getTempTable());
		String sql = event.getSysExcelImport().buildCreateTempTableSql();
		try {
			logger.debug("sql: \n{}", sql);
			jdbcTemplate.execute(sql);
		} catch(Exception ex) {
			logger.debug("", ex);
			logger.debug("创建Excel导入的辅佐中间表{}失败! \nsql:{}", event.getSysExcelImport().getTempTable(), sql);
		}
	}
	
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT) 
	public void dropTempTable(DropTempTableEvent event) {
		logger.debug("删除excel导入用的辅佐临时表{}.", event.getTempTable());
		try {
			jdbcTemplate.execute("drop table " + event.getTempTable());
		} catch(Exception ex) {
			logger.debug("", ex);
			logger.debug("删除Excel导入的辅佐中间表{}失败!", event.getTempTable());
		}
	}
	
	@EventListener
	@Async
	public void clearTempTable(TempTableDataTransferedEvent event) {
		logger.debug("导入完成事务提交后，执行临时表的清理，临时文件的清理，清理当前导入相关数据和文件，同时也清理一天前执行的导入遗留的数据和文件.");
		SysExcelImportInstance instance = event.getInstance();
		this.sysExcelImportInstanceService.clearTempTable(instance);
	} 

}

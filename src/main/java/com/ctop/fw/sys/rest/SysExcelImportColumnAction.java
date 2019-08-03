package com.ctop.fw.sys.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ctop.fw.sys.entity.SysExcelImportColumn;
import com.ctop.fw.sys.service.SysExcelImportColumnService;

@RestController
@RequestMapping(path = "/rest/sys/sysExcelImportColumn")
public class SysExcelImportColumnAction {
	private static Logger logger = LoggerFactory.getLogger(SysExcelImportColumnAction.class);

	@Autowired
	SysExcelImportColumnService sysExcelImportColumnService;
	 

	@RequestMapping(value = "/getDraftColumnsByTable", method = RequestMethod.GET)
	@ResponseBody
	public List<SysExcelImportColumn> getDraftColumnsByTableName(@RequestParam("table") String table) {
		logger.debug("根据正式表{}自动生成导入字段配置!", table);
		return sysExcelImportColumnService.buildDraftColumns(table);
	} 
}

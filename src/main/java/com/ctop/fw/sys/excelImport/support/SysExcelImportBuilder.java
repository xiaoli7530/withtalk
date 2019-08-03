package com.ctop.fw.sys.excelImport.support;

import java.util.ArrayList;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ctop.fw.common.utils.BusinessException;
import com.ctop.fw.common.utils.StringUtil;
import com.ctop.fw.sys.entity.SysExcelImport;
import com.ctop.fw.sys.entity.SysExcelImportColumn;
import com.ctop.fw.sys.service.SysExcelImportColumnService;
import com.ctop.fw.sys.service.SysExcelImportService;

@Component
public class SysExcelImportBuilder {
	private static SysExcelImportService sysExcelImportService;
	private static SysExcelImportColumnService sysExcelImportColumnService;
	
    private static ScriptEngineManager scriptEngineManager = new ScriptEngineManager();  
    private static ScriptEngine nashorn = scriptEngineManager.getEngineByName("nashorn"); 

    public static SysExcelImport findByImportCode(String importCode) {
    	return sysExcelImportService.findByImportCode(importCode);
    }
    
    public static void deleteByImportCode(String importCode) {
    	sysExcelImportService.deleteByImportCode(importCode);
    }
    
    public static List<SysExcelImportColumn> buildDraftColumns(String table) {
    	return sysExcelImportColumnService.buildDraftColumns(table);
    }
    
    public static void saveSysExcelImport(SysExcelImport sysExcelImport) {
    	sysExcelImportService.addSysExcelImport(sysExcelImport);
    }
    
    public static SysExcelImport buildDraftSysExcelImport(String importCode, String importName, String targetTable, String tempTable) {
    	SysExcelImport sysExcelImport = new SysExcelImport();
    	sysExcelImport.setImportCode(importCode);
    	sysExcelImport.setImportName(importName);
    	sysExcelImport.setTargetTable(targetTable);
    	sysExcelImport.setTempTable(tempTable);
    	sysExcelImport.setNameRowIndex(0L);
    	sysExcelImport.setStartRowIndex(1L);
    	sysExcelImport.setPageSize(1000L);
    	sysExcelImport.setUpdatedBy("N");
    	if(StringUtil.isNotEmpty(targetTable)) {
    		sysExcelImport.setColumns(buildDraftColumns(targetTable));
    	} else {
    		sysExcelImport.setColumns(new ArrayList<SysExcelImportColumn>());
    	}
    	return sysExcelImport;
    }
    
    public static void callBuildScript(String script) {
    	 scriptEngineManager = new ScriptEngineManager();  
    	 scriptEngineManager.getEngineByName("nashorn");
		try {
			script = " " + script;
			nashorn.eval(script);
		} catch ( ScriptException e) {
			throw new BusinessException(e, "调用Excel导入模版生成脚本出错. {0}", new Object[]{e.getMessage()});
		}
    }
    
    
	
	@Autowired
	public void setSysExcelImportService(SysExcelImportService sysExcelImportService) {
		SysExcelImportBuilder.sysExcelImportService = sysExcelImportService;
	}
	
	@Autowired
	public void setSysExcelImportColumnService(SysExcelImportColumnService sysExcelImportColumnService) {
		SysExcelImportBuilder.sysExcelImportColumnService = sysExcelImportColumnService;
	}

}

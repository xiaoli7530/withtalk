package com.ctop.fw.sys.event;

import org.springframework.context.ApplicationEvent;

import com.ctop.fw.sys.entity.SysExcelImport;
import com.ctop.fw.sys.entity.SysExcelImportInstance;

public class TempTableDataTransferedEvent extends ApplicationEvent {
	private static final long serialVersionUID = 3663727165580917064L;
	private SysExcelImport excelImport;
	private SysExcelImportInstance instance;
	public TempTableDataTransferedEvent(Object source, SysExcelImport excelImport, SysExcelImportInstance instance) {
		super(source);
		this.excelImport = excelImport;
		this.instance = instance;
	}
	public SysExcelImport getExcelImport() {
		return excelImport;
	}
	public void setExcelImport(SysExcelImport excelImport) {
		this.excelImport = excelImport;
	}
	public SysExcelImportInstance getInstance() {
		return instance;
	}
	public void setInstance(SysExcelImportInstance instance) {
		this.instance = instance;
	}
}

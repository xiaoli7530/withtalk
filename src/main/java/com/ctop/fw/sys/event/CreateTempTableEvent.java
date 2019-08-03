package com.ctop.fw.sys.event;

import org.springframework.context.ApplicationEvent;

import com.ctop.fw.sys.entity.SysExcelImport;

public class CreateTempTableEvent extends ApplicationEvent {

	private static final long serialVersionUID = 8872464140886574465L;
	private SysExcelImport sysExcelImport;
	public CreateTempTableEvent(Object source, SysExcelImport sysExcelImport) {
		super(source);
		this.sysExcelImport = sysExcelImport;
	}
	public SysExcelImport getSysExcelImport() {
		return sysExcelImport;
	}
	public void setSysExcelImport(SysExcelImport sysExcelImport) {
		this.sysExcelImport = sysExcelImport;
	}
}

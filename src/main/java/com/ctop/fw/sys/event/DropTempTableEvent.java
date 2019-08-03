package com.ctop.fw.sys.event;

import org.springframework.context.ApplicationEvent;

public class DropTempTableEvent extends ApplicationEvent { 
	private static final long serialVersionUID = -2388798079954554792L;
	private String tempTable;
	
	public DropTempTableEvent(Object source, String tempTable) {
		super(source);
		this.tempTable = tempTable;
	}
	public String getTempTable() {
		return tempTable;
	}
	public void setTempTable(String tempTable) {
		this.tempTable = tempTable;
	}
	
}

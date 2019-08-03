package com.ctop.fw.common.excelexport;

import java.io.Serializable;

public class EntityRevisionAuditField implements Serializable { 
	private static final long serialVersionUID = -3314739035207772945L;
	private String field;
	private String label;
	private String dict; 
	
	public EntityRevisionAuditField() {}
	
	public EntityRevisionAuditField(String field, String label) {
		this.field = field;
		this.label = label;
	}
	
	public EntityRevisionAuditField(String field, String label, String dict) {
		this.field = field;
		this.label = label;
		this.dict = dict;
	}
	
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getDict() {
		return dict;
	}
	public void setDict(String dict) {
		this.dict = dict;
	}
}

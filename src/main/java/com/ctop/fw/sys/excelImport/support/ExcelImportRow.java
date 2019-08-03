package com.ctop.fw.sys.excelImport.support;

public class ExcelImportRow {
	private String[] fields;
	private String instanceUuid;
	private int rowNum;
	private String message;
	
	
	/**取出插入表的参数*/
	public Object[] getInsertParams() {
		Object[] arr = new Object[fields.length + 3];
		arr[0] = instanceUuid;
		arr[1] = rowNum;
		arr[2] = message;
		System.arraycopy(this.fields, 0, arr, 3, fields.length);
		return arr;
	}
	
	public ExcelImportRow getCopy() {
		ExcelImportRow copy = new ExcelImportRow();
		copy.rowNum = rowNum;
		copy.message = message;
		copy.instanceUuid = instanceUuid;
		copy.fields = new String[fields.length];
		System.arraycopy(fields, 0, copy.fields, 0, fields.length);
		return copy;
	}

	public String[] getFields() {
		return fields;
	}
	public void setFields(String[] fields) {
		this.fields = fields;
	}
	public int getRowNum() {
		return rowNum;
	}
	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public void appendMessage(String message) {
		if(this.message == null) {
			this.message = message;
		} else {
			this.message = this.message.concat(message);
		}
	}

	public String getInstanceUuid() {
		return instanceUuid;
	}

	public void setInstanceUuid(String instanceUuid) {
		this.instanceUuid = instanceUuid;
	}
}

package com.ctop.fw.common.excelexport;

public class GridColumnFixValueGetter extends GridColumnValueGetter {
	private static final long serialVersionUID = 3409208325049874271L;
	private Object value;
	
	public GridColumnFixValueGetter(Object value) {
		this.value = value;
	}
	
	@Override
	public Object getColumnValue(GridColumn column, Object rowData) {
		return value;
	}
}
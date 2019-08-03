package com.ctop.fw.common.excelexport;

import org.apache.poi.ss.usermodel.Cell;

public class RowNumberCellRenderer implements CellRenderer {
	
	private int rownum = 0;

	public RowNumberCellRenderer() {
	}

	@Override
	public void render(Cell cell, Object value, Object bean,GridColumn gridColumn) {
		this.rownum++;
		cell.setCellValue(this.rownum);
	}
	
	
}

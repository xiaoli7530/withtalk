package com.ctop.fw.common.excelexport;

import org.apache.poi.ss.usermodel.Cell;

public interface CellRenderer {

	public void render(Cell cell, Object value, Object bean,GridColumn gridColumn);
}

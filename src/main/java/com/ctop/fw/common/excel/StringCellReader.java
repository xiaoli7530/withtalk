package com.ctop.fw.common.excel;

import org.apache.poi.ss.usermodel.Cell;

public class StringCellReader extends CellReader<String> {

	@Override
	public String readCell(Cell cell) {
		return cell.getStringCellValue();
	}

}

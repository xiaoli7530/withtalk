package com.ctop.fw.common.excelexport;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;

import com.ctop.fw.common.utils.StringUtil;

public class DateCellRenderer implements CellRenderer {
	private SimpleDateFormat format;
	
	public DateCellRenderer(String format) {
		format = StringUtil.isEmpty(format) ? "yyyy-MM-dd HH:mm:ss" : format;
		this.format = new SimpleDateFormat(format);
	}

	public void render(Cell cell, Object input, Object rowData,GridColumn gridColumn) {
		if(input instanceof String) {
			cell.setCellValue( (String) input );
			return;
		} else if(input instanceof Date) {
			cell.setCellValue(format.format((Date) input));
		} else {
			cell.setCellValue("");
		}
	}

}

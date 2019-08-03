package com.ctop.fw.common.excel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;

import com.ctop.fw.common.utils.BusinessException;

public class DateCellReader extends CellReader<Date> {
	
	private SimpleDateFormat dateFormat;
	private String format;
	
	public DateCellReader(String format) {
		this.format = format;
		this.dateFormat = new SimpleDateFormat(format);
	}

	@Override
	public Date readCell(Cell cell) {
		int cellType = cell.getCellType();
		switch (cellType) {
		case Cell.CELL_TYPE_BLANK:
			return null;
		case Cell.CELL_TYPE_NUMERIC:
			// 针对日期特殊处理
			return cell.getDateCellValue();
		case Cell.CELL_TYPE_STRING:
			String text = cell.getStringCellValue();
			try {
				return this.dateFormat.parse(text);
			} catch (ParseException e) {
				int x = cell.getRowIndex(), y = cell.getColumnIndex();
				throw new BusinessException("单元格({0}, {1})无法识别的日期{2}, 格式:{3}", new Object[]{x, y, text, this.format});
			}
		}
		return null;
	}

}

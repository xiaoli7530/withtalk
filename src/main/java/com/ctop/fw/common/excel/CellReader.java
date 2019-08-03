package com.ctop.fw.common.excel;

import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;

public abstract class CellReader<T> {
	
	public static CellReader<String> STRING = new StringCellReader();
	
	public static CellReader<Date> dateReader(String format) {
		return new DateCellReader(format);
	}

	public abstract T readCell(Cell cell);
	
//	protected void test() {
//		int cellType = cell.getCellType();
//		switch (cellType) {
//		case Cell.CELL_TYPE_BLANK:
//			return null;
//		case Cell.CELL_TYPE_NUMERIC:
//			if (column.isDateTargetType()) {
//				// 针对日期特殊处理
//				Date date = cell.getDateCellValue();
//				if (date != null) {
//					String format = DEFAULT_DATE_FORMAT;
//					if (column.getDateFormat() != null) {
//						format = column.getDateFormat();
//						format = format.replace("HH24", "HH");
//						format = format.replace("mi", "mm");
//					}
//					return new SimpleDateFormat(format).format(date);
//				}
//				return null;
//			} else {
//				double num = cell.getNumericCellValue();
//				long numL = (long) num;
//				return numL == num ? String.valueOf(numL) : String.valueOf(num);
//			}
//		case Cell.CELL_TYPE_STRING:
//			return cell.getStringCellValue();
//		case Cell.CELL_TYPE_FORMULA:
//			int fCellType = cell.getCachedFormulaResultType();
//			return readCellValue(column, cell, fCellType);
//		case Cell.CELL_TYPE_BOOLEAN:
//			return cell.getBooleanCellValue() ? "Y" : "N";
//		}
//		return null;
//	}
}

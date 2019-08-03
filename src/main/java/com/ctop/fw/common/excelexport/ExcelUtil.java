package com.ctop.fw.common.excelexport;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.ctop.fw.common.utils.BusinessException;
import com.ctop.fw.common.utils.StringUtil;
import com.microsoft.schemas.office.visio.x2012.main.CellType;

public class ExcelUtil { 
	

	public static Workbook createWorkBook(String filePath) {
		File file = new File(filePath);
		try {
			return WorkbookFactory.create(file);
		} catch (EncryptedDocumentException | InvalidFormatException | IOException e) {
			throw BusinessException.template("");
		}
	}
	 
	public static void close(Workbook wb) {
		try {
			wb.close();
		} catch (IOException e) {
			//
		}
	}
	
	 
	public static List<String> getAllSheetNames(Workbook workbook) {
		int num = workbook.getNumberOfSheets();
		List<String> list = new ArrayList<String>(num);
		for(int i=0; i<num; i++) {
			list.add(workbook.getSheetName(i));
		}
		return list;
	}
	
	public static Workbook readExcelTemplate4Export(InputStream is) { 
		try {
			Workbook wb = WorkbookFactory.create(is);
			return wb;
		} catch (EncryptedDocumentException | InvalidFormatException | IOException e) {
			throw new BusinessException(e, "无法识别的Excel文件!", new Object[]{});
		}
	}
	  
	public static Workbook newSXSSFWorkbook(int num) {
//		return new SXSSFWorkbook(num);
		return new XSSFWorkbook();
	}
	
	/**
	 * 将A1, A2这样的的位置转换成 [1, 2], [1, 2]这样的地址
	 * 
	 * @param location
	 * @return
	 */
	public static int[] convertExcelLocation(String location) {
		return StringUtil.convertExcelLocation(location);
	}
	
	public static Cell getCell(Sheet sheet, String location) {
		int[] xy = convertExcelLocation(location);
		return getCell(sheet, xy[0], xy[1]);
	}
	
	public static Cell getCell(Sheet sheet, int x, int y) { 
		Row row = sheet.getRow(x);
		if(row == null) { 
			row = sheet.createRow(x);
		}
		Cell cell = row.getCell(y);
		if(cell == null) {
			cell = row.createCell(y);
		}
		return cell;
	}
	
	public static void writeCellValue(Sheet sheet, String location, String value) {
		Cell cell = getCell(sheet, location);
		cell.setCellValue(value);
	}
	
	public static void writeCellValue(Sheet sheet, String location, Number value) {
		Cell cell = getCell(sheet, location);
		if(value != null) {
			cell.setCellValue(value.doubleValue());
		}
	}
	
	public static void writeCellValue(Sheet sheet, int row, int column, String value) {
		Cell cell = getCell(sheet, row, column);
		cell.setCellValue(value);
	}
	
	public static void writeCellValue(Sheet sheet, int row, int column, Date value) {
		writeCellValue(sheet, row, column, value, "yyyy-MM-dd");
	}
	
	public static void writeCellValue(Sheet sheet, String location, Date value) {
		writeCellValue(sheet, location, value, "yyyy-MM-dd");
	}
	
	public static void writeCellValue(Sheet sheet, String location, Date value, String format) {
		if(value == null) {
			return;
		}  
		Cell cell = getCell(sheet, location);
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		cell.setCellValue(dateFormat.format(value));
	}
	
	public static void writeCellValue(Sheet sheet, int row, int column, Date value, String format) {
		if(value == null) {
			return;
		}
		Cell cell = getCell(sheet, row, column);
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		cell.setCellValue(dateFormat.format(value));
	}
	
	public static void writeCellValue(Sheet sheet, int row, int column, Number value) {
		Cell cell = getCell(sheet, row, column);
		if(value != null) {
			cell.setCellValue(value.doubleValue());
		}
	}
	public static String getCellValue(Cell cell,SimpleDateFormat sdf) {  
	    String ret = "";  
	    if (cell == null) return ret;  
	    int type = cell.getCellType();  
	    switch (type) {  
	    case Cell.CELL_TYPE_BLANK:  
	        ret = "";  
	        break;  
	    case Cell.CELL_TYPE_BOOLEAN:  
	        ret = String.valueOf(cell.getBooleanCellValue());  
	        break;  
	    case Cell.CELL_TYPE_ERROR:  
	        ret = null;  
	        break;  
	    case Cell.CELL_TYPE_FORMULA:  
	        Workbook wb = cell.getSheet().getWorkbook();  
	        CreationHelper crateHelper = wb.getCreationHelper();  
	        FormulaEvaluator evaluator = crateHelper.createFormulaEvaluator();  
	        ret = getCellValue(evaluator.evaluateInCell(cell),null);  
	        break;  
	    case Cell.CELL_TYPE_NUMERIC:  
	        if (DateUtil.isCellDateFormatted(cell)) {  
	            Date theDate = cell.getDateCellValue();  
	            ret = sdf.format(theDate);  
	        } else {  
	            ret = NumberToTextConverter.toText(cell.getNumericCellValue());  
	        }  
	        break;  
	    case Cell.CELL_TYPE_STRING:  
	        ret = cell.getRichStringCellValue().getString();  
	        break;  
	    default:  
	        ret = "";  
	    }  
	    return StringUtils.isNotBlank(ret) ? ret.trim() : ""; // 有必要自行trim  
	}  
}

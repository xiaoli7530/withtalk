package com.ctop.fw.common.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 
 * @author zhouyx
 *
 */
public class ExcelUtil {
	/**
	 * 解析Excel的默认日期格式
	 */
	private final static String DEF_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	/**
	 * 解析Excel的自定义日期格式
	 */
	private static String dateFormat = "";
	
	/**
	 * 
	 * @param type			xls-2003;xlsx-2007
	 * @param headerNames	列头名称
	 * @param headerCodes	列头编码
	 * @param excelData
	 * @param exportFileName
	 * @param response
	 */
	public static void generateExcel(String type, String[] headerCodes, String[] headerNames,
			List<Map<String, Object>> excelData, String exportFileName, HttpServletResponse response,
			HttpServletRequest request) {
		// 1、创建Workbook
		Workbook wb = null;
		if("xls".equals(type)) {
			wb = new HSSFWorkbook();
		} else {
			wb = new XSSFWorkbook();
		}
		
		// 2、创建Sheet
		//Sheet sheet = wb.createSheet("test");
		Sheet sheet = wb.createSheet();
		
		// 3、创建标题行
		Row header = sheet.createRow(0);
		// 标题行字体
		Font headerFont = createFont(wb, "Verdana", (short)12, HSSFColor.BLACK.index, 
				Font.BOLDWEIGHT_BOLD, false, false);
		// 标题行样式
		CellStyle headerStyle = wb.createCellStyle();
		headerStyle.setAlignment(CellStyle.ALIGN_CENTER);
		headerStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		headerStyle.setFillForegroundColor(HSSFColor.LIGHT_TURQUOISE.index);
		headerStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		// 设置边框
		headerStyle.setBottomBorderColor(HSSFColor.BLACK.index);
		headerStyle.setBorderBottom(CellStyle.BORDER_THIN);
		headerStyle.setBorderLeft(CellStyle.BORDER_THIN);
		headerStyle.setBorderRight(CellStyle.BORDER_THIN);
		headerStyle.setBorderTop(CellStyle.BORDER_THIN);
		// 设置字体
		headerStyle.setFont(headerFont);
		headerStyle.setWrapText(true);// 若文本中有换行符(\n)，也会跟着换行
		headerStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 垂直居中
		if(headerNames == null || headerNames.length < 1) {
			headerNames = headerCodes;
		}
		// 输出标题行
		for (int i = 0; i < headerNames.length; i++) {
			Cell headerCell = header.createCell(i);
			headerCell.setCellStyle(headerStyle);	// 设置单元格样式
			headerCell.setCellValue(headerNames[i]);	// 设置单元格值
		}
		
		// 4、输出内容行
		// 创建普通单元格样式
		CellStyle bodyStyle = wb.createCellStyle();
		bodyStyle.setAlignment(CellStyle.ALIGN_CENTER);
		bodyStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		for(int i = 0; i < excelData.size(); i++) {
			Map<String, Object> data = excelData.get(i);
			Row row = sheet.createRow(i+1);// 第0行为列头
			for(int j = 0; j < headerCodes.length; j++) {
				row.createCell(j).setCellStyle(bodyStyle);
				Object cellValue = data.get(headerCodes[j]);
				String cellValueStr = "";
				if(cellValue != null) {
					cellValueStr = cellValue.toString();
				}
				row.createCell(j).setCellValue(cellValueStr);
				row.setHeight((short)450);	// 设置行高
			}
		}
		
		// 设置列宽度为自适应。注意：要在内容输出后设置
		for (int i = 0; i < headerNames.length; i++) {
			sheet.autoSizeColumn(i);	
		}
		
		/**
		 * 通过文件输出流生成Excel文件
		 */
		/*OutputStream os;
		try {
			os = new FileOutputStream(exportFilePath);
			wb.write(os);
			os.flush();
			os.close();
			System.out.println("Excel文件导出完成！导出文件路径：" + exportFilePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		 
		/**
		 * Web形式输出Excel
		 */
		try {
			//下载文件的文件名处理
			String agent = request.getHeader("USER-AGENT");  
	        if (null != agent && -1 != agent.indexOf("MSIE") || null != agent  
	        		&& -1 != agent.indexOf("Trident")) {
	        	// IE浏览器
	        	exportFileName = URLEncoder.encode(exportFileName, "UTF-8");  
	        } else {  
	        	// 其他浏览器
	        	exportFileName = new String(exportFileName.getBytes(), "ISO8859_1");
	        } 
			
	        response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document;charset=UTF-8"); 
			response.addHeader("Content-Disposition", "attachment; filename=" + exportFileName);
			wb.write(response.getOutputStream());
			response.flushBuffer(); 
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * 解析Excel。包括2003和2007
	 * @param	fileName	Excel文件路径
	 * @param 	startRow	内容开始行(从0开始)
	 * @param	startColumn	内容开始列（从0开始）
	 * @param	headers		列头
	 * @return	List<map<key, value>> key与headers对应
	 */
	public static List<Map<String, String>> paserExcel(String fileName, int startRow,
			int startColumn, String[] headers) {
		InputStream is = null;
		Workbook wb = null;
		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
		try {
			is = new FileInputStream(fileName);
			if(fileName.endsWith(".xls")) {
				wb = new HSSFWorkbook(is);	// Excel-2003
			} else {
				wb = new XSSFWorkbook(is);	// Excel-2007
			}
			
			// 1、循环工作表Sheet
			for (int wbNum = 0; wbNum < wb.getNumberOfSheets(); wbNum++) {
				Sheet sheet = wb.getSheetAt(wbNum);
				if(sheet == null) continue;
				
				// 2、循环Row
				int endRow = sheet.getLastRowNum() + startRow;
				for (int rowNum = startRow; rowNum < endRow; rowNum++) {
					Row row = sheet.getRow(rowNum);
					// 若是原来有数据，而用delete删掉，则row不为null，此时会读取该Row空的单元格 
					if(row == null) continue;
					
					// 3、循环Cell
					Map<String, String> cellData = new HashMap<String, String>();
					for (int i = 0; i < headers.length; i++) {
						Cell cell = row.getCell(i+startColumn);
						cellData.put(headers[i], getCellValue(cell));
					}
					// 判断Row是否为空数据
					if(!isEmptyMap(cellData)) {
						dataList.add(cellData);
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		ExcelUtil.dateFormat = "";//清空自定义日期格式
		return dataList;
	}
	
	/**
	 * 设置日期时间格式。默认为：yyyy-MM-dd HH:mm:ss
	 * @param dateFormat
	 */
	public static void setDtFormat(String dateFormat) {
		ExcelUtil.dateFormat = dateFormat;
	}

	// 获取单元格中的数据
	private static String getCellValue(Cell cell) {
		if(cell == null) {
			return "";
		}
		
		int cellType = cell.getCellType();
		if(cellType == Cell.CELL_TYPE_NUMERIC) {	// 数值型
			if(HSSFDateUtil.isCellDateFormatted(cell)) {	// 日期格式
				return getDateFromCell(cell);
			} else {
				double d = cell.getNumericCellValue();
				int i = (int) d;
				if(d > i) {
					return "" + d;
				} else {
					return "" + i;
				}
				//return "" + cell.getNumericCellValue();
			}
		} else if(cellType == Cell.CELL_TYPE_BOOLEAN) {	// 布尔型
			return "" + cell.getBooleanCellValue();
		} else {	// 其他
			return cell.getStringCellValue();
		}
	}
	
	// 获取单元格中日期型数据
	private static String getDateFromCell(Cell cell) {
		String formart = ExcelUtil.DEF_DATE_FORMAT;
		String dateFormat = ExcelUtil.dateFormat.trim();
		if(dateFormat != null && "".equals(dateFormat)) {
			formart = dateFormat;
		}
		SimpleDateFormat format = new SimpleDateFormat(formart);
		return format.format(cell.getDateCellValue());
	}

	// 判断Map的值是否为空。若为空或null，返回true；否则返回false
	private static boolean isEmptyMap(Map<String, String> map) {
		Iterator<Map.Entry<String, String>> itera = map.entrySet().iterator();
		while(itera.hasNext()) {
			Map.Entry<String, String> entry = (Map.Entry<String, String>)itera.next();  
			if(entry.getValue() != null && !"".equals(entry.getValue())) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 
	 * @param wb
	 * @param fontName
	 * @param size
	 * @param color
	 * @param boldweight
	 * @param isItalic	是否使用斜体
	 * @param Strikeout	是否使用划线
	 * @return
	 */
	private static Font createFont(Workbook wb, String fontName, short size, 
			short color, short boldweight, boolean isItalic, boolean isStrikeout) {
		Font font = wb.createFont();
		font.setFontName(fontName);
		font.setFontHeightInPoints(size);
        font.setColor(color);
        font.setBoldweight(boldweight);
        font.setItalic(isItalic);
        font.setStrikeout(isStrikeout);
		
		return font;
	}
	
	
	
}

package com.ctop.fw.common.excelexport;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.tomcat.util.http.fileupload.IOUtils;

import com.ctop.fw.common.constants.Constants.YesNo;
import com.ctop.fw.common.entity.BaseEntity;
import com.ctop.fw.common.model.BaseDto;
import com.ctop.fw.common.utils.StringUtil;
import com.ctop.fw.common.utils.ZipCompressor;

/**
 * 
 * @author 龚建军
 *
 */
public class DatagridRenderer {
	private Datagrid datagrid;
	private Workbook wb;
	private List<GridColumn> flatColumns = new ArrayList<GridColumn>();
	private Sheet sheet;
	private int rownum;
	private CellStyler styleProvider;

	public DatagridRenderer(Datagrid datagrid) {
		Workbook wb = ExcelUtil.newSXSSFWorkbook(1000);
		String title = StringUtil.isEmpty(datagrid.getTitle()) ? "导出列表" : datagrid.getTitle();
		Sheet sheet = wb.createSheet(title);
		this.init(datagrid, sheet);
	}

	public DatagridRenderer(Datagrid datagrid, Sheet sheet) {
		this.init(datagrid, sheet);
	}

	private void init(Datagrid datagrid, Sheet sheet) {
		this.datagrid = datagrid;
		this.wb = sheet.getWorkbook();
		this.sheet = sheet;
		this.styleProvider = new CellStyler();
		this.styleProvider.initWithWorkbook(wb);
		this.datagrid.populateColumnIndex();
		this.flatColumns = this.datagrid.buildFlatColumns();
	}
	
	public String getTitle() {
		String title = this.datagrid.getTitle();
		return StringUtil.isEmpty(title) ? "download.xlsx" : title;
	}
	
	private Cell getCell(int rowIndex, int columnIndex) {
		Row row = this.sheet.getRow(rowIndex);
		if (row == null) {
			row = this.sheet.createRow(rowIndex);
		}
		Cell cell = row.getCell(columnIndex);
		if (cell == null) {
			cell = row.createCell(columnIndex);
		}
		return cell;
	}

	public void createSheet(String sheetName) {
		if (sheetName != null) {
			this.sheet = this.wb.createSheet(sheetName);
		} else {
			this.sheet = this.wb.createSheet();
		}
		this.rownum = 0;
	}

	private void buildTitleRows4Columns(int rowBaseIndex, int columnBaseIndex) {
		this.rownum = rowBaseIndex;
		List<List<GridColumn>> columns = this.datagrid.getColumns();
		for (int i = 0; i < columns.size(); i++) {
			this.sheet.createRow(rownum++);
		}
		for (List<GridColumn> list : columns) {
			for (GridColumn column : list) {
				this.writeHeader(column, rowBaseIndex, columnBaseIndex);
			}
			//TODO update by dais 2017年12月4日14:26:47  修改合并表头确实表头数据问题
			rowBaseIndex ++ ;
		}
	}

	private void writeHeader(GridColumn column, int rowIndexBase, int columnIndexBase) {
		if (column.isExportIgnored()) {
			return;
		}
		int colspan = column.getColspan() > 0 ? column.getColspan() : 1;
		int rowspan = column.getRowspan() > 0 ? column.getRowspan() : 1;
		int rowEnd = rowIndexBase + (rowspan - 1);
		int colStart = columnIndexBase + column.getColumnIndex();
		int colEnd = columnIndexBase + column.getColumnIndex() + (colspan - 1);
		if (colspan != 1 || rowspan != 1) {
			CellRangeAddress address = new CellRangeAddress(rowIndexBase, rowEnd, colStart, colEnd);
			this.sheet.addMergedRegion(address);
		}
		Cell cell = this.getCell(rowIndexBase, colStart);
		cell.setCellValue(column.getTitle());
		styleProvider.provideHeaderStyle(column, cell);
	}

	public void renderTitleRows() {
		this.renderTitleRows(this.rownum, 0);
	}

	/** 从指定单元格开始显示头 */
	public void renderTitleRows(String location) {
		int[] xy = ExcelUtil.convertExcelLocation(location);
		this.renderTitleRows(xy[0], xy[1]);
	}

	public void renderTitleRows(int startRowIndex, int startColumnIndex) {
		this.buildTitleRows4Columns(startRowIndex, startColumnIndex);

		for (GridColumn column : this.flatColumns) {
			int width = column.getIntWidth();
			width = (width == 0 ? 100 : width) * 32;
			this.sheet.setColumnWidth(column.getColumnIndex(), width);
		}
	}
	

	public void appendRowDatas(List<Object> rowDatas) {
		this.renderData(rowDatas, this.rownum, 0);
	}
	
	public void renderData(String startLocation, List<?> rowDatas) {
		int[] xy = ExcelUtil.convertExcelLocation(startLocation);
		this.renderData(rowDatas, xy[0], xy[1]);
	}
	
	/**
	 * 在指定位置生成表格数据，
	 * @param rowDatas
	 * @param startRowIndex
	 * @param startColumnIndex
	 */
	public void renderData(List<?> rowDatas, int startRowIndex, int startColumnIndex) {
		this.rownum = startRowIndex;
		datagrid.processColumnRenderer(rowDatas);
		Row styleRow = this.sheet.getRow(this.rownum);
		if(! (this.sheet instanceof SXSSFSheet)) {
			if (sheet.getLastRowNum() > this.rownum) {
				this.sheet.shiftRows(this.rownum, sheet.getLastRowNum(), rowDatas.size(), true, false);
				styleRow = this.sheet.getRow(this.rownum + rowDatas.size());
			}
		}  
		for (Object rowData : rowDatas) { 
			Row row = this.createRow();
			for (GridColumn column : this.flatColumns) {
				int colIndex = startColumnIndex + column.getColumnIndex();
				Cell cell = row.createCell(colIndex);
				Cell styleCell = styleRow != null ? styleRow.getCell(colIndex) : null;
				CellStyle style = styleCell != null ? styleCell.getCellStyle() : null;
				column.renderCellValue(cell, rowData);
				this.styleProvider.provideBodyStyle(rowData, column, cell, style);
			}
		}
	}

	public Workbook getWorkbook() {
		return this.wb;
	}

	private Row createRow() {
		if (rownum >= 60000) {
			String sheetName = this.sheet.getSheetName();
			int pos = sheetName.indexOf("_");
			if (pos == -1) {
				sheetName = sheetName + "_1";
			} else {
				int num = 1;
				try {
					num = Integer.parseInt(sheetName.substring(pos + 1));
					num += 1;
					sheetName = sheetName.substring(0, pos) + "_" + num;
				} catch (Exception ex) {
					sheetName = sheetName + "_" + num;
				}
			}
			this.createSheet(sheetName);
			this.renderTitleRows();
		} 
		Row row = this.sheet.createRow(rownum++);
		return row;
	}

	/**
	 * 下载流
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void download(String name, HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document;charset=UTF-8"); 
		String userAgent = request.getHeader("User-Agent");
		name = StringUtil.encodeDownloadName(name, userAgent);
		response.setHeader("Content-disposition", String.format("attachment; filename=\"%s\"", name));
		this.wb.write(response.getOutputStream());
		response.flushBuffer(); 
	}
	
	public byte[] toBytes() throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			this.wb.write(bos);
			return bos.toByteArray();
		} finally {
			IOUtils.closeQuietly(bos);
		}
	}
	
	public static void downloadZip(String tempZipFile, HttpServletRequest request, HttpServletResponse response,Map<String,File> excelTempFileMap) 
			throws IOException{
		response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document;charset=UTF-8"); 
		String userAgent = request.getHeader("User-Agent");
		tempZipFile = StringUtil.encodeDownloadName(tempZipFile, userAgent);
		response.setHeader("Content-disposition", String.format("attachment; filename=\"%s\"", tempZipFile));
		ZipCompressor.compress(response.getOutputStream(), excelTempFileMap);
		response.flushBuffer();     
	}
	
	public static class CellStyler {
		protected Workbook wb;
		protected CellStyle headerStyle;
		protected CellStyle bodyStyle;
		protected CellStyle bodyDeleteStyle;
		
		public void initWithWorkbook(Workbook wb) {
			this.wb = wb;
			this.headerStyle = wb.createCellStyle();
			this.bodyStyle = wb.createCellStyle(); 
			Font font = wb.createFont();
			font.setBold(true);
			headerStyle.setFont(font);
			headerStyle.setAlignment(CellStyle.ALIGN_CENTER);
			headerStyle.setWrapText(true);// 若文本中有换行符(\n)，也会跟着换行
			headerStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 垂直居中
			
			bodyStyle = this.wb.createCellStyle();
		}
		
		public void provideHeaderStyle(GridColumn column, Cell cell) {
			cell.setCellStyle(this.headerStyle);
		}
		
		public void provideBodyStyle(Object rowData, GridColumn column, Cell cell, CellStyle baseStyle) {
			if(baseStyle != null) {
				cell.setCellStyle(baseStyle);
			} else {
				cell.setCellStyle(this.bodyStyle);
			}
			if (rowData instanceof BaseDto && YesNo.NO.equals(((BaseDto) rowData).getIsActive())
					|| rowData instanceof BaseEntity && YesNo.NO.equals(((BaseEntity) rowData).getIsActive())) {
				if (this.bodyDeleteStyle == null) {
					this.bodyDeleteStyle = this.wb.createCellStyle();
					this.bodyDeleteStyle.cloneStyleFrom(cell.getCellStyle());
					Font font = this.wb.createFont(); // 设置字体的样式  
		            font.setStrikeout(true);
		            this.bodyDeleteStyle.setFont(font);
				}
				cell.setCellStyle(this.bodyDeleteStyle);
			}
		}
	}
	
	
}

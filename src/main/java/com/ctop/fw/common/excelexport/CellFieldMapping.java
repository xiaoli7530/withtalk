package com.ctop.fw.common.excelexport;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

import com.ctop.fw.common.utils.StringUtil;

/**
 * 记录字段与excel中的单元格的对应关系；
 * 并提供方法将一个bean中的对应属性写入到指定的单元格；
 * @author 龚建军
 *
 */
public class CellFieldMapping {
	
	private List<CellField> cellFields = new ArrayList<CellField>();
	
	public CellFieldMapping mapCellField(String location, String field) {
		int[] xy = StringUtil.convertExcelLocation(location);
		this.cellFields.add(new CellField(xy[0], xy[1], field, CellRendererBuilder.NOOP));
		return this;
	}
	
	public CellFieldMapping mapCellField(int rowIndex, int columnIndex, String field) {
		this.cellFields.add(new CellField(rowIndex, columnIndex, field, CellRendererBuilder.NOOP));
		return this;
	}
	
	public CellFieldMapping mapCellField(int rowIndex, int columnIndex, String field, CellRenderer renderer) {
		this.cellFields.add(new CellField(rowIndex, columnIndex, field, renderer));
		return this;
	}
	
	/**
	 * 将bean的属性值写入Excel sheet;
	 * @param sheet
	 * @param bean
	 */
	public void renderFields(Sheet sheet, Object bean) {
		BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(bean);
		for(CellField cellField : cellFields) {
			Object value = bw.getPropertyValue(cellField.getField());
			cellField.renderCell(sheet, value, bean);
		}
	}
	
	/**
	 * 
	 * @author 龚建军
	 *
	 */
	public static class CellField {
		private int rowIndex;
		private int columnIndex;
		private String field;
		private CellRenderer cellRenderer;
		
		public CellField(int rowIndex, int columnIndex, String field, CellRenderer renderer) {
			this.rowIndex = rowIndex;
			this.columnIndex = columnIndex;
			this.field = field;
			this.cellRenderer = renderer;
		}
		
		/**
		 * 
		 * @param sheet
		 * @param value
		 * @param bean
		 */
		public void renderCell(Sheet sheet, Object value, Object bean) {  
			Row row = sheet.getRow(this.rowIndex);
			if(row == null) {
				row = sheet.createRow(this.rowIndex);
			}
			Cell cell = row.getCell(this.columnIndex);
			if(cell == null) {
				cell = row.createCell(this.columnIndex);
			}
			this.cellRenderer.render(cell, value, bean,null);
		}
		
		public int getRowIndex() {
			return rowIndex;
		}
		public void setRowIndex(int rowIndex) {
			this.rowIndex = rowIndex;
		}
		public int getColumnIndex() {
			return columnIndex;
		}
		public void setColumnIndex(int columnIndex) {
			this.columnIndex = columnIndex;
		}
		public CellRenderer getCellRenderer() {
			return cellRenderer;
		}
		public void setCellRenderer(CellRenderer cellRenderer) {
			this.cellRenderer = cellRenderer;
		}
		public String getField() {
			return field;
		}
		public void setField(String field) {
			this.field = field;
		}
	}
}

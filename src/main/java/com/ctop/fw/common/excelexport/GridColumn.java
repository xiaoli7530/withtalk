package com.ctop.fw.common.excelexport;

import java.io.Serializable;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;

import com.ctop.fw.common.utils.StringUtil;

public class GridColumn implements Serializable {
	private static final long serialVersionUID = -3407186091085034712L;
	private String title;
	private String field; 
	private int colspan = 1;
	private int rowspan = 1;
	private String align;
	private String halign = "center";
	private boolean hidden = false;
	private boolean checkbox = false;
	private boolean exportIgnored = false;
	
	private String dateFormat;
	private String dict;
	private String textField;
	private String format;
	
	//纯excel中显示用；
	private String excelRenderer;
	private String rateText;
	private Integer decimals;

	private CellRenderer renderer;
	private String width;
	private int columnIndex = -1;
	

	private String auditedEntityClass;
	private String auditedEntityIdField;
	private List<EntityRevisionAuditField> auditFields;
		
	private GridColumnValueGetter valueGetter = new GridColumnValueGetter();
	
	public GridColumn() {}
	
	public GridColumn(String title, String field, int columnIndex, CellRenderer renderer) {
		this.title = title;
		this.field = field;
		this.columnIndex = columnIndex;
		this.renderer = renderer;
	} 
	
	/**支持是否导出*/
	public boolean isExportIgnored() {
		return hidden || checkbox || exportIgnored;
	}
	
	public int getIntWidth() {
		if(StringUtil.isEmpty(this.width)) {
			return 100;
		}
		String width = this.width.replace("px", "");
		return Integer.parseInt(width);
	}
	  
	
	public void renderCellValue(Cell cell, Object rowData) {
		Object cellValue = this.valueGetter.getColumnValue(this, rowData);
		renderer.render(cell, cellValue, rowData,this);
	}
	
	public void buildRenderer() {
		if(this.renderer == null) {
			if(StringUtil.isNotEmpty(this.getExcelRenderer())){
				this.renderer = CellRendererBuilder.INSTANCE.buidlRenderer(this);
			} else {
				this.renderer = CellRendererBuilder.NOOP;
			}
		}
	}

	public String getExcelRenderer() {
		if(StringUtil.isNotEmpty(excelRenderer)) {
			return excelRenderer;
		}
		if(StringUtil.isNotEmpty(this.dict)) {
			return "dict";
		} else if(StringUtil.isNotEmpty(this.dateFormat)) {
			return "date";
		}
		return null;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public int getColspan() {
		return colspan;
	}

	public void setColspan(int colspan) {
		this.colspan = colspan;
	}

	public int getRowspan() {
		return rowspan;
	}

	public void setRowspan(int rowspan) {
		this.rowspan = rowspan;
	}

	public String getAlign() {
		return align;
	}

	public void setAlign(String align) {
		this.align = align;
	}

	public String getHalign() {
		return halign;
	}

	public void setHalign(String halign) {
		this.halign = halign;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public boolean isCheckbox() {
		return checkbox;
	}

	public void setCheckbox(boolean checkbox) {
		this.checkbox = checkbox;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public String getDict() {
		return dict;
	}

	public void setDict(String dict) {
		this.dict = dict;
	}

	public String getTextField() {
		return textField;
	}

	public void setTextField(String textField) {
		this.textField = textField;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public void setExcelRenderer(String excelRenderer) {
		this.excelRenderer = excelRenderer;
	}

	public CellRenderer getRenderer() {
		return renderer;
	}

	public void setRenderer(CellRenderer renderer) {
		this.renderer = renderer;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getRateText() {
		return rateText;
	}

	public void setRateText(String rateText) {
		this.rateText = rateText;
	}

	public Integer getDecimals() {
		return decimals;
	}

	public void setDecimals(Integer decimals) {
		this.decimals = decimals;
	}

	public int getColumnIndex() {
		return columnIndex;
	}

	public void setColumnIndex(int columnIndex) {
		this.columnIndex = columnIndex;
	}

	public String getAuditedEntityClass() {
		return auditedEntityClass;
	}

	public void setAuditedEntityClass(String auditedEntityClass) {
		this.auditedEntityClass = auditedEntityClass;
	}

	public String getAuditedEntityIdField() {
		return auditedEntityIdField;
	}

	public void setAuditedEntityIdField(String auditedEntityIdField) {
		this.auditedEntityIdField = auditedEntityIdField;
	}

	public List<EntityRevisionAuditField> getAuditFields() {
		return auditFields;
	}

	public void setAuditFields(List<EntityRevisionAuditField> auditFields) {
		this.auditFields = auditFields;
	}

	public GridColumnValueGetter getValueGetter() {
		return valueGetter;
	}

	public void setValueGetter(GridColumnValueGetter valueGetter) {
		this.valueGetter = valueGetter;
	}
}

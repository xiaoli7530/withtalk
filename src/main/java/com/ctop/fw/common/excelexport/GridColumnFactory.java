package com.ctop.fw.common.excelexport;

import java.util.Date;
import java.util.List;

import com.ctop.fw.common.excelexport.CellRendererBuilder.EmptyColumnRenderer;

public class GridColumnFactory {

	/**
	 * 文本列
	 * @param field
	 * @param columnIndex
	 * @return
	 */
	public static GridColumn text(String field, String title) {
		GridColumn column = new GridColumn();
		column.setField(field);
		column.setTitle(title);
		return column;
	}
	
	/**
	 * 文本列
	 * @param field
	 * @param columnIndex
	 * @return
	 */
	public static GridColumn text(String field, String title, GridColumnValueGetter valueGetter) {
		GridColumn column = new GridColumn();
		column.setField(field);
		column.setTitle(title);
		column.setValueGetter(valueGetter);
		return column;
	}
	
	/**
	 * 文本列
	 * @param field
	 * @param columnIndex
	 * @return
	 */
	public static GridColumn fixValueText(String field, String title, Object fixValue) {
		GridColumn column = new GridColumn();
		column.setField(field);
		column.setTitle(title);
		column.setValueGetter(GridColumnValueGetter.buildFixValueReader(fixValue));
		return column;
	}
	
	/**
	 * 空的列
	 * @param field
	 * @param title
	 * @return
	 */
	public static GridColumn emptyColumn(String field, String title) {
		GridColumn column = new GridColumn();
		column.setField(field);
		column.setTitle(title);
		column.setRenderer(new EmptyColumnRenderer());
		return column;
	}
	
	/**
	 * 日期列
	 * @param field
	 * @param columnIndex
	 * @param dateFormat
	 * @return
	 */
	public static GridColumn date(String field, String title, String dateFormat) {
		GridColumn column = new GridColumn();
		column.setField(field);
		column.setTitle(title);
		column.setRenderer(new DateCellRenderer(dateFormat));
		return column;
	}
	
	/**
	 * 字典列
	 * @param field
	 * @param columnIndex
	 * @param dict
	 * @return
	 */
	public static GridColumn dict(String field, String title, String dict) {
		GridColumn column = new GridColumn();
		column.setField(field);
		column.setTitle(title);
		column.setRenderer(new DictCellRenderer(dict));
		return column;
	}
	
	/**
	 * 序号
	 * @return
	 */
	public static GridColumn rowNumber(String title) {
		GridColumn column = new GridColumn(); 
		column.setField("_rowNumber");
		column.setTitle(title);
		column.setRenderer(new RowNumberCellRenderer());
		return column;
	}
	
	/**
	 * 实体修改记录列
	 * @param field
	 * @param columnIndex
	 * @param entityClass
	 * @param auditFields
	 * @return
	 */
	public static <T> GridColumn entityRevision(String field, String title, Class<T> entityClass, List<EntityRevisionAuditField> auditFields) {
		GridColumn column = new GridColumn();
		column.setField(field);
		column.setTitle(title);
		column.setAuditedEntityIdField(field);
		column.setAuditedEntityClass(entityClass.getName());
		column.setRenderer(new EntityRevisionCellRenderer(field, entityClass.getName(), auditFields));
		return column;
	}
	
	/**
	 * 实体的上次修改日期
	 * @param title
	 * @param valueGetter
	 * @return
	 */
	public static GridColumn entityRevisionLastUpdateDate(String title, GridColumnEntityRevisionValueGetter valueGetter) {
		GridColumn column = new GridColumn();
		column.setField("_entityRevisionLastUpdateDate");
		column.setTitle(title);
		column.setValueGetter(valueGetter.buildEntityRevisionUpdateDateGetter());
		return column;
	}
	
	/**
	 * 实体的修改字段的日志，字段值的变更历史；
	 * @param title
	 * @param valueGetter
	 * @return
	 */
	public static GridColumn entityRevisionContent(String title, GridColumnEntityRevisionValueGetter valueGetter) {
		GridColumn column = new GridColumn();
		column.setField("_entityRevisionContent");
		column.setTitle(title);
		column.setValueGetter(valueGetter.buildEntityRevisionUpdateContentGetter());
		return column;
	}
	
	/**
	 * 实体的修改字段的修改次数；
	 * @param title
	 * @param valueGetter
	 * @return
	 */
	public static GridColumn entityRevisionTimes(String title, GridColumnEntityRevisionValueGetter valueGetter) {
		GridColumn column = new GridColumn();
		column.setField("_entityRevisionTimes");
		column.setTitle(title);
		column.setValueGetter(valueGetter.buildEntityRevisionUpdateTimesGetter());
		return column;
	}
	
	/**
	 * 
	 * @param title
	 * @param createdDate
	 * @return
	 */
	public static GridColumn adc(String title, Date createdDate) {
		GridColumn column = new GridColumn(); 
		column.setField("_adc");
		column.setTitle(title);
		column.setRenderer(new AdcCellRenderer(createdDate));
		return column;
	}
}

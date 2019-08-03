package com.ctop.fw.common.excelexport;

import java.lang.reflect.Field;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.springframework.util.ReflectionUtils;

/**
 * 显示A, D, 或 C
 * A: 新增
 * D: 删除
 * C: 修改
 * 新增的根据createdDate创建日期大于指定日期来断定；
 * 删除根据isActive
 * 修改根据version
 * @author 龚建军
 *
 */
public class AdcCellRenderer implements CellRenderer {
	
	private Date createdDate;
	private transient Field createdDateField;
	private transient Field isActiveField;
	private transient Field versionField;
	
	public AdcCellRenderer(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Override
	public void render(Cell cell, Object value, Object bean,GridColumn gridColumn) {
		if(bean == null) {
			return;
		}
		if(createdDateField == null) {
			createdDateField = ReflectionUtils.findField(bean.getClass(), "createdDate");
			createdDateField.setAccessible(true);
		}
		if(isActiveField == null) {
			isActiveField = ReflectionUtils.findField(bean.getClass(), "isActive");
			isActiveField.setAccessible(true);
		}
		if(versionField == null) {
			versionField = ReflectionUtils.findField(bean.getClass(), "version");
			versionField.setAccessible(true);
		}
		String isActive = (String) ReflectionUtils.getField(this.isActiveField, bean);
		if("N".equals(isActive)) {
			cell.setCellValue("D");
			return;
		}

		Number version = (Number) ReflectionUtils.getField(this.versionField, bean);
		if(version.doubleValue() > 0) {
			cell.setCellValue("C");
			return;
		}
		Date date = (Date) ReflectionUtils.getField(createdDateField, bean);
		if(date.after(createdDate)) {
			cell.setCellValue("A");
		}
	}

}

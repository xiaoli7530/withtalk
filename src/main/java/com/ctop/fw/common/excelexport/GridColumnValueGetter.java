package com.ctop.fw.common.excelexport;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Map;

import org.springframework.util.ReflectionUtils;

/**
 * 
 * @author 龚建军
 *
 */
public class GridColumnValueGetter implements Serializable {
	private static final long serialVersionUID = -4370101032217748951L;

	/**
	 * 返回一个固定值的columnReader;
	 * @param value
	 * @return
	 */
	public static GridColumnValueGetter buildFixValueReader(Object value) {
		return new GridColumnFixValueGetter(value);
	}
	
	private transient Field _field;

	public Object getColumnValue(GridColumn column, Object rowData) {
		String field = column.getField();
		if(rowData instanceof Map) {
			Map<?, ?> map = (Map<?, ?>) rowData;
			return map.get(field);
		} else if(rowData != null){
			if(_field == null) {
				_field = ReflectionUtils.findField(rowData.getClass(), field);
				if(_field != null) {
					_field.setAccessible(true);
				}
			}
			if(_field == null) {
				return null;
			}
			return ReflectionUtils.getField(_field, rowData);
		}
		return "";
	}
}

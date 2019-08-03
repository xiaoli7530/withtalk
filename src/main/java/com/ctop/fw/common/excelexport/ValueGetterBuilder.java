package com.ctop.fw.common.excelexport;

import java.util.function.Predicate;

public class ValueGetterBuilder {

	public static GridColumnValueGetter concat(GridColumnValueGetter getter1, GridColumnValueGetter getter2) {
		return new GridColumnValueGetter() {
			private static final long serialVersionUID = 4791907253493936229L;

			public Object getColumnValue(GridColumn column, Object rowData) {
				Object value1 = getter1.getColumnValue(column, rowData);
				Object value2 = getter2.getColumnValue(column, rowData);
				String text1 = value1 != null ? value1.toString() : "";
				String text2 = value2 != null ? value2.toString() : "";
				return text1 + text2;
			}
		};
	}
	
	public static GridColumnValueGetter matchCondition(GridColumnValueGetter condition, Predicate predicate, 
			GridColumnValueGetter trueValueGetter, 
			GridColumnValueGetter falseValueGetter) {
		return new GridColumnValueGetter() {
			private static final long serialVersionUID = 4791907253493936229L;

			public Object getColumnValue(GridColumn column, Object rowData) {
				Object value1 = condition.getColumnValue(column, rowData); 
				if (predicate != null && predicate.test(value1)) {
					return trueValueGetter.getColumnValue(column, rowData);
				} else {
					return falseValueGetter;
				}
			}
		};
	}
}

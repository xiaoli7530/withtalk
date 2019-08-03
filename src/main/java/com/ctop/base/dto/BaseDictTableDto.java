package com.ctop.base.dto;

import java.io.Serializable;import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ctop.fw.common.model.BaseDto;

/**
 * <pre>
 * 功能说明：${table.className}实体的数据传输对像
 * 创建者：${user}
 * 创建时间：${date}
 * 版本：${version}
 * </pre>
 */
public class BaseDictTableDto extends BaseDto implements Serializable {
	private static final long serialVersionUID = 7729507880920971714L;
	private String whereClause;
	private String orderClause;
	private String remark;
	private String bdtUuid;
	private String dictCode;
	private String sql;
	private String columnKey;
	private String columnDisplay;
	private String columnDisplayName;
	private String showColumn1;
	private String showColumn2;
	private String showColumn3;
	private String showColumn4;
	private String showColumn5;
	private String showColumn1Name;
	private String showColumn2Name;
	private String showColumn3Name;
	private String showColumn4Name;
	private String showColumn5Name;
	private String columnsJson;
	private String type;
	
	public String getWhereClause() {
		return whereClause;
	}

	public void setWhereClause(String whereClause) {
		this.whereClause = whereClause;
	}
	
	public String getOrderClause() {
		return orderClause;
	}

	public void setOrderClause(String orderClause) {
		this.orderClause = orderClause;
	}
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public String getBdtUuid() {
		return bdtUuid;
	}

	public void setBdtUuid(String bdtUuid) {
		this.bdtUuid = bdtUuid;
	}
	
	public String getDictCode() {
		return dictCode;
	}

	public void setDictCode(String dictCode) {
		this.dictCode = dictCode;
	}
	
	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}
	
	public String getColumnKey() {
		return columnKey;
	}

	public void setColumnKey(String columnKey) {
		this.columnKey = columnKey;
	}
	
	public String getColumnDisplay() {
		return columnDisplay;
	}

	public void setColumnDisplay(String columnDisplay) {
		this.columnDisplay = columnDisplay;
	}
	
	public String getColumnDisplayName() {
		return columnDisplayName;
	}

	public void setColumnDisplayName(String columnDisplayName) {
		this.columnDisplayName = columnDisplayName;
	}
	
	public String getShowColumn1() {
		return showColumn1;
	}

	public void setShowColumn1(String showColumn1) {
		this.showColumn1 = showColumn1;
	}
	
	public String getShowColumn2() {
		return showColumn2;
	}

	public void setShowColumn2(String showColumn2) {
		this.showColumn2 = showColumn2;
	}
	
	public String getShowColumn3() {
		return showColumn3;
	}

	public void setShowColumn3(String showColumn3) {
		this.showColumn3 = showColumn3;
	}
	
	public String getShowColumn4() {
		return showColumn4;
	}

	public void setShowColumn4(String showColumn4) {
		this.showColumn4 = showColumn4;
	}
	
	public String getShowColumn5() {
		return showColumn5;
	}

	public void setShowColumn5(String showColumn5) {
		this.showColumn5 = showColumn5;
	}
	
	public String getShowColumn1Name() {
		return showColumn1Name;
	}

	public void setShowColumn1Name(String showColumn1Name) {
		this.showColumn1Name = showColumn1Name;
	}
	
	public String getShowColumn2Name() {
		return showColumn2Name;
	}

	public void setShowColumn2Name(String showColumn2Name) {
		this.showColumn2Name = showColumn2Name;
	}
	
	public String getShowColumn3Name() {
		return showColumn3Name;
	}

	public void setShowColumn3Name(String showColumn3Name) {
		this.showColumn3Name = showColumn3Name;
	}
	
	public String getShowColumn4Name() {
		return showColumn4Name;
	}

	public void setShowColumn4Name(String showColumn4Name) {
		this.showColumn4Name = showColumn4Name;
	}
	
	public String getShowColumn5Name() {
		return showColumn5Name;
	}

	public void setShowColumn5Name(String showColumn5Name) {
		this.showColumn5Name = showColumn5Name;
	}

	public String getColumnsJson() {
		return columnsJson;
	}

	public void setColumnsJson(String columnsJson) {
		this.columnsJson = columnsJson;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}


package com.ctop.base.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.GenericGenerator;

import com.ctop.fw.common.entity.BaseEntity;


/**
 * <pre>
 * 功能说明：${table.className}实体类
 * 创建者：${user}
 * 创建时间：${date}
 * 版本：${version}
 * </pre>
 */
@SuppressWarnings("serial") 
@Entity
@Table(name = "BASE_DICT_TABLE")
@BatchSize(size = 20)
public class BaseDictTable extends BaseEntity implements Serializable {

	@Column(name = "WHERE_CLAUSE")
	private String whereClause;//附加的where条件,包含where关键字，定义变量格式：{aaa}

	@Column(name = "ORDER_CLAUSE")
	private String orderClause;//附加的排序条件，包含order关键字

	@Column(name = "REMARK")
	private String remark;//备注

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "BDT_UUID")
	private String bdtUuid;//UUID

	@Column(name = "DICT_CODE")
	private String dictCode;//字典代码

	@Column(name = "SQL")
	private String sql;//查询sql

	@Column(name = "COLUMN_KEY")
	private String columnKey;//字典code对应的字段名

	@Column(name = "COLUMN_DISPLAY")
	private String columnDisplay;//字典name对应的字段名

	@Column(name = "COLUMN_DISPLAY_NAME")
	private String columnDisplayName;//多列显示的时候表头

	@Column(name = "SHOW_COLUMN1")
	private String showColumn1;//多列显示的时候字段1

	@Column(name = "SHOW_COLUMN2")
	private String showColumn2;//多列显示的时候字段2

	@Column(name = "SHOW_COLUMN3")
	private String showColumn3;//多列显示的时候字段3

	@Column(name = "SHOW_COLUMN4")
	private String showColumn4;//多列显示的时候字段4

	@Column(name = "SHOW_COLUMN5")
	private String showColumn5;//多列显示的时候字段5

	@Column(name = "SHOW_COLUMN1_NAME")
	private String showColumn1Name;//多列显示的时候表头1

	@Column(name = "SHOW_COLUMN2_NAME")
	private String showColumn2Name;//多列显示的时候表头2

	@Column(name = "SHOW_COLUMN3_NAME")
	private String showColumn3Name;//多列显示的时候表头3

	@Column(name = "SHOW_COLUMN4_NAME")
	private String showColumn4Name;//多列显示的时候表头4

	@Column(name = "SHOW_COLUMN5_NAME")
	private String showColumn5Name;//多列显示的时候表头5
	
	@Column(name = "COLUMNS_JSON")
	private String columnsJson;
	@Column(name = "TYPE")
	private String type;
	
	public String buildFullSql() {
		StringBuilder sql = new StringBuilder();
		sql.append(this.sql != null ? this.sql : "").append(" ");
		sql.append(this.whereClause != null ? this.whereClause : "").append(" ");
		sql.append(this.orderClause != null ? this.orderClause : "");
		return sql.toString();
	}

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


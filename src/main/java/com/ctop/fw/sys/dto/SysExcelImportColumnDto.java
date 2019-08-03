package com.ctop.fw.sys.dto;

import java.util.Date;
import java.io.Serializable;import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;

import com.ctop.fw.common.model.BaseDto;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * <pre>
 * 功能说明：SysExcelImportColumn实体的数据传输对像
 * 创建者：${user}
 * 创建时间：${date}
 * 版本：${version}
 * </pre>
 */
public class SysExcelImportColumnDto extends BaseDto implements Serializable {
	private String columnUuid;
	private String importUuid;
	private String columnName;
	private String columnTitle;
	private String targetTable;
	private String targetColumn;
	private String targetType;
	private Integer targetLength;
	private Integer targetPrecision;
	private Integer targetScale;
	private String required;
	private String dictNo;
	private String vtype;
	private String dateFormat;
	private String allowMultiValue;
	private String multiValueSeparator;
	private String ignoreChars;
	private String ignoreRowIfEmpty;
	private String targetColumnPkInd;
	private String columnName1;
	private String columnName2;
	private String columnName3;
	private String columnName4;
	private String columnName5;
	private String uniqueInTarget;
	private String updateNullOnly;
	private String bizSeqCode;
	private String sqlValueExp;

	
	public String getColumnUuid() {
		return columnUuid;
	}

	public void setColumnUuid(String columnUuid) {
		this.columnUuid = columnUuid;
	}
	
	public String getImportUuid() {
		return importUuid;
	}

	public void setImportUuid(String importUuid) {
		this.importUuid = importUuid;
	}
	
	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	
	public String getColumnTitle() {
		return columnTitle;
	}

	public void setColumnTitle(String columnTitle) {
		this.columnTitle = columnTitle;
	}
	
	public String getTargetTable() {
		return targetTable;
	}

	public void setTargetTable(String targetTable) {
		this.targetTable = targetTable;
	}
	
	public String getTargetColumn() {
		return targetColumn;
	}

	public void setTargetColumn(String targetColumn) {
		this.targetColumn = targetColumn;
	}
	
	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}
	
	public Integer getTargetLength() {
		return targetLength;
	}

	public void setTargetLength(Integer targetLength) {
		this.targetLength = targetLength;
	}
	
	public Integer getTargetPrecision() {
		return targetPrecision;
	}

	public void setTargetPrecision(Integer targetPrecision) {
		this.targetPrecision = targetPrecision;
	}
	
	public Integer getTargetScale() {
		return targetScale;
	}

	public void setTargetScale(Integer targetScale) {
		this.targetScale = targetScale;
	}
	
	public String getRequired() {
		return required;
	}

	public void setRequired(String required) {
		this.required = required;
	}
	
	public String getDictNo() {
		return dictNo;
	}

	public void setDictNo(String dictNo) {
		this.dictNo = dictNo;
	}
	
	public String getVtype() {
		return vtype;
	}

	public void setVtype(String vtype) {
		this.vtype = vtype;
	}
	
	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
	
	public String getAllowMultiValue() {
		return allowMultiValue;
	}

	public void setAllowMultiValue(String allowMultiValue) {
		this.allowMultiValue = allowMultiValue;
	}
	
	public String getMultiValueSeparator() {
		return multiValueSeparator;
	}

	public void setMultiValueSeparator(String multiValueSeparator) {
		this.multiValueSeparator = multiValueSeparator;
	}
	
	public String getIgnoreChars() {
		return ignoreChars;
	}

	public void setIgnoreChars(String ignoreChars) {
		this.ignoreChars = ignoreChars;
	}
	
	public String getIgnoreRowIfEmpty() {
		return ignoreRowIfEmpty;
	}

	public void setIgnoreRowIfEmpty(String ignoreRowIfEmpty) {
		this.ignoreRowIfEmpty = ignoreRowIfEmpty;
	}
	
	public String getTargetColumnPkInd() {
		return targetColumnPkInd;
	}

	public void setTargetColumnPkInd(String targetColumnPkInd) {
		this.targetColumnPkInd = targetColumnPkInd;
	}
	
	public String getColumnName1() {
		return columnName1;
	}

	public void setColumnName1(String columnName1) {
		this.columnName1 = columnName1;
	}
	
	public String getColumnName2() {
		return columnName2;
	}

	public void setColumnName2(String columnName2) {
		this.columnName2 = columnName2;
	}
	
	public String getColumnName3() {
		return columnName3;
	}

	public void setColumnName3(String columnName3) {
		this.columnName3 = columnName3;
	}
	
	public String getColumnName4() {
		return columnName4;
	}

	public void setColumnName4(String columnName4) {
		this.columnName4 = columnName4;
	}
	
	public String getColumnName5() {
		return columnName5;
	}

	public void setColumnName5(String columnName5) {
		this.columnName5 = columnName5;
	}
	
	public String getUniqueInTarget() {
		return uniqueInTarget;
	}

	public void setUniqueInTarget(String uniqueInTarget) {
		this.uniqueInTarget = uniqueInTarget;
	}
	
	public String getUpdateNullOnly() {
		return updateNullOnly;
	}

	public void setUpdateNullOnly(String updateNullOnly) {
		this.updateNullOnly = updateNullOnly;
	}
	
	public String getBizSeqCode() {
		return bizSeqCode;
	}

	public void setBizSeqCode(String bizSeqCode) {
		this.bizSeqCode = bizSeqCode;
	}
	
	public String getSqlValueExp() {
		return sqlValueExp;
	}

	public void setSqlValueExp(String sqlValueExp) {
		this.sqlValueExp = sqlValueExp;
	}

}


package com.ctop.fw.sys.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.GenericGenerator;

import com.ctop.fw.common.entity.BaseEntity;
import com.ctop.fw.common.utils.StringUtil;
import com.ctop.fw.sys.excelImport.support.validate.DateValidator;
import com.ctop.fw.sys.excelImport.support.validate.VTypeValidator;
import com.fasterxml.jackson.annotation.JsonIgnore;


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
@Table(name = "SYS_EXCEL_IMPORT_COLUMN")
@BatchSize(size = 20)
public class SysExcelImportColumn extends BaseEntity implements Serializable {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "COLUMN_UUID")
	private String columnUuid;//主键

	@Column(name = "IMPORT_UUID", insertable=false, updatable=false)
	private String importUuid;//导入模板主键

	@Column(name = "COLUMN_NAME")
	private String columnName;//字段名称

	@Column(name = "TEMP_COLUMN")
	private String tempColumn;//字段名称(文本）

	@Column(name = "TARGET_TABLE")
	private String targetTable;//目标表

	@Column(name = "TARGET_COLUMN")
	private String targetColumn;//目标表字段

	@Column(name = "TARGET_TYPE")
	private String targetType;//目标表类型

	@Column(name = "TARGET_LENGTH")
	private Integer targetLength;//目标字段长度

	@Column(name = "TARGET_PRECISION")
	private Integer targetPrecision;//目标字段精度

	@Column(name = "TARGET_SCALE")
	private Integer targetScale;//浮点位数

	@Column(name = "REQUIRED")
	private String required;//是否必须

	@Column(name = "DICT_NO")
	private String dictNo;//数据字典编号

	@Column(name = "VTYPE")
	private String vtype;//校验类型

	@Column(name = "DATE_FORMAT")
	private String dateFormat;//日期格式

	@Column(name = "ALLOW_MULTI_VALUE")
	private String allowMultiValue;//excel的列中是否可以有多个值

	@Column(name = "MULTI_VALUE_SEPARATOR")
	private String multiValueSeparator;//有多个值时，用来分隔值的字符

	@Column(name = "IGNORE_CHARS")
	private String ignoreChars;//excel中忽略的值

	@Column(name = "IGNORE_ROW_IF_EMPTY")
	private String ignoreRowIfEmpty;//该字段为空，则表示为空记录，用来读取excel行时识别空记录

	@Column(name = "TARGET_COLUMN_PK_IND")
	private String targetColumnPkInd;//目标表列是否为主键，是的话

	@Column(name = "COLUMN_NAME1")
	private String columnName1;//冗余的名称，可以作多个名称匹配，只要一个匹配上就是匹配上了

	@Column(name = "COLUMN_NAME2")
	private String columnName2;//

	@Column(name = "COLUMN_NAME3")
	private String columnName3;//

	@Column(name = "COLUMN_NAME4")
	private String columnName4;//

	@Column(name = "COLUMN_NAME5")
	private String columnName5;//

	@Column(name = "UNIQUE_IN_TARGET")
	private String uniqueInTarget;//目标表字段是否有唯一限制，导入到临时表后可自动校验

	@Column(name = "UPDATE_NULL_ONLY")
	private String updateNullOnly;//目标表已存在记录，然后在更新的时候，根据是目标字段值是否为NULL来更新目标字段

	@Column(name = "BIZ_SEQ_CODE")
	private String bizSeqCode;//业务单据号生成规则编号
	
	@Column(name = "SQL_VALIDATE_EXP")
	private String sqlValidateExp;
	
	@Column(name = "SQL_VALIDATE_MESSAGE")
	private String sqlValidateMessage;

	@Column(name = "SQL_VALUE_EXP")
	private String sqlValueExp;//该字段的值是根据SQL计算出来的
//	
//	@ManyToOne(cascade=CascadeType.ALL)
//    @JoinColumn(name="IMPORT_UUID")
//    private SysExcelImport excelImport;
	
	
	@Transient
	@JsonIgnore
	private VTypeValidator validator;
	@Transient
	@JsonIgnore
	private DateValidator dateValidator;
	
//    //暂时不用了
	@Transient
	private String excelColumnIndex;
	@Transient
	private Integer excelColumnIndexInt;
//	
	public void generateIntColumnIndex () {
		try {
			//配置数字的方式
			this.excelColumnIndexInt = Integer.parseInt(this.excelColumnIndex);
		} catch(Exception ex) {
			//配置字母的方式
			this.excelColumnIndexInt = convertAlphaNum(this.excelColumnIndex) - 1;
		}
	}
	
	public String formateDate(Date date, String format) {
		if (this.getDateFormat() != null) {
			format = this.getDateFormat();
		}
		format = format.replace("HH24", "HH");
		format = format.replace("mi", "mm");
		return new SimpleDateFormat(format).format(date);
	}
	
	private int convertAlphaNum(String alphaNum) {
		int base = (int) ('Z' - 'A') + 1;
		int total = 0;
		for(int i=0; i < alphaNum.length(); i++) {
			char c = alphaNum.charAt(i);
			int num = (int) (c - 'A') + 1;
			total = total * base + num;
		}
		return total;
	}
	
	public boolean isDateTargetType() {
		return "date".equalsIgnoreCase(this.targetType) || (StringUtil.isEmpty(this.targetType) && StringUtil.isNotEmpty(this.dateFormat));
	}
	
	public boolean isRequiredBool() {
		return "Y".equalsIgnoreCase(this.required);
	}
	
	public boolean isNumber() {
		return "NUMBER".equalsIgnoreCase(this.targetType);
	}
	
	//判断是否为忽略字符
	public boolean isInIgnoreChars(String str) {
		if(this.ignoreChars == null || str == null || str.length() > 1) {
			return false;
		}
		return this.ignoreChars.indexOf(str) != -1;
	}
	

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
	
	public String getTempColumn() {
		return tempColumn;
	}

	public void setTempColumn(String tempColumn) {
		this.tempColumn = tempColumn;
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

	public VTypeValidator getValidator() {
		return validator;
	}

	public void setValidator(VTypeValidator validator) {
		this.validator = validator;
	}

	public String getExcelColumnIndex() {
		return excelColumnIndex;
	}

	public void setExcelColumnIndex(String excelColumnIndex) {
		this.excelColumnIndex = excelColumnIndex;
	}

	public Integer getExcelColumnIndexInt() {
		return excelColumnIndexInt;
	}

	public void setExcelColumnIndexInt(Integer excelColumnIndexInt) {
		this.excelColumnIndexInt = excelColumnIndexInt;
	}

	public String getSqlValidateExp() {
		return sqlValidateExp;
	}

	public void setSqlValidateExp(String sqlValidateExp) {
		this.sqlValidateExp = sqlValidateExp;
	}

	public String getSqlValidateMessage() {
		return sqlValidateMessage;
	}

	public void setSqlValidateMessage(String sqlValidateMessage) {
		this.sqlValidateMessage = sqlValidateMessage;
	}

	public DateValidator getDateValidator() {
		return dateValidator;
	}

	public void setDateValidator(DateValidator dateValidator) {
		this.dateValidator = dateValidator;
	}
 
}


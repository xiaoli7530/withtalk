package com.ctop.fw.sys.entity;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Types;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.sql.DataSource;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.GenericGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.BatchSqlUpdate;
import org.springframework.util.ReflectionUtils;

import com.ctop.core.pdf.parser.TableColumn;
import com.ctop.fw.common.entity.BaseEntity;
import com.ctop.fw.common.utils.BusinessException;
import com.ctop.fw.common.utils.ListUtil;
import com.ctop.fw.common.utils.StringUtil;
import com.ctop.fw.sys.excelImport.support.ExcelImportRow;
import com.ctop.fw.sys.excelImport.support.validate.AndValidator;
import com.ctop.fw.sys.excelImport.support.validate.DateValidator;
import com.ctop.fw.sys.excelImport.support.validate.DictValidator;
import com.ctop.fw.sys.excelImport.support.validate.RequiredValidator;
import com.ctop.fw.sys.excelImport.support.validate.VTypeValidator;
import com.ctop.fw.sys.excelImport.support.validate.VTypeValidatorBuilder;
import com.ctop.fw.sys.excelImport.support.validate.ValidateResult;

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
@Table(name = "SYS_EXCEL_IMPORT")
@BatchSize(size = 20)
public class SysExcelImport extends BaseEntity implements Serializable {
	private static Logger logger = LoggerFactory.getLogger(SysExcelImport.class);
	private static String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static String IGNORE_ROW_MODE_BOTH = "BOTH";
	public static String IGNORE_ROW_MODE_EITHER = "EITHER";

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "IMPORT_UUID")
	private String importUuid;// 主键

	@Column(name = "IMPORT_CODE")
	private String importCode;// 导入代码

	@Column(name = "IMPORT_NAME")
	private String importName;// 导入名称

	@Column(name = "START_ROW_INDEX")
	private Long startRowIndex;// excel中的数据起始行

	@Column(name = "TARGET_TABLE")
	private String targetTable;// 导入的目标表

	@Column(name = "TEMP_TABLE")
	private String tempTable;// 导入时用到的临时表

	@Lob
	@Column(name = "PROCESS_SCRIPT")
	private String processScript;// 导入临时表后处理数据的存储过程

	@Lob
	@Column(name = "TRANSFER_SCRIPT")
	private String transferScript;// 转移数据的存储过程

	@Column(name = "PAGE_SIZE")
	private Long pageSize;// 分页保存excel到临时表时用的页大小

	@Column(name = "NAME_ROW_INDEX")
	private Long nameRowIndex;// excel中的标题行

	@Column(name = "IMPORT_TYPE")
	private String importType;// 导入的类型（同类型下的模板要不都有ref_type,ref_uuid,
								// 要不都没有，没有时只能有一个模板，有ref_uuid，导入时必须选择ref_uuid再导入）

	@Column(name = "IGNORE_ROW_MODE")
	private String ignoreRowMode;// 忽略行的模式，跟IGNORE_ROW_IF_EMPTY配合用，有两个模式BOTH
									// 所有字段为空才忽略, EITHER有一个就忽略

	@Column(name = "SEQ")
	private Long seq;// 序号

	@Column(name = "TPL_URL")
	private String tplUrl;// 模板路径

	@Column(name = "MODULE")
	private String module;// 导入模板的模块

	@Column(name = "UPDATABLE")
	private String updatable;// 该导入配置是否支持更新数据

	@Column(name = "UPDATABLE_DEFAULT")
	private String updatableDefault;// 默认 是否更新已存在数据

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "IMPORT_UUID")
	private List<SysExcelImportColumn> columns;

	@Transient
	private DateFormat dateFormat;
	//
	// /**配置中，有的临时表中列的值跟其他临时表其他列是一样的，解决同一个excel列到多个临时表列，当excel中这个列的值要切分后，切分后的值也要放到多个列中
	// * 这里处理配置的列数据中是否有列值引用其他列值的情况
	// * */
	// private void processReferingBetweenColumns() {
	// List<Integer> referingIndexes = new ArrayList<Integer>();
	// List<String> referedNames = new ArrayList<String>();
	//// for(int i=columns.size() - 1; i>=0; i--) {
	//// SysExcelImportColumn column = this.columns.get(i);
	//// if(!StringUtil.isEmpty(column.getRefedTempColumn())) {
	//// referingIndexes.add(i);
	//// referedNames.add(column.getRefedTempColumn());
	//// }
	//// }
	// this.referedColumnIndexes = new Integer[referedNames.size()];
	// for(int i=columns.size() - 1; i>=0; i--) {
	// SysExcelImportColumn column = this.columns.get(i);
	// int index = referedNames.indexOf(column.getTempColumn());
	// if(index != -1) {
	// referedColumnIndexes[index] = i;
	// }
	// }
	// this.referingColumnIndexes = new Integer[referingIndexes.size()];
	// this.referingColumnIndexes =
	// referingIndexes.toArray(this.referingColumnIndexes);
	// }
	//
	// /**处理临时表中的列引用到别的列的值的情况，复制其他列值到引用列中*/
	// private void updateReferingColumnValueFromRefered(ExcelImportRow row) {
	// for(int i=0; i<this.referingColumnIndexes.length; i++) {
	// Integer referingIndex = this.referingColumnIndexes[i];
	// Integer referedIndex = this.referedColumnIndexes[i];
	// if(referingIndex == null) {
	// continue;
	// }
	//
	// if(referedIndex == null) {
	// SysExcelImportColumn referingColumn = this.columns.get(referingIndex);
	// throw BusinessException.template("列" + referingColumn.getTempColumn() +
	// "的值来源列列" + referingColumn.getRefedTempColumn() + "不存在.");
	// }
	// row.getFields()[referingIndex] = row.getFields()[referedIndex];
	// }
	// }

	public void generateIntColumnIndex() {
		for (SysExcelImportColumn col : columns) {
			col.generateIntColumnIndex();
		}
	}

	public SysExcelImportColumn markColumnIndexByColumnName(String columnName, int colIndex) {
		SysExcelImportColumn matched = null;
		for (SysExcelImportColumn column : columns) {
			if (strEquals(column.getColumnName(), columnName) || strEquals(column.getColumnName1(), columnName)
					|| strEquals(column.getColumnName2(), columnName) || strEquals(column.getColumnName3(), columnName)
					|| strEquals(column.getColumnName4(), columnName)
					|| strEquals(column.getColumnName5(), columnName)) {
				matched = column;
				matched.setExcelColumnIndexInt(colIndex);
			}
		}
		return matched;
	}
	
	public SysExcelImportColumn markColumnIndexByColumnName(TableColumn tc, int colIndex) {
		SysExcelImportColumn matched = null;
		for (SysExcelImportColumn column : columns) {
			String propertyName = StringUtil.convertUnderscoreNameToPropertyName(column.getTempColumn());
			if(strEquals(column.getTempColumn(), tc.getField())
					|| strEquals(propertyName, tc.getField())) {
				matched = column;
				matched.setExcelColumnIndexInt(colIndex);
			}
		}
		return matched;
	}

	private boolean strEquals(String str1, String str2) {
		str1 = str1 != null ? str1.trim() : null;
		str2 = str2 != null ? str2.trim() : null;
		return str1 != null && str1.equalsIgnoreCase(str2);
	}

	/** 生成校验字段值的校验器 */
	public void buildColumnValidator() {
		VTypeValidatorBuilder builder = VTypeValidatorBuilder.INSTANCE;
		for (SysExcelImportColumn column : columns) {
			List<VTypeValidator> validators = new ArrayList<VTypeValidator>();
			if (!StringUtil.isEmpty(column.getVtype())) {
				VTypeValidator validator = builder.buidlValidator(column.getVtype());
				validators.add(validator);
			}
			if (column.isRequiredBool()) {
				validators.add(RequiredValidator.INSTANCE);
			}
			if (column.isNumber()) {
				StringBuilder buff = new StringBuilder();
				buff.append(column.getTargetPrecision()).append(",").append(column.getTargetScale());
				validators.add(builder.buildPrecisionScaleValidator(buff.toString()));
			}
			if (column.isDateTargetType()) {
				String format = DEFAULT_DATE_FORMAT;
				if (column.getDateFormat() != null) {
					format = column.getDateFormat();
					format = format.replace("HH24", "HH");
					format = format.replace("mi", "mm");
				}
				DateValidator dateValidator  = new DateValidator(format);
				column.setDateValidator(dateValidator);
			}
			if (!StringUtil.isEmpty(column.getDictNo())) {
				validators.add(builder.buildDictValidator(column.getDictNo()));
			} else if (column.getTargetLength() != null && !column.isDateTargetType()) {
				// 有字典校验的不要校验字段长度；
				validators.add(builder.buildLengthValidator(column.getTargetLength().toString()));
			}
			VTypeValidator[] validatorArr = new VTypeValidator[validators.size()];
			validators.toArray(validatorArr);
			VTypeValidator validator = new AndValidator(validatorArr);
			column.setValidator(validator);
		}
	}

	/** 是否需要扩展行, 只能有一个列允许多值 */
	public boolean isExpandRowRequired() {
		if (this.columns != null) {
			for (SysExcelImportColumn column : this.columns) {
				if ("Y".equalsIgnoreCase(column.getAllowMultiValue())
						&& !StringUtil.isEmpty(column.getMultiValueSeparator())) {
					return true;
				}
			}
		}
		return false;
	}

	/** 是否需要校验空白行 */
	public boolean isCheckIgnoreRowRequired() {
		if (this.columns != null) {
			for (SysExcelImportColumn column : columns) {
				if ("Y".equalsIgnoreCase(column.getIgnoreRowIfEmpty())) {
					return true;
				}
			}
		}
		return false;
	}

	/** 扩展行，一行到多行, 只能有一个列允许多值 */
	public List<ExcelImportRow> expandMultiRow(ExcelImportRow row) {
		List<ExcelImportRow> rows = new ArrayList<ExcelImportRow>(2);
		int i = 0;
		int pkColumnIndex = -1;
		for (SysExcelImportColumn column : this.columns) {
			pkColumnIndex++;
			if ("Y".equalsIgnoreCase(column.getTargetColumnPkInd())) {
				break;
			}
		}
		for (SysExcelImportColumn column : this.columns) {
			if ("Y".equalsIgnoreCase(column.getAllowMultiValue())
					&& !StringUtil.isEmpty(column.getMultiValueSeparator())
					&& !StringUtil.isEmpty(row.getFields()[i])) {
				String[] arr = row.getFields()[i].split(column.getMultiValueSeparator());
				if (arr.length > 1) {
					for (String value : arr) {
						if (!StringUtil.isEmpty(value)) {
							ExcelImportRow copy = row.getCopy();
							copy.getFields()[i] = value;
							rows.add(copy);
							// 扩展行记录时，主键生成新的
							if (pkColumnIndex >= 0 && pkColumnIndex < this.columns.size()) {
								copy.getFields()[pkColumnIndex] = (UUID.randomUUID().toString()).replace("-", "");
							}
						}
					}
				}
				break;
			}
			i++;
		}
		return rows;
	}

	public ExcelImportRow readExcelRow(Row row) {
		ExcelImportRow rowData = new ExcelImportRow();
		rowData.setRowNum(row.getRowNum());
		String[] values = new String[columns.size()];
		int index = -1;
		StringBuilder builder = new StringBuilder();
		String text = null;
		for (SysExcelImportColumn column : columns) {
			index++;
			// 根据计算出来的列号来取值
			if (column.getExcelColumnIndexInt() != null) {
				Cell cell = row.getCell(column.getExcelColumnIndexInt());
				if (cell != null) {
					int cellType = cell.getCellType();
					if (cellType == Cell.CELL_TYPE_ERROR) {
						rowData.appendMessage("单元格" + column.getExcelColumnIndex() + "读取失败。");
					} else {
						text = readCellValue(column, cell, cellType);
						if (text != null) {
							// 前后去空格(空字符串)
							int from = 0, to = -1, length = text.length();
							for (int i = 0; i < length; i++) {
								if (Character.isWhitespace(text.charAt(i))) {
									if (from < to) {
										builder.append(text.substring(from, i));
									}
									from = i;
								} else {
									to = i;
								}
							}
							if (from <= to) {
								builder.append(text.substring(from, to + 1));
							}
							values[index] = builder.toString().trim();
							builder.delete(0, builder.length());
							// values[index] = values[index].replace("（", "(");
							// values[index] = values[index].replace("）", ")");
							// values[index] = values[index].replace("：", ":");
						}
						if (column.isInIgnoreChars(values[index])) {
							values[index] = null;
						}
					}
				}
			} else if ("Y".equalsIgnoreCase(column.getTargetColumnPkInd())) {
				// 配置是目标表的主键的，自动生成主键
				values[index] = (UUID.randomUUID().toString()).replace("-", "");
				;
			}
		}
		rowData.setFields(values);
		return rowData;
	}

	/** 根据列配置是否应该忽略本行 */
	public boolean shouldIgnoreRowData(ExcelImportRow rowData) {
		if (StringUtil.isEmpty(ignoreRowMode)) {
			throw BusinessException.template("请指定忽略行记录模式。");
		}
		boolean isBothMode = IGNORE_ROW_MODE_BOTH.equals(this.ignoreRowMode);
		boolean ignoreRow = isBothMode;
		boolean hasIgnoreIndCol = false;
		int index = 0;
		for (SysExcelImportColumn column : columns) {
			if ("Y".equalsIgnoreCase(column.getIgnoreRowIfEmpty())) {
				hasIgnoreIndCol = true;
				if (isBothMode) {
					// BOTH MODE
					ignoreRow = ignoreRow && StringUtil.isEmpty(rowData.getFields()[index]);
				} else {
					// EITHER MODE
					if (StringUtil.isEmpty(rowData.getFields()[index])) {
						return true;
					}
				}
			}
			index++;
		}
		return hasIgnoreIndCol && ignoreRow;
	}

	private String readCellValue(SysExcelImportColumn column, Cell cell, int cellType) {
		switch (cellType) {
		case Cell.CELL_TYPE_BLANK:
			return null;
		case Cell.CELL_TYPE_NUMERIC:
			if (column.isDateTargetType()) {
				// 针对日期特殊处理
				Date date = cell.getDateCellValue();
				if (date != null) {
					return column.formateDate(date, DEFAULT_DATE_FORMAT);
				}
				return null;
			} else {
				double num = cell.getNumericCellValue();
				long numL = (long) num;
				return numL == num ? String.valueOf(numL) : String.valueOf(num);
			}
		case Cell.CELL_TYPE_STRING:
			return cell.getStringCellValue();
		case Cell.CELL_TYPE_FORMULA:
			int fCellType = cell.getCachedFormulaResultType();
			return readCellValue(column, cell, fCellType);
		case Cell.CELL_TYPE_BOOLEAN:
			return cell.getBooleanCellValue() ? "Y" : "N";
		}
		return null;
	}

	public String buildCreateTempTableSql() {
		StringBuilder sql = new StringBuilder();
		sql.append("create table ").append(this.tempTable)
				.append("( IMPORT_INSTANCE_UUID varchar2(32), import_row_num number(10),  import_message varchar2(4000), ")
				.append(" exist_in_target char(1) ");
		if (this.columns != null) {
			for (SysExcelImportColumn column : this.columns) {
				sql.append(",").append(column.getTempColumn()).append(" varchar2(4000)");
			}
		}
		sql.append(")");
		return sql.toString();
	}

	public String buildSelectTempTableSql() {
		List<String> columnNames = ListUtil.getOneFieldValue(this.columns, "tempColumn", String.class);
		StringBuilder sql = new StringBuilder()
				.append("select import_instance_uuid, import_row_num, import_message, exist_in_target,")
				.append(ListUtil.join(columnNames, ",")).append(" from ").append(this.getTempTable())
				.append(" t where t.import_instance_uuid = :instanceUuid ");
		return sql.toString();
	}

	public String buildInsertSql() {
		StringBuilder sql = new StringBuilder();
		List<String> list = new ArrayList<String>();
		list.add("import_instance_uuid");
		list.add("import_row_num");
		list.add("import_message");
		list.addAll(ListUtil.getOneFieldValue(this.columns, "tempColumn", String.class));
		sql.append("insert into ").append(this.tempTable).append("(");
		sql.append(ListUtil.join(list, ",")).append(")");
		sql.append("values(");
		for (int i = 0; i < list.size(); i++) {
			if (i != 0) {
				sql.append(",");
			}
			sql.append("?");
		}
		sql.append(")");
		return sql.toString();
	}

	public BatchSqlUpdate buildBatchSql4Insert(DataSource ds) {
		BatchSqlUpdate sql = new BatchSqlUpdate(ds, this.buildInsertSql());
		sql.declareParameter(new SqlParameter(Types.VARCHAR));
		sql.declareParameter(new SqlParameter(Types.NUMERIC));
		sql.declareParameter(new SqlParameter(Types.VARCHAR));
		for (int i = 0; i < this.columns.size(); i++) {
			sql.declareParameter(new SqlParameter(Types.VARCHAR));
		}
		return sql;
	}

	public String buildBaseProcessSqlBlock() {
		if (this.columns != null) {
			String tempSql = new StringBuilder()
					.append(this.buildValidateBySqlValidateExpFieldSql())
					.append(this.buildCheckExistSql())
					.append(this.buildPopulateSqlExpValueFieldSql())
					.toString();
			if (StringUtil.isEmpty(tempSql)) {
				return "";
			}
			String sql = new StringBuilder().append("declare begin ").append(tempSql).append("end;").toString();

			logger.debug("生成默认的处理中间表数据SQL块：\n {}", sql);
			return sql;
		}
		return "";
	}
	
	
	public String buildBeforeTransferSqlBlock() {
		if (this.columns != null) {
			String tempSql = new StringBuilder()
					.append(this.buildPopulateSqlExpValueFieldSql())
					.toString();
			if (StringUtil.isEmpty(tempSql)) {
				return "";
			}
			String sql = new StringBuilder().append("declare begin ").append(tempSql).append("end;").toString();
			logger.debug("生成移动正式表数据的SQL BLOCK： 1.计算SQL_VALUE_EXP：\n {}", sql);
			return sql;
		}
		return "";
	}
	

	public String buildDefaultTransferSqlBlock(SysExcelImportInstance instance) {
		if (this.columns != null) {
			Set<String> targetTables = this.columns.stream().filter(c -> !StringUtil.isEmpty(c.getTargetTable()))
					.map(c -> c.getTargetTable()).collect(Collectors.toSet());
			StringBuilder sql = new StringBuilder().append("declare begin \n");
			targetTables.stream().forEach(tt -> {
				// 新数据插入目标表
				sql.append(this.buildInsertIntoTargetSql(tt));
				// 更新已有数据
				if ("Y".equals(this.updatable) && "Y".equals(instance.getUpdateExist())) {
					sql.append(this.buildUpdateTargetSql(tt));
				}
			});
			// 导入成功后删除数据
			sql.append("\ndelete from ").append(this.tempTable).append(" where import_instance_uuid = :instanceUuid; ");
			sql.append("end;");
			String sqlText = sql.toString();
			logger.debug("生成默认的转移中间表数据SQL块：\n {}", sqlText);
			return sqlText;
		}
		return "";
	}
	
	/** 生成校验临时表中的SQL_Validate_EXP的SQL 
	 * update temp_table t set t.import_message = nvl(t.import_message, '') + ''
	 * where t.import_instance_uuid = :instanceUuid 
	 *   and exists (sqlValidateExp)
	 * */
	private String buildValidateBySqlValidateExpFieldSql() {
		if (this.columns != null) {
			return this.columns.stream().filter(c -> !StringUtil.isEmpty(c.getSqlValidateExp()))
					.map(c -> {
						String validateMessage = StringUtil.isNotEmpty(c.getSqlValidateMessage()) ? c.getColumnName() + "校验无效；" : c.getSqlValidateMessage();
						return new StringBuilder()
								.append("update ")
								.append(this.tempTable).append(" t set ")
								.append("t.import_message = nvl(t.import_message, '') || '")
								.append(validateMessage)
								.append("' where t.import_instance_uuid = :instanceUuid and ")
								.append(c.getSqlValidateExp())
								.append(";")
								.toString();
						//return c.getTempColumn().concat("=(").concat(c.getSqlValueExp()).concat(")");
					})
					.collect(Collectors.joining(";"));
		}
		return "";
	}


	/** 生成计算临时表中的SQL_VALUE_EXP的SQL */
	private String buildPopulateSqlExpValueFieldSql() {
		if (this.columns != null) {
			String updateFieldsSql = this.columns.stream().filter(c -> !StringUtil.isEmpty(c.getSqlValueExp()))
					.map(c -> c.getTempColumn().concat("=(").concat(c.getSqlValueExp()).concat(")"))
					.collect(Collectors.joining(","));
			if (!StringUtil.isEmpty(updateFieldsSql)) {
				return new StringBuilder().append("update ").append(this.tempTable).append(" t set ")
						.append(updateFieldsSql)
						.append(" where import_instance_uuid = :instanceUuid and import_message is null;\n")
						.toString();
			}
		}
		return "";
	}

	/**
	 * 根据唯一字段配置与正式表匹配，得到哪些记录是更新记录， update temp_base_dict t set t.exist_in_target
	 * = 'Y', t.dict_uuid = (select f.dict_uuid from base_dict f where
	 * f.dict_uuid = t.dict_uuid) where exists (select 1 from base_dict f where
	 * f.dict_uuid = t.dict_uuid and f.is_active='Y')
	 * 
	 * @return
	 */
	private String buildCheckExistSql() {
		if (this.columns != null) {
			Optional<SysExcelImportColumn> pkColumn = this.columns.stream()
					.filter(c -> "Y".equalsIgnoreCase(c.getTargetColumnPkInd())).findFirst();
			Optional<SysExcelImportColumn> ukColumn = this.columns.stream()
					.filter(c -> "Y".equalsIgnoreCase(c.getUniqueInTarget())).findFirst();
			if (pkColumn.isPresent() && ukColumn.isPresent()) {
				String matchConditions = this.columns.stream().filter(c -> "Y".equalsIgnoreCase(c.getUniqueInTarget()))
						.map(c -> "f.".concat(c.getTargetColumn()).concat("=t.").concat(c.getTempColumn()))
						.collect(Collectors.joining(" and "));
				return new StringBuilder()
						.append("\nupdate temp_base_dict t set t.exist_in_target = 'Y', t.dict_uuid = ")
						.append("(select f.dict_uuid from base_dict f where ").append(matchConditions).append(" ) ")
						.append("\nwhere exists (select 1 from base_dict f where ").append(matchConditions)
						.append(" ) ").append("\nand t.import_instance_uuid = :instanceUuid;\n").toString();
			}
		}
		return "";
	}

	/**
	 * insert into targetTable(field1, field2....) select value1, value2 from
	 * tempTable where import_instance_uuid = :instanceUuid
	 * 
	 * @param tt
	 * @return
	 */
	private String buildInsertIntoTargetSql(String tt) {
		List<String> targetColumns = new ArrayList<String>();
		List<String> tempColumns = new ArrayList<String>();
		this.columns.stream().filter(c -> tt.equals(c.getTargetTable()) && !StringUtil.isEmpty(c.getTargetColumn()))
				.forEach(c -> {
					targetColumns.add(c.getTargetColumn());
//			          when c.dict_no is not null then 'excel_import_util.getDictIdByName(''' || c.dict_no || ''', ' || c.temp_column || ')' 
//			          when c.target_type in ('NUMBER') then 'to_number(' || c.temp_column || ')'
//			          when c.target_type in ('CHAR', 'VARCHAR2') then c.temp_column
//			          when c.target_type in ('DATE') then 'to_date(' || c.temp_column || ',''' || nvl(c.date_format, 'yyyy-MM-dd HH24:mi:ss') || ''')' 
//			          else c.temp_column
					String tempColumnValue = c.getTempColumn();
					switch(c.getTargetType()) {
					case "NUMBER" : 
						tempColumnValue = "to_number(" + c.getTempColumn() + ") ";
						break;
					case "DATE":
						String format =  StringUtil.isEmpty(c.getDateFormat()) ? "yyyy-MM-dd HH24:mi:ss" : c.getDateFormat();
						tempColumnValue = "to_date(" + c.getTempColumn() + ", '" + format + "') ";
						break;
					}
					tempColumns.add(tempColumnValue);
				});
		return new StringBuilder().append("\ninsert into ").append(this.targetTable).append("\n(")
				.append(ListUtil.join(targetColumns, ",")).append(")\n").append("select ")
				.append(ListUtil.join(tempColumns, ",")).append("\n from ").append(this.tempTable)
				.append("\n where import_instance_uuid = :instanceUuid and nvl(exist_in_target, 'N') = 'N'; \n")
				.toString();
	}

	/**
	 * merge into targetTable f using ( select t.* from tempTable t where
	 * t.import_instance_uuid = :instanceUuid and t.pkColumn is not null ) t on
	 * (f.pkColumn = t.pkColumn) when matched then update set f.field1 =
	 * t.tField1, f.field2 = nvl(f.field2, t.tfield2)
	 * 
	 * @param tt
	 * @return
	 */
	private String buildUpdateTargetSql(String tt) {
		Optional<SysExcelImportColumn> pkColumn = this.columns.stream()
				.filter(c -> tt.equals(c.getTargetTable()) && "Y".equalsIgnoreCase(c.getTargetColumnPkInd()))
				.findFirst();
		if (!pkColumn.isPresent()) {
			return "";
		}
		String updateFields = this.columns.stream()
				.filter(c -> tt.equals(c.getTargetTable()) && !StringUtil.isEmpty(c.getTargetColumn())
						&& !"Y".equals(c.getTargetColumnPkInd()))
				.map(c -> new StringBuilder("f.").append(c.getTargetColumn()).append("=")
						.append("Y".equals(c.getUpdateNullOnly()) ? "nvl(f." + c.getTargetColumn() : "").append("t.")
						.append(c.getTempColumn()).append("Y".equals(c.getUpdateNullOnly()) ? ")" : ""))
				.collect(Collectors.joining(","));
		return new StringBuilder().append("\nmerge into ").append(this.targetTable).append(" f using (")
				.append("\nselect * from ").append(this.tempTable)
				.append(" t where t.import_instance_uuid = :instanceUuid and nvl(t.exist_in_target, 'N') = 'Y' and t.")
				.append(pkColumn.get().getTempColumn()).append(" is not null ").append("\n) t on (f.")
				.append(pkColumn.get().getTargetColumn()).append("=t.").append(pkColumn.get().getTempColumn())
				.append(") ").append("\n when matched then update set  ").append(updateFields).append(";\n").toString();
	}

	public String buildUpdateBaseEntityFieldsSql() {
		List<String> fields = Arrays.asList("CREATED_BY", "CREATED_DATE", "UPDATED_BY", "UPDATED_DATE", "IS_ACTIVE",
				"VERSION");
		if (this.columns != null) {
			return new StringBuilder("update ").append(this.tempTable).append(" set ")
					.append(this.columns.stream().filter(c -> fields.contains(c.getTempColumn()))
							.map(c -> c.getTempColumn() + "=:" + this.camelCase(c.getTempColumn()))
							.collect(Collectors.joining(",")))
					.append(" where import_instance_uuid = :instanceUuid").toString();

		}
		return "";
	}

	private String camelCase(String name) {
		String[] arr = name.toLowerCase().split("_");
		StringBuilder text = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			if (i == 0) {
				text.append(arr[i]);
			} else if (arr[i].length() > 0) {
				text.append(Character.toUpperCase(arr[i].charAt(0)));
				text.append(arr[i].substring(1));
			}
		}
		return text.toString();
	}

	public boolean validateRowData(ExcelImportRow row) {
		int i = 0;
		boolean valid = true;
		StringBuilder buff = new StringBuilder();
		for (SysExcelImportColumn column : this.columns) {
			VTypeValidator validator = column.getValidator();
			String value = row.getFields()[i];
			ValidateResult result = validator.validate(value);
			if (column.getDateValidator() != null && result.isValid()) {
				result  = column.getDateValidator().validate(value);
			}
			if (!result.isValid()) {
				buff.append("[").append(column.getColumnName());
				// if(column.getTargetColumn() != null) {
				// buff.append(",").append(column.getTargetColumn());
				// }
				buff.append(": ").append(result.getMessage()).append("] ");
			} else {
				//是数据字典单
				if(StringUtil.isNotEmpty(column.getDictNo())) {
					DictValidator dictValidator = VTypeValidatorBuilder.INSTANCE.buildDictValidator(column.getDictNo());
					row.getFields()[i] = dictValidator.getCode(value);
				} else if (column.getDateValidator() != null) {
					// 如果是日期的话，转换成指定的格式文本；
					row.getFields()[i] = result.getMessage();
				}
			}
			i++;
			valid = valid && result.isValid();
		}
		row.appendMessage(buff.toString());
		return valid;
	}

	public SysExcelImportColumn getColumnByTempColumn(String tempColumn) {
		for (SysExcelImportColumn column : this.columns) {
			if (column.getTempColumn().equalsIgnoreCase(tempColumn)) {
				return column;
			}
		}
		return null;
	}

	public SysExcelImportColumn addExcelImportColumn(String tempColumn, String targetTable, String targetColumn,
			String targetType, Integer targetLength) {
		SysExcelImportColumn column = new SysExcelImportColumn();
		column.setTempColumn(tempColumn);
		column.setTargetColumn(targetColumn);
		column.setTargetTable(targetTable);
		column.setTargetColumn(targetColumn);
		column.setTargetType(targetType);
		column.setTargetLength(targetLength);
		if (this.columns == null) {
			this.columns = new ArrayList<SysExcelImportColumn>();
		}
		this.columns.add(column);
		return column;
	}

	public void updateColumnField(String tempColumn, String fieldName, String value) {
		if (this.columns == null) {
			this.columns = new ArrayList<SysExcelImportColumn>();
		}
		Optional<SysExcelImportColumn> op = this.columns.stream().filter(c -> tempColumn.equals(c.getTempColumn()))
				.findFirst();
		if (op.isPresent()) {
			SysExcelImportColumn column = op.get();
			String name = StringUtil.convertUnderscoreNameToPropertyName(fieldName);
			Field field = ReflectionUtils.findField(SysExcelImportColumn.class, name);
			field.setAccessible(true);
			try {
				field.set(column, value);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				throw new BusinessException(e, "Excel模版字段{0}无法设值!", new Object[]{fieldName});
			}
		}
	}

	public Long getStartRowIndex() {
		return startRowIndex;
	}

	public void setStartRowIndex(Long startRowIndex) {
		this.startRowIndex = startRowIndex;
	}

	public String getTargetTable() {
		return targetTable;
	}

	public void setTargetTable(String targetTable) {
		this.targetTable = targetTable;
	}

	public String getTempTable() {
		return tempTable;
	}

	public void setTempTable(String tempTable) {
		this.tempTable = tempTable;
	}

	public String getProcessScript() {
		return processScript;
	}

	public void setProcessScript(String processScript) {
		this.processScript = processScript;
	}

	public String getTransferScript() {
		return transferScript;
	}

	public void setTransferScript(String transferScript) {
		this.transferScript = transferScript;
	}

	public Long getPageSize() {
		return pageSize;
	}

	public void setPageSize(Long pageSize) {
		this.pageSize = pageSize;
	}

	public Long getNameRowIndex() {
		return nameRowIndex;
	}

	public void setNameRowIndex(Long nameRowIndex) {
		this.nameRowIndex = nameRowIndex;
	}

	public String getImportType() {
		return importType;
	}

	public void setImportType(String importType) {
		this.importType = importType;
	}

	public String getIgnoreRowMode() {
		return ignoreRowMode;
	}

	public void setIgnoreRowMode(String ignoreRowMode) {
		this.ignoreRowMode = ignoreRowMode;
	}

	public Long getSeq() {
		return seq;
	}

	public void setSeq(Long seq) {
		this.seq = seq;
	}

	public String getTplUrl() {
		return tplUrl;
	}

	public void setTplUrl(String tplUrl) {
		this.tplUrl = tplUrl;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getUpdatable() {
		return updatable;
	}

	public void setUpdatable(String updatable) {
		this.updatable = updatable;
	}

	public String getUpdatableDefault() {
		return updatableDefault;
	}

	public void setUpdatableDefault(String updatableDefault) {
		this.updatableDefault = updatableDefault;
	}

	public String getImportUuid() {
		return importUuid;
	}

	public void setImportUuid(String importUuid) {
		this.importUuid = importUuid;
	}

	public String getImportName() {
		return importName;
	}

	public void setImportName(String importName) {
		this.importName = importName;
	}

	public List<SysExcelImportColumn> getColumns() {
		return columns;
	}

	public void setColumns(List<SysExcelImportColumn> columns) {
		this.columns = columns;
	}

	public String getImportCode() {
		return importCode;
	}

	public void setImportCode(String importCode) {
		this.importCode = importCode;
	}
}

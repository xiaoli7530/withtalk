package com.ctop.fw.common.utils;


import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.ctop.fw.common.model.Filter;
import com.ctop.fw.common.model.KendoPageRequestData;
import com.ctop.fw.common.model.NuiPageRequestData;
import com.ctop.fw.common.model.PageRequestData;
import com.ctop.fw.common.model.PageResponseData;
import com.ctop.fw.common.model.SortField;


public class SqlBuilder {
	protected static Logger logger = LoggerFactory.getLogger(SqlBuilder.class);
	private static boolean debugEnabled = logger.isDebugEnabled();
	private static boolean traceEnabled = logger.isTraceEnabled();
	
	private int paramKeySeq = 0;
	/** 内部计数器，用来在生成别名时保证唯一 */
	private int count = 0;
	private StringBuilder buffer = new StringBuilder(); 
	/** 别名到属性名的双向MAP */
	private Map<String, String> alias2PropertyMap = new HashMap<String, String>();
	/** property唯一，column不唯一，即同一column可以设置到DTO的多个属性中 */
	private Map<String, String> property2ColumnMap = new HashMap<String, String>();
	private Map<String, Object> params = new HashMap<String, Object>();
	private EntityMetadataUtil metaUtil;
	
	/** 用来过滤不需要的字段，在通过appendField, appendTableColumns方式添加字段时，只有该列表中指定的属性才会真正添加进行，该属性为空或null时表示不限制
	 *  该功能，在资金登记综合查询中用到，用来尽可能提高查询性能
	 *  注意可能会有多的逗号产生，注意调用buffer.removePreCommaIfPossible
	 */
	private List<String> requiredProperties;
	/**存放实体表别名及对应实体类型, 使用本类的addEntity方式添加，将在SQL中生成{alias.*}的字符串，
	 * 而且property2ColumnMap, alias2PropertyMap中, 将没有其相关的映射信息; ResultTransformer会将实体中的相同属性放入DTO相同属性中
	 * 该数据会使用SQLQuery.addEntity(alias, entityType)的方式调用
	 * */
	private Map<String, Class<?>> entityMap;
	private Map<String, String> fieldFilterSqlmap;
	/**
	 * 条件
	 */
	private String groupBy = null;
	
	public SqlBuilder(EntityMetadataUtil metaUtil) {
		this.metaUtil = metaUtil;
	}
	
	/** 追加如 T.COMPANY_CDE as "companyCode" 的字符串 */
	public SqlBuilder appendField(String column, String propertyName) {
		return appendField(column, propertyName, " ");
	}
	
	private boolean isNotBlank(String text) {
		return text != null && !"".equals(text);
	}
	
	private boolean isBlank(String text) {
		return !this.isNotBlank(text);
	}
	
	public void groupBy(String groupBy){
		this.groupBy = groupBy;
	}
	
	/** a.part_code => (exists (select 1 from wm_warehouse_detail a where {0} ) ) */
	public void defineFieldFilterSql(String field, String sql) {
		if (this.fieldFilterSqlmap == null) {
			this.fieldFilterSqlmap = new HashMap<String, String>();
		}
		this.fieldFilterSqlmap.put(field, sql);
	}
	
	/** a.part_code => (exists (select 1 from wm_warehouse_detail a where {0} ) ) */
	public String getFieldFilterSql(String field) {
		if (this.fieldFilterSqlmap != null) {
			return this.fieldFilterSqlmap.get(field);
		} 
		return null;
	}
	
	/**
	 * <pre>
	 * 追加字段，将字段映射到DTO中的字段名对应的“驼峰式”名的属性中
	 * 如将“a.OBJECT_NAME, a.PACKAGE_NAME” 转换成 “a.OBJECT_NAME as "objectName", a.PACKAGE_NAME as "packageName"”
	 * </pre>
	 * @param columns
	 * @return
	 */
	public SqlBuilder appendCamelFields(String columns) {
		if(isNotBlank(columns)) {
			if(debugEnabled) {
				logger.debug("将字段序列:" + columns + "添加映射到其对应的驼峰式属性中.");
			}
			columns = columns.trim();
			if(columns.startsWith(",")) {
				buffer.append(",");
			}
			String[] arr = columns.split("\\s*,\\s*");
			if(arr.length > 0) {
				int start = 0;
				int end = arr.length;
				if("".equals(arr[0])) {
					start = 1;
				}
				if("".equals(arr[arr.length - 1])) {
					end = end - 1;
				}
				for(int i = start; i < end; i++) {
					String column = arr[i];
					int dotPos = column.indexOf(".");
					if(dotPos != -1) {
						column = column.substring(dotPos + 1);
					}
					String camelName = JdbcUtils.convertUnderscoreNameToPropertyName(column);
					if(i != end - 1) {
						appendField(arr[i], camelName, ",");
					} else {
						appendField(arr[i], camelName);
					}
				}
			}
			if(columns.endsWith(",")) {
				buffer.append(",");
			}
		}
		return this;
	}

	/** 追加如 T.COMPANY_CDE as "companyCode" 的字符串, endStr定义结束字符 */
	public SqlBuilder appendField(String column, String propertyName, String endStr) {
		if(!isPropertyRequired(propertyName)) {
			//有限制只取的属性时，过滤不需要的
			if (debugEnabled) {
				logger.debug("属性：" + propertyName + "不需要，过滤.");
			}
			return this;
		}
		if (alias2PropertyMap.containsValue(propertyName)) {
			if (debugEnabled) {
				logger.debug("不能定义重复的属性名" + propertyName);
			}
			throw new RuntimeException("不能定义重复的属性名" + propertyName);
		}
		String alias = convertPropertyNameToRealAlias(propertyName);
		alias2PropertyMap.put(alias, propertyName);
		property2ColumnMap.put(propertyName, column);
		buffer.append(" ").append(column).append(" as \"").append(alias).append("\"").append(endStr == null ? "" : endStr);
		if (traceEnabled) {
			logger.trace("映射列到属性名, 列：" + column + " -> 别名：" + alias + " -> 属性名：" + propertyName);
		}
		return this;
	}
	
	private boolean isPropertyRequired(String propertyName) {
		return requiredProperties == null || requiredProperties.isEmpty() || requiredProperties.contains(propertyName);
	}
	
	/**别名缓存，避免生成太多字符串*/
	private static String[] aliases = new String[500];
	static {
		for(int i = 0; i < aliases.length; i++) {
			aliases[i] = "a__" + i;
		}
	}
	/** 用来支持长度大于30的属性名，及user.companyCode形式的属性名 */
	private String convertPropertyNameToRealAlias(String propertyName) {
		// return "a__" + (count++);
		// return count < aliases.length ? aliases[count++] : "a__" + (count++);
		count++;
		String alias = propertyName;
		if (alias.indexOf('.') >= 0) {
			alias = alias.replaceAll("\\.", "_");
		}
		if (alias.length() > 30) {
			alias = alias.substring(0, 10) + "_" + count;
		}
		return alias;
	}
	
	/**
	 * 直接将表中的字段拼到SQL中,(避免使用table.*的方式)
	 * @param tableName
	 * @return
	 */
	public SqlBuilder appendRawTableColumns(String tableName) {
		return appendRawTableColumns("", tableName, null);
	}
	
	/**
	 * 直接将表中的字段拼到SQL中,(避免使用table.*的方式)
	 * @param tableAlias
	 * @param tableName
	 * @return
	 */
	public SqlBuilder appendRawTableColumns(String tableAlias, String tableName) {
		return appendRawTableColumns(tableAlias, tableName, null);
	}
	
	/**
	 * 直接将表中的字段拼到SQL中,(避免使用table.*的方式)
	 * @param tableName
	 * @param tableAlias
	 * @param excludedColumns
	 * @return
	 */
	public SqlBuilder appendRawTableColumns(String tableAlias, String tableName, String[] excludedColumns) {
		List<String[]> columnPropertys = metaUtil.getTableColumnPropertyNames(tableName, excludedColumns);
		return appendRawTableColumns(tableAlias, columnPropertys);
	}
	
	/**
	 * 直接将表中的字段拼到SQL中,(避免使用table.*的方式)
	 * @param tableAlias
	 * @param entityClass
	 * @param includedProperty
	 * @return
	 */
	public SqlBuilder appendRawTableColumns(String tableAlias, Class<?> entityClass, String[] includedProperty) {
		List<String[]> columnPropertys = getColumnPropertyByClass(entityClass, includedProperty, null);
		return appendRawTableColumns(tableAlias, columnPropertys);
	}
	
	/**
	 * 直接将表中的字段拼到SQL中,(避免使用table.*的方式)
	 * @param tableAlias
	 * @param columnPropertys
	 */
	private SqlBuilder appendRawTableColumns(String tableAlias, List<String[]> columnPropertys) {
		int i = 0;
		int size = columnPropertys.size();
		String prefix = isBlank(tableAlias) ? "" : tableAlias + ".";
		for(String[] columnProperty : columnPropertys) {
			buffer.append(prefix).append(columnProperty[0]);
			if(i < size - 1) {
				buffer.append(",");
			} else {
				buffer.append(" ");
			}
			i++;
		}
		return this;
	}

	/**
	 * 根据实体，追加实体所对应表中列和属性名如 T.COMPANY_CDE as "companyCode", T.COMPANY_NME as
	 * "companyName" 的字符串 有些实体的hibernate注释写得不规范，如CustomerCompany中有多个@Id注释，
	 * 这个会导致id属性及对应的列不能被正常取出来，这种 情况下必须用appendField的方式添加没有被添加的
	 * 列及属性
	 */
	public SqlBuilder appendTableColumns(String tableName, String tableAlias, String... excludedColumns) {
		return appendColumns4NestedProperty(tableName, tableAlias, "", excludedColumns);
	}

	/**
	 * 根据列所对应实体，追加表中列对应属性名如 T.COMPANY_CDE as "company.companyCode",
	 * T.COMPANY_NME as "company.companyName" 的字符串
	 */
	public SqlBuilder appendColumns4NestedProperty(String tableName, String tableAlias, String nestedProperty,
			String... excludedColumns) {
		return appendColumns4NestedProperty(tableName, tableAlias, nestedProperty, excludedColumns, " ");
	}

	/**
	 * 根据列所对应实体，追加表中列对应属性名如 T.COMPANY_CDE as "companyCode", T.COMPANY_NME as
	 * "companyName" 的字符串
	 */
	private SqlBuilder appendColumns4NestedProperty(String tableName, String tableAlias, String nestedPropertyPrefix,
			String[] excludedColumns, String strEnd) {
		List<String[]> columnPropertys = metaUtil.getTableColumnPropertyNames(tableName, excludedColumns);
		appendColumnProperty(tableAlias, nestedPropertyPrefix, columnPropertys, strEnd);
		return this;
	}

	/**
	 * 根据实体，追加实体所对应表中列和属性名如 T.COMPANY_CDE as "companyCode", T.COMPANY_NME as
	 * "companyName" 的字符串 有些实体的hibernate注释写得不规范，如CustomerCompany中有多个@Id注释，
	 * 这个会导致id属性及对应的列不能被正常取出来，这种 情况下必须用appendField的方式添加没有被添加的
	 * 列及属性
	 */
	public SqlBuilder appendTableColumns(Class<?> entityClass, String tableAlias, String... excludedProperties) {
		return appendColumns4NestedProperty(entityClass, tableAlias, "", excludedProperties);
	}

	/**
	 * 根据实体，追加实体所对应表中列和属性名如 T.COMPANY_CDE as "customer.companyCode",
	 * T.COMPANY_NME as "customer.companyName" 的字符串
	 */
	public SqlBuilder appendColumns4NestedProperty(Class<?> entityClass, String tableAlias, String nestedProperty,
			String... excludedProperties) {
		return appendColumns4NestedProperty(entityClass, tableAlias, nestedProperty, excludedProperties, " ");
	}
	
	/**
	 * 根据实体，追加实体所对应表中列和属性名如 T.COMPANY_CDE as "customer.companyCode",
	 * T.COMPANY_NME as "customer.companyName" 的字符串
	 */
	public SqlBuilder includeTableColumns(Class<?> entityClass, String tableAlias, String... includeProperties) {
		List<String[]> columnPropertys = getColumnPropertyByClass(entityClass, includeProperties, null);
		appendColumnProperty(tableAlias, "", columnPropertys, " ");
		return this;
	}
	
	/**
	 * 根据实体，追加实体所对应表中列和属性名如 T.COMPANY_CDE as "customer.companyCode",
	 * T.COMPANY_NME as "customer.companyName" 的字符串
	 */
	public SqlBuilder includeColumns4NestedProperty(Class<?> entityClass, String tableAlias, String nestedProperty,
			String... includeProperties) {
		List<String[]> columnPropertys = getColumnPropertyByClass(entityClass, includeProperties, null);
		appendColumnProperty(tableAlias, nestedProperty, columnPropertys, " ");
		return this;
	}
	
	/**
	 * addEntity方式添加，将在SQL中生成{alias.*}的字符串，
	 * 而且property2ColumnMap, alias2PropertyMap中, 将没有其相关的映射信息; ResultTransformer会将实体中的相同属性放入DTO相同属性中
	 * 该数据会使用SQLQuery.addEntity(alias, entityType)的方式调用, 而且会将查询出来的实体放入session一级缓存
	 * 这样当需更新这此实体时，hibernate无需再查询数据库
	 * 查询出来的对象需要用来修改时，建议用此方法
	 * @param tableAlias
	 * @param entityType
	 * @return
	 */
	public SqlBuilder addEntity(String tableAlias, Class<?> entityType) {
		buffer.append(" {").append(tableAlias).append(".*} ");
		if(this.entityMap == null) {
			this.entityMap = new HashMap<String, Class<?>>(4);
		}
		this.entityMap.put(tableAlias, entityType);
		return this;
	}
	

	/**
	 * 根据实体，追加实体所对应表中列和属性名如 T.COMPANY_CDE as "companyCode", T.COMPANY_NME as
	 * "companyName" 的字符串
	 */
	private SqlBuilder appendColumns4NestedProperty(Class<?> entityClass, String tableAlias, String nestedPropertyPrefix,
			String[] excludedProperties, String strEnd) {
		List<String[]> columnPropertys = getColumnPropertyByClass(entityClass, null, excludedProperties);
		appendColumnProperty(tableAlias, nestedPropertyPrefix, columnPropertys, strEnd);
		return this;
	}

	/**
	 * 将表->属性前缀.属性名 生成SQL，生成alias2Property,
	 * 这种方式将可以生成复杂的DTO，如可以给user.company.companyNameCn设值
	 */
	private void appendColumnProperty(String tableAlias, String nestedProperty, List<String[]> columnPropertys, String strEnd) {
		if (nestedProperty == null) {
			nestedProperty = "";
		} else {
			nestedProperty = nestedProperty.trim();
		}
		if (!StringUtils.isEmpty(nestedProperty) && !nestedProperty.endsWith(".")) {
			nestedProperty = nestedProperty + ".";
		}
		
		boolean hasPropertyInBefore = false;
		for (String[] columnProperty : columnPropertys) {
			String propertyName = nestedProperty + columnProperty[1];
			if(isPropertyRequired(propertyName)) {
				if(hasPropertyInBefore) {
					this.buffer.append(",");
				}
				String columnName = tableAlias + "." + columnProperty[0];
				appendField(columnName, propertyName, " ");
				hasPropertyInBefore = true;
			}
		}
		buffer.append(strEnd);
	}

	private List<String[]> getColumnPropertyByClass(Class<?> entityClass, String[] includedProperties, String[] excludedProperties) {
		return metaUtil.getColumnPropertyByClass(entityClass, includedProperties, excludedProperties);
	}

	/** 加入该方法用于指定属性对应的列，用于查询条件中方便使用 */
	public SqlBuilder setPropertyColumnInfo(String column, String propertyName) {
		property2ColumnMap.put(propertyName, column);
		return this;
	}

	/** 加入该方法用于指定属性对应的列，用于查询条件中方便使用 */
	public SqlBuilder setPropertyColumnInfo(Class<?> entityClass, String tableAlias) {
		List<String[]> columnPropertys = getColumnPropertyByClass(entityClass, null, null);
		for (String[] columnProperty : columnPropertys) {
			property2ColumnMap.put(/* nestedProperty + */columnProperty[1], tableAlias + "." + columnProperty[0]);
		}
		return this;
	}
	
	/**
	 * 属性名转换到对应的列名，列名必须是用appendField或appendColumn..,
	 * setPropertyColumnInfo,的方式添加的
	 */
	public String getColumnName(String propertyName) {
		String column = propertyName;
		if (property2ColumnMap.containsKey(propertyName)) {
			column = property2ColumnMap.get(propertyName);
		}
		return column;
	}
	
//	/**
//	 * 取别名
//	 * 
//	 * @param columnName
//	 * @return
//	 */
//	public String getColumnAliasName(String fieldName) {
//		String aliasName = fieldName;
//		if (alias2PropertyMap.containsValue(fieldName)) {
//			aliasName = (String) alias2PropertyMap.getKey(fieldName);
//		}
//		return aliasName;
//	}
	
	
	 
	
//	private SqlBuilder2 appendCondition(String propertyName, String operator, Object value, boolean parenthesisWithVal) {
//		if(!isValueValid(operator, value)) {
//			return this;
//		}
//		String valName =  buildRandomValName(propertyName);
//		String columnName = getColumnName(propertyName);
//		buffer.append(" and ");
//		buffer.append(columnName).append(" ").append(operator);
//		if(parenthesisWithVal) {
//			buffer.append("(");
//		}
//		buffer.append(" :").append(valName);
//		if(parenthesisWithVal) {
//			buffer.append(")");
//		}
//		buffer.append(" ");
//		valueMap.put(valName, value);
//		return this;
//	}
	
	/**别名缓存，避免生成太多字符串*/
	private static String[] valNames = new String[100];
	static {
		for(int i = 0; i < valNames.length; i++) {
			valNames[i] = "p" + i + "_";
		}
	}
	private String buildParamKey(String field, Object value) {
		String temp = paramKeySeq < valNames.length ? valNames[paramKeySeq++] : "p" + (paramKeySeq++) + "_";
		if (debugEnabled) {
			logger.debug("占位符参数名" + field + " -> " + temp);
		}
		this.params.put(temp, value);
		return temp;
	}

	public Condition equal(String field, Object value) {
		String paramKey = buildParamKey(field, value);
		String column = this.getColumnName(field);
		String sql = "" + column + " = :" + paramKey;
		return new Condition(sql, this, field);
	}

	public Condition notEqual(String field, Object value) {
		String paramKey = buildParamKey(field, value);
		String column = this.getColumnName(field);
		String sql = "" + column + " <> :" + paramKey;
		return new Condition(sql, this, field);
	}

	public Condition isNull(String field) {
		String column = this.getColumnName(field);
		String sql = "" + column + " is null ";
		return new Condition(sql, this, field);
	}

	public Condition isNotNull(String field) {
		String column = this.getColumnName(field);
		String sql = "" + column + " is not null ";
		return new Condition(sql, this, field);
	}
	
	public Condition isEmpty(String field) {
		String column = this.getColumnName(field);
		String sql = "" + column + " is null ";
		return new Condition(sql, this, field);
	}

	public Condition isNotEmpty(String field) {
		String column = this.getColumnName(field);
		String sql = "" + column + " is not null ";
		return new Condition(sql, this, field);
	}

	public Condition lessThan(String field, Object value) {
		String column = this.getColumnName(field);
		String paramKey = buildParamKey(field, value);
		String sql = "" + column + " < :" + paramKey;
		return new Condition(sql, this, field);
	}

	public Condition lessThanOrEqualTo(String field, Object value) {
		String column = this.getColumnName(field);
		String paramKey = buildParamKey(field, value);
		String sql = "" + column + " <= :" + paramKey;
		return new Condition(sql, this, field);
	}

	public Condition greaterThan(String field, Object value) {
		String column = this.getColumnName(field);
		String paramKey = buildParamKey(field, value);
		String sql = "" + column + " > :" + paramKey;
		return new Condition(sql, this, field);
	}

	public Condition greaterThanOrEqualTo(String field, Object value) {
		String column = this.getColumnName(field);
		String paramKey = buildParamKey(field, value);
		String sql = "" + column + " >= :" + paramKey;
		return new Condition(sql, this, field);
	}
	
	public Condition geAndLt(String field, Object valueFrom, Object valueTo) {
		if (valueFrom == null || "".equals(valueFrom)) {
			return this.lessThan(field, valueTo);
		}
		if (valueTo == null || "".equals(valueTo)) {
			return this.greaterThanOrEqualTo(field, valueFrom);
		}
		String column = this.getColumnName(field);
		String paramKeyFrom = buildParamKey(field, valueFrom);
		String paramKeyTo = buildParamKey(field, valueTo);
		String sql = "" + column + " >= :" + paramKeyFrom + " and " + column + " < :" + paramKeyTo;
		return new Condition(sql, this, field);
	}
	
	public Condition startsWith(String field, Object value) {
		return this.startsWith(field, value, false);
	}

	public Condition startsWith(String field, Object value, boolean ignoreCase) {
		if (value == null || "".equals(value)) {
			return null;
		}
		String column = this.getColumnName(field);
		if (ignoreCase) {
			column = "UPPER(" + column + ")";
			value = ((String) value).toUpperCase();
		}
		value = value + "%";
		String paramKey = buildParamKey(field, value);
		String sql = "" + column + " like :" + paramKey;
		return new Condition(sql, this, field);
	}
	
	public Condition endsWith(String field, Object value) {
		return this.endsWith(field, value, false);
	}
	public Condition endsWith(String field, Object value, boolean ignoreCase) {
		if (value == null || "".equals(value)) {
			return null;
		}
		String column = this.getColumnName(field);
		if (ignoreCase) {
			column = "UPPER(" + column + ")";
			value = ((String) value).toUpperCase();
		}
		value = "%" + value;
		String paramKey = buildParamKey(field, value);
		String sql = "" + column + " like :" + paramKey;
		return new Condition(sql, this, field);
	}
	public Condition contains(String field, Object value) {
		return this.contains(field, value, false);
	}
	public Condition contains(String field, Object value, boolean ignoreCase) {
		if (value == null || "".equals(value)) {
			return null;
		}
		String column = this.getColumnName(field);
		if (ignoreCase) {
			column = "UPPER(" + column + ")";
			value = ((String) value).toUpperCase();
		}
		value = "%" + value + "%";
		String paramKey = buildParamKey(field, value);
		String sql = "" + column + " like :" + paramKey;
		return new Condition(sql, this, field);
	}
	
	public Condition notIn(String field, Collection<?> value) {
		if (value == null || value.size() == 0) {
			return null;
		}
		String column = this.getColumnName(field);
		String paramKey = buildParamKey(field, value);
		String sql = "" + column + " not in :" + paramKey;
		return new Condition(sql, this, field);
	}
	
	public Condition in(String field, Collection<?> value) {
		if (value == null || value.size() == 0) {
			return null;
		}
		String column = this.getColumnName(field);
		String paramKey = buildParamKey(field, value);
		String sql = "" + column + " in :" + paramKey;
		return new Condition(sql, this, field);
	}

	public Condition and(Condition... conditions) {
		String sql = Arrays.asList(conditions).stream().filter(condition -> condition != null)
				.map(condition -> condition.finalSql()).collect(Collectors.joining(" and "));
		sql = sql == null || "".equals(sql) ? "(1=1)" : "(" + sql + ")";
		return new Condition(sql, this, null);
	}
	
	public Condition or(Condition... conditions) {
		String sql = Arrays.asList(conditions).stream().filter(condition -> condition != null)
				.map(condition -> condition.finalSql()).collect(Collectors.joining(" or "));
		sql = sql == null || "".equals(sql) ? " (1!=1) " : "(" + sql + ") ";
		return new Condition(sql, this, null);
	}
	
	public Condition not(Condition condition) {
		String sql = "not (" + condition.finalSql() + ") ";
		return new Condition(sql, this, null);
	}
	
	public Condition alwaysTrue() {
		return new Condition("1=1", this, null);
	}
	
	public SqlBuilder append(String sqlPart) {
		this.buffer.append(sqlPart);
		return this;
	}
	
	public void andEqual(String field, Object value) {
		if(value != null && !"".equals(value)) {
			this.buffer.append(" and ").append(this.equal(field, value).finalSql());
		}
	}
	

	/**
	 * <pre>
	 * 当查询条件不能为空的时候的使用此方法拼装查询;
	 * 避免进行全表查询 
	 * </pre>
	 * @param field
	 * @param value
	 */
	public void andEqualNotNull(String field, Object value) {
		if(value == null || "".equals(value)) {
			throw new RuntimeException(field+",不能为空");
		}
		this.buffer.append(" and ").append(this.equal(field, value).finalSql);
	}
	
	public void where(Condition condition) {
		this.buffer.append(" where ");
		if(condition != null) {
			this.buffer.append(condition.finalSql());
		} else {
			this.buffer.append(" 1=1 ");
		}
	}
	
	public void append(Condition condition) {
		if(condition != null) {
			this.buffer.append(" and ");
			this.buffer.append(condition.finalSql());
		}
	}
	
	public void whereFilter(Filter filter) {
		this.buffer.append(" where ");
		this.buffer.append(filter.toCondition(this).finalSql());
	}
	
	public void appendFilter(Filter filter) {
		this.buffer.append(filter.toCondition(this).finalSql());
	}
	
	public String asc(String field) {
		String column = this.getColumnName(field);
		return "" + column + " asc";
	}
	
	public String desc(String field) {
		String column = this.getColumnName(field);
		return "" + column + " desc";
	}
	
	public void orderBy(List<SortField> sorts) {
		if(sorts != null && sorts.size() > 0) {
			String orderBy = sorts.stream().map(sort -> {
				if ("desc".equals(sort.getDir())) {
					return this.desc(sort.getField());
				} else  {
					return this.asc(sort.getField());
				}
			}).collect(Collectors.joining(","));
			if(orderBy != null && !"".equals(orderBy)) {
				this.buffer.append(" order by ").append(orderBy);
			}
		}
	}
	@SuppressWarnings("unchecked")
	public <T> List<T> query(EntityManager em, Class<T> resultClass,Integer maxResult) {
		Session session = em.unwrap(Session.class);
		String sql2 = this.buffer.toString();
		SQLQuery query2 = session.createSQLQuery(sql2);
		query2.setProperties(this.params); 
		if(maxResult!=null&&maxResult>0) {
			query2.setFirstResult(0);
			query2.setMaxResults(maxResult);
		}
		query2.setResultTransformer(getResultTransformer(resultClass));    
		return query2.list();
	}
	/**
	 * groupby
	 * @param sorts
	 */
	private void groupBy() {
		if(!this.isBlank(groupBy)){
			this.buffer.append(" group by ").append(groupBy);
		}
	}
	
	public <T> SQLQuery buildNativeQuery(EntityManager em, KendoPageRequestData request, Class<T> resultClass) {
		Session session = em.unwrap(Session.class);
		SQLQuery query = session.createSQLQuery(this.buffer.toString());
		query.setProperties(this.params);
		query.setFirstResult(request.getSkip());
		query.setMaxResults(request.getPageSize());
		query.setResultTransformer(Transformers.aliasToBean(resultClass));
		return query;
	}
	
	/**
	 * 通过表名得到主键列
	 * @param em
	 * @param tableName
	 * @return
	 */
	public String getTableKeyByName(EntityManager em,String tableName){
		String ret ="";
		Session session = em.unwrap(Session.class);
		String sql ="select s.owner,s.constraint_name,s.table_name,s.column_name from USER_CONS_COLUMNS s where exists (select 1 from user_constraints c "
				+ " where c.table_name = '"+tableName+"'"
				+ " and c.constraint_name = s.constraint_name and c.constraint_type = 'P')";
		SQLQuery  query = session.createSQLQuery(sql);
		List<Object[]> list = query.list();
		if(!ListUtil.isEmpty(list)){
			if(list.size() > 0){
				Object[] o = list.get(0);
				if(o.length == 4){
					ret = (String)o[3];
				}
			}
		}
		System.out.println(ret);
		return ret;
	}
	
	public void addParameter(String name, Object value) {
		this.params.put(name, value);
	}
	
	public <T> PageResponseData<T> pageQuery(EntityManager em, PageRequestData request, Class<T> resultClass) {
		return this.pageQuery(em, request, true, resultClass);
	}
	
	/**
	 * 分页查询，
	 * @param em
	 * @param request
	 * @param andFilter
	 * @param resultClass
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> PageResponseData<T> pageQuery(EntityManager em, PageRequestData request, boolean andFilter, Class<T> resultClass) {
		if(andFilter) {
			//拼查询条件；
			this.append(" and ");
			this.appendFilter(request.getFilter());
			if (request instanceof NuiPageRequestData) {
				NuiPageRequestData nprd = (NuiPageRequestData) request;
				this.append(" and ");
				this.appendFilter(nprd.buildFilter4FilterRules());
			}
		}
		//分组
		this.groupBy();
		Session session = em.unwrap(Session.class);
		String sql = "select count(1) from (" + this.buffer.toString() + ") t";
		SQLQuery query = session.createSQLQuery(sql);
		query.setProperties(this.params);
		Number totalCount = (Number) query.uniqueResult();
		
		int offset = request.getPageIndex() * request.getPageSize();
		//排序
		this.orderBy(request.getSort());
		String sql2 = "" + this.buffer.toString() + "";
		SQLQuery query2 = session.createSQLQuery(sql2);
		query2.setProperties(this.params);
		query2.setFirstResult(offset);
		query2.setMaxResults(request.getPageSize());
		query2.setResultTransformer(getResultTransformer(resultClass));    
		List<T> data = query2.list();
		return new PageResponseData<T>(request,  totalCount.intValue(), data);
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> query(EntityManager em, Class<T> resultClass) {
		Session session = em.unwrap(Session.class);
		String sql2 = this.buffer.toString();
		SQLQuery query2 = session.createSQLQuery(sql2);
		query2.setProperties(this.params); 
		query2.setResultTransformer(getResultTransformer(resultClass));    
		return query2.list();
	}
	
	public ResultTransformer getResultTransformer(Class<?> resultClass) {
		if(ClassUtils.isAssignable(Map.class, resultClass)) {
			return AliasToEntityMapResultTransformer.INSTANCE;
		}
		return new Alias2PropertyResultTransformer(resultClass, alias2PropertyMap, entityMap, false, this.metaUtil);
	}

	public static class Condition {
		private String finalSql;
		private SqlBuilder sb;
		private String field;
		
		private Condition(String sql, SqlBuilder sb, String field) {
			String filterSql = sb.getFieldFilterSql(field);
			if (StringUtil.isEmpty(filterSql)) {
				this.finalSql = sql;
			} else {
				this.finalSql = filterSql.replace("{0}", sql);
			}
			this.sb = sb;
			this.field = field;
		}
		
		public String finalSql() {
			return finalSql;
		}
		
	}

	/**
	 * @param class1
	 * @param ignoreFields
	 */
	public void appendTableColumnsExt(Class<?> entityClass, String tableAlias,List<String> excludedProperties) {
		appendColumns4NestedProperty(entityClass, tableAlias, "", excludedProperties.toArray(new String[excludedProperties.size()]));
		
	}

	public StringBuilder getBuffer() {
		return buffer;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	
}

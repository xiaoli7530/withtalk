package com.ctop.fw.common.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.ctop.base.utils.DateUtil;
import com.ctop.fw.common.utils.SqlBuilder;
import com.ctop.fw.common.utils.StringUtil;
import com.ctop.fw.common.utils.SqlBuilder.Condition;

/**
 *   kendo dataSource 的过滤条件格式
 *   filter: {
    // leave data items which are "Food" or "Tea"
    logic: "or",
    filters: [
      { field: "category", operator: "eq", value: "Food" },
      { field: "name", operator: "eq", value: "Tea" }
    ]
  }
 */
public class Filter implements Serializable{
	private static final long serialVersionUID = -2118467731374228676L;
	//field: "category", operator: "eq", value: "Food"
	private String field;
	//: "eq" (equal to), "neq" (not equal to), "isnull" (is equal to null), "isnotnull" (is not equal to null), 
	//"lt" (less than), "lte" (less than or equal to), "gt" (greater than), "gte" (greater than or equal to), 
	//"startswith", "endswith", "contains", "isempty", "isnotempty".
	private String operator;//eq, le, lt, gt, ge, 
	private String value;
	private String type; //string, date, number
	
	private String logic = "and";//or  and
	private List<Filter> filters = new ArrayList<Filter>();
	private String format;
	
	public Comparable<?> getRealValue() {
		if("string".equals(type) || "number".equals(type) || type == null) {
			return this.value;
		}
		if("date".equals(this.type)) {
			if(StringUtil.isNotEmpty(this.format) && this.format.toLowerCase().equals("yyyy")){
				this.value  = this.value + "-01-01";
				SimpleDateFormat dateFormat = new SimpleDateFormat(format);
				try {
					return dateFormat.parse(this.value);
				} catch (ParseException e) {
					return null;
				}
			}else if(StringUtil.isNotEmpty(this.format) && this.format.toLowerCase().equals("yyyy-mm")){
				this.value  = this.value + "-01";
				SimpleDateFormat dateFormat = new SimpleDateFormat(format);
				try {
					return dateFormat.parse(this.value);
				} catch (ParseException e) {
					return null;
				}
			}else{			
				format = StringUtil.isEmpty(this.format) ? "yyyy-MM-dd" : this.format;
				//如果前台format带分钟秒钟且operator等于lte,lt，自动匹配23点59分
				if(StringUtil.equals("yyyy-MM-dd HH:mm:ss", this.format)){
					if(StringUtil.equals("lte",this.operator) || StringUtil.equals("lt",this.operator)){
						this.value =  this.value + " 23:59:59";
					}else{
						this.value =  this.value + " 00:00:00";
					}				
				}				
				SimpleDateFormat dateFormat = new SimpleDateFormat(format);
				try {
					return dateFormat.parse(this.value);
				} catch (ParseException e) {
					return null;
				}
			}
		}
		return null;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T> Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		Comparable realValue = this.getRealValue();
		if(this.operator != null) {
			switch (this.operator) {
			case "eq":
				return cb.equal(root.get(field), realValue);
			case "ne":
				return cb.notEqual(root.get(field), realValue);
			case "neq":
				return cb.notEqual(root.get(field), realValue);
			case "isnull":
				return cb.isNull(root.get(field));
			case "isnotnull":
				return cb.isNotNull(root.get(field));
			case "lt":
				return cb.lessThan(root.get(field), realValue);
			case "lte":
				return cb.lessThanOrEqualTo(root.get(field), realValue);
			case "gt":
				return cb.greaterThan(root.get(field), realValue);
			case "gte":
				return cb.greaterThanOrEqualTo(root.get(field), realValue);
			case "startswith":
				return cb.like(root.get(field), realValue + "%");
			case "notstartswith":
				return cb.not(cb.like(root.get(field), realValue + "%"));
			case "endswith":
				return cb.like(root.get(field), "%" + realValue);
			case "notendswith":
				return cb.not(cb.like(root.get(field), "%" + realValue));
			case "contains":
				return cb.like(root.get(field), "%" + realValue + "%");
			case "notcontains":
				return cb.not(cb.like(root.get(field), "%" + realValue + "%"));
			case "isempty":
				return cb.isEmpty(root.get(field));
			case "isnotempty":
				return cb.isNotEmpty(root.get(field));
			case "in": 
				return root.get(field).in(Arrays.asList(this.value.split("\\s*,\\s*")));
			case "notin": 
				return root.get(field).in(Arrays.asList(this.value.split("\\s*,\\s*")));
			default:
				return null;
			}
		} else {
			List<Predicate> list = new ArrayList<Predicate>();
			for(Filter item : this.filters) {
				Predicate p = item.toPredicate(root, query, cb);
				if( p != null) {
					list.add(p);
				}
			}
			Predicate[] predicates = list.toArray(new Predicate[list.size()]);
			if("or".equals(this.logic)) {
				return cb.or(predicates);
			} else {
				return cb.and(predicates);
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	public Condition toCondition(SqlBuilder sb) {
		if(this.operator != null) {

			if("".equals(this.value) || this.value == null) {
				return null;
			}
			Comparable realValue = this.getRealValue();
			switch (this.operator) {
			case "eq":
				// 日期==某天， =>  date1 <= date < date2
				if (realValue instanceof Date) {
					Date from = (Date) realValue;
					Date to = null;
					if(this.format.toLowerCase().equals("yyyy")){
						to = DateUtil.yearLastDate(from);
					}if(this.format.toLowerCase().equals("yyyy-mm")){
						to = DateUtil.monthLastDate(from);
					}else{
						if(this.format.length() < 8){
							to = DateUtil.monthLastDate(from);
						}else{
							 to = DateUtil.dateAddDay(from, 1);
						}
					}
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
					String format2 = simpleDateFormat.format(to);
					return sb.geAndLt(field, from, to);
				}
				return sb.equal(field, realValue);
			case "ne":
				return sb.notEqual(field, realValue);
			case "neq":
				return sb.notEqual(field, realValue);
			case "isnull":
				return sb.isNull(field);
			case "isnotnull":
				return sb.isNotNull(field);
			case "lt":
				return sb.lessThan(field, realValue);
			case "lte":
				return sb.lessThanOrEqualTo(field, realValue);
			case "gt":
				return sb.greaterThan(field, realValue);
			case "gte":
				return sb.greaterThanOrEqualTo(field, realValue);
			case "startswith":
				return sb.startsWith(field, realValue, true);
			case "notstartswith":
				return sb.not(sb.startsWith(field, realValue, true));
			case "endswith":
				return sb.endsWith(field, realValue, true);
			case "notendswith":
				return sb.not(sb.endsWith(field, realValue, true));
			case "contains":
				return sb.contains(field, realValue, true);
			case "notcontains":
				return sb.not(sb.contains(field, realValue, true));
			case "isempty":
				return sb.isEmpty(field);
			case "isnotempty":
				return sb.isNotEmpty(field);
			case "notin":
				String[] arr = this.value.split("\\s*,\\s*");
				return sb.notIn(field, Arrays.asList(arr));
			case "in":
				String[] arr2 = this.value.split("\\s*,\\s*");
				return sb.in(field, Arrays.asList(arr2));
			default:
				return null;
			}
		} else {
			List<Condition> list = new ArrayList<Condition>(); 
			for(Filter item : this.filters) {
				Condition p = item.toCondition(sb);
				if( p != null) {
					list.add(p);
				}
			}
			Condition[] conditions = list.toArray(new Condition[list.size()]);
			if("or".equals(this.logic)) {
				return sb.or(conditions);
			} else if ("and".equals(this.logic)){
				return sb.and(conditions);
			} else {
				return sb.alwaysTrue();
			}
		}
	}
	

	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getLogic() {
		return logic;
	}
	public void setLogic(String logic) {
		this.logic = logic;
	}
	public List<Filter> getFilters() {
		return filters;
	}
	public void setFilters(List<Filter> filters) {
		this.filters = filters;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}
	
	public String getOp() {
		return this.operator;
	}
	
	public void setOp(String op) {
		this.operator = op;
	}
}

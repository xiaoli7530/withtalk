package com.ctop.fw.common.utils;

import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.springframework.beans.BeanUtils;
import com.ctop.fw.common.model.Filter;
import com.ctop.fw.common.model.NuiPageRequestData;
import com.ctop.fw.common.utils.ListUtil;
import com.ctop.fw.common.utils.StringUtil;

public class FilterUtil {
	private static DualHashBidiMap<String, Integer> CHAR_VALUES =new DualHashBidiMap<>();//用于计算字符串的顺序
	static{
		int i=0;
		CHAR_VALUES.put("0", i++);
		CHAR_VALUES.put("1", i++);
		CHAR_VALUES.put("2", i++);
		CHAR_VALUES.put("3", i++);
		CHAR_VALUES.put("4", i++);
		CHAR_VALUES.put("5", i++);
		CHAR_VALUES.put("6", i++);
		CHAR_VALUES.put("7", i++);
		CHAR_VALUES.put("8", i++);
		CHAR_VALUES.put("9", i++);
		CHAR_VALUES.put("A", i++);
		CHAR_VALUES.put("B", i++);
		CHAR_VALUES.put("C", i++);
		CHAR_VALUES.put("D", i++);
		CHAR_VALUES.put("E", i++);
		CHAR_VALUES.put("F", i++);
		CHAR_VALUES.put("G", i++);
		CHAR_VALUES.put("H", i++);
		CHAR_VALUES.put("I", i++);
		CHAR_VALUES.put("J", i++);
		CHAR_VALUES.put("K", i++);
		CHAR_VALUES.put("L", i++);
		CHAR_VALUES.put("M", i++);
		CHAR_VALUES.put("N", i++);
		CHAR_VALUES.put("O", i++);
		CHAR_VALUES.put("P", i++);
		CHAR_VALUES.put("Q", i++);
		CHAR_VALUES.put("R", i++);
		CHAR_VALUES.put("S", i++);
		CHAR_VALUES.put("T", i++);
		CHAR_VALUES.put("U", i++);
		CHAR_VALUES.put("V", i++);
		CHAR_VALUES.put("W", i++);
		CHAR_VALUES.put("X", i++);
		CHAR_VALUES.put("Y", i++);
		CHAR_VALUES.put("Z", i++);
		CHAR_VALUES.put("a", i++);
		CHAR_VALUES.put("b", i++);
		CHAR_VALUES.put("c", i++);
		CHAR_VALUES.put("d", i++);
		CHAR_VALUES.put("e", i++);
		CHAR_VALUES.put("f", i++);
		CHAR_VALUES.put("g", i++);
		CHAR_VALUES.put("h", i++);
		CHAR_VALUES.put("i", i++);
		CHAR_VALUES.put("j", i++);
		CHAR_VALUES.put("k", i++);
		CHAR_VALUES.put("l", i++);
		CHAR_VALUES.put("m", i++);
		CHAR_VALUES.put("n", i++);
		CHAR_VALUES.put("o", i++);
		CHAR_VALUES.put("p", i++);
		CHAR_VALUES.put("q", i++);
		CHAR_VALUES.put("r", i++);
		CHAR_VALUES.put("s", i++);
		CHAR_VALUES.put("t", i++);
		CHAR_VALUES.put("u", i++);
		CHAR_VALUES.put("v", i++);
		CHAR_VALUES.put("w", i++);
		CHAR_VALUES.put("x", i++);
		CHAR_VALUES.put("y", i++);
		CHAR_VALUES.put("z", i++);

	}
	
	/**
	 * 设置当前登录人权限内的项目UUID查询条件
	 * @param request
	 */
/*	public static void setProjectUuidQueryParam(NuiPageRequestData request) {
		String projectUuid = UserContextUtil.getUser().getCurrentProjectUuid();
		if(StringUtil.isEmpty(projectUuid)) {
			List<PsEppprProjectDto> projectList = UserContextUtil.getUser().getProjetList();
			if(projectList == null || projectList.size() == 0) {
				addQueryParam(request, "projectUuid", "eq", StringUtil.getUuid());//设置一个查询不到数据的条件
			} else {
				addQueryParam(request, "projectUuid", "in", ListUtil.joinField(projectList, "projectUuid", ","));
			}
		} else {
			addQueryParam(request, "projectUuid", "eq", projectUuid);
		}
	}*/
	
	/**
	 * 设置当前登录人权限内的项目UUID查询条件
	 * @param request
	 */
/*	public static void setPsProjectUuidQueryParam(NuiPageRequestData request) {
		String projectUuid = UserContextUtil.getUser().getCurrentProjectUuid();
		if(StringUtil.isEmpty(projectUuid)) {
			List<PsEppprProjectDto> projectList = UserContextUtil.getUser().getProjetList();
			if(ListUtil.isNullOrEmpty(projectList)){
				addQueryParam(request, "psProjectUuid", "eq", StringUtil.getUuid());//设置一个查询不到数据的条件
			}else{
				addQueryParam(request, "psProjectUuid", "in", ListUtil.joinField(projectList, "projectUuid", ","));
			}			
		} else {
			addQueryParam(request, "psProjectUuid", "eq", projectUuid);
		}
	}*/
	
	/**
	 * <pre>
	 * 查询的时候手动添加查询条件
	 * 如果条件中已经存在，则不添加
	 * </pre>
	 * @param request
	 * @param field
	 * @param operator
	 * @param value
	 */
	public static void addQueryParam(NuiPageRequestData request,String field,String operator,String value) {
		addQueryParam(request, field, operator, value,null);
	}
	/**
	 * <pre>
	 * 查询的时候手动添加查询条件
	 * 如果条件中已经存在，则不添加
	 * </pre>
	 * @param request
	 * @param field
	 * @param operator
	 * @param value
	 * @param type
	 */
	public static void addQueryParam(NuiPageRequestData request,String field,String operator,String value,String type) {
		List<Filter> filters = request.getFilter().getFilters();
		if(filters==null) {
			filters = new ArrayList<Filter>();
			request.getFilter().setFilters(filters);
		}
		Filter filter = ListUtil.find(filters, new Predicate<Filter>(){
			@Override
			public boolean test(Filter t) {
				return t.getField().equals(field);
			}
			
		});
		if(filter==null) {
			filter = new Filter();
			filter.setField(field);
			filter.setOperator(operator);
			filter.setValue(value);
			filters.add(filter);
		}
		
	}

	
	/**
	 * 取request的body
	 * @param request
	 * @return
	 */
	public static String getRequestBody(HttpServletRequest request) {
		
		try {
			BufferedReader br = request.getReader();
			
			StringBuilder wholeStr = new StringBuilder(1024);
			String str;
			while ((str = br.readLine()) != null) {
				wholeStr.append(str);
			}
			return wholeStr.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * <pre>
	 * 从request中获取传入的参数名称
	 * 用于控制更新对象的操作
	 * </pre>
	 * @param prefix
	 * @return
	 */
	public static Set<String> getExistsRequestParam(Object obj){
		if(obj==null) {
			return Collections.emptySet(); 
		}
		HashSet<String> params = new HashSet<String>();
		try {
			PropertyDescriptor[] targetPds = BeanUtils.getPropertyDescriptors(obj.getClass());
			for (PropertyDescriptor p : targetPds) {
				if (p.getReadMethod().invoke(obj) != null) {//如果对象中的属性为空，应该是界面上没传过来，所以控制不更新
					params.add(p.getName());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return params;
	}
	
	/**
	 * 按62进制相加
	 * @param str
	 * @param addNum
	 * @return
	 */
	protected static String add(String str,int addNum) {
		if(StringUtil.isEmpty(str)) {
			return numToString(addNum);
		}
		long num = stringTonum(str)+addNum;
		return numToString(num);
	}
	/**
	 * 将62进制的字符串转换为10进制的数字
	 * @param str
	 * @return
	 */
	protected static long stringTonum(String str) {
		long r=0;
		long dnum = CHAR_VALUES.size();//进制
		for(int i=0;i<str.length();i++){
			String s =String.valueOf(str.charAt(i));
			int value = CHAR_VALUES.getOrDefault(s, 0);
			r=r*dnum+value;
		}
		return r;
	}
	
	/**
	 * 将数字转换为62进制的字符串
	 * @param num
	 * @return
	 */
	protected static String numToString(long num) {
		StringBuilder strBuilder = new StringBuilder();
		long dnum = CHAR_VALUES.size();//除数
		long rnum = 0;//余数
		long mnum = 0;//商
		while(num>0) {
			rnum = num %dnum;
			mnum = num /dnum;
			strBuilder.append(CHAR_VALUES.getKey(rnum));
			num = mnum;
		}
		strBuilder= strBuilder.reverse();
		return strBuilder.toString();
	}
	
	/**
	 * 连接有顺序的字符串(只支持英文字母+数字)，连续的字符串用 "开始~结束"格式显示
	 * @param strs
	 * @param maxLength
	 * @return
	 */
	public static String joinSeqStr(List<String> strs,int maxLength) {
		if(ListUtil.isEmpty(strs)) {
			return "";
		}
		if(strs.size()<=3) {
			return ListUtil.join(strs, ",");
		}
		StringBuilder strBuilder = new StringBuilder();
		Collections.sort(strs);//先排序
		String beginStr=null;//某段开始的字符串
		String endStr=null;//某段结束的字符串
		int count=0;//开始字符串和结束字符串的间隔数
		for(String str:strs) {
			if(beginStr==null) {
				beginStr = str;
				count=0;
				endStr=str;
				strBuilder.append(beginStr);
			} else {
				if(add(endStr, 1).equals(str)){
					endStr = str;
					count++;
				} else {//另外一段的开始
					if(count>0){
						strBuilder.append("~").append(endStr);
					}
					beginStr=str;
					endStr=str;
					count=0;
					if((strBuilder.length()+(","+beginStr).length())>=maxLength) {//如果超过长度则退出
						strBuilder.append("...");
						break;
					}
					strBuilder.append(",").append(beginStr);
				}
			}
		}
		return strBuilder.toString();
	}
	
	
	
	public static void main(String[] args) {
	}
	
}

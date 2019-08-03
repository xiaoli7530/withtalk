package com.ctop.fw.common.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.converters.BigDecimalConverter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.PropertyAccessorFactory;


public class CommonAssembler {
	public final static String[] DEFAULT_IGNORE_PROPS=new String[]{"version","createdDate","createdBy","updatedDate","updatedBy"};//默认排除的字段

	public static <T> T assemble(Object source, Class<T> targetClass, String... ignoreProperties) {
		if(source == null){
			return null;
		}
		T target = BeanUtils.instantiate(targetClass);
		assemble(source, target, null, ignoreProperties);
		return target;
	}
	
	public static void assemble(Object source, Object target, String... ignoreProperties) {
		
		assemble(source, target, null, ignoreProperties);
		
	}
	
	/**
	 * 将原对象的值复制到目标对象
	 * @param source 原对象
	 * @param target 目标对象
	 * @param includeProperties 包含的属性
	 * @param ignoreProperties  排除的属性（排除的比包含属性优先级高）
	 */
	public static void assemble(Object source, Object target,Set<String> includeProperties, String... ignoreProperties) {
		if (target == null || source == null) {
			return;
		}
		String[] ignorePropertiesLast = ignoreProperties;
		if (includeProperties != null && includeProperties.size() > 0) {//将target对象中不在include的属性全部加到排除中
			List<String> ignorePropertiesList = new ArrayList<String>();
			if(ignoreProperties!=null) {//明确定义的排除属性
				for(String s : ignoreProperties) {
					ignorePropertiesList.add(s);
				}
			}
			
			PropertyDescriptor[] targetPds = BeanUtils.getPropertyDescriptors(target.getClass());
			for(PropertyDescriptor p : targetPds) {
				if(!includeProperties.contains(p.getName())) {//不包含在include属性中的属性
					ignorePropertiesList.add(p.getName());
				}
			}
			
			ignorePropertiesLast = ignorePropertiesList.toArray(new String[ignorePropertiesList.size()]);
		} 
		BeanUtils.copyProperties(source, target, ignorePropertiesLast);
	}
	
	public static <S, T> List<T> assemble(List<S> list, Class<T> targetClass, String... ignoreProperties) {
		if(list == null) {
			return Collections.emptyList();
		}
		return list.stream().map(s -> assemble(s, targetClass, ignoreProperties)).collect(Collectors.toList());
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
	 * 将原对象的值复制到目标对象，为null的字段不拷贝
	 * @param source
	 * @param targetClass
	 * @param ignoreProperties
	 * @return
	 */
	public static <T> T assembleIgnoreNull(Object source, Class<T> targetClass, String... ignoreProperties) {
		if(source == null){
			return null;
		}
		T target = BeanUtils.instantiate(targetClass);
		BeanUtils.copyProperties(source, target, concat(getNullPropertyNames(source), ignoreProperties));
		return target;
	}
	
	/**
	 * 将原对象的值复制到目标对象，为null的字段不拷贝
	 * @param source
	 * @param target
	 * @param ignoreProperties
	 */
	public static void assembleIgnoreNull(Object source, Object target, String... ignoreProperties) {
		BeanUtils.copyProperties(source, target, concat(getNullPropertyNames(source), ignoreProperties));
	}
	
	/**
	 * 将原对象的值复制到目标对象，为null的字段不拷贝
	 * @param list
	 * @param targetClass
	 * @param ignoreProperties
	 * @return
	 */
	public static <S, T> List<T> assembleIgnoreNull(List<S> list, Class<T> targetClass, String... ignoreProperties) {
		if(list == null) {
			return Collections.emptyList();
		}
		return list.stream().map(s -> assembleIgnoreNull(s, targetClass, ignoreProperties)).collect(Collectors.toList());
	}
	
	/**
	 * 得到值为空的属性名
	 * @param source
	 * @return
	 */
	public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for(java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
            	emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
	
	public static <T> T[] concat(T[] first, T[] second) {  
		T[] result = Arrays.copyOf(first, first.length + second.length);  
		System.arraycopy(second, 0, result, first.length, second.length);  
		return result;  
	} 
	
	/**
	 * 得的对象指定属性的值
	 * @param o
	 * @param fieldName
	 * @return
	 */
	public static Object getObjectFieldValue(Object o ,String fieldName) {  
		try {    
			String firstLetter = fieldName.substring(0, 1).toUpperCase();    
			String getter = "get" + firstLetter + fieldName.substring(1);    
			Method method = o.getClass().getMethod(getter, new Class[] {});    
			Object value = method.invoke(o, new Object[] {});    
			return value;    
		} catch (Exception e) {    
			return null;    
		}    
	} 
	
	/**
	 * 得到对象的属性名称
	 * @param o
	 * @return
	 */
	public static String[] getObjectFieldNames(Object o){  
	    Field[] fields=o.getClass().getDeclaredFields();  
	        String[] fieldNames=new String[fields.length];  
	    for(int i=0;i<fields.length;i++){  
	        System.out.println(fields[i].getType());  
	        fieldNames[i]=fields[i].getName();  
	    }  
	    return fieldNames;  
	} 
	
	/**
	 * 根据属性名称获取值
	 * @param bean
	 * @param propertyName
	 * @param valueType
	 * @return
	 */
	public static <H> H getPropertyValue(Object bean, String propertyName, Class<H> valueType) {
		BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(bean);
		@SuppressWarnings("unchecked")
		H value = (H) bw.getPropertyValue(propertyName);
		return value;
	}
	
	/**
	 * MAP 转对象
	 * @param map
	 * @param beanClass
	 * @return
	 * @throws Exception
	 */
	public static <T> T mapToObject(Map<String, Object> map, T obj, Class<T> beanClass) {
		if (map == null) {
			return null;
		}
		
		// BigDecimal数据转换
		ConvertUtils.register(new BigDecimalConverter(BigDecimal.ZERO), java.math.BigDecimal.class);
		// 日期转换器
		ConvertUtils.register(new Converter() {
			@SuppressWarnings("rawtypes")
			@Override
			public Object convert(Class arg0, Object arg1) {
				if (arg1 == null) {
					return null;
				}
				if (!(arg1 instanceof String)) {
					throw new ConversionException("只支持字符串转换 !");
				}
				String str = (String) arg1;
				if (str.trim().equals("")) {
					return null;
				}
				
				SimpleDateFormat sd = null;
				if(str.length() == 10) {
					sd = new SimpleDateFormat("yyyy-MM-dd");
				}else if(str.length() == 16){
					sd = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				}else if(str.length() == 19) {
					sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				} 

				try {
					return sd.parse(str);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		 }, java.util.Date.class);
				
		try {
			if (obj == null) {
				obj = beanClass.newInstance();
			}

			org.apache.commons.beanutils.BeanUtils.populate(obj, map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

	/**
	 * map数组转对象数组
	 * @param mapList
	 * @param beanClass
	 * @return
	 */
	public static <T> List<T> mapToObjectList(List<Map<String, Object>> mapList, Class<T> beanClass) {
		if (ListUtil.isEmpty(mapList)) {
			return Collections.emptyList();
		}
		List<T> resultList = new ArrayList<>(mapList.size());
		for (Map<String, Object> map : mapList) {
			resultList.add(mapToObject(map, null, beanClass));
		}
		return resultList;

	}
	
	/**
	 * 对象转Map
	 * @param obj
	 * @return
	 */
	public static Map<String, Object> objectToMap(Object obj) {
	    Map<String, Object> result = new HashMap<String, Object>();
	    String[] fields = CommonAssembler.getObjectFieldNames(obj);
	    for(int i = 0; i < fields.length; i++) {
	      result.put(fields[i], CommonAssembler.getObjectFieldValue(obj, fields[i]));
	    }
	    
	    return result;
	 }
	
}

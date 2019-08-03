package com.ctop.fw.common.utils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import com.ctop.fw.common.constants.Constants.YesNo;
import com.ctop.fw.common.entity.BaseEntity;


public class ListUtil {
	private static Logger log = Logger.getLogger(ListUtil.class);
	public static boolean isEmpty(Collection<?> coll) {
		return coll == null || coll.isEmpty();
	}
	
	public static <T> List<T> union(List<T> list1, List<T> list2) {
		if(list1 == null) {
			return list2;
		}
		if(list2 == null) {
			return list1;
		}
		List<T> list = new ArrayList<T>(list1.size() + list2.size());
		list.addAll(list1);
		list.addAll(list2);
		return list;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T, H> List<H> union(List<T> list1, String propertyName, Class<H> clz) {
		List<List> list = getOneFieldValue(list1, propertyName, List.class);
		List<H> all = new ArrayList<H>();
		for(List item : list) {
			all.addAll(item);
		}
		return all;
	}

	/***/
	public static <T, H> List<H> map(ModelMapper modelMapper, List<T> list, Class<H> targetClass) {
		if( list == null) {
			return Collections.emptyList();
		}
		return list.stream().map(item -> modelMapper.map(item, targetClass)).collect(Collectors.toList());
	}
	
	public static String join(Collection<String> list, String seperator) {
		if(list == null) {
			return "";
		}
		return list.stream().collect(Collectors.joining(seperator));
	}
	
	public static <T> String joinField(List<T> list, String field, String seperator) {
		List<String> values = getOneFieldValue(list, field, String.class);
		return join(values, seperator);
	}
	
	public static <T, H> List<H> getOneFieldValue(Collection<T> list, String property, Class<H> type) {
		return getOneFieldValue(list, property, type, null);
	}
	
	public static <T, H> List<H> getOneFieldValue(Collection<T> list, String property, Class<H> type,
			Predicate<? super T> predicate) {
		if (list == null) {
			return Collections.emptyList();
		}
		Set<H> result = new LinkedHashSet<H>(list.size());
		if (!list.isEmpty()) {
			for (T bean : list) {
				if (predicate == null || predicate.test(bean)) {
					BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(bean);
					@SuppressWarnings("unchecked")
					H value = (H) bw.getPropertyValue(property);
					if (value != null) {
						result.add(value);
					}
				}
			}
		}
		return new ArrayList<H>(result);
	}
	
	/**
	 * 将列表中某字段统一设成某个值
	 *
	 * @param <T>
	 * @param list
	 * @param propertyName
	 * @param propertyValue
	 */
	public static <T> void setFieldValue(List<T> list, String name, Object value) {
		if (list != null && !list.isEmpty()) {
			Class<?> clz = list.get(0).getClass();
			Field field = ReflectionUtils.findField(clz, name);
			field.setAccessible(true);
			for (T e : list) {
				ReflectionUtils.setField(field, e, value);
			}
		}
	}
	
	/**
	 * 给列表中的每个元素的字段设值
	 * @param list
	 * @param name
	 * @param values
	 */
	public static <T,H> void setFieldValueFromList(List<T> list, String name, List<H> values) {
		if(list == null || values == null) {
			return;
		}
		int size1 = list.size(), size2 = values.size();
		Assert.isTrue(size1 == size2, "方法调用setFieldValueFromList, 参数检查异常!");
		Class<?> clz = list.get(0).getClass();
		Field field = ReflectionUtils.findField(clz, name);
		field.setAccessible(true);
		for (int i=0; i<size1; i++) {
			T element = list.get(i);
			H value = values.get(i);
			ReflectionUtils.setField(field, element, value);
		}
	}
	
	public static <T> int findIndex(List<T> list, Predicate<? super T> predicate) {
		int index = 0;
		for(T item : list) {
			if(predicate.test(item)) {
				return index;
			}
			index++;
		}
		return -1;
	}
	
	public static <T> T find(List<T> list, Predicate<? super T> predicate) {
		for(T item : list) {
			if(predicate.test(item)) {
				return item;
			}
		}
		return null;
	}
	
	/**
	 * 设置明细数据，明细不能物理删除，原来存在，当前不存在的逻辑删除；
	 * 原来不存在的新增；
	 */
	public static <T extends BaseEntity> List<T> mergeList4Update(List<T> newList, List<T> oldList, Function<T, String> keyGetter) {
		List<T> list = oldList;
		if(newList == null){
			newList = new ArrayList<>();
		}
		if(oldList == null) {
			list = newList;
		} else {
			// 先逻辑删除所有；
			for(BaseEntity item : list) {
				item.setIsActive(YesNo.NO);
			}
			// 从方法中传入的才是真正需要的
			List<T> newers = new ArrayList<T>(newList.size());
			for(T detail : newList) { 
				// 为空的不要；
				if (detail == null) {
					continue;
				}
				detail.setIsActive(YesNo.YES);
				int index = ListUtil.findIndex(list, d -> StringUtil.equals(keyGetter.apply(d), keyGetter.apply(detail)));
				if(index != -1) {
					// 更新已存在的
					list.set(index, detail);
				} else {
					newers.add(detail);
				}
			}
			list.addAll(newers);
		}
		return list;
	}
	
	/**
	 * 将列表中的元素的某个属性设置成指定的值；调用setter方法设置值；
	 * 只有在属性值不等于该指定的value时才需要调用setter设值；
	 * @param list
	 * @param property
	 * @param value
	 */
	public static <T> void setPropertyValueIfIsNotTheSameValue(List<T> list, String property, Object value ) {
		if(list != null) {
			for(T item : list) {
				BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(item);
				if(bw.getPropertyValue(property) != value) {
					bw.setPropertyValue(property, value);
				}
			}
		}
	}
	
	/**
	 * 分组数组
	 * @param list
	 * @param keyMap
	 * @return
	 */
	public static <K,V> Map<K, List<V>> groupToMap(List<V> list , Function<V, K> keyGetter){
		if(list != null){
			Map<K,List<V>> result = new LinkedHashMap<>();
			for(V v : list){
				K pk =keyGetter.apply(v);
				List<V> l = result.get(pk);
				if(l == null){
					l = new ArrayList<>();
					result.put((K) pk, l);
				}
				l.add(v);
			}
			return result;
		}
		return new HashMap<>();
	}
	/**
	 * 循环遍历数组相加
	 * @param list
	 * @param keyGetter
	 * @return
	 */
	public static <V> BigDecimal loopBigDecimalAdd(List<V> list , Function<V, BigDecimal> keyGetter){
		if(list != null){
			BigDecimal number = BigDecimal.ZERO;
			for(V v : list){
				number = number.add(keyGetter.apply(v));
			}
			return number;
		}
		return BigDecimal.ZERO;
	}

	/**
	 * 将list装换为map
	 * @param details
	 * @param object
	 * @return
	 */
	public static <K,V> Map<K, V> toKeyMap(List<V> details,Function<V, K> keyGetter) {
		Map<K, V> map = new HashMap<>();
		if(!isEmpty(details)){
			details.forEach(t ->{
				map.put(keyGetter.apply(t), t);
			});
		}
		return map;
	}

	/**
	 * 根据字节截取字符串
	 */
	public static String SplitString(String str, int bytes) { 
        int count = 0; //统计字节数         
        String reStr = ""; //返回字符串
         if (str == null) { 
               return "";
         } 
        char[] tempChar = str.toCharArray(); 
        for (int i = 0; i < tempChar.length; i++) {
             String s1 = str.valueOf(tempChar[i]); 
             byte[] b = s1.getBytes();
             count += b.length;
             if (count <= bytes) {
                 reStr += tempChar[i];
             }
         }
        return reStr;
     } 
	
	/**
	 * 排除list中重复的记录
	 *
	 * @param list
	 * @return
	 */
	public static <T> List<T> uniqueList(List<T> list) {
		if (isNullOrEmpty(list)) {
			return list;
		}
		Map<T, String> map = new HashMap<T, String>(list.size());
		for (T o : list) {
			map.put(o, "1");
		}
		List<T> r = list;
		if (map.size() != list.size()) {
			r = new ArrayList<T>(map.size());
			for (T o : list) {
				if (map.get(o) != null) {
					r.add(o);
					map.remove(o);
				}
			}
		}
		map = null;
		list = null;
		return r;

	}
	
	/**
	 * List是否为空的验证
	 *
	 * @param parm
	 * @return
	 */
	public static <T> boolean isNullOrEmpty(List<T> parm) {
		boolean rtn = false;
		if (null == parm || parm.size() == 0) {
			rtn = true;
		}
		return rtn;
	}
	
	/**
	 * List不是空
	 * @param parm
	 * @return
	 */
	public static <T> boolean isNotNullOrEmpty(List<T> parm) {		 
		return !isNullOrEmpty(parm);
	}
	
	/**
	 * 对数组进行排重
	 *
	 * @param t
	 * @return
	 */
	public static <T extends Object> T[] uniqueArray(T[] t) {
		if (t == null || t.length < 1)
			return t;
		T[] r = null;
		HashMap<T,T> map = new HashMap<T,T>(t.length);
		for (int i = 0; i < t.length; i++) {
			map.put(t[i], t[i]);
		}
		if (map.size() == t.length) {
			r = t;
		} else {
			r  = (T[])Array.newInstance(t[0].getClass(), map.size());
			r = map.values().toArray(r);
		}
		return r;
	}
	
	/**
	 * 按照属性对对象列表进行去重
	 * @param keyExtractor
	 * @return
	 */
	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object,Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}

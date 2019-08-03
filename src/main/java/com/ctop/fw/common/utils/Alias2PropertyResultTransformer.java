package com.ctop.fw.common.utils;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.transform.ResultTransformer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.propertyeditors.CustomBooleanEditor;
import org.springframework.util.ClassUtils;

/**
 * @author gongjianjun 默认的将结果集转换成DTO的result transformer aliasToProperty
 */
public class Alias2PropertyResultTransformer implements ResultTransformer, Serializable {
	private static final long serialVersionUID = 3336688780989939682L;
	private static final String ROWNUM_FIELD_NAME = "ROWNUM_";
	protected Logger logger = Logger.getLogger(Alias2PropertyResultTransformer.class);
	/** 内部转换用，oracle别名有长度限制，对超长别名，截取长度，生成一临时别名，将临时别名与真正属性的映射关系保存 */
	private Map<String, String> aliasToPropertyMapping;
	private Class<?> mappedClass;
	private boolean foundPropertyIgnoreCase;
	private List<String> propertyNames;
	private Map<String, Class<?>> entityMap;
	private EntityMetadataUtil metaUtil;

	public Alias2PropertyResultTransformer(Class<?> mappedClass, Map<String, String> aliasToPropertyMapping,
			Map<String, Class<?>> entityMap, boolean foundPropertyIgnoreCase, EntityMetadataUtil metaUtil) {
		this.mappedClass = mappedClass;
		this.aliasToPropertyMapping = aliasToPropertyMapping;
		this.foundPropertyIgnoreCase = foundPropertyIgnoreCase;
		this.entityMap = entityMap;
		this.metaUtil = metaUtil;
		if (foundPropertyIgnoreCase) {
			PropertyDescriptor[] properties = BeanUtils.getPropertyDescriptors(mappedClass);
			propertyNames = new ArrayList<String>(properties.length);
			for (PropertyDescriptor p : properties) {
				propertyNames.add(p.getName());
			}
		}
	}

	public Object transformTuple(Object[] tuple, String[] aliases) {
		Object mappedObject = BeanUtils.instantiate(this.mappedClass);
		BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(mappedObject);
		// 确保可以设置nested property.
		bw.setAutoGrowNestedPaths(true);
		bw.registerCustomEditor(Boolean.class, new CustomBooleanEditor("Y", "N", true));
		for (int i = 0; i < aliases.length; i++) {
			String alias = aliases[i];
			// 将实体中的属性复制到DTO中
			if (entityMap != null && tuple[i] != null && entityMap.containsKey(alias)
					&& ClassUtils.isAssignable(entityMap.get(alias), tuple[i].getClass())) {
				List<String[]> list = metaUtil.getColumnPropertyByClass(tuple[i].getClass(), null, null);
				BeanWrapper entityBw = PropertyAccessorFactory.forBeanPropertyAccess(tuple[i]);
				for (String[] columnProperty : list) {
					Object value = entityBw.getPropertyValue(columnProperty[1]);
					bw.setPropertyValue(columnProperty[1], value);
				}
				// BeanUtils.copyProperties(tuple[i], mappedObject);
				continue;
			}
			// 如果是null,则不设置到DTO中，希望DTO中的属性没有默认值，要不不能这么做了
			if (ROWNUM_FIELD_NAME.equals(alias)) {// || tuple[i] == null
				continue;
			}
			// 默认别名为属性名
			String propertyName = alias;
			// 如果有强制指定属性名
			if (aliasToPropertyMapping.containsKey(alias)) {
				propertyName = aliasToPropertyMapping.get(alias);
			}
			if (bw.isWritableProperty(propertyName)) {
				if (tuple[i] instanceof Character) {
					tuple[i] = String.valueOf(tuple[i]);
				}
				bw.setPropertyValue(propertyName, tuple[i]);
			} else if (foundPropertyIgnoreCase) {
				String realPropertyName = null;
				for (String temp : propertyNames) {
					if (temp.equalsIgnoreCase(propertyName)) {
						realPropertyName = temp;
						break;
					}
				}
				if (realPropertyName != null) {
					bw.setPropertyValue(realPropertyName, tuple[i]);
				} else {
					if (logger.isTraceEnabled()) {
						logger.trace("属性" + propertyName + "在类型" + this.mappedClass.getName()
								+ "不可写，请确定是否有该属性或为该属性定义setter方法!");
					}
				}
			} else {
				if (logger.isTraceEnabled()) {
					logger.trace(
							"属性" + propertyName + "在类型" + this.mappedClass.getName() + "不可写，请确定是否有该属性或为该属性定义setter方法!");
				}
			}
		}
		return mappedObject;
	}

	public List<?> transformList(@SuppressWarnings("rawtypes") List collection) {
		return collection;
	}

}

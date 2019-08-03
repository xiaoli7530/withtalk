package com.ctop.fw.common.utils;
 

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.hibernate.type.Type;

public class EntityMetadataUtil {
	
	private SessionFactory sessionFactory;
	private static Map<Class<?>, List<String[]>> classColumnPropertyCache = new HashMap<Class<?>, List<String[]>>();

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
		this.buildTableColumnPropertyInfoFromHibernate();
	}

	//现在是在假定只有一个SessionFactory的情况下
	private Map<String, String[][]> tableColumnPropertyMap = null;

	public List<String[]> getTableColumnPropertyNames(String tableName, String... excludedColumns) {
		String[][] columnPropertys = tableColumnPropertyMap.get(tableName.toLowerCase());
		if (excludedColumns == null || excludedColumns.length == 0) {
			return Arrays.asList(columnPropertys);
		}
		List<String[]> list = new ArrayList<String[]>();
		for (String[] columnProperty : columnPropertys) {
			if (!isIgnoreCaseIn(columnProperty[0], excludedColumns)) {
				list.add(columnProperty);
			}
		}
		return list;
	}

	private static boolean isIgnoreCaseIn(String str, String[] strs) {
		if (strs != null) {
			for (String temp : strs) {
				if (temp != null && temp.equalsIgnoreCase(str)) {
					return true;
				}
			}
		}
		return false;
	}

	private void buildTableColumnPropertyInfoFromHibernate() {
		if (tableColumnPropertyMap == null) {
			synchronized (this) {
				tableColumnPropertyMap = new HashMap<String, String[][]>();
				Map<String, ClassMetadata> metaMap = sessionFactory.getAllClassMetadata();
				for (String key : (Set<String>) metaMap.keySet()) {
					AbstractEntityPersister classMetadata = (AbstractEntityPersister) metaMap.get(key);
					String tableName = classMetadata.getTableName().toLowerCase();
					// System.out.println("\n\ntableName:" + tableName + "");
					String[] propertyNames = classMetadata.getPropertyNames();
					List<String[]> list = new ArrayList<String[]>(propertyNames.length);
					for (String propertyName : propertyNames) {
						String[] columnNames = classMetadata.getPropertyColumnNames(propertyName);
						// 只记录一个字段映射到一个属性的映射，属性如果是以"_" 是hibernate生成的，不记录
						if (columnNames.length == 1 && !propertyName.startsWith("_")) {
							Type type = classMetadata.getPropertyType(propertyName);
							// System.out.println(propertyName + ": " +
							// classMetadata.getPropertyType(propertyName).isAssociationType());
							if (!type.isAssociationType() && !type.isCollectionType() && !type.isComponentType()) {
								list.add(new String[] { columnNames[0], propertyName });
							}
						}
					}

					String identitiyName = classMetadata.getIdentifierPropertyName();
					if (identitiyName != null) {
						String[] columns = classMetadata.getPropertyColumnNames(identitiyName);
						if (columns.length == 1) {
							list.add(new String[] { columns[0], identitiyName });
						}
					}
					String[][] mapping = list.toArray(new String[list.size()][2]);
					tableColumnPropertyMap.put(tableName, mapping);
				}
			}
		}
	}
	
	/**
	 * 返回指定实体的对应的列->简单属性名的列表
	 * @param sessionFactory
	 * @param entityClass
	 * @param includedProperties
	 * @param excludedProperties
	 * @return
	 */
	public List<String[]> getColumnPropertyByClass(Class<?> entityClass, String[] includedProperties, String[] excludedProperties) {
		List<String[]> list = Collections.emptyList();
		excludedProperties = excludedProperties == null ? new String[]{} : excludedProperties;
		includedProperties = includedProperties == null ? new String[]{} : includedProperties;
		List<String> ecludedList = Arrays.asList(excludedProperties);
		List<String> includedList = Arrays.asList(includedProperties);
		
		if(classColumnPropertyCache.containsKey(entityClass)) {
			list = classColumnPropertyCache.get(entityClass);
		} else {
			ClassMetadata meta = sessionFactory.getClassMetadata(entityClass);
			if (meta == null) {
				// TODO 抛异常 ？？
				return Collections.emptyList();
			}
			synchronized (classColumnPropertyCache) {			
				AbstractEntityPersister classMetadata = (AbstractEntityPersister) meta;
				String[] propertyNames = classMetadata.getPropertyNames();
				list = new ArrayList<String[]>(propertyNames.length + 1);

				String identitiyName = classMetadata.getIdentifierPropertyName();
				if (identitiyName != null) {
					String[] columns = classMetadata.getPropertyColumnNames(identitiyName);
					if (columns.length == 1) {
						list.add(new String[] { columns[0], identitiyName });
					}
				}
				
				for (String propertyName : propertyNames) {
					String[] columnNames = classMetadata.getPropertyColumnNames(propertyName);
					// 只记录一个字段映射到一个属性的映射，属性如果是以"_" 是hibernate生成的，不记录
					if (columnNames.length == 1 && columnNames[0]!=null && !propertyName.startsWith("_") ) {
						Type type = classMetadata.getPropertyType(propertyName);
						if (!type.isAssociationType() && !type.isCollectionType() && !type.isComponentType()) {
							list.add(new String[] { columnNames[0], propertyName });
						}
					}
				}
				classColumnPropertyCache.put(entityClass, list);
			}
		}
		
		List<String[]> resultList = new ArrayList<String[]>(list.size());
		for(String[] arr : list) {
			if(!ecludedList.contains(arr[1])) {
				if(includedList.isEmpty() || (!includedList.isEmpty() && includedList.contains(arr[1]))) {
					resultList.add(arr);
				}
			}
		}
		return resultList;
	}
	
	/**
	 * <pre>
	 * 将指定实体的所有属性加到映射中; 映射的别名跟属性名是一样的
	 * 用这个方法而不自已手动一个一个的加有个额外的好外，当实体有变化时，
	 * 加入映射中的属性会相应变化
	 * </pre>
	 * @param entityClass
	 * @param list
	 */
	public void addAllPropertyIntoProjectionList(Class<?> entityClass, ProjectionList list) {
		List<String[]> columnProperties = getColumnPropertyByClass(entityClass, new String[]{}, new String[]{});
		for(String[] arr : columnProperties) {
			list.add(Projections.property(arr[1]), arr[1]);
		}
	}

}

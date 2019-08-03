package com.ctop.fw.common.excelexport;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Id;

import org.hibernate.Session;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import com.ctop.fw.common.entity.CommonRevisionEntity;
import com.ctop.fw.common.utils.AppContextUtil;
import com.ctop.fw.common.utils.BusinessException;
import com.ctop.fw.common.utils.ListUtil;

public class GridColumnEntityRevisionValueGetter extends GridColumnValueGetter implements RowDatasAware{
	private static final long serialVersionUID = 6438949833798577650L;
	private Class<?> entityClaz;
	private Map<String, List<Object>> uuidRevisionsMap;
	private String idField;
	private PlatformTransactionManager transactionManager;
	private EntityManager entityManager;
	private Field field;
	private List<EntityRevisionAuditField> auditFields;
	
	public GridColumnEntityRevisionValueGetter(String idField, String entityClass, List<EntityRevisionAuditField> auditFields) {
		Assert.hasText(entityClass);
		Assert.hasText(idField);
		Assert.notEmpty(auditFields, "实体修改记录字段不能为空!");
		try {
			this.entityClaz = Class.forName(entityClass);
		} catch (ClassNotFoundException e) {
			throw new BusinessException(e, "找不到实体类型{0}", new Object[]{entityClass});
		}
		this.transactionManager = AppContextUtil.getBean(PlatformTransactionManager.class);
		this.entityManager = AppContextUtil.getBean(EntityManager.class);
		this.idField = idField; 
		this.auditFields = auditFields;
	}
	
	public GridColumnEntityRevisionValueGetter(String idField, Class<?> entityClass, List<EntityRevisionAuditField> auditFields) {
		Assert.notNull(entityClass);
		Assert.hasText(idField);
		Assert.notEmpty(auditFields, "实体修改记录字段不能为空!"); 
		this.entityClaz = entityClass;
		this.transactionManager = AppContextUtil.getBean(PlatformTransactionManager.class);
		this.entityManager = AppContextUtil.getBean(EntityManager.class);
		this.idField = idField; 
		this.auditFields = auditFields;
	}

	public GridColumnEntityRevisionValueGetter(GridColumn column) {
		this(column.getAuditedEntityIdField(), column.getAuditedEntityClass(), column.getAuditFields());
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static boolean equals(Object o1, Object o2) {
		if (o1 == null && o2 == null || o1 == o2) {
			return true;
		}
		if (o1 != null && o1.equals(o2)) {
			return true;
		}
		if (o1 != null && o2 == null || o1 == null && o2 != null) {
			return false;
		}
		if(o1 != null && o1 instanceof Comparable) { 
			Comparable f1 = (Comparable) o1;
			Comparable f2 = (Comparable) o2;
			return f1.compareTo(f2) == 0;
		} 
		return false;
	}
	
	public List<Object> getRevisions(GridColumn column, Object rowData) {
		if(this.uuidRevisionsMap == null) {
			this.uuidRevisionsMap = new HashMap<String, List<Object>>();
		}
		if(field == null) {
			this.field = ReflectionUtils.findField(rowData.getClass(), idField);
			this.field.setAccessible(true);
		}
		String uuid = (String) ReflectionUtils.getField(field, rowData);
		List<Object> revisions = this.uuidRevisionsMap.get(uuid);
		return revisions;
	}

	@Override
	public Object getColumnValue(GridColumn column, Object rowData) {
		List<Object> revisions = this.getRevisions(column, rowData);
		if(ListUtil.isEmpty(revisions) || revisions.size() == 1) {
			return Collections.emptyList();
		}
		List<Object> results = revisions.stream().map(obj -> ((Object[]) obj)[0]).collect(Collectors.toList());
		Iterator<Object> it = results.iterator();
		Object pre = it.next();
		while(it.hasNext()) {
			Object cur = it.next();
			
			boolean allEquals = true;
			for(EntityRevisionAuditField auditField : this.auditFields) {
				Field tempField = ReflectionUtils.findField(this.entityClaz, auditField.getField());
				tempField.setAccessible(true);
				Object preValue = ReflectionUtils.getField(tempField, pre);
				Object curValue = ReflectionUtils.getField(tempField, cur);
				if(!equals(preValue, curValue)) {
					allEquals = false;
				}
			}
			if (allEquals) {
				it.remove();
			}
			pre = cur;
		}
		return results;
	}
	
	public EntityRevisionUpdateDateGetter buildEntityRevisionUpdateDateGetter() {
		return new EntityRevisionUpdateDateGetter(this);
	}
	
	public EntityRevisionUpdateContentGetter buildEntityRevisionUpdateContentGetter() {
		return new EntityRevisionUpdateContentGetter(this);
	}
	
	public EntityRevisionUpdateTimesGetter buildEntityRevisionUpdateTimesGetter() {
		return new EntityRevisionUpdateTimesGetter(this);
	}
	
	public static class EntityRevisionUpdateDateGetter extends GridColumnValueGetter implements RowDatasAware {
		private static final long serialVersionUID = 1L;
		GridColumnEntityRevisionValueGetter getter;
		public EntityRevisionUpdateDateGetter(GridColumnEntityRevisionValueGetter getter) {
			this.getter = getter;
		}
		
		public Object getColumnValue(GridColumn column, Object rowData) {
			List<Object> revisions = (List<Object>) this.getter.getRevisions(column, rowData);
			if(revisions.isEmpty()) {
				return null;
			}
			Object[] last = (Object[]) revisions.get(revisions.size() - 1);
			CommonRevisionEntity lastRevisionEntity = (CommonRevisionEntity) last[1];
			return lastRevisionEntity.getRevisionDate();
		}

		@Override
		public void setRowDatas(List<?> rowDatas) {
			this.getter.setRowDatas(rowDatas);
		}
	}
	
	public static class EntityRevisionUpdateContentGetter extends GridColumnValueGetter implements RowDatasAware {
		private static final long serialVersionUID = 1L;
		GridColumnEntityRevisionValueGetter getter;
		public EntityRevisionUpdateContentGetter(GridColumnEntityRevisionValueGetter getter) {
			this.getter = getter;
		}

		@SuppressWarnings("unchecked")
		public Object getColumnValue(GridColumn column, Object rowData) {
			StringBuilder revisionText = new StringBuilder();
			List<Object> revisions = (List<Object>) getter.getColumnValue(column, rowData);
			if(revisions.isEmpty()) {
				return null;
			}
			for(EntityRevisionAuditField auditField : this.getter.getAuditFields()) {
				Field tempField = ReflectionUtils.findField(this.getter.entityClaz, auditField.getField());
				tempField.setAccessible(true);
				//每个字段的值变更历史
				List<Object> fieldValues = new ArrayList<Object>();
				for(Object revision : revisions) {
					Object fieldValue = ReflectionUtils.getField(tempField, revision);
					Object last = fieldValues.size() > 0 ? fieldValues.get(fieldValues.size() - 1) : null;
					if(!GridColumnEntityRevisionValueGetter.equals(fieldValue, last)) {
						fieldValues.add(fieldValue);
					}
				}
				//生成： 数量更改(1-> 2)
				if(fieldValues.size() > 1) {
					String text = fieldValues.stream().map(d -> d != null ? d.toString() : "").collect(Collectors.joining(" -> "));
					revisionText.append(auditField.getLabel()).append(": ").append(text).append(";");
				}
			}
			return revisionText.toString();
		}
		
		@Override
		public void setRowDatas(List<?> rowDatas) {
			this.getter.setRowDatas(rowDatas);
		}
	}
	
	public static class EntityRevisionUpdateTimesGetter extends GridColumnValueGetter implements RowDatasAware {
		private static final long serialVersionUID = 1L;
		GridColumnEntityRevisionValueGetter getter;
		public EntityRevisionUpdateTimesGetter(GridColumnEntityRevisionValueGetter getter) {
			this.getter = getter;
		}

		@SuppressWarnings("unchecked")
		public Object getColumnValue(GridColumn column, Object rowData) {
			List<Object> revisions = (List<Object>) getter.getColumnValue(column, rowData);
			if(revisions.isEmpty()) {
				return null;
			}
			int times = revisions.size() - 1;
			return times == 0 ? null : times;
		}
		
		@Override
		public void setRowDatas(List<?> rowDatas) {
			this.getter.setRowDatas(rowDatas);
		}
	}
	 
	private List<?> _rowDatas = null;
	/**
	 * 用来批量取多条实体数据的日志记录；
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void setRowDatas(List<?> rowDatas) {
		if (this._rowDatas == rowDatas) {
			return;
		}
		this._rowDatas = rowDatas;
		List<String> uuids = ListUtil.getOneFieldValue(rowDatas, this.idField, String.class);
		DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
		definition.setReadOnly(true);
		TransactionStatus transaction = transactionManager.getTransaction(definition);
		try {
			Session session = this.entityManager.unwrap(Session.class);
			AuditReader reader = AuditReaderFactory.get(session);
			// 根据版本号， 实体主键，找到修改之前该版本的数据
			//TODO 改成按更新时间倒序
			List<Object> entityRevisions = reader.createQuery()
					.forRevisionsOfEntity(this.entityClaz, false, true)
					.add(AuditEntity.id().in(uuids)).addOrder(AuditEntity.id().asc())
					.addOrder(AuditEntity.revisionNumber().asc()).getResultList();
			
			Field tempField = null;
			if(!entityRevisions.isEmpty()) {
				Object[] firstPair =  (Object[]) entityRevisions.get(0);
				Object first = firstPair[0];
				Field[] fields = first.getClass().getDeclaredFields();
				for(Field field : fields) {
					if(field.isAnnotationPresent(Id.class)) {
						tempField = field;
					}
				}
				if(tempField != null) {
					tempField.setAccessible(true);
				}
			}
			final Field idField = tempField;
			this.uuidRevisionsMap = entityRevisions.stream().collect(Collectors.groupingBy((Object row) -> {
				if(idField != null) {
					Object first = ((Object[])row)[0];
					return (String) ReflectionUtils.getField(idField, first);
				}
				return "";
			}));
			transactionManager.commit(transaction);
		} catch (Exception e) {
			transactionManager.rollback(transaction);
			e.printStackTrace();
		}
	}

	public String getIdField() {
		return idField;
	}

	public void setIdField(String idField) {
		this.idField = idField;
	}

	public List<EntityRevisionAuditField> getAuditFields() {
		return auditFields;
	}

	public void setAuditFields(List<EntityRevisionAuditField> auditFields) {
		this.auditFields = auditFields;
	}

	public Class<?> getEntityClaz() {
		return entityClaz;
	}

	public void setEntityClaz(Class<?> entityClaz) {
		this.entityClaz = entityClaz;
	}
}

package com.ctop.fw.common.excelexport;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Id;

import org.apache.poi.ss.usermodel.Cell;
import org.hibernate.Session;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import com.ctop.fw.common.utils.AppContextUtil;
import com.ctop.fw.common.utils.BusinessException;
import com.ctop.fw.common.utils.ListUtil;

public class EntityRevisionCellRenderer implements CellRenderer, RowDatasAware {

	private Class<?> entityClaz;
	private Map<String, List<Object>> uuidRevisionsMap;
	private String idField;
	private PlatformTransactionManager transactionManager;
	private EntityManager entityManager;
	private Field field;
	private List<EntityRevisionAuditField> auditFields;
	
	public EntityRevisionCellRenderer(String idField, String entityClass, List<EntityRevisionAuditField> auditFields) {
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

	public EntityRevisionCellRenderer(GridColumn column) {
		this(column.getAuditedEntityIdField(), column.getAuditedEntityClass(), column.getAuditFields());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void render(Cell cell, Object value, Object bean,GridColumn gridColumn) {
		if(this.uuidRevisionsMap == null) {
			this.uuidRevisionsMap = new HashMap<String, List<Object>>();
		}
		if(field == null) {
			this.field = ReflectionUtils.findField(bean.getClass(), idField);
			this.field.setAccessible(true);
		}
		String uuid = (String) ReflectionUtils.getField(field, bean);
		List<Object> revisions = this.uuidRevisionsMap.get(uuid);
		if(ListUtil.isEmpty(revisions) || revisions.size() == 1) {
			return;
		}
		StringBuilder revisionText = new StringBuilder();
		for(EntityRevisionAuditField auditField : this.auditFields) {
			Field tempField = ReflectionUtils.findField(this.entityClaz, auditField.getField());
			tempField.setAccessible(true);
			//每个字段的值变更历史
			List<Object> fieldValues = new ArrayList<Object>();
			for(Object revision : revisions) {
				Object fieldValue = ReflectionUtils.getField(tempField, revision);
				if(fieldValues.size() > 0) {
					Object last = fieldValues.get(fieldValues.size() - 1);
					if(fieldValue != null && fieldValue.equals(last) || (fieldValue == null && last == null)) {
						continue;
					} else if(fieldValue != null && fieldValue instanceof Comparable) {
						if(last == null) {
							fieldValues.add(fieldValue);
							continue;
						}
						Comparable f1 = (Comparable) fieldValue;
						Comparable f2 = (Comparable) last;
						if(f1.compareTo(f2) != 0) {
							fieldValues.add(fieldValue);
						}
					} else if(fieldValue == null && last != null) {
						fieldValues.add(fieldValue);
					}
				} else {
					fieldValues.add(fieldValue);
				}
			}
			//生成： 数量更改(1-> 2)
			if(fieldValues.size() > 1) {
				String text = fieldValues.stream().map(d -> d != null ? d.toString() : "").collect(Collectors.joining(" -> "));
				revisionText.append(auditField.getLabel()).append(": ").append(text).append(";");
			}
		}
		cell.setCellValue(revisionText.toString());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setRowDatas(List<?> rowDatas) {
		
		List<String> uuids = ListUtil.getOneFieldValue(rowDatas, this.idField, String.class);
		DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
		definition.setReadOnly(true);
		TransactionStatus transaction = transactionManager.getTransaction(definition);
		try {
			Session session = this.entityManager.unwrap(Session.class);
			AuditReader reader = AuditReaderFactory.get(session);
			// 根据版本号， 实体主键，找到修改之前该版本的数据
			List<Object> entityRevisions = reader.createQuery()
					.forRevisionsOfEntity(this.entityClaz, true, true)
					.add(AuditEntity.id().in(uuids)).addOrder(AuditEntity.id().asc())
					.addOrder(AuditEntity.revisionNumber().asc()).getResultList();
			
			Field tempField = null;
			if(!entityRevisions.isEmpty()) {
				Object first =  entityRevisions.get(0);
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
					return (String) ReflectionUtils.getField(idField, row);
				}
				return "";
			}));
			transactionManager.commit(transaction);
		} catch (Exception e) {
			transactionManager.rollback(transaction);
			e.printStackTrace();
		}
	}

}

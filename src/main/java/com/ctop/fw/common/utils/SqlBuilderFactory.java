package com.ctop.fw.common.utils;

import javax.persistence.EntityManagerFactory;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SqlBuilderFactory {
	
	private static SessionFactory sessionFactory;
	
	/**
	 * 方便静态方法中访问sessionFactory;
	 * @param sessionFactory
	 */
	@Autowired
	public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
		SqlBuilderFactory.sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
	}
	
	/**
	 * @return
	 */
	public static SqlBuilder sqlBuilder() {
		EntityMetadataUtil metaUtil = new EntityMetadataUtil();
		metaUtil.setSessionFactory(SqlBuilderFactory.sessionFactory);
		SqlBuilder builder = new SqlBuilder(metaUtil);
		return builder;
	}
}

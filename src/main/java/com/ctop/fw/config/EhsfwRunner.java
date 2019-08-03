package com.ctop.fw.config;

import java.util.Date;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.event.spi.MergeEvent;
import org.hibernate.event.spi.MergeEventListener;
import org.hibernate.event.spi.PersistEvent;
import org.hibernate.event.spi.PersistEventListener;
import org.hibernate.event.spi.SaveOrUpdateEvent;
import org.hibernate.event.spi.SaveOrUpdateEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.ctop.fw.common.entity.BaseEntity;
import com.ctop.fw.common.entity.ICreatedTracker;
import com.ctop.fw.common.utils.StringUtil;
import com.ctop.fw.common.utils.UserContextUtil;

/**
 * 系统启动后执行的代码；
 */
@Component
public class EhsfwRunner implements ApplicationRunner {
	private static Logger logger = LoggerFactory.getLogger(EhsfwRunner.class);
 
	@Autowired
	EntityManagerFactory entityManagerFactory;

	@Override
	public void run(ApplicationArguments arg0) throws Exception {
		SessionFactoryImplementor sessionFactory = entityManagerFactory.unwrap( SessionFactoryImplementor.class );
		logger.info("配置实体持久化的监听器...");
		EventListenerRegistry registry = sessionFactory.getServiceRegistry().getService(EventListenerRegistry.class);
		registry.prependListeners(EventType.SAVE_UPDATE, new WMSSaveOrUpdateEventListener());
		registry.prependListeners(EventType.PERSIST, new WMSPersistEventListener());
		registry.prependListeners(EventType.MERGE, new WMSMergeEventListener());
	}
	
	public static void setBaseFieldInfo(Object obj) {
		String accountUuid = UserContextUtil.getAccountUuid();
		if (obj instanceof BaseEntity) {
			BaseEntity entity = (BaseEntity) obj;
			if(StringUtil.isEmpty(entity.getCreatedBy()) && StringUtil.isNotEmpty(accountUuid)) {
				entity.setCreatedBy(accountUuid);
			}
			if(entity.getCreatedDate() == null) {
				entity.setCreatedDate(new Date());
			}
			if (StringUtil.isNotEmpty(accountUuid)) {
				entity.setUpdatedBy(accountUuid);
			}
			entity.setUpdatedDate(new Date());
			if(entity.getIsActive() == null) {
				entity.setIsActive("Y");
			}
		} else if(obj instanceof ICreatedTracker) {
			ICreatedTracker entity = (ICreatedTracker) obj;
			if(StringUtil.isEmpty(entity.getCreatedBy())) {
				if (StringUtil.isNotEmpty(accountUuid)) {
					entity.setCreatedBy(accountUuid);
				}
			}
			if(entity.getCreatedDate() == null) {
				entity.setCreatedDate(new Date());
			}
			if(entity.getIsActive() == null) {
				entity.setIsActive("Y");
			}
		}
	}
	
	public static class WMSPersistEventListener implements PersistEventListener {

		private static final long serialVersionUID = 5377122461050008113L;

		@Override
		public void onPersist(PersistEvent event) throws HibernateException {
			logger.debug("持久化实体...");
			setBaseFieldInfo(event.getObject());
		}

		@Override
		@SuppressWarnings("rawtypes")
		public void onPersist(PersistEvent event, Map createdAlready) throws HibernateException {
			logger.debug("持久化实体(createdAlready:true)..."); 
			setBaseFieldInfo(event.getObject());
		}
	}
	
	public static class WMSMergeEventListener implements MergeEventListener {
		private static final long serialVersionUID = 8555491285380280797L;

		public void onMerge(MergeEvent event) throws HibernateException {
			logger.debug("持久化MergeEvent...");
			setBaseFieldInfo(event.getOriginal());
		}

		@SuppressWarnings("rawtypes")
		public void onMerge(MergeEvent event, Map copiedAlready) throws HibernateException {
			setBaseFieldInfo(event.getOriginal());
		}
	}

	public static class WMSSaveOrUpdateEventListener implements SaveOrUpdateEventListener {

		/**
		 * 
		 */
		private static final long serialVersionUID = 8334973052196102060L;

		@Override
		public void onSaveOrUpdate(SaveOrUpdateEvent event) throws HibernateException {
			logger.debug("持久化实体(onSaveOrUpdate)...");
			setBaseFieldInfo(event.getEntity());
		}
	}
}
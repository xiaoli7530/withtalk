package com.ctop.fw.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

 
@Configuration
@EnableTransactionManagement
@ImportResource({"classpath:application-context.xml"})
public class JpaConfig {

	@Autowired
	Environment env;
 
//	@Primary //默认数据源
//	@Bean(name = "dataSource")
//	@ConfigurationProperties(prefix = "datasource.oracle")
//	public DataSource dataSource() {
//		return DataSourceBuilder.create().build();
//	}

//	@Bean(name = "entityManagerFactory")
//	@Qualifier("dataSource")
//	public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
//		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
//		vendorAdapter.setDatabase(Database.ORACLE);
//		vendorAdapter.setGenerateDdl(true); 
//		vendorAdapter.setShowSql(true); 
//		vendorAdapter.getJpaPropertyMap().put("ddlAuto", "update");
//
//		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
//		factory.setJpaVendorAdapter(vendorAdapter);
//		factory.setPackagesToScan(getClass().getPackage().getName());
//		factory.setDataSource(dataSource);
//		
//
//		return factory;
//	}
//
//	@Bean
//	@Qualifier("entityManagerFactory")
//	public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
//		JpaTransactionManager txManager = new JpaTransactionManager();
//		txManager.setEntityManagerFactory(entityManagerFactory);
//		return txManager;
//	}
	
	@Bean
	@Qualifier("jdbcTemplate")
	public JdbcTemplate jdbcTemplate(DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}
	
//	@Bean
//	@Qualifier("transactionManager") 
//	public PlatformTransactionManager  transactionManager(){
//		return new JTATransactionManager();
//	}
//	
	 
}
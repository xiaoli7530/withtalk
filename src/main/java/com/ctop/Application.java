package com.ctop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 文档地址：
 * http://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle
 *  
 * 命令行启动：
 *  java -jar target/wms.jar
 *  支持用eclipse远程调试，debug模式
 *  java -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=8000,suspend=n  -jar target/wms.jar
 *  用mvn启动
 *  mvn spring-boot:run
 */ 
@SpringBootApplication
@ImportResource()//指定XML配置文件
@Configuration //配置文件类
//自动生成配置数据，加启动参数--debug 查看默认生成的配置
//@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
@ComponentScan //自动扫描包 com.ctop开头 下的spring组件 (@Component, @Service, @Repository, @Controller)
@EntityScan
@EnableJpaRepositories
@EnableCaching
@EnableScheduling
@EnableTransactionManagement
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@Bean
	public EmbeddedServletContainerCustomizer containerCustomizer() {

	    return new EmbeddedServletContainerCustomizer() {
	        @Override
	        public void customize(ConfigurableEmbeddedServletContainer container) {

	            ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/404.html");
	            container.addErrorPages(error404Page);
	        }
	    };
	}
}

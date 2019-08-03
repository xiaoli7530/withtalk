package com.ctop.fw.config;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.filter.OrderedCharacterEncodingFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.support.ErrorPageFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.ctop.fw.common.utils.BusinessException;


@Configuration
// @EnableWebSecurity
// @EnableWebMvcSecurity
public class WebConfig implements ServletContextAware {
	static Logger logger = LoggerFactory.getLogger(WebConfig.class);
	private ServletContext servletContext;

/*	@Value("${fw.user_security.excludes}")
	private String fwExcludes;*/

	@Bean
	RestTemplate buildEpmsRestTemplate() {
		RestTemplate restTemplate = new RestTemplateBuilder()
				// .rootUri(this.ltsRestRoot)
				.errorHandler(new ResponseErrorHandler() {

					@Override
					public boolean hasError(ClientHttpResponse response) throws IOException {
						HttpStatus status = response.getStatusCode();
						return status.is5xxServerError() || status.is4xxClientError();
					}

					@Override
					public void handleError(ClientHttpResponse response) throws IOException {
						if(response != null) {
							throw new BusinessException(response);
						}
					}})
				.build();
		ClientHttpRequestFactory httpFactory = ((HttpComponentsClientHttpRequestFactory) (restTemplate
				.getRequestFactory()));
		if (httpFactory instanceof HttpComponentsClientHttpRequestFactory) {// 设置http超时时间,避免长时间阻塞
			((HttpComponentsClientHttpRequestFactory) httpFactory).setConnectTimeout(10000);
			((HttpComponentsClientHttpRequestFactory) httpFactory).setReadTimeout(30000 * 4);
		} else {
			throw new RuntimeException(httpFactory + ",不为HttpComponentsClientHttpRequestFactory");
		}
		return restTemplate;
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/rest/**").allowedOrigins("*")
						.allowedMethods("GET", "PUT", "DELETE", "POST", "DELETE", "OPTIONS").allowCredentials(true)
						.allowedHeaders("accept", "content-type", "content-length", "file-name").maxAge(3600);
			}

			/**
			 */
			@Override
			public void addInterceptors(InterceptorRegistry registry) {
				// 不做拦截的接口；
			//	List<String> excludes = Arrays.asList(fwExcludes.split(","));
				// WEB请求的拦截器
				UserSecurityInterceptor security = new UserSecurityInterceptor();
				InterceptorRegistration registration = registry.addInterceptor(security).addPathPatterns("/pages/**")
						.addPathPatterns("/p/**").addPathPatterns("/rest/**").addPathPatterns("/bpm/**");

			/*	for (String exclude : excludes) {
					registration.excludePathPatterns(exclude);
				}*/
			}
		};
	}

	/**
	 * 加日志的filter
	 * 
	 * @return
	 */
	@Bean
	public FilterRegistrationBean registerLogFilter() {
		FilterRegistrationBean bean = new FilterRegistrationBean();
		LogFilter filter = new LogFilter();
		filter.setIncludeRequestPayload(true);
		filter.setIncludeResponsePayload(true);
		filter.setMaxPayloadLength(100000);
		bean.setFilter(filter);
		bean.setUrlPatterns(Arrays.asList("/rest/rf/*", "/rest/sys/sysAccount/login"));
		return bean;
	}

	@Bean
	public FilterRegistrationBean registerSsoUserSyncFilter() {
		FilterRegistrationBean bean = new FilterRegistrationBean();
		SsoUserSyncFilter filter = new SsoUserSyncFilter();
		bean.setUrlPatterns(Arrays.asList("*"));
		bean.setFilter(filter);
		bean.setAsyncSupported(true);
		return bean;
	}

	/**
	 * API接口设置response头, 以防浏览器缓存数据请求；
	 * 
	 * @return
	 */
	@Bean
	public FilterRegistrationBean registerNoCacheFilter() {
		FilterRegistrationBean bean = new FilterRegistrationBean();
		NoCacheFilter filter = new NoCacheFilter();
		filter.setPathPrefixes(Arrays.asList("/rest"));
		bean.setFilter(filter);
		bean.setAsyncSupported(true);
		return bean;
	}

	@Bean
	public FilterRegistrationBean registerOrderedCharacterEncodingFilter() {
		FilterRegistrationBean bean = new FilterRegistrationBean();
		OrderedCharacterEncodingFilter filter = new OrderedCharacterEncodingFilter();
		filter.setEncoding("UTF-8");
		filter.setForceRequestEncoding(true);
		filter.setForceResponseEncoding(true);
		bean.setFilter(filter);
		bean.setAsyncSupported(true);
		return bean;
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	@Bean
	public ErrorPageFilter errorPageFilter() {
		return new ErrorPageFilter();
	}

	@Bean
	public FilterRegistrationBean disableSpringBootErrorFilter(ErrorPageFilter filter) {
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		filterRegistrationBean.setFilter(filter);
		filterRegistrationBean.setEnabled(false);
		return filterRegistrationBean;
	}
}
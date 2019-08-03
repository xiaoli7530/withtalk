package com.ctop.fw.schedule;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import com.ctop.base.dto.BaseCountrysDto;
import com.ctop.base.service.BaseCountrysService;
import com.ctop.base.service.BaseDictService;
import com.ctop.fw.common.utils.AppContextUtil;
import com.ctop.fw.common.utils.StringUtil;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class JavascriptDataSchedule implements ServletContextAware {
	private static Logger logger = LoggerFactory.getLogger(JavascriptDataSchedule.class);

	@Autowired
	BaseDictService baseDictService; 
	@Autowired
	BaseCountrysService baseCountrysService;
	
	private ServletContext servletContext;
	private Map<String, CachedJsData> map = new HashMap<>();
	
	/**
	 * 十分钟生成一次
	 */
	@Scheduled(fixedRate = 1000 * 60 * 10, initialDelay=2000)
	public void generateBaseDictSortScript() {
		logger.info("生成精简版数据字典脚本数据");
		Map<String, List<Map<String, String>>> dictsMap = baseDictService.findAllAvailableShort();
		CachedJsData dists = map.get("shortDicts");
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
			mapper.setSerializationInclusion(Include.NON_NULL);
			String json = mapper.writeValueAsString(dictsMap);
			String content = "window.dicts = " + json;
			if(dists != null && StringUtil.equals(content, dists.content)) {
				return;
			}
			if (dists == null) {
				dists = new CachedJsData();
				dists.setName("dicts");
				this.map.put("dicts", dists);
			}
			dists.setLastModifiedDate(new Date());
			dists.setContent(content);
		} catch (IOException e) {
			logger.error("生成数据字典脚本数据异常!", e);
		}
	}
	
	public void generateBaseDictScriptAsync() {
		Executors.newCachedThreadPool().execute(new Runnable() {
			public void run() {
				try { 
					generateBaseDictSortScript();  
				} catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * 这个基本不会变；
	 */
	@Scheduled(fixedRate = 1000 * 60 * 60, initialDelay=5000)
	public void generateCountrysScript() {
		logger.info("服务启动完成...");
	/*	logger.info("生成国家城市省份脚本数据");
		List<BaseCountrysDto> countrys = this.baseCountrysService.findAllCountrysAndAreas();
		CachedJsData countrysData = map.get("countrys");
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
			mapper.setSerializationInclusion(Include.NON_NULL);
			String json = mapper.writerWithView(BaseCountrysDto.SimplifyView.class).writeValueAsString(countrys);
			String content = "window.countrys = " + json;
			if(countrysData != null && StringUtil.equals(countrysData.content, content) ) {
				return;
			}
			if (countrysData == null) {
				countrysData = new CachedJsData();
				countrysData.setName("countrys");
				this.map.put("countrys", countrysData);
			}
			countrysData.setLastModifiedDate(new Date());
			countrysData.setContent(content);
		} catch (IOException e) {
			logger.error("生成国家城市省份脚本数据!", e);
		}*/
	}
	
	
	
	
	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
	
	public CachedJsData getJsData(String name) {
		Environment env = AppContextUtil.getBean(Environment.class);
		// 开发，测试环境 实时生成；
		if (env.acceptsProfiles("development", "test")) {
//			if ("countrys".equals(name)) {
//				this.generateCountrysScript();
//			} 
			if ("dicts".equals(name)) {
				this.generateBaseDictSortScript();
			} 
		}
		return this.map.get(name);
	}
	

	public static class CachedJsData {
		private Date lastModifiedDate;
		private String name;
		private String content;
		
		public Date getLastModifiedDate() {
			return lastModifiedDate;
		}
		public void setLastModifiedDate(Date lastModifiedDate) {
			this.lastModifiedDate = lastModifiedDate;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
	}
}

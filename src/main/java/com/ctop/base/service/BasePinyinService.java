package com.ctop.base.service;

import java.util.List;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.ctop.base.dto.BasePinyinDto;
import com.ctop.base.dto.PinyinBuilder;
import com.ctop.fw.common.utils.SqlBuilder;
import com.ctop.fw.common.utils.SqlBuilderFactory;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * <pre>
 * 功能说明：
 * 示例程序如下：
 * 创建者：${user}
 * 创建时间：${date}
 * 版本：${version}
 * </pre>
 */
@Service
@Transactional
public class BasePinyinService implements ApplicationContextAware{
	private static Logger logger = LoggerFactory.getLogger(BasePinyinService.class);
	@Autowired
	private EntityManager entityManager;
	private ApplicationContext applicationContext;
	private PinyinBuilder pinyinBuilder;
	 
	@PostConstruct
	public void init() {
		Executors.newCachedThreadPool().execute(new Runnable() {
			public void run() {
				try {
					logger.info("初始化拼音库...");
					initPinyinData(); 
				} catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		});
	}
	
	public void initPinyinData() { 
		try {
			Resource resource = applicationContext.getResource("classpath:pinyin.json");
			ObjectMapper mapper = new ObjectMapper();
			pinyinBuilder = mapper.readValue(resource.getInputStream(), PinyinBuilder.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(pinyinBuilder == null) {
			pinyinBuilder = new PinyinBuilder();
			pinyinBuilder.initWithPinyinList(this.findAllPinyin());
		}
	}
	
	/**
	 * 生成拼音：生成的拼音中包含全拼的拼音及缩写的拼音(只含头字母) -- 格式：pdl pengdailiang
	 * @param text
	 * @return
	 */
	public String generatePinyin(String text) {
		if(this.pinyinBuilder == null) {
			this.initPinyinData();
		}
		return this.pinyinBuilder.generatePinyin(text);
	}
	 
	private List<BasePinyinDto> findAllPinyin() {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select chs as \"chs\", py as \"py\" from BASE_PINYIN order by chs asc ");
		return sql.query(entityManager, BasePinyinDto.class);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}


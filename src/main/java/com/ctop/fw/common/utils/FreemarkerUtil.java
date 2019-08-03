package com.ctop.fw.common.utils;

import java.io.File;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import freemarker.template.Configuration;
import freemarker.template.Template;


public class FreemarkerUtil {

	private static FreemarkerUtil _instance = new FreemarkerUtil();
	private String configPath;
	private Configuration cfg;
	private FreemarkerUtil() {
		cfg=this.initConfig("");
	}

	public static FreemarkerUtil getInstance() {
		return _instance;
	}
	
	public static FreemarkerUtil getInstance(String rootPath) {
		if(_instance.configPath==null||!_instance.configPath.equals(rootPath)) {
			_instance.cfg = _instance.initConfig(rootPath);
		}		
		return _instance;
	}

	/**
	 * 用统一的方法初始化配置信息
	 * 
	 * @param configPath
	 * @return
	 */
	private Configuration initConfig(String configPath) {
		this.configPath = configPath;
		Configuration ccfg = new Configuration();
		try {
			ccfg.setDirectoryForTemplateLoading(new File(configPath));
			ccfg.setNumberFormat("#");
			ccfg.setEncoding(Locale.getDefault(), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ccfg;
	}


	/**
	 * 根据map里面的数据和模板生成最终内容(根目录为：WEB-INF\templet)
	 * 
	 * @param map
	 * @param templet
	 * @return
	 */
	public String process(Map<?, ?> map, String templet) {
		return this.process(cfg, map, templet);
	}
	
	public String process(Object map, String templet) {
		return this.process(cfg, map, templet);
	}

	/**
	 * 根据map里面的数据和模板生成最终内容
	 * 
	 * @param map
	 * @param templet
	 * @return
	 */
	public String process(Configuration ccfg, Map<?, ?> map, String templet) {
		String r = "";
		StringWriter out = new StringWriter();
		try {
			Template t = ccfg.getTemplate(templet);
			t.process(map, out);
			r = out.getBuffer().toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return r;
	}
	
	public String process(Configuration ccfg, Object map, String templet) {
		String r = "";
		StringWriter out = new StringWriter();
		try {
			Template t = ccfg.getTemplate(templet);
			t.process(map, out);
			r = out.getBuffer().toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return r;
	}

	/**
	 * 根据目录和模板名称生成内容
	 * 
	 * @param map
	 * @param dic
	 * @param templet
	 * @return
	 */
	public String process(Map<?, ?> map, String dic, String templet) {
		String r = "";
		Configuration cfg2 =  this.initConfig(dic);
		r=this.process(cfg2, map, templet);
		return r;
	}


}

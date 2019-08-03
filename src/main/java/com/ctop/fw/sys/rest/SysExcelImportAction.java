package com.ctop.fw.sys.rest;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ctop.fw.common.model.NuiPageRequestData;
import com.ctop.fw.common.model.PageResponseData;
import com.ctop.fw.common.utils.AppContextUtil;
import com.ctop.fw.common.utils.StringUtil;
import com.ctop.fw.sys.entity.SysExcelImport;
import com.ctop.fw.sys.excelImport.support.SysExcelImportBuilder;
import com.ctop.fw.sys.service.SysExcelImportService;

@RestController
@RequestMapping(path = "/rest/sys/sysExcelImport")
public class SysExcelImportAction {
	private static Logger logger = LoggerFactory.getLogger(SysExcelImportAction.class);

	@Autowired
	SysExcelImportService sysExcelImportService;

	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseBody
	public PageResponseData<SysExcelImport> getSysExcelImportsList(@RequestBody NuiPageRequestData request) {
		return sysExcelImportService.sqlPageQuery(request);
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	@ResponseBody
	public SysExcelImport getSysExcelImport(@RequestParam("importUuid") String importUuid) {
		return sysExcelImportService.getById(importUuid);
	}

	@RequestMapping(value = "/findByImportCode", method = RequestMethod.GET)
	@ResponseBody
	public SysExcelImport findByImportCode(@RequestParam("importCode") String importCode) {
		return sysExcelImportService.findByImportCode(importCode);
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseStatus(HttpStatus.CREATED)
	public SysExcelImport createSysExcelImport(@RequestBody SysExcelImport sysExcelImport) {
		return this.sysExcelImportService.addSysExcelImport(sysExcelImport);
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseStatus(HttpStatus.OK)
	public SysExcelImport updateSysExcelImport(@RequestBody SysExcelImport sysExcelImport) {
		return this.sysExcelImportService.updateSysExcelImport(sysExcelImport);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void deleteSysExcelImport(@RequestParam("importUuid") String importUuid) {
		sysExcelImportService.deleteSysExcelImport(importUuid);
	}

	@RequestMapping(value = "/deleteList", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void deleteSysExcelImports(@RequestBody List<String> importUuids) {
		sysExcelImportService.deleteSysExcelImports(importUuids);
	}

	@RequestMapping(value = "/addSysExcelImportByScript", method = RequestMethod.POST, produces = {
			"application/json" })
	@ResponseStatus(HttpStatus.CREATED)
	public void addSysExcelImportByScript(@RequestBody String script) {
		SysExcelImportBuilder.callBuildScript(script);
	}

	@RequestMapping(value = "/downloadTemplate", method = RequestMethod.GET)
	public void downloadTemplate(@RequestParam("importCode") String importCode, HttpServletRequest request, HttpServletResponse response) {
	    try {
	        String templateUrl = this.sysExcelImportService.getTemplateUrl(importCode);
	        if(templateUrl != null) { 
		        Resource resource = AppContextUtil.getContext().getResource(templateUrl);
	            response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
	            response.setContentLength((int) resource.contentLength());
	            response.setCharacterEncoding("utf-8");
	            
	            String name = resource.getFilename();  
	            String userAgent = request.getHeader("User-Agent");  
	    		name = StringUtil.encodeDownloadName(name, userAgent);
	            response.setHeader("Content-disposition", String.format("attachment; filename=\"%s\"", name));
	            
//	            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(resource.getFilename(), "utf-8")); 
		        InputStream is = resource.getInputStream();
		       	IOUtils.copy(resource.getInputStream(), response.getOutputStream());
		        is.close();
		        response.flushBuffer();
	        }
	    } catch (IOException ex) {
	        ex.printStackTrace();
	    }
	}
}

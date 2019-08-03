package com.ctop.base.rest;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map; 
import java.util.Set;

import com.ctop.fw.common.model.NuiPageRequestData;
import com.ctop.fw.common.model.PageResponseData;
import com.ctop.fw.common.utils.CommonAssembler;

import com.ctop.base.dto.BasePageTemplateDto;
import com.ctop.base.entity.BasePageTemplate;
import com.ctop.base.service.BasePageTemplateService;

@RestController
@RequestMapping(path = "/rest/base/basePageTemplate")
public class BasePageTemplateAction {
	private static Logger logger = LoggerFactory.getLogger(BasePageTemplateAction.class);

	@Autowired
	BasePageTemplateService basePageTemplateService;
	
	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseBody
	public PageResponseData<BasePageTemplateDto> getBasePageTemplatesList(@RequestBody NuiPageRequestData request) {
		return basePageTemplateService.sqlPageQuery(request);
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	@ResponseBody
	public BasePageTemplateDto getBasePageTemplate(@RequestParam("pptUuid") String pptUuid) {
		return basePageTemplateService.getById(pptUuid);
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseStatus(HttpStatus.CREATED)
	public BasePageTemplateDto createBasePageTemplate(@RequestBody BasePageTemplateDto basePageTemplateDto) {
		return this.basePageTemplateService.addBasePageTemplate(basePageTemplateDto);
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseStatus(HttpStatus.OK)
	public BasePageTemplateDto updateBasePageTemplate(@RequestBody BasePageTemplateDto basePageTemplateDto) {
		//从requst中获取有效的参数进行修改，确保不会修改到无关的字段
		Set<String> httpParams =  CommonAssembler.getExistsRequestParam(basePageTemplateDto);
		return this.basePageTemplateService.updateBasePageTemplate(basePageTemplateDto,httpParams);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void deleteBasePageTemplate(@RequestParam("pptUuid") String pptUuid) {
		basePageTemplateService.deleteBasePageTemplate(pptUuid);
	}
		
	@RequestMapping(value = "/deleteList", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void deleteBasePageTemplates(@RequestBody List<String> pptUuids) {
		basePageTemplateService.deleteBasePageTemplates(pptUuids);
	}
}

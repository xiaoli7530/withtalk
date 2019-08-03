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

import com.ctop.base.dto.BaseMonitorStandardDto;
import com.ctop.base.entity.BaseMonitorStandard;
import com.ctop.base.service.BaseMonitorStandardService;

@RestController
@RequestMapping(path = "/rest/base/baseMonitorStandard")
public class BaseMonitorStandardAction {
	private static Logger logger = LoggerFactory.getLogger(BaseMonitorStandardAction.class);

	@Autowired
	BaseMonitorStandardService baseMonitorStandardService;
	
	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseBody
	public PageResponseData<BaseMonitorStandardDto> getBaseMonitorStandardsList(@RequestBody NuiPageRequestData request) {
		return baseMonitorStandardService.sqlPageQuery(request);
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	@ResponseBody
	public BaseMonitorStandardDto getBaseMonitorStandard(@RequestParam("msUuid") String msUuid) {
		return baseMonitorStandardService.getById(msUuid);
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseStatus(HttpStatus.CREATED)
	public BaseMonitorStandardDto createBaseMonitorStandard(@RequestBody BaseMonitorStandardDto baseMonitorStandardDto) {
		return this.baseMonitorStandardService.addBaseMonitorStandard(baseMonitorStandardDto);
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseStatus(HttpStatus.OK)
	public BaseMonitorStandardDto updateBaseMonitorStandard(@RequestBody BaseMonitorStandardDto baseMonitorStandardDto) {
		//从requst中获取有效的参数进行修改，确保不会修改到无关的字段
		Set<String> httpParams =  CommonAssembler.getExistsRequestParam(baseMonitorStandardDto);
		return this.baseMonitorStandardService.updateBaseMonitorStandard(baseMonitorStandardDto,httpParams);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void deleteBaseMonitorStandard(@RequestParam("msUuid") String msUuid) {
		baseMonitorStandardService.deleteBaseMonitorStandard(msUuid);
	}
		
	@RequestMapping(value = "/deleteList", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void deleteBaseMonitorStandards(@RequestBody List<String> msUuids) {
		baseMonitorStandardService.deleteBaseMonitorStandards(msUuids);
	}
}

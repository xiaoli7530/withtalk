package com.ctop.fw.sys.rest;


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

import com.ctop.fw.sys.dto.SysRemindConfigDto;
import com.ctop.fw.sys.entity.SysRemindConfig;
import com.ctop.fw.sys.service.SysRemindConfigService;


@RestController
@RequestMapping(path = "/rest/sys/sysRemindConfig")
public class SysRemindConfigAction {
	private static Logger logger = LoggerFactory.getLogger(SysRemindConfigAction.class);

	@Autowired
	SysRemindConfigService sysRemindConfigService;
	
	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseBody
	public PageResponseData<SysRemindConfigDto> getSysRemindConfigsList(@RequestBody NuiPageRequestData request) {
		
		return sysRemindConfigService.sqlPageQuery(request);
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	@ResponseBody
	public SysRemindConfigDto getSysRemindConfig(@RequestParam("rcUuid") String rcUuid) {
		return sysRemindConfigService.getById(rcUuid);
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseStatus(HttpStatus.CREATED)
	public SysRemindConfigDto createSysRemindConfig(@RequestBody SysRemindConfigDto sysRemindConfigDto) {
		return this.sysRemindConfigService.addSysRemindConfig(sysRemindConfigDto);
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseStatus(HttpStatus.OK)
	public SysRemindConfigDto updateSysRemindConfig(@RequestBody SysRemindConfigDto sysRemindConfigDto) {
		//从requst中获取有效的参数进行修改，确保不会修改到无关的字段
		Set<String> httpParams =  CommonAssembler.getExistsRequestParam(sysRemindConfigDto);
		return this.sysRemindConfigService.updateSysRemindConfig(sysRemindConfigDto,httpParams);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void deleteSysRemindConfig(@RequestParam("rcUuid") String rcUuid) {
		sysRemindConfigService.deleteSysRemindConfig(rcUuid);
	}
		
	@RequestMapping(value = "/deleteList", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void deleteSysRemindConfigs(@RequestBody List<String> rcUuids) {
		sysRemindConfigService.deleteSysRemindConfigs(rcUuids);
	}
}

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

import com.ctop.fw.sys.dto.SysRemindConfigEmpsDto;
import com.ctop.fw.sys.entity.SysRemindConfigEmps;
import com.ctop.fw.sys.service.SysRemindConfigEmpsService;


@RestController
@RequestMapping(path = "/rest/sys/sysRemindConfigEmps")
public class SysRemindConfigEmpsAction {
	private static Logger logger = LoggerFactory.getLogger(SysRemindConfigEmpsAction.class);

	@Autowired
	SysRemindConfigEmpsService sysRemindConfigEmpsService;
	
	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseBody
	public PageResponseData<SysRemindConfigEmpsDto> getSysRemindConfigEmpssList(@RequestBody NuiPageRequestData request) {
 
		return sysRemindConfigEmpsService.sqlPageQuery(request);
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	@ResponseBody
	public SysRemindConfigEmpsDto getSysRemindConfigEmps(@RequestParam("rceUuid") String rceUuid) {
		return sysRemindConfigEmpsService.getById(rceUuid);
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseStatus(HttpStatus.CREATED)
	public SysRemindConfigEmpsDto createSysRemindConfigEmps(@RequestBody SysRemindConfigEmpsDto sysRemindConfigEmpsDto) {
		return this.sysRemindConfigEmpsService.addSysRemindConfigEmps(sysRemindConfigEmpsDto);
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseStatus(HttpStatus.OK)
	public SysRemindConfigEmpsDto updateSysRemindConfigEmps(@RequestBody SysRemindConfigEmpsDto sysRemindConfigEmpsDto) {
		//从requst中获取有效的参数进行修改，确保不会修改到无关的字段
		Set<String> httpParams =  CommonAssembler.getExistsRequestParam(sysRemindConfigEmpsDto);
		return this.sysRemindConfigEmpsService.updateSysRemindConfigEmps(sysRemindConfigEmpsDto,httpParams);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void deleteSysRemindConfigEmps(@RequestParam("rceUuid") String rceUuid) {
		sysRemindConfigEmpsService.deleteSysRemindConfigEmps(rceUuid);
	}
		
	@RequestMapping(value = "/deleteList", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void deleteSysRemindConfigEmpss(@RequestBody List<String> rceUuids) {
		sysRemindConfigEmpsService.deleteSysRemindConfigEmpss(rceUuids);
	}
}

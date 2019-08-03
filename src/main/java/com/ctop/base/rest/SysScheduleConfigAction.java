package com.ctop.base.rest;


import java.util.List;
import java.util.Set;

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

import com.ctop.base.dto.SysScheduleConfigDto;
import com.ctop.base.service.SysScheduleConfigService;
import com.ctop.base.service.TaskSchedulerService;
import com.ctop.fw.common.model.NuiPageRequestData;
import com.ctop.fw.common.model.PageResponseData;
import com.ctop.fw.common.utils.CommonAssembler;

@RestController
@RequestMapping(path = "/rest/base/sysScheduleConfig")
public class SysScheduleConfigAction {
	private static Logger logger = LoggerFactory.getLogger(SysScheduleConfigAction.class);

	@Autowired
	SysScheduleConfigService sysScheduleConfigService;
	
	@Autowired
	TaskSchedulerService taskSchedulerService;

	
	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseBody
	public PageResponseData<SysScheduleConfigDto> getSysScheduleConfigsList(@RequestBody NuiPageRequestData request) {
		return sysScheduleConfigService.sqlPageQuery(request);
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	@ResponseBody
	public SysScheduleConfigDto getSysScheduleConfig(@RequestParam("scheduleId") String scheduleId) {
		return sysScheduleConfigService.getById(scheduleId);
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseStatus(HttpStatus.CREATED)
	public SysScheduleConfigDto createSysScheduleConfig(@RequestBody SysScheduleConfigDto sysScheduleConfigDto) {
		return this.sysScheduleConfigService.addSysScheduleConfig(sysScheduleConfigDto);
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseStatus(HttpStatus.OK)
	public SysScheduleConfigDto updateSysScheduleConfig(@RequestBody SysScheduleConfigDto sysScheduleConfigDto) {
		Set<String> httpParams =  CommonAssembler.getExistsRequestParam(sysScheduleConfigDto);
		return this.sysScheduleConfigService.updateSysScheduleConfig(sysScheduleConfigDto,httpParams);
	}

	@RequestMapping(value = "/delete")
	@ResponseStatus(HttpStatus.OK)
	public void deleteSysScheduleConfig(@RequestParam("scheduleId") String scheduleId) {
		sysScheduleConfigService.deleteSysScheduleConfig(scheduleId);
	}
		
	@RequestMapping(value = "/deleteList")
	@ResponseStatus(HttpStatus.OK)
	public void deleteSysScheduleConfigs(@RequestBody List<String> scheduleIds) {
		sysScheduleConfigService.deleteSysScheduleConfigs(scheduleIds);
	}
	
	@RequestMapping(value = "/pause", produces = { "application/json" })
	@ResponseStatus(HttpStatus.OK)
	public SysScheduleConfigDto pause(@RequestParam String scheduleId) {
		return this.sysScheduleConfigService.pause(scheduleId);
	}
	
	@RequestMapping(value = "/reply",produces = { "application/json" })
	@ResponseStatus(HttpStatus.OK)
	public SysScheduleConfigDto reply(@RequestParam String scheduleId) throws ClassNotFoundException {
		SysScheduleConfigDto result = this.sysScheduleConfigService.reply(scheduleId);
		result.setCurrentComputerName(taskSchedulerService.getCurrentMachineName());
		return result;
	}
	
	@RequestMapping(value = "/reGetMachines",produces = { "application/json" })
	@ResponseStatus(HttpStatus.OK)
	public SysScheduleConfigDto reGetMachines()  {
		SysScheduleConfigDto result = new SysScheduleConfigDto();
		result.setCurrentComputerName(taskSchedulerService.getCurrentMachineName());
		result.setCurrentComputerIp(taskSchedulerService.getCurrentMachineAddress());
		return result;
	}
	
	@RequestMapping(value = "/exeTask",produces = { "application/json" })
	@ResponseStatus(HttpStatus.OK)
	public SysScheduleConfigDto exeTask(@RequestParam String scheduleId) throws ClassNotFoundException {
		SysScheduleConfigDto result = this.sysScheduleConfigService.exeTask(scheduleId);
		return result;
	}
	
}

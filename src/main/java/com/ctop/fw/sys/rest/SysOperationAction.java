package com.ctop.fw.sys.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ctop.fw.common.model.NuiPageRequestData;
import com.ctop.fw.common.model.PageResponseData;
import com.ctop.fw.sys.dto.SysOperationDto;
import com.ctop.fw.sys.entity.SysOperation;
import com.ctop.fw.sys.service.SysOperationService;

@RestController
@RequestMapping(path = "/rest/sys/sysOperation")
public class SysOperationAction {
	private static Logger logger = LoggerFactory.getLogger(SysOperationAction.class);

	@Autowired
	SysOperationService sysOperationService;
	
	@RequestMapping(value = "/all", method = RequestMethod.GET, produces = { "application/json" })
	public List<SysOperationDto> getAllSysOperations() { 
		return sysOperationService.queryAllOperations();
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = { "application/json" })
	public PageResponseData<SysOperationDto> getSysOperationsList(@RequestBody NuiPageRequestData request) {
		return sysOperationService.sqlPageQuery(request);
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	@ResponseBody
	public SysOperationDto getSysOperation(@RequestParam("operationUuid") String operationUuid) {
		return sysOperationService.getById(operationUuid);
	}

	@RequestMapping(value="/create",method = RequestMethod.POST, produces = { "application/json" })
	@ResponseStatus(HttpStatus.CREATED)
	public SysOperationDto createSysOperation(@RequestBody SysOperationDto sysOperationDto) {
		return this.sysOperationService.addSysOperation(sysOperationDto);
	}

	@RequestMapping(value="/update",method = RequestMethod.POST, produces = { "application/json" })
	@ResponseStatus(HttpStatus.OK)
	public SysOperationDto updateSysOperation(@RequestBody SysOperationDto sysOperationDto) {
		return this.sysOperationService.updateSysOperation(sysOperationDto);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void deleteSysOperation(@RequestParam("operationUuid") String id) {
		sysOperationService.deleteSysOperation(id);
	}
		
	@RequestMapping(value="/deleteList",method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void deleteSysOperations(@RequestBody List<String> operationUuids) {
		sysOperationService.deleteSysOperations(operationUuids);
	}
}

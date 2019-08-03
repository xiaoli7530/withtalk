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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ctop.fw.common.model.NuiPageRequestData;
import com.ctop.fw.common.model.PageResponseData;
import com.ctop.fw.common.utils.UserContextUtil;
import com.ctop.fw.sys.dto.SysPermissionDto;
import com.ctop.fw.sys.dto.SysPermissionSimpleDto;
import com.ctop.fw.sys.service.SysPermissionService;

@RestController
@RequestMapping(path = "/rest/sys/sysPermission")
public class SysPermissionAction {
	private static Logger logger = LoggerFactory.getLogger(SysPermissionAction.class);

	@Autowired
	SysPermissionService sysPermissionService;
	 
	
	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = { "application/json" })
	public PageResponseData<SysPermissionDto> getSysPermissionsList(@RequestBody NuiPageRequestData request) {
		return sysPermissionService.sqlPageQuery(request);
	}

	@RequestMapping(value = "/inResource/{resourceUuid}", method = RequestMethod.GET, produces = { "application/json" })
	public List<SysPermissionDto> getSysPermissionsList(@PathVariable("resourceUuid") String resourceUuid) {
		return sysPermissionService.findSysPermissionByResourceUuid(resourceUuid);
	} 
	
	@RequestMapping(value = "/currentCompany", method = RequestMethod.GET, produces = { "application/json" })
	@ResponseBody
	public List<SysPermissionSimpleDto> findSysPermissionByCompanyUuid() {
		String companyUuid = UserContextUtil.getCompanyUuid();
		return this.sysPermissionService.findSysPermissionByCompanyUuid(companyUuid);
	}
	
	@RequestMapping(value = "/resourceUuid/{resourceUuid}", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseStatus(HttpStatus.CREATED)
	public void createSysPermission(@PathVariable("resourceUuid") String resourceUuid, @RequestBody List<SysPermissionDto> sysPermissionDtos) {
		String companyUuid = UserContextUtil.getCompanyUuid();
		this.sysPermissionService.saveSysPermissions(companyUuid, resourceUuid, sysPermissionDtos);
	}

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void deleteSysPermission(@PathVariable("id") String id) {
    		String companyUuid = UserContextUtil.getCompanyUuid();
		sysPermissionService.deleteSysPermission(companyUuid, id);
	}
		
    @RequestMapping(value = "/deleteList", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void deleteSysPermissions(@RequestBody List<String> permissions) {
		String companyUuid = UserContextUtil.getCompanyUuid();
		sysPermissionService.deleteSysPermissions(companyUuid, permissions);
	}
	
}

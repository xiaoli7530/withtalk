package com.ctop.fw.sys.rest;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

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

import com.ctop.fw.common.model.NuiPageRequestData;
import com.ctop.fw.common.model.PageResponseData;
import com.ctop.fw.common.utils.CommonAssembler;
import com.ctop.fw.common.utils.UserContextUtil;
import com.ctop.fw.sys.dto.SysRoleAndAccountDto;
import com.ctop.fw.sys.dto.SysRoleDto;
import com.ctop.fw.sys.service.SysRoleService;

@RestController
@RequestMapping(path = "/rest/sys/sysRole")
public class SysRoleAction {
	private static Logger logger = LoggerFactory.getLogger(SysRoleAction.class);

	@Autowired
	SysRoleService sysRoleService; 
	
	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = { "application/json" })
	public PageResponseData<SysRoleDto> getSysRolesList(@RequestBody NuiPageRequestData request) {
		return sysRoleService.sqlPageQuery(request);
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	@ResponseBody
	public SysRoleDto getSysRole(@RequestParam("roleUuid") String id) {
		return sysRoleService.getById(id);
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseStatus(HttpStatus.CREATED)
	public SysRoleDto createSysRole(@Valid @RequestBody SysRoleDto sysRoleDto) {
		sysRoleDto.setCompanyUuid(UserContextUtil.getCompanyUuid());
		return this.sysRoleService.addSysRole(sysRoleDto);
	}

	@RequestMapping(value = "/update", produces = { "application/json" })
	@ResponseStatus(HttpStatus.OK)
	public SysRoleDto updateSysRole(@Valid @RequestBody SysRoleDto sysRoleDto) {
		sysRoleDto.setCompanyUuid(UserContextUtil.getCompanyUuid());
		//从requst中获取有效的参数进行修改，确保不会修改到无关的字段
		Set<String> httpParams =  CommonAssembler.getExistsRequestParam(sysRoleDto);
		return this.sysRoleService.updateSysRole(sysRoleDto,httpParams);
	}

	@RequestMapping(value = "/delete")
	@ResponseStatus(HttpStatus.OK)
	public void deleteSysRole(@RequestParam("roleUuid") String roleUuid) {
		sysRoleService.deleteSysRoles(Arrays.asList(roleUuid));
	}
		
	@RequestMapping(value = "/deleteList")
	@ResponseStatus(HttpStatus.OK)
	public void deleteSysRoles(@RequestBody List<String> roleUuids) {
		sysRoleService.deleteSysRoles(roleUuids);
	}
	
	@RequestMapping(value="/getRoleWithPermissionUuids", method = RequestMethod.GET)
	@ResponseBody
	public SysRoleDto getRoleWithPermissionUuids(@RequestParam String roleUuid) {
		return sysRoleService.getRoleWithPermissionUuids(roleUuid);
	}
	
	@RequestMapping(value="/assignPermission", method = RequestMethod.POST)
	public void assignPermission(@RequestBody SysRoleDto roleDto) {
		this.sysRoleService.assignRolePermission(roleDto);
	}
	
	@RequestMapping(value="/get/roles", method = RequestMethod.POST)
	@ResponseBody
	public List<SysRoleDto> getAvailableRoles() {
		return sysRoleService.findAvailableRoles();
	}
	
	
	/**
	 * 根据角色类型获取
	 * @return
	 */
	@RequestMapping(value="/get/rolesByRoleOrigin", method = RequestMethod.GET)
	@ResponseBody
	public List<SysRoleAndAccountDto> getAvailableRolesByRoleOrigin(@RequestParam String roleOrigin,String accountUuid) {
		return sysRoleService.getAvailableRolesByRoleOrigin(roleOrigin,accountUuid);
	}
	
	@RequestMapping(value = "/getAllRoles", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<SysRoleDto> getAllRoles(@RequestBody List<String> roleUuids) {
		return sysRoleService.selectAllRoles(roleUuids);
	}
	
}

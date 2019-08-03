package com.ctop.fw.sys.rest;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
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
import com.ctop.fw.common.model.BaseTreeDto;
import com.ctop.fw.common.model.NuiPageRequestData;
import com.ctop.fw.common.model.PageResponseData;
import com.ctop.fw.common.utils.CommonAssembler;
import com.ctop.fw.common.utils.UserContextUtil;
import com.ctop.fw.sys.dto.SysResourceDto;
import com.ctop.fw.sys.entity.SysResource;
import com.ctop.fw.sys.service.SysResourceService;

@RestController
@RequestMapping(path = "/rest/fw/sysResource")
public class SysResourceAction {
	private static Logger logger = LoggerFactory.getLogger(SysResourceAction.class);

	@Autowired
	SysResourceService sysResourceService; 
	
	
	
	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = { "application/json" })
	public PageResponseData<SysResourceDto> getSysResourcesList(@RequestBody NuiPageRequestData request) {
		return sysResourceService.sqlPageQuery(request);
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	@ResponseBody
	public SysResourceDto getSysResource(@RequestParam("resourceUuid") String id) {
		return sysResourceService.getById(id);
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseStatus(HttpStatus.CREATED)
	public SysResourceDto createSysResource(@RequestBody SysResourceDto sysResourceDto) {
		sysResourceDto.setCompanyUuid(UserContextUtil.getCompanyUuid());
		return this.sysResourceService.addSysResource(sysResourceDto);
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseStatus(HttpStatus.OK)
	public SysResourceDto updateSysResource(@RequestBody SysResourceDto sysResourceDto) {
		sysResourceDto.setCompanyUuid(UserContextUtil.getCompanyUuid());
		//从requst中获取有效的参数进行修改，确保不会修改到无关的字段
		Set<String> httpParams =  CommonAssembler.getExistsRequestParam(sysResourceDto);
		return this.sysResourceService.updateSysResource(sysResourceDto,httpParams);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void deleteSysResource(@RequestParam("resourceUuid") String id) {
		sysResourceService.deleteSysResource(id);
	}
		
	@RequestMapping(value = "/deleteList", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void deleteSysResources(@RequestBody List<String> resourceUuids) {
		sysResourceService.deleteSysResources(resourceUuids);
	}
	
	@RequestMapping(value = "/findByExample", method = RequestMethod.POST)
	@ResponseBody
	public List<SysResourceDto> findByExample(@RequestBody SysResource example) {
		return sysResourceService.findByExample(example);
	}
	
	@RequestMapping(value = "/currentCompany", method = RequestMethod.GET)
	@ResponseBody
	public List<SysResourceDto> findByCurrentCompany() {
		SysResource example = new SysResource();
		//example.setCompanyUuid(companyUuid);
		return sysResourceService.findByExample(example);
	}
	
	@RequestMapping(value = "/currentCompany/tree", method = RequestMethod.GET)
	@ResponseBody
	public List<BaseTreeDto> findCurrentCompanyResourceTree() {
		String companyUuid = UserContextUtil.getCompanyUuid();
		List<SysResourceDto> list = sysResourceService.findSysResourcesByCompanyUuid(companyUuid,"");
		List<BaseTreeDto> treeList = list.stream().map(l -> BaseTreeDto.buildFromSysResource(l)).collect(Collectors.toList());
		return BaseTreeDto.buildCascadeList(treeList); 
	}
	
	
	@RequestMapping(value = "/loadUserMenus",method = RequestMethod.POST)
	@ResponseBody
	public List<BaseTreeDto> loadUserMenus(){
		List<SysResourceDto> list = sysResourceService.findSysResourcesAllMenu(); 
		List<BaseTreeDto> treeList = list.stream().map(l -> BaseTreeDto.buildFromSysResource(l)).collect(Collectors.toList());
		return BaseTreeDto.buildCascadeList(treeList); 
	}
	
	@RequestMapping(value = "/loadMenusByTree",method = RequestMethod.POST)
	@ResponseBody
	public List<BaseTreeDto> loadMenusByTree(){
		List<SysResourceDto> list = sysResourceService.findSysResourcesAllMenu(); 
		List<BaseTreeDto> treeList = list.stream().map(l -> BaseTreeDto.buildFromSysResource(l)).collect(Collectors.toList());
		return treeList; 
	}
	
	
	
}

package com.ctop.fw.hr.rest;


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
import com.ctop.fw.common.model.DepartmentTreeDto;
import com.ctop.fw.common.model.NuiPageRequestData;
import com.ctop.fw.common.model.PageResponseData;
import com.ctop.fw.common.utils.CommonAssembler;
import com.ctop.fw.hr.dto.HrDepartmentDto;
import com.ctop.fw.hr.service.HrDepartmentService;
import com.ctop.fw.sys.dto.SysResourceDto;

@RestController
@RequestMapping(path = "/rest/hr/hrDepartment")
public class HrDepartmentAction {
	private static Logger logger = LoggerFactory.getLogger(HrDepartmentAction.class);

	@Autowired
	HrDepartmentService hrDepartmentService;
	
	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseBody
	public PageResponseData<HrDepartmentDto> getHrDepartmentsList(@RequestBody NuiPageRequestData request) {
		return hrDepartmentService.sqlPageQuery(request);
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	@ResponseBody
	public HrDepartmentDto getHrDepartment(@RequestParam("departmentUuid") String departmentUuid) {
		return hrDepartmentService.getById(departmentUuid);
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseStatus(HttpStatus.CREATED)
	public HrDepartmentDto createHrDepartment(@RequestBody HrDepartmentDto hrDepartmentDto) {
		return this.hrDepartmentService.addHrDepartment(hrDepartmentDto);
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseStatus(HttpStatus.OK)
	public HrDepartmentDto updateHrDepartment(@RequestBody HrDepartmentDto hrDepartmentDto) {
		Set<String> httpParams =  CommonAssembler.getExistsRequestParam(hrDepartmentDto);
		return this.hrDepartmentService.updateHrDepartment(hrDepartmentDto,httpParams);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void deleteHrDepartment(@RequestParam("departmentUuid") String departmentUuid) {
		hrDepartmentService.deleteHrDepartment(departmentUuid);
	}
		
	@RequestMapping(value = "/deleteList", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void deleteHrDepartments(@RequestBody List<String> departmentUuids) {
		hrDepartmentService.deleteHrDepartments(departmentUuids);
	}
	
	@RequestMapping(value = "/loadDeparmentByTree",method = RequestMethod.POST)
	@ResponseBody
	public List<DepartmentTreeDto> loadDeparmentByTree(){
		List<HrDepartmentDto> list = hrDepartmentService.selectDepartmentTree();
		List<DepartmentTreeDto> treeList = list.stream().map(l -> DepartmentTreeDto.buildFromDepartment(l)).collect(Collectors.toList());
		return DepartmentTreeDto.buildCascadeList(treeList); 
	}
	@RequestMapping(value = "/getDeparmentByTree",method = RequestMethod.GET)
	@ResponseBody
	public List<DepartmentTreeDto> getDeparmentByTree(){
		List<HrDepartmentDto> list = hrDepartmentService.selectDepartmentTree();
		List<DepartmentTreeDto> treeList = list.stream().map(l -> DepartmentTreeDto.buildFromDepartment(l)).collect(Collectors.toList());
		return DepartmentTreeDto.buildCascadeList(treeList); 
	}
}

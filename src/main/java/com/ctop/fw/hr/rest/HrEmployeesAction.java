package com.ctop.fw.hr.rest;


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

import com.ctop.fw.common.model.ComboTreeDto;
import com.ctop.fw.common.model.NuiPageRequestData;
import com.ctop.fw.common.model.PageResponseData;
import com.ctop.fw.common.utils.CommonAssembler;
import com.ctop.fw.common.utils.UserContextUtil;
import com.ctop.fw.hr.dto.HrEmployeesDto;
import com.ctop.fw.hr.service.HrEmployeesService;


@RestController
@RequestMapping(path = "/rest/hr/hrEmployees")
public class HrEmployeesAction {
	private static Logger logger = LoggerFactory.getLogger(HrEmployeesAction.class);

	@Autowired
	HrEmployeesService hrEmployeesService;
	
	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseBody
	public PageResponseData<HrEmployeesDto> getHrEmployeessList(@RequestBody NuiPageRequestData request) {
		return hrEmployeesService.sqlPageQuery(request);
	}
	
	@RequestMapping(value = "/listForEle", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseBody
	public PageResponseData<HrEmployeesDto> getHrEmployeessListForEle(@RequestBody NuiPageRequestData request) {
		return hrEmployeesService.sqlPageQueryForElement(request);
	}
	
	@RequestMapping(value = "/all", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseBody
	public List<HrEmployeesDto> getHrEmployeessAll(@RequestParam(name="q",required=false) String q,String defaultPeople) {
		return hrEmployeesService.getHrEmployeeAll(q,defaultPeople);
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	@ResponseBody
	public HrEmployeesDto getHrEmployees(@RequestParam("empUuid") String empUuid) {
		return hrEmployeesService.getById(empUuid);
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseStatus(HttpStatus.CREATED)
	public HrEmployeesDto createHrEmployees(@RequestBody HrEmployeesDto hrEmployeesDto) {
		return this.hrEmployeesService.addHrEmployees(hrEmployeesDto);
	}
	@RequestMapping(value = "/namelist", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseBody
	public List<HrEmployeesDto> getHrEmployeessall(@RequestParam(name="q",required=false) String q,@RequestParam(name="isDept",required=false) String isDept) {
		return hrEmployeesService.getHrEmployee(q,isDept);
	}
	
	@RequestMapping(value = "/namelist2", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseBody
	public List<HrEmployeesDto> getHrEmployeessall2(@RequestParam(name="q",required=false) String q,@RequestParam(name="departmentUuid",required=false)String departmentUuid) {
		
		return hrEmployeesService.getHrEmployee2(q,departmentUuid);
	}
	
	@RequestMapping(value = "/namelist3", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseBody
	public List<HrEmployeesDto> getHrEmployeessall3(@RequestParam(name="q",required=false) String q) {
		String departmentUuid= UserContextUtil.getDepartmentUuid();
		return hrEmployeesService.getHrEmployee2(q,departmentUuid);
	}
	
	
	@RequestMapping(value = "/update", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseStatus(HttpStatus.OK)
	public HrEmployeesDto updateHrEmployees(@RequestBody HrEmployeesDto hrEmployeesDto) {
		//从requst中获取有效的参数进行修改，确保不会修改到无关的字段
		Set<String> httpParams =  CommonAssembler.getExistsRequestParam(hrEmployeesDto);
		return this.hrEmployeesService.updateHrEmployees(hrEmployeesDto,httpParams);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void deleteHrEmployees(@RequestParam("empUuid") String empUuid) {
		hrEmployeesService.deleteHrEmployees(empUuid);
	}
		
	@RequestMapping(value = "/deleteList", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void deleteHrEmployeess(@RequestBody List<String> empUuids) {
		hrEmployeesService.deleteHrEmployeess(empUuids);
	}
	@RequestMapping(value = "/getEmployeesByTree",method = RequestMethod.GET)
	@ResponseBody
	public List<ComboTreeDto> getEmployeesByTree(){
		List<ComboTreeDto> list = hrEmployeesService.selectEmployeesTree();
		return ComboTreeDto.buildCascadeList(list); 
	}
	
	@RequestMapping(value = "/getHrEmpsByDept", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseBody
	public List<HrEmployeesDto> getHrEmpsByDept(@RequestParam(name="q",required=false) String q,@RequestParam(name="isDept",required=false) String isDept) {
		return hrEmployeesService.getHrEmpsByDept(q,isDept);
	}
	@RequestMapping(value = "/getHrEmpsByDeptId", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseBody
	public List<HrEmployeesDto> getHrEmpsByDeptId(@RequestParam(name="q",required=false) String q, String deptId) {
		return hrEmployeesService.getHrEmpsByDeptId(q,deptId);
	}
	
	@RequestMapping(value = "/createByEle", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseStatus(HttpStatus.CREATED)
	public HrEmployeesDto createHrEmployeesByEle(@RequestBody HrEmployeesDto hrEmployeesDto) {
		return this.hrEmployeesService.addHrEmployeesByEle(hrEmployeesDto);
	}
	
	@RequestMapping(value = "/getHrEmployeesByEmpName", method = RequestMethod.GET)
	@ResponseBody
	public List<HrEmployeesDto> getHrEmployeesByEmpName(@RequestParam(name="q",required=false) String q,String notLoginPerson,String notEmail,String defaultPeople) {
		return hrEmployeesService.getHrEmployeesByEmpName(q,notLoginPerson,notEmail,defaultPeople);
	}
	
	@RequestMapping(value = "/getHrEmployeesByCombo", method = RequestMethod.GET)
	@ResponseBody
	public List<HrEmployeesDto> getHrEmployeesByCombo(@RequestParam(name="q",required=false) String q,
			@RequestParam(name="deptId",required=false) String deptId) {
		return hrEmployeesService.getHrEmployeesByCombo(q, deptId);
	}
	
	@RequestMapping(value = "/getHrEmployeesByDre", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.CREATED)
	public List<HrEmployeesDto> getHrEmployeesByDre(@RequestParam(name="q",required=false) String q,String groupCodes) {
		return this.hrEmployeesService.getHrEmployeesByDre(q,groupCodes);
	}
	
	@RequestMapping(value = "/findHrEmployees", method = RequestMethod.POST, produces = { "application/json" })
	public List<HrEmployeesDto> findAllProject(@RequestParam(name="q",required=false) String q,@RequestParam(name="empUuid",required=false) String empUuid) {
 		String order = empUuid;
		return hrEmployeesService.findHrEmployees(q,order,null);
	}
	
	@RequestMapping(value = "/findTechnicianEmp", method = RequestMethod.GET)
	@ResponseBody
	public List<HrEmployeesDto> findTechnicianEmp(String q) {
		return hrEmployeesService.findTechnicianEmp(q);
	}
}

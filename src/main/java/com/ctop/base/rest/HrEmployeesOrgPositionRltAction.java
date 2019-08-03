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

import com.ctop.base.dto.HrEmployeesOrgPositionRltDto;
import com.ctop.base.entity.HrEmployeesOrgPositionRlt;
import com.ctop.base.service.HrEmployeesOrgPositionRltService;

@RestController
@RequestMapping(path = "/rest/base/hrEmployeesOrgPositionRlt")
public class HrEmployeesOrgPositionRltAction {
	private static Logger logger = LoggerFactory.getLogger(HrEmployeesOrgPositionRltAction.class);

	@Autowired
	HrEmployeesOrgPositionRltService hrEmployeesOrgPositionRltService;
	
	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseBody
	public PageResponseData<HrEmployeesOrgPositionRltDto> getHrEmployeesOrgPositionRltsList(@RequestBody NuiPageRequestData request) {
		return hrEmployeesOrgPositionRltService.sqlPageQuery(request);
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	@ResponseBody
	public HrEmployeesOrgPositionRltDto getHrEmployeesOrgPositionRlt(@RequestParam("rltUuid") String rltUuid) {
		return hrEmployeesOrgPositionRltService.getById(rltUuid);
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseStatus(HttpStatus.CREATED)
	public HrEmployeesOrgPositionRltDto createHrEmployeesOrgPositionRlt(@RequestBody HrEmployeesOrgPositionRltDto hrEmployeesOrgPositionRltDto) {
		return this.hrEmployeesOrgPositionRltService.addHrEmployeesOrgPositionRlt(hrEmployeesOrgPositionRltDto);
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseStatus(HttpStatus.OK)
	public HrEmployeesOrgPositionRltDto updateHrEmployeesOrgPositionRlt(@RequestBody HrEmployeesOrgPositionRltDto hrEmployeesOrgPositionRltDto) {
		//从requst中获取有效的参数进行修改，确保不会修改到无关的字段
		Set<String> httpParams =  CommonAssembler.getExistsRequestParam(hrEmployeesOrgPositionRltDto);
		return this.hrEmployeesOrgPositionRltService.updateHrEmployeesOrgPositionRlt(hrEmployeesOrgPositionRltDto,httpParams);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void deleteHrEmployeesOrgPositionRlt(@RequestParam("rltUuid") String rltUuid) {
		hrEmployeesOrgPositionRltService.deleteHrEmployeesOrgPositionRlt(rltUuid);
	}
		
	@RequestMapping(value = "/deleteList", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void deleteHrEmployeesOrgPositionRlts(@RequestBody List<String> rltUuids) {
		hrEmployeesOrgPositionRltService.deleteHrEmployeesOrgPositionRlts(rltUuids);
	}
}

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

import com.ctop.base.dto.HrPositionInfoDto;
import com.ctop.base.entity.HrPositionInfo;
import com.ctop.base.service.HrPositionInfoService;

@RestController
@RequestMapping(path = "/rest/base/hrPositionInfo")
public class HrPositionInfoAction {
	private static Logger logger = LoggerFactory.getLogger(HrPositionInfoAction.class);

	@Autowired
	HrPositionInfoService hrPositionInfoService;
	
	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseBody
	public PageResponseData<HrPositionInfoDto> getHrPositionInfosList(@RequestBody NuiPageRequestData request) {
		return hrPositionInfoService.sqlPageQuery(request);
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	@ResponseBody
	public HrPositionInfoDto getHrPositionInfo(@RequestParam("positionInfoUuid") String positionInfoUuid) {
		return hrPositionInfoService.getById(positionInfoUuid);
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseStatus(HttpStatus.CREATED)
	public HrPositionInfoDto createHrPositionInfo(@RequestBody HrPositionInfoDto hrPositionInfoDto) {
		return this.hrPositionInfoService.addHrPositionInfo(hrPositionInfoDto);
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseStatus(HttpStatus.OK)
	public HrPositionInfoDto updateHrPositionInfo(@RequestBody HrPositionInfoDto hrPositionInfoDto) {
		//从requst中获取有效的参数进行修改，确保不会修改到无关的字段
		Set<String> httpParams =  CommonAssembler.getExistsRequestParam(hrPositionInfoDto);
		return this.hrPositionInfoService.updateHrPositionInfo(hrPositionInfoDto,httpParams);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void deleteHrPositionInfo(@RequestParam("positionInfoUuid") String positionInfoUuid) {
		hrPositionInfoService.deleteHrPositionInfo(positionInfoUuid);
	}
		
	@RequestMapping(value = "/deleteList", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void deleteHrPositionInfos(@RequestBody List<String> positionInfoUuids) {
		hrPositionInfoService.deleteHrPositionInfos(positionInfoUuids);
	}
}

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

import com.ctop.base.dto.BaseExpressRegionDto;
import com.ctop.base.entity.BaseExpressRegion;
import com.ctop.base.service.BaseExpressRegionService;
import com.ctop.fw.common.model.NuiPageRequestData;
import com.ctop.fw.common.model.PageResponseData;

@RestController
@RequestMapping(path = "/rest/base/baseExpressRegion")
public class BaseExpressRegionAction {
	private static Logger logger = LoggerFactory.getLogger(BaseExpressRegionAction.class);

	@Autowired
	BaseExpressRegionService baseExpressRegionService;
	
	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseBody
	public PageResponseData<BaseExpressRegionDto> getBaseExpressRegionsList(@RequestBody NuiPageRequestData request) {
		return baseExpressRegionService.sqlPageQuery(request);
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	@ResponseBody
	public BaseExpressRegionDto getBaseExpressRegion(@RequestParam("erUuid") String erUuid) {
		return baseExpressRegionService.getById(erUuid);
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseStatus(HttpStatus.CREATED)
	public BaseExpressRegionDto createBaseExpressRegion(@RequestBody BaseExpressRegionDto baseExpressRegionDto) {
		return this.baseExpressRegionService.addBaseExpressRegion(baseExpressRegionDto);
	}

	@RequestMapping(value = "/update", method = RequestMethod.PUT, produces = { "application/json" })
	@ResponseStatus(HttpStatus.OK)
	public BaseExpressRegionDto updateBaseExpressRegion(@RequestBody BaseExpressRegionDto baseExpressRegionDto) {
		return this.baseExpressRegionService.updateBaseExpressRegion(baseExpressRegionDto);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void deleteBaseExpressRegion(@RequestParam("erUuid") String erUuid) {
		baseExpressRegionService.deleteBaseExpressRegion(erUuid);
	}
		
	@RequestMapping(value = "/deleteList", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void deleteBaseExpressRegions(@RequestBody List<String> erUuids) {
		baseExpressRegionService.deleteBaseExpressRegions(erUuids);
	}
}

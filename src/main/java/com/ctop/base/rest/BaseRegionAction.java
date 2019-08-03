package com.ctop.base.rest;


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

import com.ctop.base.dto.BaseRegionDto;
import com.ctop.base.entity.BaseRegion;
import com.ctop.base.service.BaseRegionService;
import com.ctop.fw.common.model.NuiPageRequestData;
import com.ctop.fw.common.model.PageResponseData;

@RestController
@RequestMapping(path = "/rest/base/baseRegion")
public class BaseRegionAction {
	//private static Logger logger = LoggerFactory.getLogger(BaseRegionAction.class);

	@Autowired
	BaseRegionService baseRegionService;
	
	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseBody
	public PageResponseData<BaseRegionDto> getBaseRegionsList(@RequestBody NuiPageRequestData request) {
		return baseRegionService.sqlPageQuery(request);
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	@ResponseBody
	public BaseRegionDto getBaseRegion(@RequestParam("regionUuid") String regionUuid) {
		return baseRegionService.getById(regionUuid);
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseStatus(HttpStatus.CREATED)
	public BaseRegionDto createBaseRegion(@RequestBody BaseRegionDto baseRegionDto) {
		return this.baseRegionService.addBaseRegion(baseRegionDto);
	}

	@RequestMapping(value = "/update", method = RequestMethod.PUT, produces = { "application/json" })
	@ResponseStatus(HttpStatus.OK)
	public BaseRegionDto updateBaseRegion(@RequestBody BaseRegionDto baseRegionDto) {
		return this.baseRegionService.updateBaseRegion(baseRegionDto);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void deleteBaseRegion(@RequestParam("regionUuid") String regionUuid) {
		baseRegionService.deleteBaseRegion(regionUuid);
	}
		
	@RequestMapping(value = "/deleteList", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void deleteBaseRegions(@RequestBody List<String> regionUuids) {
		baseRegionService.deleteBaseRegions(regionUuids);
	}
	
	@RequestMapping(value = "/findRegionByCityUuid", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseStatus(HttpStatus.OK)
	public List<BaseRegion> findRegionByCityUuid(@RequestParam("cityUuid") String cityUuid) {
		return baseRegionService.findRegionByCityUuid(cityUuid);
	}
}

package com.ctop.base.rest;


import java.util.List;

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

import com.ctop.base.dto.BaseDictDetailDto;
import com.ctop.base.service.BaseDictDetailService;
import com.ctop.fw.common.model.NuiPageRequestData;
import com.ctop.fw.common.model.PageResponseData;

@RestController
@RequestMapping(path = "/rest/base/baseDictDetail")
public class BaseDictDetailAction {
	private static Logger logger = LoggerFactory.getLogger(BaseDictDetailAction.class);

	@Autowired
	BaseDictDetailService baseDictDetailService;
	
	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseBody
	public PageResponseData<BaseDictDetailDto> getBaseDictDetailsList(@RequestBody NuiPageRequestData request) {
		return baseDictDetailService.sqlPageQuery(request);
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	@ResponseBody
	public BaseDictDetailDto getBaseDictDetail(@RequestParam("bdlUuid") String bdlUuid) {
		return baseDictDetailService.getById(bdlUuid);
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseStatus(HttpStatus.CREATED)
	public BaseDictDetailDto createBaseDictDetail(@RequestBody BaseDictDetailDto baseDictDetailDto) {
		return this.baseDictDetailService.addBaseDictDetail(baseDictDetailDto);
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseStatus(HttpStatus.OK)
	public BaseDictDetailDto updateBaseDictDetail(@RequestBody BaseDictDetailDto baseDictDetailDto) {
		return this.baseDictDetailService.updateBaseDictDetail(baseDictDetailDto);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public void deleteBaseDictDetail(@RequestParam("bdlUuid") String bdlUuid) {
		baseDictDetailService.deleteBaseDictDetail(bdlUuid);
	}
		
	@RequestMapping(value = "/deleteList", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void deleteBaseDictDetails(@RequestBody List<String> bdlUuids) {
		baseDictDetailService.deleteBaseDictDetails(bdlUuids);
	}
}

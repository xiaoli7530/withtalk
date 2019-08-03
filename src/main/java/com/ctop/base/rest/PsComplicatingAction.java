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

import com.ctop.base.dto.PsComplicatingDto;
import com.ctop.base.entity.PsComplicating;
import com.ctop.base.service.PsComplicatingService;

@RestController
@RequestMapping(path = "/rest/base/psComplicating")
public class PsComplicatingAction {
	private static Logger logger = LoggerFactory.getLogger(PsComplicatingAction.class);

	@Autowired
	PsComplicatingService psComplicatingService;
	
	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseBody
	public PageResponseData<PsComplicatingDto> getPsComplicatingsList(@RequestBody NuiPageRequestData request) {
		return psComplicatingService.sqlPageQuery(request);
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	@ResponseBody
	public PsComplicatingDto getPsComplicating(@RequestParam("pccUuid") String pccUuid) {
		return psComplicatingService.getById(pccUuid);
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseStatus(HttpStatus.CREATED)
	public PsComplicatingDto createPsComplicating(@RequestBody PsComplicatingDto psComplicatingDto) {
		return this.psComplicatingService.addPsComplicating(psComplicatingDto);
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseStatus(HttpStatus.OK)
	public PsComplicatingDto updatePsComplicating(@RequestBody PsComplicatingDto psComplicatingDto) {
		//从requst中获取有效的参数进行修改，确保不会修改到无关的字段
		Set<String> httpParams =  CommonAssembler.getExistsRequestParam(psComplicatingDto);
		return this.psComplicatingService.updatePsComplicating(psComplicatingDto,httpParams);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void deletePsComplicating(@RequestParam("pccUuid") String pccUuid) {
		psComplicatingService.deletePsComplicating(pccUuid);
	}
		
	@RequestMapping(value = "/deleteList", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void deletePsComplicatings(@RequestBody List<String> pccUuids) {
		psComplicatingService.deletePsComplicatings(pccUuids);
	}
}

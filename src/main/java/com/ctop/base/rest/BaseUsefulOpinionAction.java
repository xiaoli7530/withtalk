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

import com.ctop.base.dto.BaseUsefulOpinionDto;
import com.ctop.base.entity.BaseUsefulOpinion;
import com.ctop.base.service.BaseUsefulOpinionService;

@RestController
@RequestMapping(path = "/rest/base/baseUsefulOpinion")
public class BaseUsefulOpinionAction {
	private static Logger logger = LoggerFactory.getLogger(BaseUsefulOpinionAction.class);

	@Autowired
	BaseUsefulOpinionService baseUsefulOpinionService;
	
	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseBody
	public PageResponseData<BaseUsefulOpinionDto> getBaseUsefulOpinionsList(@RequestBody NuiPageRequestData request) {
		return baseUsefulOpinionService.sqlPageQuery(request);
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	@ResponseBody
	public BaseUsefulOpinionDto getBaseUsefulOpinion(@RequestParam("uoId") String uoId) {
		return baseUsefulOpinionService.getById(uoId);
	}
	
	@RequestMapping(value = "/getBaseUsefulOpinionByUser", method = RequestMethod.GET)
	@ResponseBody
	public List<BaseUsefulOpinionDto> getBaseUsefulOpinionByUser() {
		return baseUsefulOpinionService.getBaseUsefulOpinionByUser();
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseStatus(HttpStatus.CREATED)
	public BaseUsefulOpinionDto createBaseUsefulOpinion(@RequestBody BaseUsefulOpinionDto baseUsefulOpinionDto) {
		return this.baseUsefulOpinionService.addBaseUsefulOpinion(baseUsefulOpinionDto);
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseStatus(HttpStatus.OK)
	public BaseUsefulOpinionDto updateBaseUsefulOpinion(@RequestBody BaseUsefulOpinionDto baseUsefulOpinionDto) {
		//从requst中获取有效的参数进行修改，确保不会修改到无关的字段
		Set<String> httpParams =  CommonAssembler.getExistsRequestParam(baseUsefulOpinionDto);
		return this.baseUsefulOpinionService.updateBaseUsefulOpinion(baseUsefulOpinionDto,httpParams);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void deleteBaseUsefulOpinion(@RequestParam("uoId") String uoId) {
		baseUsefulOpinionService.deleteBaseUsefulOpinion(uoId);
	}
		
	@RequestMapping(value = "/deleteList", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void deleteBaseUsefulOpinions(@RequestBody List<String> uoIds) {
		baseUsefulOpinionService.deleteBaseUsefulOpinions(uoIds);
	}
}

package com.ctop.base.rest;


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

import com.ctop.base.dto.BaseSkuDto;
import com.ctop.base.service.BaseSkuService;
import com.ctop.fw.common.model.NuiPageRequestData;
import com.ctop.fw.common.model.PageResponseData;
import com.ctop.fw.common.utils.CommonAssembler;
import com.ctop.fw.common.utils.UserContextUtil;

@RestController
@RequestMapping(path = "/rest/base/baseSku")
public class BaseSkuAction {
	private static Logger logger = LoggerFactory.getLogger(BaseSkuAction.class);

	@Autowired
	BaseSkuService baseSkuService;
	
	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseBody
	public PageResponseData<BaseSkuDto> getBaseSkusList(@RequestBody NuiPageRequestData request) {
		return baseSkuService.sqlPageQuery(request);
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	@ResponseBody
	public BaseSkuDto getBaseSku(@RequestParam("skuUuid") String skuUuid) {
		return baseSkuService.getById(skuUuid);
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseStatus(HttpStatus.CREATED)
	public BaseSkuDto createBaseSku(@RequestBody BaseSkuDto baseSkuDto) {
		baseSkuDto.setCompanyUuid(UserContextUtil.getCompanyUuid());
		return this.baseSkuService.addBaseSku(baseSkuDto);
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseStatus(HttpStatus.OK)
	public BaseSkuDto updateBaseSku(@RequestBody BaseSkuDto baseSkuDto) {
		//从requst中获取有效的参数进行修改，确保不会修改到无关的字段
		Set<String> httpParams =  CommonAssembler.getExistsRequestParam(baseSkuDto);
		return this.baseSkuService.updateBaseSku(baseSkuDto,httpParams);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void deleteBaseSku(@RequestParam("skuUuid") String skuUuid) {
		baseSkuService.deleteBaseSku(skuUuid);
	}
		
	@RequestMapping(value = "/deleteList", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void deleteBaseSkus(@RequestBody List<String> skuUuids) {
		baseSkuService.deleteBaseSkus(skuUuids);
	}
	
	@RequestMapping(value = "/combogridList", method = RequestMethod.POST)
	@ResponseBody
	public List<BaseSkuDto> combogridList(@RequestParam(name="q",required=false) String q) {
		return baseSkuService.find4Combogrid(UserContextUtil.getCompanyUuid(), q);
	}
}

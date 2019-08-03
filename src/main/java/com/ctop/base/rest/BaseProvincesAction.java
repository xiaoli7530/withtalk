package com.ctop.base.rest;

import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ctop.base.dto.BaseProvincesDto;
import com.ctop.base.entity.BaseProvinces;
import com.ctop.base.service.BaseProvincesService;
import com.ctop.fw.common.model.KendoPageRequestData;
import com.ctop.fw.common.model.NuiPageRequestData;
import com.ctop.fw.common.model.PageResponseData;
import com.ctop.fw.hr.dto.HrEmployeesDto;

@RestController
@RequestMapping(path = "/rest/base/baseProvinces")
public class BaseProvincesAction {
	private static Logger logger = LoggerFactory.getLogger(BaseProvincesAction.class);

	@Autowired
	BaseProvincesService baseProvincesService;
	
	@RequestMapping(method = RequestMethod.GET, produces = { "application/json" })
	public PageResponseData<BaseProvinces> getBaseProvincess(@RequestParam Map<String, Object> params) {
		KendoPageRequestData request = KendoPageRequestData.buildFromFlatMap(params);
		return baseProvincesService.pageQuery(request);
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = { "application/json" })
	public PageResponseData<BaseProvinces> getBaseProvincessList(@RequestBody NuiPageRequestData request) {
		return baseProvincesService.pageQuery(request);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public BaseProvincesDto getBaseProvinces(@PathVariable("id") String id) {
		return baseProvincesService.getById(id);
	}

	@RequestMapping(method = RequestMethod.POST, produces = { "application/json" })
	@ResponseStatus(HttpStatus.CREATED)
	public BaseProvincesDto createBaseProvinces(@RequestBody BaseProvincesDto baseProvincesDto) {
		return this.baseProvincesService.addBaseProvinces(baseProvincesDto);
	}

	@RequestMapping(method = RequestMethod.PUT, produces = { "application/json" })
	@ResponseStatus(HttpStatus.OK)
	public BaseProvincesDto updateBaseProvinces(@RequestBody BaseProvincesDto baseProvincesDto) {
		return this.baseProvincesService.updateBaseProvinces(baseProvincesDto);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void deleteBaseProvinces(@PathVariable("id") String id) {
		baseProvincesService.deleteBaseProvinces(id);
	}
		
	@RequestMapping(method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void deleteBaseProvincess(@RequestBody List<String> provinceUuids) {
		baseProvincesService.deleteBaseProvincess(provinceUuids);
	}
	
	@RequestMapping(value = "/getCombobox", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, String>> getCombobox(){
		return baseProvincesService.getCombobox();
	}
	@RequestMapping(value = "/getProvincess", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseBody
	public List<BaseProvincesDto> getProvincess(@RequestParam(name="q",required=false) String q) {
		return baseProvincesService.getProvincess(q);
	}
}

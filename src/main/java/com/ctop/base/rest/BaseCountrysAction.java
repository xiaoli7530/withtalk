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

import com.ctop.base.dto.BaseCountrysDto;
import com.ctop.base.entity.BaseCountrys;
import com.ctop.base.service.BaseCountrysService;
import com.ctop.fw.common.model.KendoPageRequestData;
import com.ctop.fw.common.model.NuiPageRequestData;
import com.ctop.fw.common.model.PageResponseData;

@RestController
@RequestMapping(path = "/rest/base/baseCountrys")
public class BaseCountrysAction {
	private static Logger logger = LoggerFactory.getLogger(BaseCountrysAction.class);

	@Autowired
	BaseCountrysService baseCountrysService;
	
	@RequestMapping(method = RequestMethod.GET, produces = { "application/json" })
	public PageResponseData<BaseCountrys> getBaseCountryss(@RequestParam Map<String, Object> params) {
		KendoPageRequestData request = KendoPageRequestData.buildFromFlatMap(params);
		return baseCountrysService.pageQuery(request);
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = { "application/json" })
	public PageResponseData<BaseCountrys> getBaseCountryssList(@RequestBody NuiPageRequestData request) {
		return baseCountrysService.pageQuery(request);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public BaseCountrysDto getBaseCountrys(@PathVariable("id") String id) {
		return baseCountrysService.getById(id);
	}

	@RequestMapping(method = RequestMethod.POST, produces = { "application/json" })
	@ResponseStatus(HttpStatus.CREATED)
	public BaseCountrysDto createBaseCountrys(@RequestBody BaseCountrysDto baseCountrysDto) {
		return this.baseCountrysService.addBaseCountrys(baseCountrysDto);
	}

	@RequestMapping(method = RequestMethod.PUT, produces = { "application/json" })
	@ResponseStatus(HttpStatus.OK)
	public BaseCountrysDto updateBaseCountrys(@RequestBody BaseCountrysDto baseCountrysDto) {
		return this.baseCountrysService.updateBaseCountrys(baseCountrysDto);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void deleteBaseCountrys(@PathVariable("id") String id) {
		baseCountrysService.deleteBaseCountrys(id);
	}
		
	@RequestMapping(method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void deleteBaseCountryss(@RequestBody List<String> countryUuids) {
		baseCountrysService.deleteBaseCountryss(countryUuids);
	}
}

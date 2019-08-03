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

import com.ctop.base.dto.BaseCitysDto;
import com.ctop.base.entity.BaseCitys;
import com.ctop.base.service.BaseCitysService;
import com.ctop.fw.common.model.KendoPageRequestData;
import com.ctop.fw.common.model.NuiPageRequestData;
import com.ctop.fw.common.model.PageResponseData;
import com.ctop.fw.hr.dto.HrEmployeesDto;

@RestController
@RequestMapping(path = "/rest/base/baseCitys")
public class BaseCitysAction {
	private static Logger logger = LoggerFactory.getLogger(BaseCitysAction.class);

	@Autowired
	BaseCitysService baseCitysService;
	
	@RequestMapping(method = RequestMethod.GET, produces = { "application/json" })
	public PageResponseData<BaseCitys> getBaseCityss(@RequestParam Map<String, Object> params) {
		KendoPageRequestData request = KendoPageRequestData.buildFromFlatMap(params);
		return baseCitysService.pageQuery(request);
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = { "application/json" })
	public PageResponseData<BaseCitys> getBaseCityssList(@RequestBody NuiPageRequestData request) {
		return baseCitysService.pageQuery(request);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public BaseCitysDto getBaseCitys(@PathVariable("id") String id) {
		return baseCitysService.getById(id);
	}

	@RequestMapping(method = RequestMethod.POST, produces = { "application/json" })
	@ResponseStatus(HttpStatus.CREATED)
	public BaseCitysDto createBaseCitys(@RequestBody BaseCitysDto baseCitysDto) {
		return this.baseCitysService.addBaseCitys(baseCitysDto);
	}

	@RequestMapping(method = RequestMethod.PUT, produces = { "application/json" })
	@ResponseStatus(HttpStatus.OK)
	public BaseCitysDto updateBaseCitys(@RequestBody BaseCitysDto baseCitysDto) {
		return this.baseCitysService.updateBaseCitys(baseCitysDto);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void deleteBaseCitys(@PathVariable("id") String id) {
		baseCitysService.deleteBaseCitys(id);
	}
		
	@RequestMapping(method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void deleteBaseCityss(@RequestBody List<String> cityUuids) {
		baseCitysService.deleteBaseCityss(cityUuids);
	}
	
	
	@RequestMapping(value = "/findAllCitys", method = RequestMethod.POST, produces = { "application/json" })
	public List<BaseCitys> findAllCitys() {
		return baseCitysService.findAllCitys();
	}
	@RequestMapping(value = "/getCombobox", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, String>> getCombobox(String provinceUuid){
		return baseCitysService.getCombobox(provinceUuid);
	}
	@RequestMapping(value = "/getCitys", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseBody
	public List<BaseCitysDto> getCitys(@RequestParam(name="q",required=false) String q,@RequestParam(name="provinceUuid",required=false) String provinceUuid) {
		return baseCitysService.getCitys(q,provinceUuid);
	}
}

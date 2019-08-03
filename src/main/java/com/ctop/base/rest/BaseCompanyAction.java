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

import com.ctop.base.dto.BaseCompanyDto;
import com.ctop.base.service.BaseCompanyService;
import com.ctop.fw.common.model.NuiPageRequestData;
import com.ctop.fw.common.model.PageResponseData;
import com.ctop.fw.common.utils.CommonAssembler;

@RestController
@RequestMapping(path = "/rest/base/baseCompany")
public class BaseCompanyAction {
	private static Logger logger = LoggerFactory.getLogger(BaseCompanyAction.class);

	@Autowired
	BaseCompanyService baseCompanyService;
	
	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseBody
	public PageResponseData<BaseCompanyDto> getBaseCompanysList(@RequestBody NuiPageRequestData request) {
		return baseCompanyService.sqlPageQuery(request);
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	@ResponseBody
	public BaseCompanyDto getBaseCompany(@RequestParam("companyUuid") String companyUuid) {
		return baseCompanyService.getById(companyUuid);
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseStatus(HttpStatus.CREATED)
	public BaseCompanyDto createBaseCompany(@RequestBody BaseCompanyDto baseCompanyDto) {
		return this.baseCompanyService.addBaseCompany(baseCompanyDto);
	}

	@RequestMapping(value = "/update", method = RequestMethod.PUT, produces = { "application/json" })
	@ResponseStatus(HttpStatus.OK)
	public BaseCompanyDto updateBaseCompany(@RequestBody BaseCompanyDto baseCompanyDto) {
		//从requst中获取有效的参数进行修改，确保不会修改到无关的字段
		Set<String> httpParams =  CommonAssembler.getExistsRequestParam(baseCompanyDto);
		return this.baseCompanyService.updateBaseCompany(baseCompanyDto,httpParams);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void deleteBaseCompany(@RequestParam("companyUuid") String companyUuid) {
		baseCompanyService.deleteBaseCompany(companyUuid);
	}
		
	@RequestMapping(value = "/deleteList", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void deleteBaseCompanys(@RequestBody List<String> companyUuids) {
		baseCompanyService.deleteBaseCompanys(companyUuids);
	}
	
	@RequestMapping(value = "/loadCompanyForCombox")
	@ResponseStatus(HttpStatus.OK)
	public List<BaseCompanyDto>  loadCompanyForCombox(String q){
		return baseCompanyService.loadCompanyForCombox(q);
	}
	
	/**跟据companyUuid查询客户信息**/
	@RequestMapping(value = "/findRevCustName",method = RequestMethod.POST, produces = { "application/json" })
	@ResponseStatus(HttpStatus.OK)
	public List<BaseCompanyDto> findRevCustName(@RequestParam("companyType") String companyType) {
		return baseCompanyService.findRevCustName(companyType);
	}
	
	/**跟据companyUuid查询客户信息**/
	@RequestMapping(value = "/queryCarryInfo",method = RequestMethod.POST, produces = { "application/json" })
	@ResponseStatus(HttpStatus.OK)
	public List<BaseCompanyDto> queryCarryInfo() {
		return baseCompanyService.queryCarryInfo();
	}
}

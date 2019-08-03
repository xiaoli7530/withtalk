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

import com.ctop.base.dto.BaseDictTableDto;
import com.ctop.base.service.BaseDictTableService;
import com.ctop.fw.common.model.NuiPageRequestData;
import com.ctop.fw.common.model.PageResponseData;
import com.ctop.fw.common.model.UserDto;
import com.ctop.fw.common.utils.UserContextUtil;
import com.ctop.fw.schedule.JavascriptDataSchedule;

@RestController
@RequestMapping(path = "/rest/base/baseDictTable")
public class BaseDictTableAction {
	private static Logger logger = LoggerFactory.getLogger(BaseDictTableAction.class);

	@Autowired
	BaseDictTableService baseDictTableService;
	@Autowired
	JavascriptDataSchedule javascriptDataSchedule;
	
	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseBody
	public PageResponseData<BaseDictTableDto> getBaseDictTablesList(@RequestBody NuiPageRequestData request) {
		return baseDictTableService.sqlPageQuery(request);
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	@ResponseBody
	public BaseDictTableDto getBaseDictTable(@RequestParam("bdtUuid") String bdtUuid) {
		return baseDictTableService.getById(bdtUuid);
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseStatus(HttpStatus.CREATED)
	public BaseDictTableDto createBaseDictTable(@RequestBody BaseDictTableDto baseDictTableDto) {
		BaseDictTableDto dto = this.baseDictTableService.addBaseDictTable(baseDictTableDto);
		javascriptDataSchedule.generateBaseDictScriptAsync();
		return dto;
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseStatus(HttpStatus.OK)
	public BaseDictTableDto updateBaseDictTable(@RequestBody BaseDictTableDto baseDictTableDto) {
		BaseDictTableDto dto =  this.baseDictTableService.updateBaseDictTable(baseDictTableDto);
		javascriptDataSchedule.generateBaseDictScriptAsync();
		return dto;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void deleteBaseDictTable(@RequestParam("bdtUuid") String bdtUuid) {
		baseDictTableService.deleteBaseDictTable(bdtUuid);
		javascriptDataSchedule.generateBaseDictScriptAsync();
		return;
	}
		
	@RequestMapping(value = "/deleteList", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void deleteBaseDictTables(@RequestBody List<String> bdtUuids) {
		baseDictTableService.deleteBaseDictTables(bdtUuids);
		javascriptDataSchedule.generateBaseDictScriptAsync();
	}
	
	/**
	 * 根据表关联数据字典配置，取
	 * @param bdtUuid
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/data/{bdtUuid}", method = RequestMethod.GET, produces = { "application/json" })
	@ResponseBody
	public List<Map<String, Object>> queryBaseDictTableData(@PathVariable String bdtUuid, @RequestParam Map<String, Object> params) {
		params.put("_companyUuid", UserContextUtil.getCompanyUuid());
		params.put("_accountUuid", UserContextUtil.getAccountUuid());
		
		UserDto user = UserContextUtil.getUser();
		if(user != null){
			params.put("_leveOneDeptUuid",user.getDepartmentUuid());
		}
		
		if(!params.containsKey("companyUuid")) {
			params.put("companyUuid", UserContextUtil.getCompanyUuid());
		}
		if(!params.containsKey("accountUuid")) {
			params.put("accountUuid", UserContextUtil.getAccountUuid());
		}
		if(!params.containsKey("empUuid")) {
			params.put("empUuid", UserContextUtil.getEmpUuid());
		}
		if(!params.containsKey("initialValue")){
			params.put("initialValue", "-1");
		}
		if(!params.containsKey("q")) {
			params.put("q", "");
		}else{
			if(params.get("q") != null){
				params.put("q", params.get("q").toString().toUpperCase());
			}			
		}
		if(!params.containsKey("value")) {
			params.put("value", "");
		}
		
		if(!params.containsKey("deptType")) {
			params.put("deptType", UserContextUtil.getUser().getDepartmentType());
		}
		return baseDictTableService.queryWithBaseDictTable(bdtUuid, params);
	}
	
	@RequestMapping(value = "/dataPost/{bdtUuid}", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseBody
	public List<Map<String, Object>> queryBaseDictTableDataPost(@PathVariable String bdtUuid, @RequestParam Map<String, Object> params) {
		params.put("_departmentUuid", UserContextUtil.getDepartmentUuid());
		params.put("_accountUuid", UserContextUtil.getAccountUuid());
		if(!params.containsKey("companyUuid")) {
			params.put("companyUuid", UserContextUtil.getCompanyUuid());
		}
		if(!params.containsKey("accountUuid")) {
			params.put("accountUuid", UserContextUtil.getAccountUuid());
		}
		if(!params.containsKey("empUuid")) {
			params.put("empUuid", UserContextUtil.getEmpUuid());
		}
		if(!params.containsKey("q")) {
			params.put("q", "");
		}
		if(!params.containsKey("deptType")) {
			params.put("deptType", UserContextUtil.getUser().getDepartmentType());
		}
		if(!params.containsKey("value")) {
			params.put("value", "");
		}
		return baseDictTableService.queryWithBaseDictTable(bdtUuid, params);
	}
}

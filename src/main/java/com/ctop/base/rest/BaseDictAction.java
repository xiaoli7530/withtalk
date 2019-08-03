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

import com.ctop.base.dto.BaseDictDto;
import com.ctop.base.service.BaseDictService;
import com.ctop.fw.common.model.NuiPageRequestData;
import com.ctop.fw.common.model.PageResponseData;
import com.ctop.fw.common.utils.CommonAssembler;

@RestController
@RequestMapping(path = "/rest/base/baseDict")
public class BaseDictAction {
	private static Logger logger = LoggerFactory.getLogger(BaseDictAction.class);

	@Autowired
	BaseDictService baseDictService;
	
	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseBody
	public PageResponseData<BaseDictDto> getBaseDictsList(@RequestBody NuiPageRequestData request) {
		return baseDictService.sqlPageQuery(request);
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	@ResponseBody
	public BaseDictDto getBaseDict(@RequestParam("dictUuid") String dictUuid) {
		return baseDictService.getById(dictUuid);
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseStatus(HttpStatus.CREATED)
	public BaseDictDto createBaseDict(@RequestBody BaseDictDto baseDictDto) {
		return this.baseDictService.addBaseDict(baseDictDto);
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseStatus(HttpStatus.OK)
	public BaseDictDto updateBaseDict(@RequestBody BaseDictDto baseDictDto) {
		//从requst中获取有效的参数进行修改，确保不会修改到无关的字段
		Set<String> httpParams =  CommonAssembler.getExistsRequestParam(baseDictDto);
		return this.baseDictService.updateBaseDict(baseDictDto,httpParams);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void deleteBaseDict(@RequestParam("dictUuid") String dictUuid) {
		baseDictService.deleteBaseDict(dictUuid);
	}
		
	@RequestMapping(value = "/deleteList", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void deleteBaseDicts(@RequestBody List<String> dictUuids) {
		baseDictService.deleteBaseDicts(dictUuids);
	}
	
	/**取出所有的字典*/
	@RequestMapping(value = "/all", method = RequestMethod.GET, produces = { "application/json" })
	public List<BaseDictDto> findAllBaseDicts() {
		return this.baseDictService.findAllAvailable();
	}
	
	/**取出所有的字典*/
	@RequestMapping(value = "/allDicts", produces = { "application/json" })
	public List<BaseDictDto> getdAllBaseDicts() {
		return this.baseDictService.getAllAvailable();
	}
	
	/**取出所有的字典*/
	@RequestMapping(value = "/withDetails", method = RequestMethod.GET)
	public BaseDictDto findBaseDictWithDetails(@RequestParam("dictCode") String dictCode) {
		return this.baseDictService.findBaseDictWithDetails(dictCode);
	}
	
	/**取出所有的字典*/
	@RequestMapping(value = "/allDictsByType", produces = { "application/json" })
	public List<BaseDictDto> getdAllBaseDicts(@RequestParam("type") String type) {
		return this.baseDictService.getAllAvailableByType(type);
	}
}

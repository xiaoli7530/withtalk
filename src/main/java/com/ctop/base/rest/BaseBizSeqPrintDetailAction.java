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

import com.ctop.base.dto.BaseBizSeqPrintDetailDto;
import com.ctop.base.entity.BaseBizSeqPrintDetail;
import com.ctop.base.service.BaseBizSeqPrintDetailService;
import com.ctop.fw.common.model.NuiPageRequestData;
import com.ctop.fw.common.model.PageResponseData;

@RestController
@RequestMapping(path = "/rest/base/baseBizSeqPrintDetail")
public class BaseBizSeqPrintDetailAction {
	private static Logger logger = LoggerFactory.getLogger(BaseBizSeqPrintDetailAction.class);

	@Autowired
	BaseBizSeqPrintDetailService baseBizSeqPrintDetailService;
	
	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseBody
	public PageResponseData<BaseBizSeqPrintDetailDto> getBaseBizSeqPrintDetailsList(@RequestBody NuiPageRequestData request) {
		return baseBizSeqPrintDetailService.sqlPageQuery(request);
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	@ResponseBody
	public BaseBizSeqPrintDetailDto getBaseBizSeqPrintDetail(@RequestParam("bbspdUuid") String bbspdUuid) {
		return baseBizSeqPrintDetailService.getById(bbspdUuid);
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseStatus(HttpStatus.CREATED)
	public BaseBizSeqPrintDetailDto createBaseBizSeqPrintDetail(@RequestBody BaseBizSeqPrintDetailDto baseBizSeqPrintDetailDto) {
		return this.baseBizSeqPrintDetailService.addBaseBizSeqPrintDetail(baseBizSeqPrintDetailDto);
	}

	@RequestMapping(value = "/update", method = RequestMethod.PUT, produces = { "application/json" })
	@ResponseStatus(HttpStatus.OK)
	public BaseBizSeqPrintDetailDto updateBaseBizSeqPrintDetail(@RequestBody BaseBizSeqPrintDetailDto baseBizSeqPrintDetailDto) {
		return this.baseBizSeqPrintDetailService.updateBaseBizSeqPrintDetail(baseBizSeqPrintDetailDto);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void deleteBaseBizSeqPrintDetail(@RequestParam("bbspdUuid") String bbspdUuid) {
		baseBizSeqPrintDetailService.deleteBaseBizSeqPrintDetail(bbspdUuid);
	}
		
	@RequestMapping(value = "/deleteList", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void deleteBaseBizSeqPrintDetails(@RequestBody List<String> bbspdUuids) {
		baseBizSeqPrintDetailService.deleteBaseBizSeqPrintDetails(bbspdUuids);
	}
}

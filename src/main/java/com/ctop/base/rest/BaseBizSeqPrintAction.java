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
import com.ctop.base.dto.BaseBizSeqPrintDto;
import com.ctop.base.entity.BaseBizSeqPrint;
import com.ctop.base.service.BaseBizSeqPrintDetailService;
import com.ctop.base.service.BaseBizSeqPrintService;
import com.ctop.fw.common.model.NuiPageRequestData;
import com.ctop.fw.common.model.PageResponseData;
import com.ctop.fw.common.utils.UserContextUtil;

@RestController
@RequestMapping(path = "/rest/base/baseBizSeqPrint")
public class BaseBizSeqPrintAction {
	private static Logger logger = LoggerFactory.getLogger(BaseBizSeqPrintAction.class);

	@Autowired
	BaseBizSeqPrintService baseBizSeqPrintService;
	
	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseBody
	public PageResponseData<BaseBizSeqPrintDto> getBaseBizSeqPrintsList(@RequestBody NuiPageRequestData request) {
		return baseBizSeqPrintService.sqlPageQuery(request);
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	@ResponseBody
	public BaseBizSeqPrintDto getBaseBizSeqPrint(@RequestParam("bbspUuid") String bbspUuid) {
		return baseBizSeqPrintService.getById(bbspUuid);
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseStatus(HttpStatus.CREATED)
	public BaseBizSeqPrintDto createBaseBizSeqPrint(@RequestBody BaseBizSeqPrintDto baseBizSeqPrintDto) {
		baseBizSeqPrintDto.setPrinterName(UserContextUtil.getAccountUuid());
		baseBizSeqPrintDto.setCompanyUuid(UserContextUtil.getCompanyUuid());
		baseBizSeqPrintDto.setRemainQuantity(baseBizSeqPrintDto.getQuantity());		
		return this.baseBizSeqPrintService.addBaseBizSeqPrint(baseBizSeqPrintDto);
	}

	@RequestMapping(value = "/update", method = RequestMethod.PUT, produces = { "application/json" })
	@ResponseStatus(HttpStatus.OK)
	public BaseBizSeqPrintDto updateBaseBizSeqPrint(@RequestBody BaseBizSeqPrintDto baseBizSeqPrintDto) {
		return this.baseBizSeqPrintService.updateBaseBizSeqPrint(baseBizSeqPrintDto);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void deleteBaseBizSeqPrint(@RequestParam("bbspUuid") String bbspUuid) {
		baseBizSeqPrintService.deleteBaseBizSeqPrint(bbspUuid);
	}
		
	@RequestMapping(value = "/deleteList", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void deleteBaseBizSeqPrints(@RequestBody List<String> bbspUuids) {
		baseBizSeqPrintService.deleteBaseBizSeqPrints(bbspUuids);
	}
}

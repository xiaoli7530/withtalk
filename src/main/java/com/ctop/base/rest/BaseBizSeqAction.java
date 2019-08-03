package com.ctop.base.rest;

import java.util.List;

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

import com.ctop.base.dto.BaseBizSeqDto;
import com.ctop.base.entity.BaseBizSeq;
import com.ctop.base.service.BaseBizSeqService;
import com.ctop.fw.common.model.NuiPageRequestData;
import com.ctop.fw.common.model.PageResponseData;

@RestController
@RequestMapping(path = "/rest/base/baseBizSeq")
public class BaseBizSeqAction {
	private static Logger logger = LoggerFactory.getLogger(BaseBizSeqAction.class);

	@Autowired
	BaseBizSeqService baseBizSeqService;
	
	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseBody
	public PageResponseData<BaseBizSeq> getBaseBizSeqsList(@RequestBody NuiPageRequestData request) {
		return baseBizSeqService.pageQuery(request);
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	@ResponseBody
	public BaseBizSeqDto getBaseBizSeq(@RequestParam("bbsUuid") String bbsUuid) {
		return baseBizSeqService.getById(bbsUuid);
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseStatus(HttpStatus.CREATED)
	public BaseBizSeqDto createBaseBizSeq(@RequestBody BaseBizSeqDto baseBizSeqDto) {
		return this.baseBizSeqService.addBaseBizSeq(baseBizSeqDto);
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseStatus(HttpStatus.OK)
	public BaseBizSeqDto updateBaseBizSeq(@RequestBody BaseBizSeqDto baseBizSeqDto) {
		return this.baseBizSeqService.updateBaseBizSeq(baseBizSeqDto);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void deleteBaseBizSeq(@RequestParam("bbsUuid") String bbsUuid) {
		baseBizSeqService.deleteBaseBizSeq(bbsUuid);
	}
		
	@RequestMapping(value = "/deleteList", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void deleteBaseBizSeqs(@RequestBody List<String> bbsUuids) {
		baseBizSeqService.deleteBaseBizSeqs(bbsUuids);
	}
}

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

import com.ctop.base.dto.BaseBizSeqDetailDto;
import com.ctop.base.entity.BaseBizSeqDetail;
import com.ctop.base.service.BaseBizSeqDetailService;
import com.ctop.fw.common.model.KendoPageRequestData;
import com.ctop.fw.common.model.NuiPageRequestData;
import com.ctop.fw.common.model.PageResponseData;

@RestController
@RequestMapping(path = "/rest/base/baseBizSeqDetail")
public class BaseBizSeqDetailAction {
	private static Logger logger = LoggerFactory.getLogger(BaseBizSeqDetailAction.class);

	@Autowired
	BaseBizSeqDetailService baseBizSeqDetailService;
	
	@RequestMapping(method = RequestMethod.GET, produces = { "application/json" })
	public PageResponseData<BaseBizSeqDetail> getBaseBizSeqDetails(@RequestParam Map<String, Object> params) {
		KendoPageRequestData request = KendoPageRequestData.buildFromFlatMap(params);
		return baseBizSeqDetailService.pageQuery(request);
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = { "application/json" })
	public PageResponseData<BaseBizSeqDetail> getBaseBizSeqDetailsList(@RequestBody NuiPageRequestData request) {
		return baseBizSeqDetailService.pageQuery(request);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public BaseBizSeqDetailDto getBaseBizSeqDetail(@PathVariable("id") String id) {
		return baseBizSeqDetailService.getById(id);
	}

	@RequestMapping(method = RequestMethod.POST, produces = { "application/json" })
	@ResponseStatus(HttpStatus.CREATED)
	public BaseBizSeqDetailDto createBaseBizSeqDetail(@RequestBody BaseBizSeqDetailDto baseBizSeqDetailDto) {
		return this.baseBizSeqDetailService.addBaseBizSeqDetail(baseBizSeqDetailDto);
	}

	@RequestMapping(method = RequestMethod.PUT, produces = { "application/json" })
	@ResponseStatus(HttpStatus.OK)
	public BaseBizSeqDetailDto updateBaseBizSeqDetail(@RequestBody BaseBizSeqDetailDto baseBizSeqDetailDto) {
		return this.baseBizSeqDetailService.updateBaseBizSeqDetail(baseBizSeqDetailDto);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void deleteBaseBizSeqDetail(@PathVariable("id") String id) {
		baseBizSeqDetailService.deleteBaseBizSeqDetail(id);
	}
		
	@RequestMapping(method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void deleteBaseBizSeqDetails(@RequestBody List<String> bbsdUuids) {
		baseBizSeqDetailService.deleteBaseBizSeqDetails(bbsdUuids);
	}
}

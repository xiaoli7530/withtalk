package com.ctop.fw.sys.rest;

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

import com.ctop.fw.common.model.KendoPageRequestData;
import com.ctop.fw.common.model.NuiPageRequestData;
import com.ctop.fw.common.model.PageResponseData;
import com.ctop.fw.sys.dto.SysLoginLogDto;
import com.ctop.fw.sys.entity.SysLoginLog;
import com.ctop.fw.sys.service.SysLoginLogService;

@RestController
@RequestMapping(path = "/rest/sys/sysLoginLog")
public class SysLoginLogAction {
	private static Logger logger = LoggerFactory.getLogger(SysLoginLogAction.class);

	@Autowired
	SysLoginLogService sysLoginLogService;
	
	@RequestMapping(method = RequestMethod.GET, produces = { "application/json" })
	public PageResponseData<SysLoginLog> getSysLoginLogs(@RequestParam Map<String, Object> params) {
		KendoPageRequestData request = KendoPageRequestData.buildFromFlatMap(params);
		return sysLoginLogService.pageQuery(request);
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = { "application/json" })
	public PageResponseData<SysLoginLog> getSysLoginLogsList(@RequestBody NuiPageRequestData request) {
		return sysLoginLogService.pageQuery(request);
	}
}

package com.ctop.fw.sys.rest;


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
import java.util.Set;
import com.ctop.fw.common.model.NuiPageRequestData;
import com.ctop.fw.common.model.PageResponseData;
import com.ctop.fw.common.utils.CommonAssembler;
import com.ctop.fw.sys.dto.SysRemindDto;
import com.ctop.fw.sys.service.SysRemindService;


@RestController
@RequestMapping(path = "/rest/sys/sysRemind")
public class SysRemindAction {
	private static Logger logger = LoggerFactory.getLogger(SysRemindAction.class);

	@Autowired
	SysRemindService sysRemindService;
	
	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseBody
	public PageResponseData<SysRemindDto> getSysRemindsList(@RequestBody NuiPageRequestData request) {

		return sysRemindService.sqlPageQuery(request);
	}
	
	@RequestMapping(value = "/MyList", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseBody
	public PageResponseData<SysRemindDto> getSysRemindsList4My(@RequestBody NuiPageRequestData request) {

		return sysRemindService.sqlPageQuery4My(request);
	}
	

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	@ResponseBody
	public SysRemindDto getSysRemind(@RequestParam("remindUuid") String remindUuid) {
		return sysRemindService.getById(remindUuid);
	}
	

	@RequestMapping(value = "/create", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseStatus(HttpStatus.CREATED)
	public SysRemindDto createSysRemind(@RequestBody SysRemindDto sysRemindDto) {
		return this.sysRemindService.addSysRemind(sysRemindDto);
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseStatus(HttpStatus.OK)
	public SysRemindDto updateSysRemind(@RequestBody SysRemindDto sysRemindDto) {
		//从requst中获取有效的参数进行修改，确保不会修改到无关的字段
		Set<String> httpParams =  CommonAssembler.getExistsRequestParam(sysRemindDto);
		return this.sysRemindService.updateSysRemind(sysRemindDto,httpParams);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void deleteSysRemind(@RequestParam("remindUuid") String remindUuid) {
		sysRemindService.deleteSysRemind(remindUuid);
	}
		
	@RequestMapping(value = "/deleteList", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void deleteSysReminds(@RequestBody List<String> remindUuids) {
		sysRemindService.deleteSysReminds(remindUuids);
	}
	
	@RequestMapping(value = "/updateStatusByRemindUuids", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void updateStatusByRemindUuids(@RequestBody List<String> remindUuids) {
		sysRemindService.updateStatusByRemindUuids(remindUuids);
	}
}

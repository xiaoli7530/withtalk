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
import java.util.Set;

import com.ctop.fw.common.model.NuiPageRequestData;
import com.ctop.fw.common.model.PageResponseData;
import com.ctop.fw.common.utils.CommonAssembler;
import com.ctop.base.dto.PmProjectDto;
import com.ctop.base.service.PmProjectService;

@RestController
@RequestMapping(path = "/rest/base/pmProject")
public class PmProjectAction {
	private static Logger logger = LoggerFactory.getLogger(PmProjectAction.class);

	@Autowired
	PmProjectService pmProjectService;
	
	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseBody
	public PageResponseData<PmProjectDto> getPmProjectsList(@RequestBody NuiPageRequestData request) {
		return pmProjectService.sqlPageQuery(request);
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	@ResponseBody
	public PmProjectDto getPmProject(@RequestParam("projectUuid") String projectUuid) {
		return pmProjectService.getById(projectUuid);
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseStatus(HttpStatus.CREATED)
	public PmProjectDto createPmProject(@RequestBody PmProjectDto pmProjectDto) {
		return this.pmProjectService.addPmProject(pmProjectDto);
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseStatus(HttpStatus.OK)
	public PmProjectDto updatePmProject(@RequestBody PmProjectDto pmProjectDto) {
		//从requst中获取有效的参数进行修改，确保不会修改到无关的字段
		Set<String> httpParams =  CommonAssembler.getExistsRequestParam(pmProjectDto);
		return this.pmProjectService.updatePmProject(pmProjectDto,httpParams);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void deletePmProject(@RequestParam("projectUuid") String projectUuid) {
		pmProjectService.deletePmProject(projectUuid);
	}
		
	@RequestMapping(value = "/deleteList", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void deletePmProjects(@RequestBody List<String> projectUuids) {
		pmProjectService.deletePmProjects(projectUuids);
	}
	
	@RequestMapping(value = "/findAllProject", method = RequestMethod.POST, produces = { "application/json" })
	public List<PmProjectDto> findAllProject(@RequestParam(name="q",required=false) String q,@RequestParam(name="projectUuid",required=false) String projectUuid) {
		//String order = UserContextUtil.getEmpUuid();
		String order = projectUuid;
		return pmProjectService.findAllProject(q,order);
	}
	
	@RequestMapping(value = "/findEdvProject", method = RequestMethod.POST, produces = { "application/json" })
	public List<PmProjectDto> findEdvProject(@RequestParam(name="q",required=false) String q,@RequestParam(name="projectUuid",required=false) String projectUuid) {
		//String order = UserContextUtil.getEmpUuid();
		String order = projectUuid;
		return pmProjectService.findEdvProject(q,order);
	}
	
	@RequestMapping(value = "/findAllProjectWithPppr", method = RequestMethod.POST, produces = { "application/json" })
	public List<PmProjectDto> findAllProjectWithPppr(@RequestParam(name="q",required=false) String q) {
		return pmProjectService.findAllProjectWithPppr(q);
	}
	
}

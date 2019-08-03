package com.ctop.talk.rest;


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
import com.ctop.talk.dto.TalkUserGroupRelationDto;
import com.ctop.talk.service.TalkUserGroupRelationService;

@RestController
@RequestMapping(path = "/rest/talk/talkUserGroupRelation")
public class TalkUserGroupRelationAction {
	private static Logger logger = LoggerFactory.getLogger(TalkUserGroupRelationAction.class);

	@Autowired
	TalkUserGroupRelationService talkUserGroupRelationService;
	
	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseBody
	public PageResponseData<TalkUserGroupRelationDto> getTalkUserGroupRelationsList(@RequestBody NuiPageRequestData request) {
		return talkUserGroupRelationService.sqlPageQuery(request);
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	@ResponseBody
	public TalkUserGroupRelationDto getTalkUserGroupRelation(@RequestParam("userGroupRelationUuis") String userGroupRelationUuis) {
		return talkUserGroupRelationService.getById(userGroupRelationUuis);
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseStatus(HttpStatus.CREATED)
	public TalkUserGroupRelationDto createTalkUserGroupRelation(@RequestBody TalkUserGroupRelationDto talkUserGroupRelationDto) {
		return this.talkUserGroupRelationService.addTalkUserGroupRelation(talkUserGroupRelationDto);
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseStatus(HttpStatus.OK)
	public TalkUserGroupRelationDto updateTalkUserGroupRelation(@RequestBody TalkUserGroupRelationDto talkUserGroupRelationDto) {
		//从requst中获取有效的参数进行修改，确保不会修改到无关的字段
		Set<String> httpParams =  CommonAssembler.getExistsRequestParam(talkUserGroupRelationDto);
		return this.talkUserGroupRelationService.updateTalkUserGroupRelation(talkUserGroupRelationDto,httpParams);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void deleteTalkUserGroupRelation(@RequestParam("userGroupRelationUuis") String userGroupRelationUuis) {
		talkUserGroupRelationService.deleteTalkUserGroupRelation(userGroupRelationUuis);
	}
		
	@RequestMapping(value = "/deleteList", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void deleteTalkUserGroupRelations(@RequestBody List<String> userGroupRelationUuiss) {
		talkUserGroupRelationService.deleteTalkUserGroupRelations(userGroupRelationUuiss);
	}
}

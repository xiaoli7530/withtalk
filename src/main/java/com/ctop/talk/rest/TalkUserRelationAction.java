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
import com.ctop.talk.dto.TalkUserRelationDto;
import com.ctop.talk.service.TalkUserRelationService;

@RestController
@RequestMapping(path = "/rest/talk/talkUserRelation")
public class TalkUserRelationAction {
	private static Logger logger = LoggerFactory.getLogger(TalkUserRelationAction.class);

	@Autowired
	TalkUserRelationService talkUserRelationService;
	
	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseBody
	public PageResponseData<TalkUserRelationDto> getTalkUserRelationsList(@RequestBody NuiPageRequestData request) {
		return talkUserRelationService.sqlPageQuery(request);
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	@ResponseBody
	public TalkUserRelationDto getTalkUserRelation(@RequestParam("userRelationUuid") String userRelationUuid) {
		return talkUserRelationService.getById(userRelationUuid);
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseStatus(HttpStatus.CREATED)
	public TalkUserRelationDto createTalkUserRelation(@RequestBody TalkUserRelationDto talkUserRelationDto) {
		return this.talkUserRelationService.addTalkUserRelation(talkUserRelationDto);
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseStatus(HttpStatus.OK)
	public TalkUserRelationDto updateTalkUserRelation(@RequestBody TalkUserRelationDto talkUserRelationDto) {
		//从requst中获取有效的参数进行修改，确保不会修改到无关的字段
		Set<String> httpParams =  CommonAssembler.getExistsRequestParam(talkUserRelationDto);
		return this.talkUserRelationService.updateTalkUserRelation(talkUserRelationDto,httpParams);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void deleteTalkUserRelation(@RequestParam("userRelationUuid") String userRelationUuid) {
		talkUserRelationService.deleteTalkUserRelation(userRelationUuid);
	}
		
	@RequestMapping(value = "/deleteList", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void deleteTalkUserRelations(@RequestBody List<String> userRelationUuids) {
		talkUserRelationService.deleteTalkUserRelations(userRelationUuids);
	}
}

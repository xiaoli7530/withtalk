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
import com.ctop.talk.dto.TalkGroupDto;
import com.ctop.talk.service.TalkGroupService;

@RestController
@RequestMapping(path = "/rest/talk/talkGroup")
public class TalkGroupAction {
	private static Logger logger = LoggerFactory.getLogger(TalkGroupAction.class);

	@Autowired
	TalkGroupService talkGroupService;
	
	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseBody
	public PageResponseData<TalkGroupDto> getTalkGroupsList(@RequestBody NuiPageRequestData request) {
		return talkGroupService.sqlPageQuery(request);
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	@ResponseBody
	public TalkGroupDto getTalkGroup(@RequestParam("groupUuid") String groupUuid) {
		return talkGroupService.getById(groupUuid);
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseStatus(HttpStatus.CREATED)
	public TalkGroupDto createTalkGroup(@RequestBody TalkGroupDto talkGroupDto) {
		return this.talkGroupService.addTalkGroup(talkGroupDto);
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseStatus(HttpStatus.OK)
	public TalkGroupDto updateTalkGroup(@RequestBody TalkGroupDto talkGroupDto) {
		//从requst中获取有效的参数进行修改，确保不会修改到无关的字段
		Set<String> httpParams =  CommonAssembler.getExistsRequestParam(talkGroupDto);
		return this.talkGroupService.updateTalkGroup(talkGroupDto,httpParams);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void deleteTalkGroup(@RequestParam("groupUuid") String groupUuid) {
		talkGroupService.deleteTalkGroup(groupUuid);
	}
		
	@RequestMapping(value = "/deleteList", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void deleteTalkGroups(@RequestBody List<String> groupUuids) {
		talkGroupService.deleteTalkGroups(groupUuids);
	}
}

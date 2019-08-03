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
import com.ctop.talk.dto.TalkMessageDto;
import com.ctop.talk.service.TalkMessageService;

@RestController
@RequestMapping(path = "/rest/talk/talkMessage")
public class TalkMessageAction {
	private static Logger logger = LoggerFactory.getLogger(TalkMessageAction.class);

	@Autowired
	TalkMessageService talkMessageService;
	
	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseBody
	public PageResponseData<TalkMessageDto> getTalkMessagesList(@RequestBody NuiPageRequestData request) {
		return talkMessageService.sqlPageQuery(request);
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	@ResponseBody
	public TalkMessageDto getTalkMessage(@RequestParam("messageUuid") String messageUuid) {
		return talkMessageService.getById(messageUuid);
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseStatus(HttpStatus.CREATED)
	public TalkMessageDto createTalkMessage(@RequestBody TalkMessageDto talkMessageDto) {
		return this.talkMessageService.addTalkMessage(talkMessageDto);
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseStatus(HttpStatus.OK)
	public TalkMessageDto updateTalkMessage(@RequestBody TalkMessageDto talkMessageDto) {
		//从requst中获取有效的参数进行修改，确保不会修改到无关的字段
		Set<String> httpParams =  CommonAssembler.getExistsRequestParam(talkMessageDto);
		return this.talkMessageService.updateTalkMessage(talkMessageDto,httpParams);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void deleteTalkMessage(@RequestParam("messageUuid") String messageUuid) {
		talkMessageService.deleteTalkMessage(messageUuid);
	}
		
	@RequestMapping(value = "/deleteList", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void deleteTalkMessages(@RequestBody List<String> messageUuids) {
		talkMessageService.deleteTalkMessages(messageUuids);
	}
}

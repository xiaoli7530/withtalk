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
import java.util.Map;
import java.util.Set;
import com.ctop.fw.common.model.NuiPageRequestData;
import com.ctop.fw.common.model.PageResponseData;
import com.ctop.fw.common.utils.CommonAssembler;
import com.ctop.talk.dto.TalkUserDto;
import com.ctop.talk.service.TalkUserService;

@RestController
@RequestMapping(path = "/rest/talk/talkUser")
public class TalkUserAction {
	private static Logger logger = LoggerFactory.getLogger(TalkUserAction.class);

	@Autowired
	TalkUserService talkUserService;
	
	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseBody
	public PageResponseData<TalkUserDto> getTalkUsersList(@RequestBody NuiPageRequestData request) {
		return talkUserService.sqlPageQuery(request);
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	@ResponseBody
	public TalkUserDto getTalkUser(@RequestParam("userUuid") String userUuid) {
		return talkUserService.getById(userUuid);
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseStatus(HttpStatus.CREATED)
	public TalkUserDto createTalkUser(@RequestBody TalkUserDto talkUserDto) {
		return this.talkUserService.addTalkUser(talkUserDto);
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseStatus(HttpStatus.OK)
	public TalkUserDto updateTalkUser(@RequestBody TalkUserDto talkUserDto) {
		//从requst中获取有效的参数进行修改，确保不会修改到无关的字段
		Set<String> httpParams =  CommonAssembler.getExistsRequestParam(talkUserDto);
		return this.talkUserService.updateTalkUser(talkUserDto,httpParams);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void deleteTalkUser(@RequestParam("userUuid") String userUuid) {
		talkUserService.deleteTalkUser(userUuid);
	}
		
	@RequestMapping(value = "/deleteList", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void deleteTalkUsers(@RequestBody List<String> userUuids) {
		talkUserService.deleteTalkUsers(userUuids);
	}
	
	/**
	 * 登录
	 * @param talkUserDto
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseBody
	public Map<String, Object> login(@RequestBody TalkUserDto talkUserDto) {
		return this.talkUserService.login(talkUserDto);
	}
	
	/**
	 * 注册
	 * @param talkUserDto
	 * @return
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseBody
	public Map<String, Object> register(@RequestBody TalkUserDto talkUserDto) {
		return this.talkUserService.register(talkUserDto);
	}
}

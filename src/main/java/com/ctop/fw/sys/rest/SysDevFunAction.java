/**   
* @Title: SysCustomFunAction.java 
* @Package com.ctop.fw.sys.rest 
* @Description: TODO(用一句话描述该文件做什么) 
* @author dais
* @date 2017年12月11日 上午9:29:28 
* @version V1.0   
*/
package com.ctop.fw.sys.rest;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ctop.fw.sys.dto.SysDevDto;
import com.ctop.fw.sys.service.SysDevFunService;

/** 
* @ClassName: SysCustomFunAction 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author dais 
* @date 2017年12月11日 上午9:29:28 
*  
*/
@RestController
@RequestMapping(path = "/rest/sys/sysDevFun")
public class SysDevFunAction {
	/**
	 * 
	 */
	@Autowired
	private SysDevFunService sysDevFunService;
	
	/**
	 *执行sql查询指令
	 * @param sqlCmd
	 * @return
	 */
	@RequestMapping(value = "/sqlQueryCmd")
	@ResponseStatus(HttpStatus.OK)
	public List<SysDevDto> sqlQueryCmd(@RequestBody SysDevDto sysDevDto){
		return sysDevFunService.sqlQueryCmd(sysDevDto);
	}
	/**
	 * 执行修改
	 * @param sqlCmd
	 * @return 
	 */
	@RequestMapping(value = "/sqlUpdateCmd")
	@ResponseStatus(HttpStatus.OK)
	public List<SysDevDto> sqlUpdateCmd(@RequestBody SysDevDto sysDevDto){
		return sysDevFunService.sqlUpdateCmd(sysDevDto);
	}
	
	
	
}

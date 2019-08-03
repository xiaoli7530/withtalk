/**   
* @Title: SysDevFunService.java 
* @Package com.ctop.fw.sys.service 
* @Description: TODO(用一句话描述该文件做什么) 
* @author dais
* @date 2017年12月11日 上午10:35:24 
* @version V1.0   
*/
package com.ctop.fw.sys.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ctop.fw.common.utils.BusinessException;
import com.ctop.fw.common.utils.UserContextUtil;
import com.ctop.fw.sys.dto.SysDevDto;

/** 
* @ClassName: SysDevFunService 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author dais 
* @date 2017年12月11日 上午10:35:24 
*  
*/
@Service
@Transactional
public class SysDevFunService {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public List<SysDevDto> sqlQueryCmd(SysDevDto sysDevDto){
		List<SysDevDto> exeResult = new ArrayList<>();
		checkCurrentUserIsAdmin();
		checkIsLaw(sysDevDto.getParams());
		
		String[] sqls = unEncrypt(sysDevDto.getParams()).split("\\;");
		for(String sql : sqls){
			if(StringUtils.isBlank(sql)){
				continue;
			}
			SysDevDto sdd = new SysDevDto();
			try{
				List<Map<String, Object>> queryResult = jdbcTemplate.queryForList(sql);
				if(queryResult.isEmpty()){
					SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql);  
					SqlRowSetMetaData metaData = rowSet.getMetaData();  
					int columnCount = metaData.getColumnCount();  
					Map<String,Object> fieldMap = new HashMap<String,Object>();  
					for (int i = 1; i <= columnCount; i++) {    
					    fieldMap.put(metaData.getColumnName(i), null);  
					}
					queryResult.add(fieldMap);
				}
				sdd.setList(queryResult);
			}catch(Exception e){
				List<Map<String, Object>> list = new ArrayList<>();
				Map<String,Object> r = new HashMap<>();
				r.put("错误消息", e.getMessage());
				list.add(r);
				sdd.setList(list);
			}
			sdd.setTitle(sql);
			exeResult.add(sdd);
		}
		return exeResult;
	}
	@Transactional
	public List<SysDevDto> sqlUpdateCmd(SysDevDto sysDevDto){
		checkCurrentUserIsAdmin();
		String param = unEncrypt(sysDevDto.getParams());
		checkIsLaw(param);
		List<SysDevDto> rList = new ArrayList<>();
		String[] sqls = param.split("\\;");	
		for(String sql : sqls){
			SysDevDto sdd  = new SysDevDto();
			sdd.setParams(sql);
			try{
				jdbcTemplate.execute(sql);
				sdd.setInfo("执行成功!");
				sdd.setResult(true);
			}catch(Exception e){
				e.printStackTrace();
				sdd.setInfo(e.getMessage());
				sdd.setResult(false);
			}
			rList.add(sdd);
		}
		return rList;
		
	}
	
	public void checkCurrentUserIsAdmin(){
		if(!UserContextUtil.currentUserIsSuperAdmin()){
			throw new BusinessException("当前用户非管理员，无法使用");
		}
	}
	
	public void checkIsLaw(String sql){
		sql = sql.toLowerCase();
		if(sql.contains("delete") || sql.contains("truncate") || sql.contains("drop")){
			throw new BusinessException("执行中含非法字符!");
		}
	}
	
	public String unEncrypt(String str){
		StringBuffer sb = new StringBuffer();
		for(char b : str.toCharArray()){
			sb.append((char)(b - 8));
		}
		return sb.toString();
	}
}

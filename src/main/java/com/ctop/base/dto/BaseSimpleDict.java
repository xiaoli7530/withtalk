package com.ctop.base.dto;

import java.io.Serializable;

import com.ctop.fw.common.model.BaseDto;

/**
 * <pre>
 * 功能说明：简单业务字典
 * 创建者：${user}
 * 创建时间：${date}
 * 版本：${version}
 * </pre>
 */
public class BaseSimpleDict extends BaseDto implements Serializable {
	private static final long serialVersionUID = 4884934256124284530L;
	private String dictCode; 	//字典分组
	private String code;		//字典key
	private String name;		//字典展示
	public String getDictCode() {
		return dictCode;
	}
	public void setDictCode(String dictCode) {
		this.dictCode = dictCode;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}


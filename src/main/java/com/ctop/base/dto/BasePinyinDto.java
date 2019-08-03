package com.ctop.base.dto;

import java.util.Date;
import java.io.Serializable;import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;

import com.ctop.fw.common.model.BaseDto;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * <pre>
 * 功能说明：BasePinyin实体的数据传输对像
 * 创建者：${user}
 * 创建时间：${date}
 * 版本：${version}
 * </pre>
 */
public class BasePinyinDto extends BaseDto implements Serializable {
	private String chs;
	private String py;
	private String fpy;

	public char getFirstChar() {
		return chs != null ? chs.charAt(0) : '\0';
	}
	
	public String getChs() {
		return chs;
	}

	public void setChs(String chs) {
		this.chs = chs;
	}
	
	public String getPy() {
		return py;
	}

	public void setPy(String py) {
		this.py = py;
	}
	
	public String getFpy() {
		return fpy;
	}

	public void setFpy(String fpy) {
		this.fpy = fpy;
	}

}


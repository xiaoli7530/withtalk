package com.ctop.base.dto;

import java.util.Date;
import java.io.Serializable;import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;

import com.ctop.fw.common.model.BaseDto;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * <pre>
 * 功能说明：BaseBizSeq实体的数据传输对像
 * 创建者：${user}
 * 创建时间：${date}
 * 版本：${version}
 * </pre>
 */
public class BaseBizSeqDto extends BaseDto implements Serializable {
	private String bbsUuid;
	private String code;
	private String name;
	private String fullDefinition;
	private String holderDefinition;
	private Integer seqLength;
	private Integer maxLength;
	private Integer initialValue;//初始值

	public Integer getInitialValue() {
		return initialValue;
	}

	public void setInitialValue(Integer initialValue) {
		this.initialValue = initialValue;
	}
	
	public String getBbsUuid() {
		return bbsUuid;
	}

	public void setBbsUuid(String bbsUuid) {
		this.bbsUuid = bbsUuid;
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
	
	public String getFullDefinition() {
		return fullDefinition;
	}

	public void setFullDefinition(String fullDefinition) {
		this.fullDefinition = fullDefinition;
	}
	
	public String getHolderDefinition() {
		return holderDefinition;
	}

	public void setHolderDefinition(String holderDefinition) {
		this.holderDefinition = holderDefinition;
	}
	
	public Integer getSeqLength() {
		return seqLength;
	}

	public void setSeqLength(Integer seqLength) {
		this.seqLength = seqLength;
	}
	
	public Integer getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(Integer maxLength) {
		this.maxLength = maxLength;
	}

}


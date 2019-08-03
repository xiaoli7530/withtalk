package com.ctop.base.dto;

import java.util.Date;
import java.io.Serializable;import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;

import com.ctop.fw.common.model.BaseDto;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * <pre>
 * 功能说明：BaseBizSeqDetail实体的数据传输对像
 * 创建者：${user}
 * 创建时间：${date}
 * 版本：${version}
 * </pre>
 */
public class BaseBizSeqDetailDto extends BaseDto implements Serializable {
	private String bbsdUuid;
	private String bbsUuid;
	private String bizSeqHolder;
	private Long sequence;

	
	public String getBbsdUuid() {
		return bbsdUuid;
	}

	public void setBbsdUuid(String bbsdUuid) {
		this.bbsdUuid = bbsdUuid;
	}
	
	public String getBbsUuid() {
		return bbsUuid;
	}

	public void setBbsUuid(String bbsUuid) {
		this.bbsUuid = bbsUuid;
	}
	
	public String getBizSeqHolder() {
		return bizSeqHolder;
	}

	public void setBizSeqHolder(String bizSeqHolder) {
		this.bizSeqHolder = bizSeqHolder;
	}
	
	public Long getSequence() {
		return sequence;
	}

	public void setSequence(Long sequence) {
		this.sequence = sequence;
	}

}


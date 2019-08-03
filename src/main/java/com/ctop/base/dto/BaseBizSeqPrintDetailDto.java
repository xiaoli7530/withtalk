package com.ctop.base.dto;

import java.util.Date;
import java.io.Serializable;import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;

import com.ctop.fw.common.model.BaseDto;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * <pre>
 * 功能说明：BaseBizSeqPrintDetail实体的数据传输对像
 * 创建者：${user}
 * 创建时间：${date}
 * 版本：${version}
 * </pre>
 */
public class BaseBizSeqPrintDetailDto extends BaseDto implements Serializable {
	private String bbspdUuid;
	private String bbspUuid;
	private String bbsUuid;
	private String bizSeqNo;
	private String used;

	
	public String getBbspdUuid() {
		return bbspdUuid;
	}

	public void setBbspdUuid(String bbspdUuid) {
		this.bbspdUuid = bbspdUuid;
	}
	
	public String getBbspUuid() {
		return bbspUuid;
	}

	public void setBbspUuid(String bbspUuid) {
		this.bbspUuid = bbspUuid;
	}
	
	public String getBbsUuid() {
		return bbsUuid;
	}

	public void setBbsUuid(String bbsUuid) {
		this.bbsUuid = bbsUuid;
	}
	
	public String getBizSeqNo() {
		return bizSeqNo;
	}

	public void setBizSeqNo(String bizSeqNo) {
		this.bizSeqNo = bizSeqNo;
	}
	
	public String getUsed() {
		return used;
	}

	public void setUsed(String used) {
		this.used = used;
	}

}


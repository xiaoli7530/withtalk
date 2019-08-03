package com.ctop.fw.sys.dto;

import com.ctop.fw.common.model.BaseDto;
import java.util.Date;
import java.io.Serializable;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * <pre>
 * 功能说明：SysAccountWm实体的数据传输对像
 * 创建者：${user}
 * 创建时间：${date}
 * 版本：${version}
 * </pre>
 */
public class SysAccountWmDto extends BaseDto implements Serializable {
	private Long seqNo;
	private String awUuid;
	private String accountUuid;
	private String warehouseUuid;

	
	public Long getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Long seqNo) {
		this.seqNo = seqNo;
	}
	
	public String getAwUuid() {
		return awUuid;
	}

	public void setAwUuid(String awUuid) {
		this.awUuid = awUuid;
	}
	
	public String getAccountUuid() {
		return accountUuid;
	}

	public void setAccountUuid(String accountUuid) {
		this.accountUuid = accountUuid;
	}
	
	public String getWarehouseUuid() {
		return warehouseUuid;
	}

	public void setWarehouseUuid(String warehouseUuid) {
		this.warehouseUuid = warehouseUuid;
	}

}


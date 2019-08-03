package com.ctop.base.dto;

import java.util.Date;
import java.io.Serializable;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;

import com.ctop.fw.common.model.BaseDto;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * <pre>
 * 功能说明：BasePrInfo实体的数据传输对像
 * 创建者：${user}
 * 创建时间：${date}
 * 版本：${version}
 * </pre>
 */
public class BasePrInfoDto extends BaseDto implements Serializable {
	private String prUuid;
	private String prCode;
	private String currency;
	private BigDecimal ttlAmt;
	private String wbsCode;
	private String ext1;
	private String ext2;
	private String ext3;
	private String ext4;
	private String ext5;

	
	public String getPrUuid() {
		return prUuid;
	}

	public void setPrUuid(String prUuid) {
		this.prUuid = prUuid;
	}
	
	public String getPrCode() {
		return prCode;
	}

	public void setPrCode(String prCode) {
		this.prCode = prCode;
	}
	
	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	public BigDecimal getTtlAmt() {
		return ttlAmt;
	}

	public void setTtlAmt(BigDecimal ttlAmt) {
		this.ttlAmt = ttlAmt;
	}
	
	public String getWbsCode() {
		return wbsCode;
	}

	public void setWbsCode(String wbsCode) {
		this.wbsCode = wbsCode;
	}
	
	public String getExt1() {
		return ext1;
	}

	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}
	
	public String getExt2() {
		return ext2;
	}

	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}
	
	public String getExt3() {
		return ext3;
	}

	public void setExt3(String ext3) {
		this.ext3 = ext3;
	}
	
	public String getExt4() {
		return ext4;
	}

	public void setExt4(String ext4) {
		this.ext4 = ext4;
	}
	
	public String getExt5() {
		return ext5;
	}

	public void setExt5(String ext5) {
		this.ext5 = ext5;
	}

}


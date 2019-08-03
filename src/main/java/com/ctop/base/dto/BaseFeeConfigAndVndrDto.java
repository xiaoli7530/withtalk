package com.ctop.base.dto;

import java.util.Date;
import java.io.Serializable;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;

import com.ctop.fw.common.model.BaseDto;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * <pre>
 * 功能说明：线路配置及供应商
 * 创建者：${user}
 * 创建时间：${date}
 * 版本：${version}
 * </pre>
 */
public class BaseFeeConfigAndVndrDto extends BaseDto implements Serializable {
	private String bfcUuid;
	private String feeType;
	private String bmsUuid;
	private String materialSize;
	private String trUuid;
	private String companyUuid;
	private BigDecimal price;
	private BigDecimal qty;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	@JsonFormat(pattern="yyyy-MM-dd HH:mm",timezone = "GMT+8") 
	private Date startDate;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	@JsonFormat(pattern="yyyy-MM-dd HH:mm",timezone = "GMT+8") 
	private Date endDate;
	private String isCurrent;
	private String vndrName;//供应商名称
	private String routeName;//线路名称
	private String email;//供应商Email;
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRouteName() {
		return routeName;
	}

	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}

	public String getVndrName() {
		return vndrName;
	}

	public void setVndrName(String vndrName) {
		this.vndrName = vndrName;
	}

	public String getBfcUuid() {
		return bfcUuid;
	}

	public void setBfcUuid(String bfcUuid) {
		this.bfcUuid = bfcUuid;
	}
	
	public String getFeeType() {
		return feeType;
	}

	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}
	
	public String getBmsUuid() {
		return bmsUuid;
	}

	public void setBmsUuid(String bmsUuid) {
		this.bmsUuid = bmsUuid;
	}
	
	public String getMaterialSize() {
		return materialSize;
	}

	public void setMaterialSize(String materialSize) {
		this.materialSize = materialSize;
	}
	
	public String getTrUuid() {
		return trUuid;
	}

	public void setTrUuid(String trUuid) {
		this.trUuid = trUuid;
	}
	
	public String getCompanyUuid() {
		return companyUuid;
	}

	public void setCompanyUuid(String companyUuid) {
		this.companyUuid = companyUuid;
	}
	
	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
	public BigDecimal getQty() {
		return qty;
	}

	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}
	
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public String getIsCurrent() {
		return isCurrent;
	}

	public void setIsCurrent(String isCurrent) {
		this.isCurrent = isCurrent;
	}
	 

}
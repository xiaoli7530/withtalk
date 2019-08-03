package com.ctop.base.entity;

import java.util.Date;

import java.math.BigDecimal;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat;

import com.ctop.fw.common.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;


/**
 * <pre>
 * 功能说明：${table.className}实体类
 * 创建者：${user}
 * 创建时间：${date}
 * 版本：${version}
 * </pre>
 */
@SuppressWarnings("serial") 
@Entity
@Table(name = "BASE_FEE_CONFIG")
@BatchSize(size = 20)
public class BaseFeeConfig extends BaseEntity implements Serializable {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "BFC_UUID")
	private String bfcUuid;//主键

	@Column(name = "FEE_TYPE")
	private String feeType;//费用类型（仓储：storage，长途运输-整车transport_cart、长途运输-零件transport_port，短驳,叉车,蚂蚁配服ant,包装：package）

	@Column(name = "BMS_UUID")
	private String bmsUuid;//物料尺寸UUID

	@Column(name = "MATERIAL_SIZE")
	private String materialSize;//物料尺寸类型

	@Column(name = "TR_UUID")
	private String trUuid;//运输线路UUID

	@Column(name = "COMPANY_UUID")
	private String companyUuid;//供应商UUID

	@Column(name = "PRICE")
	private BigDecimal price;//费用单价

	@Column(name = "QTY")
	private BigDecimal qty;//运输车数量

	@Column(name = "START_DATE")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8") 
	private Date startDate;//费用有效期开始

	@Column(name = "END_DATE")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8") 
	private Date endDate;//费用有效期开始

	@Column(name = "IS_CURRENT")
	private String isCurrent;//是否当前有效版本

	@Column(name = "EXT1")
	private String ext1;//扩展字段1

	@Column(name = "EXT2")
	private String ext2;//扩展字段2

	@Column(name = "EXT3")
	private String ext3;//扩展字段3

	@Column(name = "EXT4")
	private String ext4;//扩展字段4

	@Column(name = "EXT5")
	private String ext5;//扩展字段5

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


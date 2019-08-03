package com.ctop.base.entity;

import java.io.Serializable;import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.GenericGenerator;

import com.ctop.fw.common.entity.BaseEntity;


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
@Table(name = "BASE_BIZ_SEQ_PRINT")
@BatchSize(size = 20)
public class BaseBizSeqPrint extends BaseEntity implements Serializable {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "BBSP_UUID")
	private String bbspUuid;//主键

	@Column(name = "COMPANY_UUID")
	private String companyUuid;//货主UUID

	@Column(name = "BBS_UUID")
	private String bbsUuid;//业务单据定义UUID

	@Column(name = "BIZ_SEQ_CODE")
	private String bizSeqCode;//业务单据规则代码（冗余用）

	@Column(name = "FULL_DEFINITION")
	private String fullDefinition;//单据号定义字符串（冗余用）

	@Column(name = "QUANTITY")
	private Long quantity;//打印数量

	@Column(name = "BIZ_SEQ_MIN")
	private String bizSeqMin;//最小单据号

	@Column(name = "BIZ_SEQ_MAX")
	private String bizSeqMax;//最大单据号

	@Column(name = "REMAIN_QUANTITY")
	private Long remainQuantity;//剩余数量

	@Column(name = "PRINTER_NAME")
	private String printerName;//打印人（登录人名称）

	@Column(name = "REMARK")
	private String remark;//备注
	
	//OneToMany一对多   cascade:关联表同时更新或删除
	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name = "BBSP_UUID") //关联的字段
	private List<BaseBizSeqPrintDetail> details;

	public String getBbspUuid() {
		return bbspUuid;
	}

	public void setBbspUuid(String bbspUuid) {
		this.bbspUuid = bbspUuid;
	}
	
	public String getCompanyUuid() {
		return companyUuid;
	}

	public void setCompanyUuid(String companyUuid) {
		this.companyUuid = companyUuid;
	}
	
	public String getBbsUuid() {
		return bbsUuid;
	}

	public void setBbsUuid(String bbsUuid) {
		this.bbsUuid = bbsUuid;
	}
	
	public String getBizSeqCode() {
		return bizSeqCode;
	}

	public void setBizSeqCode(String bizSeqCode) {
		this.bizSeqCode = bizSeqCode;
	}
	
	public String getFullDefinition() {
		return fullDefinition;
	}

	public void setFullDefinition(String fullDefinition) {
		this.fullDefinition = fullDefinition;
	}
	
	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}
	
	public String getBizSeqMin() {
		return bizSeqMin;
	}

	public void setBizSeqMin(String bizSeqMin) {
		this.bizSeqMin = bizSeqMin;
	}
	
	public String getBizSeqMax() {
		return bizSeqMax;
	}

	public void setBizSeqMax(String bizSeqMax) {
		this.bizSeqMax = bizSeqMax;
	}
	
	public Long getRemainQuantity() {
		return remainQuantity;
	}

	public void setRemainQuantity(Long remainQuantity) {
		this.remainQuantity = remainQuantity;
	}
	
	public String getPrinterName() {
		return printerName;
	}

	public void setPrinterName(String printerName) {
		this.printerName = printerName;
	}
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<BaseBizSeqPrintDetail> getDetails() {
		return details;
	}

	public void setDetails(List<BaseBizSeqPrintDetail> details) {
		this.details = details;
	}
	
}


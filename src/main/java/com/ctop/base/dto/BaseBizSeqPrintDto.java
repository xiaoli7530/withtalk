package com.ctop.base.dto;

import java.io.Serializable;import java.math.BigDecimal;
import java.util.List;

import com.ctop.base.entity.BaseBizSeqPrintDetail;
import com.ctop.fw.common.model.BaseDto;

/**
 * <pre>
 * 功能说明：BaseBizSeqPrint实体的数据传输对像
 * 创建者：${user}
 * 创建时间：${date}
 * 版本：${version}
 * </pre>
 */
public class BaseBizSeqPrintDto extends BaseDto implements Serializable {
	private String bbspUuid;
	private String companyUuid;
	private String bbsUuid;
	private String bizSeqCode;
	private String fullDefinition;
	private Long quantity;
	private String bizSeqMin;
	private String bizSeqMax;
	private Long remainQuantity;
	private String printerName;
	private String remark;
    private String bizName;
    private String printName; 
    private List<String> bizSeqNos;
 

	public String getPrintName() {
		return printName;
	}

	public void setPrintName(String printName) {
		this.printName = printName;
	}

	public String getBizName() {
		return bizName;
	}

	public void setBizName(String bizName) {
		this.bizName = bizName;
	}

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

	public List<String> getBizSeqNos() {
		return bizSeqNos;
	}

	public void setBizSeqNos(List<String> bizSeqNos) {
		this.bizSeqNos = bizSeqNos;
	}

}


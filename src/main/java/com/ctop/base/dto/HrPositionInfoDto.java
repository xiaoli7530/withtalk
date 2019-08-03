package com.ctop.base.dto;

import com.ctop.fw.common.model.BaseDto;
import java.util.Date;
import java.io.Serializable;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * <pre>
 * 功能说明：HrPositionInfo实体的数据传输对像
 * 创建者：${user}
 * 创建时间：${date}
 * 版本：${version}
 * </pre>
 */
public class HrPositionInfoDto extends BaseDto implements Serializable {
	private String positionInfoUuid;
	private String positionNbr;
	private String positionDesc;
	private String isMasterPosition;
	private String reportTo;
	private String hrDeptSetIds;
	private String positionLevel;
	private String companyCode;
	private String positionFlag;
	private String posStatus;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	@JsonFormat(pattern="yyyy-MM-dd HH:mm",timezone = "GMT+8") 
	private Date posInactiveDate;
	private String appRoles;
	private String appRolesName;
	private String parentCode;

	
	public String getPositionInfoUuid() {
		return positionInfoUuid;
	}

	public void setPositionInfoUuid(String positionInfoUuid) {
		this.positionInfoUuid = positionInfoUuid;
	}
	
	public String getPositionNbr() {
		return positionNbr;
	}

	public void setPositionNbr(String positionNbr) {
		this.positionNbr = positionNbr;
	}
	
	public String getPositionDesc() {
		return positionDesc;
	}

	public void setPositionDesc(String positionDesc) {
		this.positionDesc = positionDesc;
	}
	
	public String getIsMasterPosition() {
		return isMasterPosition;
	}

	public void setIsMasterPosition(String isMasterPosition) {
		this.isMasterPosition = isMasterPosition;
	}
	
	public String getReportTo() {
		return reportTo;
	}

	public void setReportTo(String reportTo) {
		this.reportTo = reportTo;
	}
	
	public String getHrDeptSetIds() {
		return hrDeptSetIds;
	}

	public void setHrDeptSetIds(String hrDeptSetIds) {
		this.hrDeptSetIds = hrDeptSetIds;
	}
	
	public String getPositionLevel() {
		return positionLevel;
	}

	public void setPositionLevel(String positionLevel) {
		this.positionLevel = positionLevel;
	}
	
	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	
	public String getPositionFlag() {
		return positionFlag;
	}

	public void setPositionFlag(String positionFlag) {
		this.positionFlag = positionFlag;
	}
	
	public String getPosStatus() {
		return posStatus;
	}

	public void setPosStatus(String posStatus) {
		this.posStatus = posStatus;
	}
	
	public Date getPosInactiveDate() {
		return posInactiveDate;
	}

	public void setPosInactiveDate(Date posInactiveDate) {
		this.posInactiveDate = posInactiveDate;
	}
	
	public String getAppRoles() {
		return appRoles;
	}

	public void setAppRoles(String appRoles) {
		this.appRoles = appRoles;
	}
	
	public String getAppRolesName() {
		return appRolesName;
	}

	public void setAppRolesName(String appRolesName) {
		this.appRolesName = appRolesName;
	}
	
	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

}


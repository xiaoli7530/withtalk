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

import com.ctop.fw.common.entity.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;
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
@Table(name = "HR_POSITION_INFO")
@BatchSize(size = 20)
public class HrPositionInfo extends BaseEntity implements Serializable {

	@Id
	@Column(name = "POSITION_INFO_UUID")
	private String positionInfoUuid;//UUID

	@Column(name = "POSITION_NBR")
	private String positionNbr;//岗位编号

	@Column(name = "POSITION_DESC")
	private String positionDesc;//岗位描述

	@Column(name = "IS_MASTER_POSITION")
	private String isMasterPosition;//1--表示主要组织或岗位，0--表示兼职所在的组织

	@Column(name = "REPORT_TO")
	private String reportTo;//汇报人所在的岗位,父岗位代码

	@Column(name = "HR_DEPT_SET_IDS")
	private String hrDeptSetIds;//组织唯一识别号列表

	@Column(name = "POSITION_LEVEL")
	private String positionLevel;//

	@Column(name = "COMPANY_CODE")
	private String companyCode;//公司ID

	@Column(name = "POSITION_FLAG")
	private String positionFlag;//用于区分从HRMS过来的正式岗位还是由业务自行创建的虚拟岗位

	@Column(name = "POS_STATUS")
	private String posStatus;//职位或角色状态

	@Column(name = "POS_INACTIVE_DATE")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8") 
	private Date posInactiveDate;//岗位/角色由有效变为失效状态的时间

	@Column(name = "APP_ROLES")
	private String appRoles;//角色

	@Column(name = "APP_ROLES_NAME")
	private String appRolesName;//角色名称

	@Column(name = "PARENT_CODE")
	private String parentCode;//组织dept_set_id集

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


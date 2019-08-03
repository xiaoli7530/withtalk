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
@Table(name = "HR_EMPLOYEES_ORG_POSITION_RLT")
@BatchSize(size = 20)
public class HrEmployeesOrgPositionRlt extends BaseEntity implements Serializable {

	@Id
	@Column(name = "RLT_UUID")
	private String rltUuid;//UUID

	@Column(name = "EMP_UUID")
	private String empUuid;//员工主键

	@Column(name = "HR_EMPL_ID")
	private String hrEmplId;//HrMs员工编码

	@Column(name = "POSITION_NBR")
	private String positionNbr;//岗位编号

	@Column(name = "POSITION_INFO_UUID")
	private String positionInfoUuid;//UUID

	@Column(name = "DEPARTMENT_UUID")
	private String departmentUuid;//部门主键

	@Column(name = "HR_DEPT_SET_ID")
	private String hrDeptSetId;//组织唯一识别号

	@Column(name = "POSITION_NBR_EPMS")
	private String positionNbrEpms;//岗位编号(epms)

	public String getRltUuid() {
		return rltUuid;
	}

	public void setRltUuid(String rltUuid) {
		this.rltUuid = rltUuid;
	}
	
	public String getEmpUuid() {
		return empUuid;
	}

	public void setEmpUuid(String empUuid) {
		this.empUuid = empUuid;
	}
	
	public String getHrEmplId() {
		return hrEmplId;
	}

	public void setHrEmplId(String hrEmplId) {
		this.hrEmplId = hrEmplId;
	}
	
	public String getPositionNbr() {
		return positionNbr;
	}

	public void setPositionNbr(String positionNbr) {
		this.positionNbr = positionNbr;
	}
	
	public String getPositionInfoUuid() {
		return positionInfoUuid;
	}

	public void setPositionInfoUuid(String positionInfoUuid) {
		this.positionInfoUuid = positionInfoUuid;
	}
	
	public String getDepartmentUuid() {
		return departmentUuid;
	}

	public void setDepartmentUuid(String departmentUuid) {
		this.departmentUuid = departmentUuid;
	}
	
	public String getHrDeptSetId() {
		return hrDeptSetId;
	}

	public void setHrDeptSetId(String hrDeptSetId) {
		this.hrDeptSetId = hrDeptSetId;
	}
	
	public String getPositionNbrEpms() {
		return positionNbrEpms;
	}

	public void setPositionNbrEpms(String positionNbrEpms) {
		this.positionNbrEpms = positionNbrEpms;
	}
	
}


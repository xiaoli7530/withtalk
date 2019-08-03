package com.ctop.fw.hr.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.GenericGenerator;
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
@Table(name = "HR_DEPARTMENT")
@BatchSize(size = 20)
public class HrDepartment extends BaseEntity implements Serializable {

	@Column(name = "HR_DEPT_SET_ID")
	private String hrDeptSetId;//组织唯一识别号，由SETID+DEP_ID组合而成

	@Column(name = "HR_SET_ID")
	private String hrSetId;//业务单位代码

	@Column(name = "HR_DEPT_ID")
	private String hrDeptId;//组织代码

	@Column(name = "DEPT_ENG_DESC")
	private String deptEngDesc;//组织英文描述

	@Column(name = "DEPT_SHORT_DESC")
	private String deptShortDesc;//组织短描述

	@Column(name = "DEPT_LEVL")
	private String deptLevl;//组织层级

	@Column(name = "DEPT_STATUS")
	private String deptStatus;//组织状态，缺省为"A"

	@Column(name = "DEPT_FLAG")
	private String deptFlag;//用于区分是由HRMS同步过来的正式组织数据，还是由业务自建的虚拟织

	@Column(name = "DEPT_INACTIVE_DATE")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8") 
	private Date deptInactiveDate;//部门变为INACTIVE时间

	@Column(name = "PARENT_SET_ID")
	private String parentSetId;//上级业务单位代码

	@Column(name = "PARENT_HR_DEPT_ID")
	private String parentHrDeptId;//上级组织代码

	@Column(name = "SOURCE_TYPE")
	private String sourceType;//组织来源类型（HRMS/EPMS）

	@Id
	@Column(name = "DEPARTMENT_UUID")
	private String departmentUuid;//uuid

	@Column(name = "SEQ_NO")
	private Long seqNo;//排序

	@Column(name = "PARENT_UUID")
	private String parentUuid;//父类部门

	@Column(name = "DEPARTMENT_NO")
	private String departmentNo;//部门编号

	@Column(name = "DEPARTMENT_NAME")
	private String departmentName;//名称

	@Column(name = "COMPANY_UUID")
	private String companyUuid;//公司

	@Column(name = "ORG_PHONE")
	private String orgPhone;//部门电话

	@Column(name = "ORG_MANAGER")
	private String orgManager;//部门负责人

	@Column(name = "ADDRESS")
	private String address;//地址

	@Column(name = "REMARK")
	private String remark;//备注

	@Column(name = "IS_WAREHOUSE")
	private String isWarehouse;//是否仓库

	@Column(name = "DEPT_CATEGORY")
	private String deptCategory;

	@Column(name = "HR_TARGET_SYSTEM")
	private String hrTargetSystem;
	
	@Column(name = "HR_UPDATE_TIME")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date hrUpdateTime;
	
	@Column(name = "HR_CREATE_TIME")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date hrCreateTime;
	
	@Column(name = "HR_COMPANY_CODE")
	private String hrCompanyCode;
	
	 

	public String getHrDeptSetId() {
		return hrDeptSetId;
	}

	public void setHrDeptSetId(String hrDeptSetId) {
		this.hrDeptSetId = hrDeptSetId;
	}
	
	public String getHrSetId() {
		return hrSetId;
	}

	public void setHrSetId(String hrSetId) {
		this.hrSetId = hrSetId;
	}
	
	public String getHrDeptId() {
		return hrDeptId;
	}

	public void setHrDeptId(String hrDeptId) {
		this.hrDeptId = hrDeptId;
	}
	
	public String getDeptEngDesc() {
		return deptEngDesc;
	}

	public void setDeptEngDesc(String deptEngDesc) {
		this.deptEngDesc = deptEngDesc;
	}
	
	public String getDeptShortDesc() {
		return deptShortDesc;
	}

	public void setDeptShortDesc(String deptShortDesc) {
		this.deptShortDesc = deptShortDesc;
	}
	
	public String getDeptLevl() {
		return deptLevl;
	}

	public void setDeptLevl(String deptLevl) {
		this.deptLevl = deptLevl;
	}
	
	public String getDeptStatus() {
		return deptStatus;
	}

	public void setDeptStatus(String deptStatus) {
		this.deptStatus = deptStatus;
	}
	
	public String getDeptFlag() {
		return deptFlag;
	}

	public void setDeptFlag(String deptFlag) {
		this.deptFlag = deptFlag;
	}
	
	public Date getDeptInactiveDate() {
		return deptInactiveDate;
	}

	public void setDeptInactiveDate(Date deptInactiveDate) {
		this.deptInactiveDate = deptInactiveDate;
	}
	
	public String getParentSetId() {
		return parentSetId;
	}

	public void setParentSetId(String parentSetId) {
		this.parentSetId = parentSetId;
	}
	
	public String getParentHrDeptId() {
		return parentHrDeptId;
	}

	public void setParentHrDeptId(String parentHrDeptId) {
		this.parentHrDeptId = parentHrDeptId;
	}
	
	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}
	
	public String getDepartmentUuid() {
		return departmentUuid;
	}

	public void setDepartmentUuid(String departmentUuid) {
		this.departmentUuid = departmentUuid;
	}
	
	public Long getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Long seqNo) {
		this.seqNo = seqNo;
	}
	
	public String getParentUuid() {
		return parentUuid;
	}

	public void setParentUuid(String parentUuid) {
		this.parentUuid = parentUuid;
	}
	
	public String getDepartmentNo() {
		return departmentNo;
	}

	public void setDepartmentNo(String departmentNo) {
		this.departmentNo = departmentNo;
	}
	
	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	
	public String getCompanyUuid() {
		return companyUuid;
	}

	public void setCompanyUuid(String companyUuid) {
		this.companyUuid = companyUuid;
	}
	
	public String getOrgPhone() {
		return orgPhone;
	}

	public void setOrgPhone(String orgPhone) {
		this.orgPhone = orgPhone;
	}
	
	public String getOrgManager() {
		return orgManager;
	}

	public void setOrgManager(String orgManager) {
		this.orgManager = orgManager;
	}
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public String getIsWarehouse() {
		return isWarehouse;
	}

	public void setIsWarehouse(String isWarehouse) {
		this.isWarehouse = isWarehouse;
	}

	public String getDeptCategory() {
		return deptCategory;
	}

	public void setDeptCategory(String deptCategory) {
		this.deptCategory = deptCategory;
	}

	public String getHrTargetSystem() {
		return hrTargetSystem;
	}

	public void setHrTargetSystem(String hrTargetSystem) {
		this.hrTargetSystem = hrTargetSystem;
	}

	public Date getHrUpdateTime() {
		return hrUpdateTime;
	}

	public void setHrUpdateTime(Date hrUpdateTime) {
		this.hrUpdateTime = hrUpdateTime;
	}

	public Date getHrCreateTime() {
		return hrCreateTime;
	}

	public void setHrCreateTime(Date hrCreateTime) {
		this.hrCreateTime = hrCreateTime;
	}

	public String getHrCompanyCode() {
		return hrCompanyCode;
	}

	public void setHrCompanyCode(String hrCompanyCode) {
		this.hrCompanyCode = hrCompanyCode;
	}
}


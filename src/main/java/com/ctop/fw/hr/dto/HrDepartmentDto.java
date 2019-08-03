package com.ctop.fw.hr.dto;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.ctop.fw.common.model.BaseDto;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * <pre>
 * 功能说明：HrDepartment实体的数据传输对像
 * 创建者：${user}
 * 创建时间：${date}
 * 版本：${version}
 * </pre>
 */
public class HrDepartmentDto extends BaseDto implements Serializable {
	private static final long serialVersionUID = 1368968549597399208L;
	private String hrDeptSetId;
	private String hrSetId;
	private String hrDeptId;
	private String deptEngDesc;
	private String deptShortDesc;
	private String deptLevl;
	private String deptStatus;
	private String deptFlag;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	@JsonFormat(pattern="yyyy-MM-dd HH:mm",timezone = "GMT+8") 
	private Date deptInactiveDate;
	private String parentSetId;
	private String itSystem;
	private String parentHrDeptId;
	private String sourceType;
	private String departmentUuid;
	private Long seqNo;
	private String parentUuid;
	private String departmentNo;
	private String departmentName;
	private String companyUuid;
	private String orgPhone;
	private String orgManager;
	private String address;
	private String remark;
	private String isWarehouse;
	private String deptCategory;
	private String hrTargetSystem;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date hrUpdateTime;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date hrCreateTime;
	private String hrCompanyCode;
	private String deptType;
	private Long empNum;
	private String pinyin;//拼音
	
	
	
	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public String getDeptType() {
		return deptType;
	}

	public void setDeptType(String deptType) {
		this.deptType = deptType;
	}

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
	
	public String getItSystem() {
		return itSystem;
	}

	public void setItSystem(String itSystem) {
		this.itSystem = itSystem;
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

	public Long getEmpNum() {
		return empNum;
	}

	public void setEmpNum(Long empNum) {
		this.empNum = empNum;
	}
}


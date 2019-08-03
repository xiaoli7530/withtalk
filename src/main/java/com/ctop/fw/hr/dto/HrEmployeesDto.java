package com.ctop.fw.hr.dto;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.ctop.fw.common.model.BaseDto;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * <pre>
 * 功能说明：HrEmployees实体的数据传输对像
 * 创建者：${user}
 * 创建时间：${date}
 * 版本：${version}
 * </pre>
 */
public class HrEmployeesDto extends BaseDto implements Serializable {
	private static final long serialVersionUID = -2138648810495688061L;
	private String empUuid;
	private String empCode;
	private String empName;
	private String empPhone;
	private String empSex;
	private String empEmail;
	@DateTimeFormat(pattern="yyyy-MM-dd")  
	@JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8") 
	private Date empBirthday;
	private String departmentUuid;
	private String departmentName;
	private String acctId;
	private String empType;
	@DateTimeFormat(pattern="yyyy-MM-dd")  
	@JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8") 
	private Date leaveDate;
	private String status;
	private String pinyin;
	private String empPost;
	private String remark;
	private String isEnabled;
	
	private String userCd;
	private String firstName;
	private String lastName;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	@JsonFormat(pattern="yyyy-MM-dd HH:mm",timezone = "GMT+8") 
	private Date terminationDt;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	@JsonFormat(pattern="yyyy-MM-dd HH:mm",timezone = "GMT+8") 
	private Date origHireDt;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	@JsonFormat(pattern="yyyy-MM-dd HH:mm",timezone = "GMT+8") 
	private Date lastupddttm;
	private String sgmEeType;
	private String salAdminPlan;
	private String sgmEpSpec;
	private String emplFlag;
	private String registereFlag;
	private String email;
	private String parentDeptId;
	private String parentSetId;
	private String hrStatus;
	
	private String positionNbrs;
	private String nationalInfoUuids;
	private String addressInfoUuids;
	private String contactInfoUuids;
	private String avatar ;//头像
	private Long showTips;//显示提示
	
	private String outEngineerUuid;
 
	
	private String jobName;
	private String times;
	@DateTimeFormat(pattern="yyyy-MM-dd")  
	@JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8") 
	private Date workBegin;
	@DateTimeFormat(pattern="yyyy-MM-dd")  
	@JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8") 
	private Date workEnd;
	private String pkOmJob;
	private String touchId;
	
	private String accountUuid;
	private String departmentNo;
	
	
	public String getAccountUuid() {
		return accountUuid;
	}

	public void setAccountUuid(String accountUuid) {
		this.accountUuid = accountUuid;
	}

	 

	public String getTouchId() {
		return touchId;
	}

	public void setTouchId(String touchId) {
		this.touchId = touchId;
	}

	public String getPkOmJob() {
		return pkOmJob;
	}

	public void setPkOmJob(String pkOmJob) {
		this.pkOmJob = pkOmJob;
	}

	public Date getWorkBegin() {
		return workBegin;
	}

	public void setWorkBegin(Date workBegin) {
		this.workBegin = workBegin;
	}

	public Date getWorkEnd() {
		return workEnd;
	}

	public void setWorkEnd(Date workEnd) {
		this.workEnd = workEnd;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getTimes() {
		return times;
	}

	public void setTimes(String times) {
		this.times = times;
	}
 

	public String getOutEngineerUuid() {
		return outEngineerUuid;
	}

	public void setOutEngineerUuid(String outEngineerUuid) {
		this.outEngineerUuid = outEngineerUuid;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getEmpUuid() {
		return empUuid;
	}

	public void setEmpUuid(String empUuid) {
		this.empUuid = empUuid;
	}
	
	public String getEmpCode() {
		return empCode;
	}

	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}
	
	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}
	
	public String getEmpPhone() {
		return empPhone;
	}

	public void setEmpPhone(String empPhone) {
		this.empPhone = empPhone;
	}
	
	public String getEmpSex() {
		return empSex;
	}

	public void setEmpSex(String empSex) {
		this.empSex = empSex;
	}
	
	public String getEmpEmail() {
		return empEmail;
	}

	public void setEmpEmail(String empEmail) {
		this.empEmail = empEmail;
	}
	
	public Date getEmpBirthday() {
		return empBirthday;
	}

	public void setEmpBirthday(Date empBirthday) {
		this.empBirthday = empBirthday;
	}
	
	public String getDepartmentUuid() {
		return departmentUuid;
	}

	public void setDepartmentUuid(String departmentUuid) {
		this.departmentUuid = departmentUuid;
	}
	
	public String getAcctId() {
		return acctId;
	}

	public void setAcctId(String acctId) {
		this.acctId = acctId;
	}
	
	public String getEmpType() {
		return empType;
	}

	public void setEmpType(String empType) {
		this.empType = empType;
	}
	
	public Date getLeaveDate() {
		return leaveDate;
	}

	public void setLeaveDate(Date leaveDate) {
		this.leaveDate = leaveDate;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}
	
	public String getEmpPost() {
		return empPost;
	}

	public void setEmpPost(String empPost) {
		this.empPost = empPost;
	}
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(String isEnabled) {
		this.isEnabled = isEnabled;
	}
	
	/*>>>>>>>>>>>>>>>>>>>>>>>>>>>>*/
	
	public String getUserCd() {
		return userCd;
	}

	public void setUserCd(String userCd) {
		this.userCd = userCd;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getTerminationDt() {
		return terminationDt;
	}

	public void setTerminationDt(Date terminationDt) {
		this.terminationDt = terminationDt;
	}

	public Date getOrigHireDt() {
		return origHireDt;
	}

	public void setOrigHireDt(Date origHireDt) {
		this.origHireDt = origHireDt;
	}

	public Date getLastupddttm() {
		return lastupddttm;
	}

	public void setLastupddttm(Date lastupddttm) {
		this.lastupddttm = lastupddttm;
	}

	public String getSgmEeType() {
		return sgmEeType;
	}

	public void setSgmEeType(String sgmEeType) {
		this.sgmEeType = sgmEeType;
	}

	public String getSalAdminPlan() {
		return salAdminPlan;
	}

	public void setSalAdminPlan(String salAdminPlan) {
		this.salAdminPlan = salAdminPlan;
	}

	public String getSgmEpSpec() {
		return sgmEpSpec;
	}

	public void setSgmEpSpec(String sgmEpSpec) {
		this.sgmEpSpec = sgmEpSpec;
	}

	public String getEmplFlag() {
		return emplFlag;
	}

	public void setEmplFlag(String emplFlag) {
		this.emplFlag = emplFlag;
	}

	public String getRegistereFlag() {
		return registereFlag;
	}

	public void setRegistereFlag(String registereFlag) {
		this.registereFlag = registereFlag;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getParentDeptId() {
		return parentDeptId;
	}

	public void setParentDeptId(String parentDeptId) {
		this.parentDeptId = parentDeptId;
	}

	public String getParentSetId() {
		return parentSetId;
	}

	public void setParentSetId(String parentSetId) {
		this.parentSetId = parentSetId;
	}

	public String getPositionNbrs() {
		return positionNbrs;
	}

	public void setPositionNbrs(String positionNbrs) {
		this.positionNbrs = positionNbrs;
	}

	public String getNationalInfoUuids() {
		return nationalInfoUuids;
	}

	public void setNationalInfoUuids(String nationalInfoUuids) {
		this.nationalInfoUuids = nationalInfoUuids;
	}

	public String getAddressInfoUuids() {
		return addressInfoUuids;
	}

	public void setAddressInfoUuids(String addressInfoUuids) {
		this.addressInfoUuids = addressInfoUuids;
	}
	
	public String getContactInfoUuids() {
		return contactInfoUuids;
	}

	public void setContactInfoUuids(String contactInfoUuids) {
		this.contactInfoUuids = contactInfoUuids;
	}
	
	public String getHrStatus() {
		return hrStatus;
	}

	public void setHrStatus(String hrStatus) {
		this.hrStatus = hrStatus;
	}


	public static class EmployeeUuidAndName {
		private String empUuid;
		private String empName;
		public String getEmpUuid() {
			return empUuid;
		}
		public void setEmpUuid(String empUuid) {
			this.empUuid = empUuid;
		}
		public String getEmpName() {
			return empName;
		}
		public void setEmpName(String empName) {
			this.empName = empName;
		}
	}


	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public Long getShowTips() {
		return showTips;
	}

	public void setShowTips(Long showTips) {
		this.showTips = showTips;
	}

	  

	public String getDepartmentNo() {
		return departmentNo;
	}

	public void setDepartmentNo(String departmentNo) {
		this.departmentNo = departmentNo;
	}
}


package com.ctop.fw.hr.entity;

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
@Table(name = "HR_EMPLOYEES")
@BatchSize(size = 20)
public class HrEmployees extends BaseEntity implements Serializable {

	@Column(name = "WORK_STATUS")
	private String workStatus;//工作状态（叉车）

	@Column(name = "AVATAR")
	private String avatar;//头像

	@Column(name = "SHOW_TIPS")
	private Integer showTips;//显示提示

	@Column(name = "SCAN_PLACE_NAME")
	private String scanPlaceName;//扫描地点

	@Column(name = "HR_EMPL_CLASS")
	private String hrEmplClass;//HRMS员工类别

	@Column(name = "SCAN_PLACE_UUID")
	private String scanPlaceUuid;//

	@Column(name = "PAS_UUID")
	private String pasUuid;//工位uuid

	@Column(name = "ANT_AREA")
	private String antArea;//蚂蚁配服园区；fanya-泛亚，jinqiao-金桥

	@Column(name = "FOREMAN_UUID")
	private String foremanUuid;//班组长

	@Column(name = "FOREMAN_NAME")
	private String foremanName;//班组长

	@Column(name = "IS_ENABLED")
	private String isEnabled;//是否启用（Y:启用；N:禁用）

	@Column(name = "TERMINATION_DT")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8") 
	private Date terminationDt;//TERMINATION_DT

	@Column(name = "ORIG_HIRE_DT")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8") 
	private Date origHireDt;//雇用日期

	@Column(name = "LASTUPDDTTM")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8") 
	private Date lastupddttm;//最后更新时间

	@Column(name = "FIRST_NAME")
	private String firstName;//姓

	@Column(name = "LAST_NAME")
	private String lastName;//名

	@Column(name = "SGM_EE_TYPE")
	private String sgmEeType;//SGM_EE_TYPE

	@Column(name = "EMPL_FLAG")
	private String emplFlag;//雇佣类型

	@Column(name = "REGISTERE_FLAG")
	private String registereFlag;//注册表示

	@Column(name = "SAL_ADMIN_PLAN")
	private String salAdminPlan;//SAL_ADMIN_PLAN

	@Column(name = "SGM_EP_SPEC")
	private String sgmEpSpec;//SGM_EP_SPEC

	@Column(name = "ADDRESS")
	private String address;//地址(作废)

	@Column(name = "ADDRESS_TYPE")
	private String addressType;//地址类型(作废)

	@Column(name = "POSTAL")
	private String postal;//邮编(作废)

	@Column(name = "PHONE_TYPE")
	private String phoneType;//电话类型(作废)

	@Column(name = "NATIONAL_ID_TYPE")
	private String nationalIdType;//NATIONAL_ID_TYPE(作废)

	@Column(name = "NATIONAL_ID")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8") 
	private Date nationalId;//NATIONAL_ID(作废)

	@Column(name = "USER_CD")
	private String userCd;//域帐号

	@Column(name = "EMAIL")
	private String email;//邮箱

	@Column(name = "ORG_NM")
	private String orgNm;//ORG_NM

	@Column(name = "DEPT_LEVL")
	private String deptLevl;//部门级别

	@Column(name = "REPORT_TO")
	private String reportTo;//REPORT_TO(作废)

	@Column(name = "IS_MASTER_POSITION")
	private String isMasterPosition;//IS_MASTER_POSITION(作废)

	@Column(name = "POSITION_DESC")
	private String positionDesc;//POSITION_DESC(作废)

	@Column(name = "POSITION_NBR")
	private String positionNbr;//岗位编码(作废)

	@Column(name = "PARENT_SET_ID")
	private String parentSetId;//PARENT_SET_ID

	@Column(name = "PARENT_DEPT_ID")
	private String parentDeptId;//PARENT_DEPT_ID

	@Column(name = "HRMS_DEPT_ID")
	private String hrmsDeptId;//部门ID(作废)

	@Column(name = "POSITION_NBRS")
	private String positionNbrs;//岗位编码列表

	@Column(name = "NATIONAL_INFO_UUIDS")
	private String nationalInfoUuids;//国籍UUID列表

	@Column(name = "ADDRESS_INFO_UUIDS")
	private String addressInfoUuids;//地址UUID列表

	@Column(name = "CONTACT_INFO_UUIDS")
	private String contactInfoUuids;//联系人UUID列表

	@Column(name = "HR_STATUS")
	private String hrStatus;//HR系统的状态

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "EMP_UUID")
	private String empUuid;//uuid

	@Column(name = "EMP_CODE")
	private String empCode;//员工编号

	@Column(name = "EMP_NAME")
	private String empName;//姓名

	@Column(name = "EMP_PHONE")
	private String empPhone;//电话

	@Column(name = "EMP_SEX")
	private String empSex;//性别

	@Column(name = "EMP_EMAIL")
	private String empEmail;//email

	@Column(name = "EMP_BIRTHDAY")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8") 
	private Date empBirthday;//生日

	@Column(name = "DEPARTMENT_UUID")
	private String departmentUuid;//部门UUID

	@Column(name = "ACCT_ID")
	private String acctId;//对应账号ID

	@Column(name = "EMP_TYPE")
	private String empType;//员工类别

	@Column(name = "LEAVE_DATE")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8") 
	private Date leaveDate;//离职时间

	@Column(name = "STATUS")
	private String status;//状态(正常、离职)

	@Column(name = "PINYIN")
	private String pinyin;//拼音

	@Column(name = "EMP_POST")
	private String empPost;//职位

	@Column(name = "REMARK")
	private String remark;//备注

	public String getWorkStatus() {
		return workStatus;
	}

	public void setWorkStatus(String workStatus) {
		this.workStatus = workStatus;
	}
	
	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	
	public Integer getShowTips() {
		return showTips;
	}

	public void setShowTips(Integer showTips) {
		this.showTips = showTips;
	}
	
	public String getScanPlaceName() {
		return scanPlaceName;
	}

	public void setScanPlaceName(String scanPlaceName) {
		this.scanPlaceName = scanPlaceName;
	}
	
	public String getHrEmplClass() {
		return hrEmplClass;
	}

	public void setHrEmplClass(String hrEmplClass) {
		this.hrEmplClass = hrEmplClass;
	}
	
	public String getScanPlaceUuid() {
		return scanPlaceUuid;
	}

	public void setScanPlaceUuid(String scanPlaceUuid) {
		this.scanPlaceUuid = scanPlaceUuid;
	}
	
	public String getPasUuid() {
		return pasUuid;
	}

	public void setPasUuid(String pasUuid) {
		this.pasUuid = pasUuid;
	}
	
	public String getAntArea() {
		return antArea;
	}

	public void setAntArea(String antArea) {
		this.antArea = antArea;
	}
	
	public String getForemanUuid() {
		return foremanUuid;
	}

	public void setForemanUuid(String foremanUuid) {
		this.foremanUuid = foremanUuid;
	}
	
	public String getForemanName() {
		return foremanName;
	}

	public void setForemanName(String foremanName) {
		this.foremanName = foremanName;
	}
	
	public String getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(String isEnabled) {
		this.isEnabled = isEnabled;
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
	
	public String getSgmEeType() {
		return sgmEeType;
	}

	public void setSgmEeType(String sgmEeType) {
		this.sgmEeType = sgmEeType;
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
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getAddressType() {
		return addressType;
	}

	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}
	
	public String getPostal() {
		return postal;
	}

	public void setPostal(String postal) {
		this.postal = postal;
	}
	
	public String getPhoneType() {
		return phoneType;
	}

	public void setPhoneType(String phoneType) {
		this.phoneType = phoneType;
	}
	
	public String getNationalIdType() {
		return nationalIdType;
	}

	public void setNationalIdType(String nationalIdType) {
		this.nationalIdType = nationalIdType;
	}
	
	public Date getNationalId() {
		return nationalId;
	}

	public void setNationalId(Date nationalId) {
		this.nationalId = nationalId;
	}
	
	public String getUserCd() {
		return userCd;
	}

	public void setUserCd(String userCd) {
		this.userCd = userCd;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getOrgNm() {
		return orgNm;
	}

	public void setOrgNm(String orgNm) {
		this.orgNm = orgNm;
	}
	
	public String getDeptLevl() {
		return deptLevl;
	}

	public void setDeptLevl(String deptLevl) {
		this.deptLevl = deptLevl;
	}
	
	public String getReportTo() {
		return reportTo;
	}

	public void setReportTo(String reportTo) {
		this.reportTo = reportTo;
	}
	
	public String getIsMasterPosition() {
		return isMasterPosition;
	}

	public void setIsMasterPosition(String isMasterPosition) {
		this.isMasterPosition = isMasterPosition;
	}
	
	public String getPositionDesc() {
		return positionDesc;
	}

	public void setPositionDesc(String positionDesc) {
		this.positionDesc = positionDesc;
	}
	
	public String getPositionNbr() {
		return positionNbr;
	}

	public void setPositionNbr(String positionNbr) {
		this.positionNbr = positionNbr;
	}
	
	public String getParentSetId() {
		return parentSetId;
	}

	public void setParentSetId(String parentSetId) {
		this.parentSetId = parentSetId;
	}
	
	public String getParentDeptId() {
		return parentDeptId;
	}

	public void setParentDeptId(String parentDeptId) {
		this.parentDeptId = parentDeptId;
	}
	
	public String getHrmsDeptId() {
		return hrmsDeptId;
	}

	public void setHrmsDeptId(String hrmsDeptId) {
		this.hrmsDeptId = hrmsDeptId;
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
	
}


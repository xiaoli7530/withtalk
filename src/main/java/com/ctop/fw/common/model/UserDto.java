package com.ctop.fw.common.model;

import java.io.Serializable;
import java.util.List;

import com.ctop.base.dto.BaseCompanyDto;
import com.ctop.fw.hr.dto.HrDepartmentDto;
import com.ctop.fw.sys.dto.SysAccountDto;
import com.ctop.fw.sys.dto.SysPermissionSimpleDto;
import com.ctop.fw.sys.dto.SysResourceDto;


/**
 * 当前登陆用户信息
 */
public class UserDto implements Serializable {
	private static final long serialVersionUID = -7725191871053302763L;
	private SysAccountDto sysAccount;
	private BaseCompanyDto baseCompany;
	private List<SysResourceDto> sysResources;
	private List<SysPermissionSimpleDto> sysPermissions;
	private HrDepartmentDto hrDepartment;
	private Boolean screenLocked = Boolean.FALSE;
	
	private String empUuid;
	private String empCode;
	private String empName;
	private String empEmail;
	private String departmentUuid;
	private String departmentNo;
	private String departmentName;
	private String departmentType; //部门类型  GS(公司) DW(单位)
	private String deliverySeq;//交样序号
	// 标志是否是SSO登录过来的用户
	private boolean loginFromSso = false;
	private String empPhone;
	private String websocketUri;
	private String avatar;
	private Long showTips;//显示提示
	private boolean hasWmsPerm = false;//有wms权限
	private List<String> roleCodeList;//角色代码
	private String currentProjectUuid;//当前选中的项目UUI



	public String getCurrentProjectUuid() {
		return currentProjectUuid;
	}

	public void setCurrentProjectUuid(String currentProjectUuid) {
		this.currentProjectUuid = currentProjectUuid;
	}

	public List<String> getRoleCodeList() {
		return roleCodeList;
	}

	public void setRoleCodeList(List<String> roleCodeList) {
		this.roleCodeList = roleCodeList;
	}

	public String getEmpPhone() {
		return empPhone;
	}

	public void setEmpPhone(String empPhone) {
		this.empPhone = empPhone;
	}

	public String getDeliverySeq() {
		return deliverySeq;
	}

	public void setDeliverySeq(String deliverySeq) {
		this.deliverySeq = deliverySeq;
	}
	
	public SysAccountDto getSysAccount() {
		return sysAccount;
	}
	public void setSysAccount(SysAccountDto sysAccount) {
		this.sysAccount = sysAccount;
	}
	public BaseCompanyDto getBaseCompany() {
		return baseCompany;
	}
	public void setBaseCompany(BaseCompanyDto baseCompany) {
		this.baseCompany = baseCompany;
	}
	public List<SysResourceDto> getSysResources() {
		return sysResources;
	}
	public void setSysResources(List<SysResourceDto> sysResources) {
		this.sysResources = sysResources;
	}
	public List<SysPermissionSimpleDto> getSysPermissions() {
		return sysPermissions;
	}
	public void setSysPermissions(List<SysPermissionSimpleDto> sysPermissions) {
		this.sysPermissions = sysPermissions;
	}

	public Boolean getScreenLocked() {
		return screenLocked;
	}

	public void setScreenLocked(Boolean screenLocked) {
		this.screenLocked = screenLocked;
	}

	public HrDepartmentDto getHrDepartment() {
		return hrDepartment;
	}

	public void setHrDepartment(HrDepartmentDto hrDepartment) {
		this.hrDepartment = hrDepartment;
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

	public String getDepartmentUuid() {
		return departmentUuid;
	}

	public void setDepartmentUuid(String departmentUuid) {
		this.departmentUuid = departmentUuid;
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

	public String getWebsocketUri() {
		return websocketUri;
	}

	public void setWebsocketUri(String websocketUri) {
		this.websocketUri = websocketUri;
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
		public boolean isLoginFromSso() {
		return loginFromSso;
	}

	public void setLoginFromSso(boolean loginFromSso) {
		this.loginFromSso = loginFromSso;
	}

	public boolean isHasWmsPerm() {
		return hasWmsPerm;
	}

	public void setHasWmsPerm(boolean hasWmsPerm) {
		this.hasWmsPerm = hasWmsPerm;
	}
	
	public String getDepartmentType() {
		return departmentType;
	}
	
	public void setDepartmentType(String departmentType) {
		this.departmentType = departmentType;
	}

	public String getEmpEmail() {
		return empEmail;
	}

	public void setEmpEmail(String empEmail) {
		this.empEmail = empEmail;
	}
	
	
}

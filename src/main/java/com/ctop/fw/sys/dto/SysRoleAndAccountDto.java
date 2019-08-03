package com.ctop.fw.sys.dto;

import java.io.Serializable;

public class SysRoleAndAccountDto implements Serializable {

	 
	private static final long serialVersionUID = 1L;

	private String roleUuid;
	
	private String name;
	
	private String roleCode;
	
	private String remark;
	
	private String roleOrigin;
	
	private String accountUuid;

	public String getRoleUuid() {
		return roleUuid;
	}

	public void setRoleUuid(String roleUuid) {
		this.roleUuid = roleUuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRoleOrigin() {
		return roleOrigin;
	}

	public void setRoleOrigin(String roleOrigin) {
		this.roleOrigin = roleOrigin;
	}

	public String getAccountUuid() {
		return accountUuid;
	}

	public void setAccountUuid(String accountUuid) {
		this.accountUuid = accountUuid;
	}
	
	
	
}
package com.ctop.fw.sys.dto;

import java.util.List;

public class SysAccountRoleParamDto {

	private List<String> roleUuids;
	private String accountUuid;
	public List<String> getRoleUuids() {
		return roleUuids;
	}
	public void setRoleUuids(List<String> roleUuids) {
		this.roleUuids = roleUuids;
	}
	public String getAccountUuid() {
		return accountUuid;
	}
	public void setAccountUuid(String accountUuid) {
		this.accountUuid = accountUuid;
	}
}

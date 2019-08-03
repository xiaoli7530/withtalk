package com.ctop.fw.sys.dto;

import java.util.Date;
import java.io.Serializable;import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;

import com.ctop.fw.common.model.BaseDto;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * <pre>
 * 功能说明：SysAccountRole实体的数据传输对像
 * 创建者：${user}
 * 创建时间：${date}
 * 版本：${version}
 * </pre>
 */
public class SysAccountRoleDto extends BaseDto implements Serializable {
	private String accountRoleUuid;
	private String accountUuid;
	private String roleUuid;

	
	public String getAccountRoleUuid() {
		return accountRoleUuid;
	}

	public void setAccountRoleUuid(String accountRoleUuid) {
		this.accountRoleUuid = accountRoleUuid;
	}
	
	public String getAccountUuid() {
		return accountUuid;
	}

	public void setAccountUuid(String accountUuid) {
		this.accountUuid = accountUuid;
	}
	
	public String getRoleUuid() {
		return roleUuid;
	}

	public void setRoleUuid(String roleUuid) {
		this.roleUuid = roleUuid;
	}

}


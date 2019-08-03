package com.ctop.fw.sys.dto;

import java.util.Date;
import java.io.Serializable;import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;

import com.ctop.fw.common.model.BaseDto;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * <pre>
 * 功能说明：SysRolePermission实体的数据传输对像
 * 创建者：${user}
 * 创建时间：${date}
 * 版本：${version}
 * </pre>
 */
public class SysRolePermissionDto extends BaseDto implements Serializable {
	private String rolePermissionUuid;
	private String roleUuid;
	private String permissionUuid;

	
	public String getRolePermissionUuid() {
		return rolePermissionUuid;
	}

	public void setRolePermissionUuid(String rolePermissionUuid) {
		this.rolePermissionUuid = rolePermissionUuid;
	}
	
	public String getRoleUuid() {
		return roleUuid;
	}

	public void setRoleUuid(String roleUuid) {
		this.roleUuid = roleUuid;
	}
	
	public String getPermissionUuid() {
		return permissionUuid;
	}

	public void setPermissionUuid(String permissionUuid) {
		this.permissionUuid = permissionUuid;
	}

}


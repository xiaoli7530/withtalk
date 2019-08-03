package com.ctop.fw.sys.dto;

import java.io.Serializable;import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.ctop.fw.common.model.BaseDto;

/**
 * <pre>
 * 功能说明：SysRole实体的数据传输对像
 * 创建者：${user}
 * 创建时间：${date}
 * 版本：${version}
 * </pre>
 */
public class SysRoleDto extends BaseDto implements Serializable {
	private String roleUuid;
	
	private String name;
	@NotNull
	@Size(min=1, max=200)
	private String roleCode;
	@Size(min=0, max=200)
	private String remark;
	private String companyUuid;
	private String roleOrigin;
	private String sql;
	
	private List<String> permissionUuids;
	
	private String roleType;
	
	private String fromType;
	
	public String getRoleType() {
		return roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}
	
	public String getRoleOrigin() {
		return roleOrigin;
	}

	public void setRoleOrigin(String roleOrigin) {
		this.roleOrigin = roleOrigin;
	}

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

	public List<String> getPermissionUuids() {
		return permissionUuids;
	}

	public void setPermissionUuids(List<String> permissionUuids) {
		this.permissionUuids = permissionUuids;
	}

	public String getCompanyUuid() {
		return companyUuid;
	}

	public void setCompanyUuid(String companyUuid) {
		this.companyUuid = companyUuid;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getFromType() {
		return fromType;
	}

	public void setFromType(String fromType) {
		this.fromType = fromType;
	}
	

}


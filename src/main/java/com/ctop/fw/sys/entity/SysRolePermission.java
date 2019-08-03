package com.ctop.fw.sys.entity;

import java.util.Date;

import java.io.Serializable;import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.format.annotation.DateTimeFormat;
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
@Table(name = "SYS_ROLE_PERMISSION")
@BatchSize(size = 20)
public class SysRolePermission extends BaseEntity implements Serializable {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ROLE_PERMISSION_UUID")
	private String rolePermissionUuid;//主键ID

	@Column(name = "ROLE_UUID")
	private String roleUuid;//角色ID

	@Column(name = "PERMISSION_UUID")
	private String permissionUuid;//权限ID

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


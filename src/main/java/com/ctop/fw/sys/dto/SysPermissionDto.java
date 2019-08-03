package com.ctop.fw.sys.dto;

import java.io.Serializable;import java.math.BigDecimal;

import com.ctop.fw.common.model.BaseDto;

/**
 * <pre>
 * 功能说明：SysPermission实体的数据传输对像
 * 创建者：${user}
 * 创建时间：${date}
 * 版本：${version}
 * </pre>
 */
public class SysPermissionDto extends BaseDto implements Serializable {
	private static final long serialVersionUID = 8918741991414864402L;
	private String permissionUuid;
	private String resourceUuid;
	private String operationUuid;
	private String resourceName;
	private String operationCode;
	private String operationName;
	private String interfaceUrl;
	private String name;

	
	public String getPermissionUuid() {
		return permissionUuid;
	}

	public void setPermissionUuid(String permissionUuid) {
		this.permissionUuid = permissionUuid;
	}
	
	public String getResourceUuid() {
		return resourceUuid;
	}

	public void setResourceUuid(String resourceUuid) {
		this.resourceUuid = resourceUuid;
	}
	
	public String getOperationUuid() {
		return operationUuid;
	}

	public void setOperationUuid(String operationUuid) {
		this.operationUuid = operationUuid;
	}
	
	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	
	public String getOperationCode() {
		return operationCode;
	}

	public void setOperationCode(String operationCode) {
		this.operationCode = operationCode;
	}
	
	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}
	
	public String getInterfaceUrl() {
		return interfaceUrl;
	}

	public void setInterfaceUrl(String interfaceUrl) {
		this.interfaceUrl = interfaceUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}


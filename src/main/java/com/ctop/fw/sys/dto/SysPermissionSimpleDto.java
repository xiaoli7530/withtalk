package com.ctop.fw.sys.dto;

import java.io.Serializable;import java.math.BigDecimal;

public class SysPermissionSimpleDto implements Serializable{
	private static final long serialVersionUID = -938044715422597025L;
	private String permissionUuid;
	private String resourceUuid; 
	private String operationUuid;
	private String operationCode; 
	private String interfaceUrl;
	
	public String getResourceUuid() {
		return resourceUuid;
	}
	public void setResourceUuid(String resourceUuid) {
		this.resourceUuid = resourceUuid;
	}
	public String getOperationCode() {
		return operationCode;
	}
	public void setOperationCode(String operationCode) {
		this.operationCode = operationCode;
	}
	public String getInterfaceUrl() {
		return interfaceUrl;
	}
	public void setInterfaceUrl(String interfaceUrl) {
		this.interfaceUrl = interfaceUrl;
	}
	public String getPermissionUuid() {
		return permissionUuid;
	}
	public void setPermissionUuid(String permissionUuid) {
		this.permissionUuid = permissionUuid;
	}
	public String getOperationUuid() {
		return operationUuid;
	}
	public void setOperationUuid(String operationUuid) {
		this.operationUuid = operationUuid;
	}
	
	
}

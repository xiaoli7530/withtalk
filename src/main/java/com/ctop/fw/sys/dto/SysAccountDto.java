package com.ctop.fw.sys.dto;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.ctop.fw.common.model.BaseDto;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * <pre>
 * 功能说明：${table.className}实体的数据传输对像
 * 创建者：${user}
 * 创建时间：${date}
 * 版本：${version}
 * </pre>
 */
public class SysAccountDto extends BaseDto implements Serializable {
	private static final long serialVersionUID = -7626749851847950268L;
	private String accountUuid;
	private String companyUuid;
	private String loginName;
	private String password;
	private String name;
	private String type;
	private String status;
	private String remark;
//	private String proxyId;
	private String oriPassword;
	private String loginIp;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8") 
	private Date lastLoginTime;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8") 
	private Date lastPasswordTime;
	private String refId;
	private String refType;
	private String token;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8") 
	private Date tokenBeginTime;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8") 
	private Date tokenEndTime;
	private String passwordInitFlag;
	private String loginRequestSequence;
	
	private String empName;
	private String empEmail;
	private String authType;
	private String empCode;
	private String deptName;
	private String currentProjectUuid;
	
	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getCurrentProjectUuid() {
		return currentProjectUuid;
	}

	public void setCurrentProjectUuid(String currentProjectUuid) {
		this.currentProjectUuid = currentProjectUuid;
	}
	
	public String getEmpCode() {
		return empCode;
	}

	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}

	public String getEmpEmail() {
		return empEmail;
	}
	
	public void setEmpEmail(String empEmail) {
		this.empEmail = empEmail;
	}
	
	public String getAccountUuid() {
		return accountUuid;
	}


	public void setAccountUuid(String accountUuid) {
		this.accountUuid = accountUuid;
	}
	
	public String getCompanyUuid() {
		return companyUuid;
	}

	public void setCompanyUuid(String companyUuid) {
		this.companyUuid = companyUuid;
	}
	
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
//	public String getProxyId() {
//		return proxyId;
//	}
//
//	public void setProxyId(String proxyId) {
//		this.proxyId = proxyId;
//	}
	
	public String getOriPassword() {
		return oriPassword;
	}

	public void setOriPassword(String oriPassword) {
		this.oriPassword = oriPassword;
	}
	
	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}
	
	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	
	public Date getLastPasswordTime() {
		return lastPasswordTime;
	}

	public void setLastPasswordTime(Date lastPasswordTime) {
		this.lastPasswordTime = lastPasswordTime;
	}
	
	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}
	
	public String getRefType() {
		return refType;
	}

	public void setRefType(String refType) {
		this.refType = refType;
	}
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	public Date getTokenBeginTime() {
		return tokenBeginTime;
	}

	public void setTokenBeginTime(Date tokenBeginTime) {
		this.tokenBeginTime = tokenBeginTime;
	}
	
	public Date getTokenEndTime() {
		return tokenEndTime;
	}

	public void setTokenEndTime(Date tokenEndTime) {
		this.tokenEndTime = tokenEndTime;
	}
	
	public String getPasswordInitFlag() {
		return passwordInitFlag;
	}

	public void setPasswordInitFlag(String passwordInitFlag) {
		this.passwordInitFlag = passwordInitFlag;
	}

	public String getLoginRequestSequence() {
		return loginRequestSequence;
	}

	public void setLoginRequestSequence(String loginRequestSequence) {
		this.loginRequestSequence = loginRequestSequence;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getAuthType() {
		return authType;
	}

	public void setAuthType(String authType) {
		this.authType = authType;
	} 
	
	

}


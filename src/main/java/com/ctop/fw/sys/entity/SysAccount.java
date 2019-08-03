package com.ctop.fw.sys.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import com.ctop.fw.common.constants.Constants;
import com.ctop.fw.common.constants.BizErrors.SysAccountErrors;
import com.ctop.fw.common.constants.Constants.YesNo;
import com.ctop.fw.common.entity.BaseEntity;
import com.ctop.fw.common.utils.BusinessException;
import com.ctop.fw.common.utils.StringUtil;
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
@Table(name = "SYS_ACCOUNT")
@BatchSize(size = 20)
public class SysAccount extends BaseEntity implements Serializable {

	@Id
	@Column(name = "ACCOUNT_UUID")
	private String accountUuid;//主键UUID

	@Column(name = "COMPANY_UUID")
	private String companyUuid;//货主

	@Column(name = "LOGIN_NAME")
	private String loginName;//登录名

	@Column(name = "PASSWORD")
	private String password;//密码

	@Column(name = "NAME")
	private String name;//姓名

	@Column(name = "TYPE")
	private String type;//{label:"账号类型(普通账号、管理账号)", dict:"ACCOUNT_TYPE"}

	@Column(name = "STATUS")
	private String status;//{label:"账号状态(正常、锁定)", dict:"ACCOUNT_STATUS"}

	@Column(name = "REMARK")
	private String remark;//备注

//	@Column(name = "PROXY_ID")
//	private String proxyId;//代理人ID,预留

	@Column(name = "ORI_PASSWORD")
	private String oriPassword;//上次密码

	@Column(name = "LOGIN_IP")
	private String loginIp;//客户端IP

	@Column(name = "LAST_LOGIN_TIME")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8") 
	private Date lastLoginTime;//最近登录时间

	@Column(name = "LAST_PASSWORD_TIME")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8") 
	private Date lastPasswordTime;//最近修改密码时间

	@Column(name = "REF_ID")
	private String refId;//员工ID、供应商ID

	@Column(name = "REF_TYPE")
	private String refType;//"登录用户表关联类型	员工"

	@Column(name = "TOKEN")
	private String token;//app登录的token

	@Column(name = "TOKEN_BEGIN_TIME")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8") 
	private Date tokenBeginTime;//token设置时间

	@Column(name = "TOKEN_END_TIME")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8") 
	private Date tokenEndTime;//token失效时间

	@Column(name = "PASSWORD_INIT_FLAG")
	private String passwordInitFlag;//是否为初始密码,如果是登录后直接跳转到密码修改界面
	
	@Column(name="CHECK_ERROR_TIME")
	private Integer checkErrorTime;
	
	@Column(name = "AUTH_TYPE")
	private String authType;
	
	@Column(name = "CURRENT_PROJECT_UUID")
	private String currentProjectUuid;
	
	public String getCurrentProjectUuid() {
		return currentProjectUuid;
	}

	public void setCurrentProjectUuid(String currentProjectUuid) {
		this.currentProjectUuid = currentProjectUuid;
	}

	private String buildMd5Password(String oriPassword) {
		String text = this.loginName + "_" + oriPassword;
		return StringUtil.encodeMd5(text, "UTF-8");
	}
	
	public void resetPassword() {
		this.password = this.buildMd5Password(Constants.DEFAULT_PASSWORD);
		this.setPasswordInitFlag(YesNo.YES);
	}
	
	public void changePassword(String oriPassword, String newPassword) {
		// 前置条件校验
		// 校验密码
		if (!this.checkMatchWithUserInputPassword(oriPassword)) {
			throw new BusinessException(SysAccountErrors.passwordInvalid);
		}
		this.setPassword(this.buildMd5Password(newPassword));
		this.setPasswordInitFlag(YesNo.NO);
	}
	
	public boolean checkMatchWithUserInputPassword(String oriPassword) {
		String md5Password = this.buildMd5Password(oriPassword);
		return StringUtil.equals(md5Password, this.password);
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

	public Integer getCheckErrorTime() {
		return checkErrorTime;
	}

	public void setCheckErrorTime(Integer checkErrorTime) {
		this.checkErrorTime = checkErrorTime;
	}

	public String getAuthType() {
		return authType;
	}

	public void setAuthType(String authType) {
		this.authType = authType;
	}
	
}


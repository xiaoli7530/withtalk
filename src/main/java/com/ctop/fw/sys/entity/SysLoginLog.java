package com.ctop.fw.sys.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.GenericGenerator;

import com.ctop.fw.common.constants.BizErrors.SysAccountErrors;
import com.ctop.fw.common.constants.Constants.LoginErrType;
import com.ctop.fw.common.constants.Constants.YesNo;
import com.ctop.fw.common.entity.BaseEntity;
import com.ctop.fw.common.utils.BusinessException;
import com.ctop.fw.common.utils.StringUtil;
import com.ctop.fw.sys.dto.SysAccountDto;


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
@Table(name = "SYS_LOGIN_LOG")
@BatchSize(size = 20)
public class SysLoginLog extends BaseEntity implements Serializable {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "LOG_UUID")
	private String logUuid;//UUID

	@Column(name = "ACCOUNT_UUID")
	private String accountUuid;//登陆账号

	@Column(name = "ERR_TYPE")
	private String errType;//失败类型(用户名不存在，密码错误,锁定)

	@Column(name = "LOGIN_TYPE")
	private String loginType;//登录类型（web,android,ios.ipad）

	@Column(name = "LOGIN_IP")
	private String loginIp;//登陆IP

	@Column(name = "IS_SUCC")
	private String isSucc;//登陆是否成功(Y/N)

	@Column(name = "MACCODE")
	private String maccode;//MAC地址

	@Column(name = "PHONE_NAME")
	private String phoneName;//手机号终端名称

	@Column(name = "PHONE_UID")
	private String phoneUid;//手机唯一标识

	@Column(name = "REMARK")
	private String remark;//备注

	@Column(name = "EXT1")
	private String ext1;//扩展1

	@Column(name = "EXT2")
	private String ext2;//扩展2

	@Column(name = "EXT3")
	private String ext3;//扩展3

	@Column(name = "EXT4")
	private String ext4;//扩展4

	@Column(name = "EXT5")
	private String ext5;//扩展5
	
	/**
	 * 构造错误信息
	 * @param appType
	 * @param loginName
	 * @param loginIp
	 * @param ex
	 * @return
	 */
	public static SysLoginLog buildErrorLog(String loginType, String loginName, HttpServletRequest request, RuntimeException ex) {
		SysLoginLog log = new SysLoginLog();
		log.loginType = loginType; 
		log.ext1 = loginName;
		log.setLoginIp(request.getRemoteAddr());
		log.ext2 = StringUtils.substring(request.getRequestURI(), 0, 90);
		String message = ExceptionUtils.getFullStackTrace(ex);
		log.remark = message != null ? StringUtils.substring(message, 0, 333) : "";
		if (ex instanceof BusinessException) {
			BusinessException be = (BusinessException) ex;
			if (SysAccountErrors.loginNameInvalid.equals(be.getCode())) {
				log.errType = LoginErrType.USER_NO_EXIST;
			} else if (SysAccountErrors.accountLocked4Login.equals(be.getCode())) {
				log.errType = LoginErrType.LOCKED;
			} else if (SysAccountErrors.passwordInvalid.equals(be.getCode())) {
				log.errType = LoginErrType.PASSWORD_INVALID;
			}
		} else {
			log.errType = LoginErrType.OTHER_ERROR;
		}
		log.setIsSucc(StringUtil.isEmpty(log.errType) ? YesNo.YES : YesNo.NO);
		return log;
	}
	
	public static SysLoginLog buildSuccessLog(String loginType, SysAccountDto account, HttpServletRequest request) {
		return buildSuccessLog(loginType, account, request, null, null, null);
	}
	
	/**
	 * 构造登录成功的日志；
	 * @param appType
	 * @param account
	 * @param loginType
	 * @param loginIp
	 * @param macCode
	 * @param phoneName
	 * @param phoneUid
	 * @return
	 */
	public static SysLoginLog buildSuccessLog(String loginType, SysAccountDto account, 
			HttpServletRequest request, 
			String macCode, 
			String phoneName, 
			String phoneUid ) {
		SysLoginLog log = new SysLoginLog();
		log.ext1 = account.getLoginName(); 
		log.accountUuid = account.getAccountUuid();
		log.setLoginIp(request.getRemoteAddr()); 
		log.setIsSucc( YesNo.YES);
		log.loginType = loginType;
		log.maccode = macCode;
		log.phoneName = phoneName;
		log.phoneUid = phoneUid;
		log.ext2 = StringUtils.substring(request.getRequestURI(), 0, 90);
		return log;
	}

	public String getLogUuid() {
		return logUuid;
	}

	public void setLogUuid(String logUuid) {
		this.logUuid = logUuid;
	}
	
	public String getAccountUuid() {
		return accountUuid;
	}

	public void setAccountUuid(String accountUuid) {
		this.accountUuid = accountUuid;
	}
	
	public String getErrType() {
		return errType;
	}

	public void setErrType(String errType) {
		this.errType = errType;
	}
	
	public String getLoginType() {
		return loginType;
	}

	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}
	
	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}
	
	public String getIsSucc() {
		return isSucc;
	}

	public void setIsSucc(String isSucc) {
		this.isSucc = isSucc;
	}
	
	public String getMaccode() {
		return maccode;
	}

	public void setMaccode(String maccode) {
		this.maccode = maccode;
	}
	
	public String getPhoneName() {
		return phoneName;
	}

	public void setPhoneName(String phoneName) {
		this.phoneName = phoneName;
	}
	
	public String getPhoneUid() {
		return phoneUid;
	}

	public void setPhoneUid(String phoneUid) {
		this.phoneUid = phoneUid;
	}
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public String getExt1() {
		return ext1;
	}

	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}
	
	public String getExt2() {
		return ext2;
	}

	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}
	
	public String getExt3() {
		return ext3;
	}

	public void setExt3(String ext3) {
		this.ext3 = ext3;
	}
	
	public String getExt4() {
		return ext4;
	}

	public void setExt4(String ext4) {
		this.ext4 = ext4;
	}
	
	public String getExt5() {
		return ext5;
	}

	public void setExt5(String ext5) {
		this.ext5 = ext5;
	}
	
}


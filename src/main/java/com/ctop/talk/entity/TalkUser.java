package com.ctop.talk.entity;

import java.util.Date;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.GenericGenerator;
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
@Table(name = "TALK_USER")
@BatchSize(size = 20)
public class TalkUser extends BaseEntity implements Serializable {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "USER_UUID")
	private String userUuid;//主键

	@Column(name = "LOGIN_NAME")
	private String loginName;//登录名

	@Column(name = "PASSWORD")
	private String password;//密码

	@Column(name = "USER_NAME")
	private String userName;//用户名

	@Column(name = "PHONE_NUMBER")
	private String phoneNumber;//电话号码

	@Column(name = "EMAIL")
	private String email;//邮件地址

	@Column(name = "TYPE")
	private String type;//账号类型(普通账号、管理账号)

	@Column(name = "STATUS")
	private String status;//账号状态(正常、锁定)

	@Column(name = "REMARK")
	private String remark;//备注

	@Column(name = "LAST_LOGIN_TIME")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8") 
	private Date lastLoginTime;//最近登录时间

	@Column(name = "LAST_PASSWORD_TIME")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8") 
	private Date lastPasswordTime;//最近修改密码时间

	@Column(name = "CHECK_ERROR_TIME")
	private Long checkErrorTime;//密码输入错误的次数

	@Column(name = "EXT1")
	private String ext1;//备注1

	@Column(name = "EXT2")
	private String ext2;//备注2

	@Column(name = "EXT3")
	private String ext3;//备注3

	@Column(name = "EXT4")
	private String ext4;//备注4

	@Column(name = "EXT5")
	private String ext5;//备注5

	public String getUserUuid() {
		return userUuid;
	}

	public void setUserUuid(String userUuid) {
		this.userUuid = userUuid;
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
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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
	
	public Long getCheckErrorTime() {
		return checkErrorTime;
	}

	public void setCheckErrorTime(Long checkErrorTime) {
		this.checkErrorTime = checkErrorTime;
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


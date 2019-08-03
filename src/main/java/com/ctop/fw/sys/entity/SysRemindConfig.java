package com.ctop.fw.sys.entity;

import java.util.Date;

import java.math.BigDecimal;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.format.annotation.DateTimeFormat;

import com.ctop.fw.common.entity.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;
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
@Table(name = "SYS_REMIND_CONFIG")
@BatchSize(size = 20)
public class SysRemindConfig extends BaseEntity implements Serializable {

	@Column(name = "REMIND_EMAIL_DESC")
	private String remindEmailDesc;//邮件提醒内容（带参数）
	
	@Column(name = "REMIND_FLOW_TYPE")
	private String remindFlowType;//流程提醒类型
	
	@Column(name = "URL")
	private String url;//url规则，含参数变量

	@Column(name = "REMIND_USER_TYPE")
	private String remindUserType;//通知人类型（role角色/owner所有者/borrower借用人）

	@Column(name = "IS_ACTUAL")
	private String isActual;//是否实时通知

	@Column(name = "ADVANCE_DATE")
	private Integer advanceDate;//提前天数

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "RC_UUID")
	private String rcUuid;//主键

	@Column(name = "IN_SYSTEM")
	private String inSystem;//系统：o2o,epms

	@Column(name = "BIZ_TYPE")
	private String bizType;//业务类型

	@Column(name = "ROLE_UUID")
	private String roleUuid;//角色UUID

	@Column(name = "REMIND_TITLE")
	private String remindTitle;//提醒标题(带参数)

	@Column(name = "REMIND_DESC")
	private String remindDesc;//提醒内容（带参数）

	@Column(name = "IS_REQ_EMAIL")
	private String isReqEmail;//是否需要邮件提醒

	@Column(name = "REQ_EMAIL_QTY")
	private Integer reqEmailQty;//需要邮件提醒次数

	@Column(name = "EXT1")
	private String ext1;//扩展字段1

	@Column(name = "EXT2")
	private String ext2;//扩展字段2

	@Column(name = "EXT3")
	private String ext3;//扩展字段3

	@Column(name = "EXT4")
	private String ext4;//扩展字段4

	@Column(name = "EXT5")
	private String ext5;//扩展字段5	
	@Column(name = "URGENT")
	private String urgent;//是否催办
	
	public String getUrgent() {
		return urgent;
	}

	public void setUrgent(String urgent) {
		this.urgent = urgent;
	}

	public String getRemindEmailDesc() {
		return remindEmailDesc;
	}

	public void setRemindEmailDesc(String remindEmailDesc) {
		this.remindEmailDesc = remindEmailDesc;
	}

	public String getRemindFlowType() {
		return remindFlowType;
	}

	public void setRemindFlowType(String remindFlowType) {
		this.remindFlowType = remindFlowType;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getRemindUserType() {
		return remindUserType;
	}

	public void setRemindUserType(String remindUserType) {
		this.remindUserType = remindUserType;
	}
	
	public String getIsActual() {
		return isActual;
	}

	public void setIsActual(String isActual) {
		this.isActual = isActual;
	}
	
	public Integer getAdvanceDate() {
		return advanceDate;
	}

	public void setAdvanceDate(Integer advanceDate) {
		this.advanceDate = advanceDate;
	}
	
	public String getRcUuid() {
		return rcUuid;
	}

	public void setRcUuid(String rcUuid) {
		this.rcUuid = rcUuid;
	}
	
	public String getInSystem() {
		return inSystem;
	}

	public void setInSystem(String inSystem) {
		this.inSystem = inSystem;
	}
	
	public String getBizType() {
		return bizType;
	}

	public void setBizType(String bizType) {
		this.bizType = bizType;
	}
	
	public String getRoleUuid() {
		return roleUuid;
	}

	public void setRoleUuid(String roleUuid) {
		this.roleUuid = roleUuid;
	}
	
	public String getRemindTitle() {
		return remindTitle;
	}

	public void setRemindTitle(String remindTitle) {
		this.remindTitle = remindTitle;
	}
	
	public String getRemindDesc() {
		return remindDesc;
	}

	public void setRemindDesc(String remindDesc) {
		this.remindDesc = remindDesc;
	}
	
	public String getIsReqEmail() {
		return isReqEmail;
	}

	public void setIsReqEmail(String isReqEmail) {
		this.isReqEmail = isReqEmail;
	}
	
	public Integer getReqEmailQty() {
		return reqEmailQty;
	}

	public void setReqEmailQty(Integer reqEmailQty) {
		this.reqEmailQty = reqEmailQty;
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


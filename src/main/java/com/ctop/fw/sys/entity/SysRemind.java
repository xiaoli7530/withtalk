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
@Table(name = "SYS_REMIND")
@BatchSize(size = 20)
public class SysRemind extends BaseEntity implements Serializable {

	@Column(name = "SPR_UUID")
	private String sprUuid;//sys_prep_remind表主键

	@Column(name = "OPRATION_TYPE")
	private String oprationType;//操作类型（通过:pass,拒绝:reject,退回:back等）

	@Column(name = "OPRATION_TIME")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8") 
	private Date oprationTime;//操作时间

	@Column(name = "FIRST_READ_TIME")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8") 
	private Date firstReadTime;//第一次阅读时间

	@Column(name = "HANDLE_TIME")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8") 
	private Date handleTime;//处理时间

	@Column(name = "URL")
	private String url;//url含参数

	@Column(name = "WT_UUID")
	private String wtUuid;//代办UUID

	@Column(name = "FLOW_UUID")
	private String flowUuid;//流程UUID

	@Column(name = "SPECIAL")
	private String special;//特殊处理

	@Column(name = "PROJECT_CODE")
	private String projectCode;//项目号

	@Column(name = "PROJECT_NAME")
	private String projectName;//项目名称

	@Column(name = "VEH_NO")
	private String vehNo;//车辆号

	@Column(name = "REMIND_FLOW_TYPE")
	private String remindFlowType;//流程提醒类型

	@Column(name = "IS_DEAL_BTN")
	private String isDealBtn;//是否显示处理按钮

	@Column(name = "HANDLE_STATUS")
	private String handleStatus;//处理状态：N-未处理、Y-已处理

	@Column(name = "BILL_NO")
	private String billNo;//业务单据号

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "REMIND_UUID")
	private String remindUuid;//主键

	@Column(name = "IN_SYSTEM")
	private String inSystem;//系统：o2o,epms

	@Column(name = "BIZ_TYPE")
	private String bizType;//业务类型

	@Column(name = "REF_UUID")
	private String refUuid;//关联表uuid

	@Column(name = "REMIND_TITLE")
	private String remindTitle;//提醒标题

	@Column(name = "REMIND_DESC")
	private String remindDesc;//提醒说明

	@Column(name = "CLICK_EVENT")
	private String clickEvent;//点击的响应事件

	@Column(name = "USER_ID")
	private String userId;//所有人(对应员工表)

	@Column(name = "REMIND_STATUS")
	private String remindStatus;//状态：已读、未读

	@Column(name = "REQ_EMAIL_QTY")
	private Integer reqEmailQty;//需要邮件提醒次数

	@Column(name = "EMAIL_COUNT")
	private Integer emailCount;//已发送提醒邮件次数
	
	@Column(name = "EMP_UUID")
	private String empUuid;//提醒申请人
	
	@Column(name = "EMP_NAME")
	private String empName;//提醒申请人姓名

	@Column(name = "TASK_ID")
	private String taskId;
	
	@Column(name = "PS2_AUTH_TYPE")
	private String ps2AuthType;
	
	@Column(name = "FLOW_FINISH_TIME")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8") 
	private Date flowFinishTime;
	
	@Column(name = "FLOW_STATUS")
	private String flowStatus;
	
	public String getPs2AuthType() {
		return ps2AuthType;
	}

	public void setPs2AuthType(String ps2AuthType) {
		this.ps2AuthType = ps2AuthType;
	}

	public Date getFlowFinishTime() {
		return flowFinishTime;
	}

	public void setFlowFinishTime(Date flowFinishTime) {
		this.flowFinishTime = flowFinishTime;
	}

	public String getFlowStatus() {
		return flowStatus;
	}

	public void setFlowStatus(String flowStatus) {
		this.flowStatus = flowStatus;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getEmpUuid() {
		return empUuid;
	}

	public void setEmpUuid(String empUuid) {
		this.empUuid = empUuid;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getSprUuid() {
		return sprUuid;
	}

	public void setSprUuid(String sprUuid) {
		this.sprUuid = sprUuid;
	}
	
	public String getOprationType() {
		return oprationType;
	}

	public void setOprationType(String oprationType) {
		this.oprationType = oprationType;
	}
	
	public Date getOprationTime() {
		return oprationTime;
	}

	public void setOprationTime(Date oprationTime) {
		this.oprationTime = oprationTime;
	}
	
	public Date getFirstReadTime() {
		return firstReadTime;
	}

	public void setFirstReadTime(Date firstReadTime) {
		this.firstReadTime = firstReadTime;
	}
	
	public Date getHandleTime() {
		return handleTime;
	}

	public void setHandleTime(Date handleTime) {
		this.handleTime = handleTime;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getWtUuid() {
		return wtUuid;
	}

	public void setWtUuid(String wtUuid) {
		this.wtUuid = wtUuid;
	}
	
	public String getFlowUuid() {
		return flowUuid;
	}

	public void setFlowUuid(String flowUuid) {
		this.flowUuid = flowUuid;
	}
	
	public String getSpecial() {
		return special;
	}

	public void setSpecial(String special) {
		this.special = special;
	}
	
	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}
	
	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	public String getVehNo() {
		return vehNo;
	}

	public void setVehNo(String vehNo) {
		this.vehNo = vehNo;
	}
	
	public String getRemindFlowType() {
		return remindFlowType;
	}

	public void setRemindFlowType(String remindFlowType) {
		this.remindFlowType = remindFlowType;
	}
	
	public String getIsDealBtn() {
		return isDealBtn;
	}

	public void setIsDealBtn(String isDealBtn) {
		this.isDealBtn = isDealBtn;
	}
	
	public String getHandleStatus() {
		return handleStatus;
	}

	public void setHandleStatus(String handleStatus) {
		this.handleStatus = handleStatus;
	}
	
	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	
	public String getRemindUuid() {
		return remindUuid;
	}

	public void setRemindUuid(String remindUuid) {
		this.remindUuid = remindUuid;
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
	
	public String getRefUuid() {
		return refUuid;
	}

	public void setRefUuid(String refUuid) {
		this.refUuid = refUuid;
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
	
	public String getClickEvent() {
		return clickEvent;
	}

	public void setClickEvent(String clickEvent) {
		this.clickEvent = clickEvent;
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getRemindStatus() {
		return remindStatus;
	}

	public void setRemindStatus(String remindStatus) {
		this.remindStatus = remindStatus;
	}
	
	public Integer getReqEmailQty() {
		return reqEmailQty;
	}

	public void setReqEmailQty(Integer reqEmailQty) {
		this.reqEmailQty = reqEmailQty;
	}
	
	public Integer getEmailCount() {
		return emailCount;
	}

	public void setEmailCount(Integer emailCount) {
		this.emailCount = emailCount;
	}
	
}


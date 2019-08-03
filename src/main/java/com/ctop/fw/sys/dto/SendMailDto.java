package com.ctop.fw.sys.dto;

import java.io.Serializable;

public class SendMailDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2303325855027676586L;
	
	private String applyUser;//申请人
	private String applyTime;//申请时间(格式：2016年12月20日)
	private String bizType; //业务类型
	private String bizNo;//业务单据号
	private String flowStatus;//拒绝/退回/通过
	private String reason;//拒绝原因或退回原因
	private String rollbackUser;//拒绝人或者回退人
	private String empUuid; //收件人员工ID
	private String toUserType; //发送给申请人(apply)或者工程师(engineer)
	
	
	 
	public String getToUserType() {
		return toUserType;
	}
	public void setToUserType(String toUserType) {
		this.toUserType = toUserType;
	}
	public String getApplyUser() {
		return applyUser;
	}
	public void setApplyUser(String applyUser) {
		this.applyUser = applyUser;
	}
	public String getApplyTime() {
		return applyTime;
	}
	public void setApplyTime(String applyTime) {
		this.applyTime = applyTime;
	}
	public String getBizType() {
		return bizType;
	}
	public void setBizType(String bizType) {
		this.bizType = bizType;
	}
	public String getBizNo() {
		return bizNo;
	}
	public void setBizNo(String bizNo) {
		this.bizNo = bizNo;
	}
	public String getFlowStatus() {
		return flowStatus;
	}
	public void setFlowStatus(String flowStatus) {
		this.flowStatus = flowStatus;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getRollbackUser() {
		return rollbackUser;
	}
	public void setRollbackUser(String rollbackUser) {
		this.rollbackUser = rollbackUser;
	}
	public String getEmpUuid() {
		return empUuid;
	}
	public void setEmpUuid(String empUuid) {
		this.empUuid = empUuid;
	}
	
}

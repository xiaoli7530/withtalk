package com.ctop.fw.sys.dto;

import com.ctop.fw.common.model.BaseDto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.io.Serializable;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * <pre>
 * 功能说明：SysRemind实体的数据传输对像
 * 创建者：${user}
 * 创建时间：${date}
 * 版本：${version}
 * </pre>
 */
public class SysRemindDto extends BaseDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6138150480589206780L;
	
	private String sprUuid;
	private String oprationType;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	@JsonFormat(pattern="yyyy-MM-dd HH:mm",timezone = "GMT+8") 
	private Date oprationTime;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	@JsonFormat(pattern="yyyy-MM-dd HH:mm",timezone = "GMT+8") 
	private Date firstReadTime;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	@JsonFormat(pattern="yyyy-MM-dd HH:mm",timezone = "GMT+8") 
	private Date handleTime;
	private String url;
	private String wtUuid;
	private String flowUuid;
	private String special;
	private String projectCode;
	private String projectName;
	private String vehNo;
	private String remindFlowType;
	private String isDealBtn;
	private String handleStatus;
	private String billNo;
	private String remindUuid;
	private String inSystem;
	private String bizType;
	private String refUuid;
	private String remindTitle;
	private String remindDesc;
	private String clickEvent;
	private String userId;
	private String remindStatus;
	private Integer reqEmailQty;
	private Integer emailCount;
	List<SysRemindDto> remindList = new ArrayList<SysRemindDto>();
	private String empUuid;//提醒申请人
	private String empName;//提醒申请人姓名
	private String taskId;
	private String currTaskId;
	private String assignee;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm")  
	@JsonFormat(pattern="yyyy-MM-dd HH:mm",timezone = "GMT+8") 
	private Date applyTime;
	
	 private String flowStatus;
	 @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	 @JsonFormat(pattern="yyyy-MM-dd HH:mm",timezone = "GMT+8") 
	 private Date flowFinishTime;
	 private String ps2AuthType;
	 
	 private String applyEmpName;
	 private String bizStatus;
	
	public String getBizStatus() {
		return bizStatus;
	}

	public void setBizStatus(String bizStatus) {
		this.bizStatus = bizStatus;
	}

	public String getApplyEmpName() {
		return applyEmpName;
	}

	public void setApplyEmpName(String applyEmpName) {
		this.applyEmpName = applyEmpName;
	}

	public Date getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}

	public String getFlowStatus() {
		return flowStatus;
	}

	public void setFlowStatus(String flowStatus) {
		this.flowStatus = flowStatus;
	}

	public Date getFlowFinishTime() {
		return flowFinishTime;
	}

	public void setFlowFinishTime(Date flowFinishTime) {
		this.flowFinishTime = flowFinishTime;
	}

	public String getPs2AuthType() {
		return ps2AuthType;
	}

	public void setPs2AuthType(String ps2AuthType) {
		this.ps2AuthType = ps2AuthType;
	}

	public String getCurrTaskId() {
		return currTaskId;
	}

	public void setCurrTaskId(String currTaskId) {
		this.currTaskId = currTaskId;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
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

	public List<SysRemindDto> getRemindList() {
		return remindList;
	}

	public void setRemindList(List<SysRemindDto> remindList) {
		this.remindList = remindList;
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


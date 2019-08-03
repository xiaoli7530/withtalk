package com.ctop.fw.sys.dto;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.ctop.fw.common.model.BaseDto;
import com.fasterxml.jackson.annotation.JsonFormat;

public class SysRemindForSendPsDto extends BaseDto implements Serializable{
	
	private String srUuid;
	private String remindContent;
	private String refUuid;
	private String remindTitle;
	private String recipientAccountUuid;
	private String readStatus;
	private String handleStatus;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	@JsonFormat(pattern="yyyy-MM-dd HH:mm",timezone = "GMT+8") 
	private Date firstReadTime;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	@JsonFormat(pattern="yyyy-MM-dd HH:mm",timezone = "GMT+8") 
	private Date handleTime;
	private String bizType;
	private String senderName;
	
	private String bizNo;
	private String projectUuid;
	private String projectName;
	private String projectCode;
	private String projectClassName;
	
	private String urlParams;
	private String wtUuid;
	private String flowUuid;
	
	private String isDealBtn;
	private String special;
	private String ext1;
	private String ext2;
	private String ext3;
	private String ext4;
	private String ext5;
	private String applyerName;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	@JsonFormat(pattern="yyyy-MM-dd HH:mm",timezone = "GMT+8") 
	private Date drDeadLine;

	public String getSrUuid() {
		return srUuid;
	}

	public void setSrUuid(String srUuid) {
		this.srUuid = srUuid;
	}

	public String getRemindContent() {
		return remindContent;
	}

	public void setRemindContent(String remindContent) {
		this.remindContent = remindContent;
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

	public String getRecipientAccountUuid() {
		return recipientAccountUuid;
	}

	public void setRecipientAccountUuid(String recipientAccountUuid) {
		this.recipientAccountUuid = recipientAccountUuid;
	}

	public String getReadStatus() {
		return readStatus;
	}

	public void setReadStatus(String readStatus) {
		this.readStatus = readStatus;
	}

	public String getHandleStatus() {
		return handleStatus;
	}

	public void setHandleStatus(String handleStatus) {
		this.handleStatus = handleStatus;
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

	public String getBizType() {
		return bizType;
	}

	public void setBizType(String bizType) {
		this.bizType = bizType;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getBizNo() {
		return bizNo;
	}

	public void setBizNo(String bizNo) {
		this.bizNo = bizNo;
	}

	public String getProjectUuid() {
		return projectUuid;
	}

	public void setProjectUuid(String projectUuid) {
		this.projectUuid = projectUuid;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	public String getProjectClassName() {
		return projectClassName;
	}

	public void setProjectClassName(String projectClassName) {
		this.projectClassName = projectClassName;
	}

	public String getUrlParams() {
		return urlParams;
	}

	public void setUrlParams(String urlParams) {
		this.urlParams = urlParams;
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

	public String getIsDealBtn() {
		return isDealBtn;
	}

	public void setIsDealBtn(String isDealBtn) {
		this.isDealBtn = isDealBtn;
	}

	public String getSpecial() {
		return special;
	}

	public void setSpecial(String special) {
		this.special = special;
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

	public String getApplyerName() {
		return applyerName;
	}

	public void setApplyerName(String applyerName) {
		this.applyerName = applyerName;
	}

	public Date getDrDeadLine() {
		return drDeadLine;
	}

	public void setDrDeadLine(Date drDeadLine) {
		this.drDeadLine = drDeadLine;
	}
	
}

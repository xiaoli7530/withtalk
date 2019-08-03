package com.ctop.fw.sys.dto;

import java.io.FileInputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.ctop.fw.common.model.BaseDto;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * <pre>
 * 功能说明：SysEmail实体的数据传输对像
 * 创建者：${user}
 * 创建时间：${date}
 * 版本：${version}
 * </pre>
 */
public class SysEmailDto extends BaseDto implements Serializable {
	private static final long serialVersionUID = 6344129279614535646L;
	private String emailUuid;
	private String templetUuid;
	private String attachmentUuid;
	private String inlineImageUuid;
	private String title;
	private String content;
	private String sendType;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
	private Date lastSendDate;
	private String status;
	private String ext1;
	private String ext2;
	private String ext3;
	private String ext4;
	private String ext5;
	private List<SysEmailInfoDto> sysEmailInfoDto;
	private FileInputStream attachmentInputStream;
	private String attachmentName;
	private FileInputStream inLineInputStream;
	private String inLineName;
	private String acctId;
	private String bizNo;
	private String bizType;
	private String templateName;
	private Object templateData;
	
	public String getBizNo() {
		return bizNo;
	}

	public void setBizNo(String bizNo) {
		this.bizNo = bizNo;
	}

	public String getBizType() {
		return bizType;
	}

	public void setBizType(String bizType) {
		this.bizType = bizType;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public Object getTemplateData() {
		return templateData;
	}

	public void setTemplateData(Object templateData) {
		this.templateData = templateData;
	}

	public String getAcctId() {
		return acctId;
	}

	public void setAcctId(String acctId) {
		this.acctId = acctId;
	}

	public String getEmailUuid() {
		return emailUuid;
	}

	public void setEmailUuid(String emailUuid) {
		this.emailUuid = emailUuid;
	}

	public String getTempletUuid() {
		return templetUuid;
	}

	public void setTempletUuid(String templetUuid) {
		this.templetUuid = templetUuid;
	}

	public String getAttachmentUuid() {
		return attachmentUuid;
	}

	public void setAttachmentUuid(String attachmentUuid) {
		this.attachmentUuid = attachmentUuid;
	}

	public String getInlineImageUuid() {
		return inlineImageUuid;
	}

	public void setInlineImageUuid(String inlineImageUuid) {
		this.inlineImageUuid = inlineImageUuid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSendType() {
		return sendType;
	}

	public void setSendType(String sendType) {
		this.sendType = sendType;
	}

	public Date getLastSendDate() {
		return lastSendDate;
	}

	public void setLastSendDate(Date lastSendDate) {
		this.lastSendDate = lastSendDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public List<SysEmailInfoDto> getSysEmailInfoDto() {
		return sysEmailInfoDto;
	}

	public void setSysEmailInfoDto(List<SysEmailInfoDto> sysEmailInfoDto) {
		this.sysEmailInfoDto = sysEmailInfoDto;
	}

	public FileInputStream getAttachmentInputStream() {
		return attachmentInputStream;
	}

	public void setAttachmentInputStream(FileInputStream attachmentInputStream) {
		this.attachmentInputStream = attachmentInputStream;
	}

	public String getAttachmentName() {
		return attachmentName;
	}

	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}

	public FileInputStream getInLineInputStream() {
		return inLineInputStream;
	}

	public void setInLineInputStream(FileInputStream inLineInputStream) {
		this.inLineInputStream = inLineInputStream;
	}

	public String getInLineName() {
		return inLineName;
	}

	public void setInLineName(String inLineName) {
		this.inLineName = inLineName;
	}
}

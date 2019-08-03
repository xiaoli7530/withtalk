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
@Table(name = "SYS_EMAIL")
@BatchSize(size = 20)
public class SysEmail extends BaseEntity implements Serializable {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "EMAIL_UUID")
	private String emailUuid;//uuid

	@Column(name = "TEMPLET_UUID")
	private String templetUuid;//模板UUID

	@Column(name = "ATTACHMENT_UUID")
	private String attachmentUuid;//附件UUID

	@Column(name = "INLINE_IMAGE_UUID")
	private String inlineImageUuid;//内嵌图片UUID

	@Column(name = "TITLE")
	private String title;//主题

	@Column(name = "CONTENT")
	private String content;//内容

	@Column(name = "SEND_TYPE")
	private String sendType;//发送类型(0:普通 1：急件)

	@Column(name = "LAST_SEND_DATE")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8") 
	private Date lastSendDate;//最近发送时间

	@Column(name = "STATUS")
	private String status;//状态(0:待发；1：已发)

	@Column(name = "EXT1")
	private String ext1;//

	@Column(name = "EXT2")
	private String ext2;//

	@Column(name = "EXT3")
	private String ext3;//

	@Column(name = "EXT4")
	private String ext4;//

	@Column(name = "EXT5")
	private String ext5;//

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
	
}


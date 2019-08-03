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
@Table(name = "SYS_EMAIL_INFO")
@BatchSize(size = 20)
public class SysEmailInfo extends BaseEntity implements Serializable {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "EMAIL_INFO_UUID")
	private String emailInfoUuid;//uuid

	@Column(name = "EMAIL_UUID")
	private String emailUuid;//uuid

	@Column(name = "RECEIVER_CD")
	private String receiverCd;//收件人域账号

	@Column(name = "RECEIVER_NAME")
	private String receiverName;//收件人名称

	@Column(name = "RECEIVER_EMAIL")
	private String receiverEmail;//收件人邮箱地址

	@Column(name = "SEND_DATE")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8") 
	private Date sendDate;//发送时间

	@Column(name = "STATUS")
	private String status;//状态(0:待发；1：已发；-1:发送失败)

	@Column(name = "REMARK")
	private String remark;//备注

	public String getEmailInfoUuid() {
		return emailInfoUuid;
	}

	public void setEmailInfoUuid(String emailInfoUuid) {
		this.emailInfoUuid = emailInfoUuid;
	}
	
	public String getEmailUuid() {
		return emailUuid;
	}

	public void setEmailUuid(String emailUuid) {
		this.emailUuid = emailUuid;
	}
	
	public String getReceiverCd() {
		return receiverCd;
	}

	public void setReceiverCd(String receiverCd) {
		this.receiverCd = receiverCd;
	}
	
	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	
	public String getReceiverEmail() {
		return receiverEmail;
	}

	public void setReceiverEmail(String receiverEmail) {
		this.receiverEmail = receiverEmail;
	}
	
	public Date getSendDate() {
		return sendDate;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
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
	
}


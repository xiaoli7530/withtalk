package com.ctop.talk.entity;

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
@Table(name = "TALK_MESSAGE")
@BatchSize(size = 20)
public class TalkMessage extends BaseEntity implements Serializable {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "MESSAGE_UUID")
	private String messageUuid;//主键

	@Column(name = "SEND_USER_UUID")
	private String sendUserUuid;//发送人talk_user主键

	@Column(name = "RECEIVE_USER_UUID")
	private String receiveUserUuid;//接收人talk_user主键

	@Column(name = "MESSAGE_CONTENT")
	private String messageContent;//消息内容

	@Column(name = "SEND_TIME")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8") 
	private Date sendTime;//发送时间

	@Column(name = "IS_SEND")
	private String isSend;//是否有效(Y:有效；N:无效)

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

	public String getMessageUuid() {
		return messageUuid;
	}

	public void setMessageUuid(String messageUuid) {
		this.messageUuid = messageUuid;
	}
	
	public String getSendUserUuid() {
		return sendUserUuid;
	}

	public void setSendUserUuid(String sendUserUuid) {
		this.sendUserUuid = sendUserUuid;
	}
	
	public String getReceiveUserUuid() {
		return receiveUserUuid;
	}

	public void setReceiveUserUuid(String receiveUserUuid) {
		this.receiveUserUuid = receiveUserUuid;
	}
	
	public String getMessageContent() {
		return messageContent;
	}

	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}
	
	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
	
	public String getIsSend() {
		return isSend;
	}

	public void setIsSend(String isSend) {
		this.isSend = isSend;
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


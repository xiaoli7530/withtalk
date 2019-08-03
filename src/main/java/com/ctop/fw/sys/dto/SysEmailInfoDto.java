package com.ctop.fw.sys.dto;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.ctop.fw.common.model.BaseDto;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * <pre>
 * 功能说明：SysEmailInfo实体的数据传输对像
 * 创建者：${user}
 * 创建时间：${date}
 * 版本：${version}
 * </pre>
 */
public class SysEmailInfoDto extends BaseDto implements Serializable {
	private static final long serialVersionUID = 3785419185967704293L;
	private String emailInfoUuid;
	private String emailUuid;
	private String receiverCd;
	private String receiverName;
	private String receiverEmail;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	@JsonFormat(pattern="yyyy-MM-dd HH:mm",timezone = "GMT+8") 
	private Date sendDate;
	private String status;
	private String remark;

	
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


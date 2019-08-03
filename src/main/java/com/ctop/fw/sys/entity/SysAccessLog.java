package com.ctop.fw.sys.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import com.ctop.fw.common.constants.Constants.YesNo;
import com.ctop.fw.common.entity.ICreatedTracker;
import com.ctop.fw.common.utils.UserContextUtil;
import com.ctop.fw.config.LogFilter.AccessLog;
import com.fasterxml.jackson.annotation.JsonFormat;
 
@Entity
@Table(name = "SYS_ACCESS_LOG")
public class SysAccessLog implements ICreatedTracker {
	

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "LOG_UUID")
	private String logUuid;//主键UUID
	
	@Column(name = "REQUEST_TIME")
	private Date requestTime;
	
	@Column(name = "REQUEST_URI")
	private String requestUri;
	
	@Column(name = "REQUEST_CODE")
	private String requestCode;
	
	@Column(name = "RESPONSE_TIME")
	private Date responseTime;
	
	@Column(name = "CLIENT")
	private String client;
	
	@Column(name = "STATUS_CODE")
	private String statusCode;
	
//	@Column(name = "PAYLOAD",length = 10240)
//	private String payload; 
	//TODO 先屏蔽，单独放到BASE_CLOB表中
	
	@Column(name = "OPERATOR")
	private String operator; 

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8") 
	@Column(name = "CREATED_DATE")
	protected Date createdDate;
	
	@Column(name = "CREATED_BY")
	protected String createdBy;
	
	@Column(name = "IS_ACTIVE")
	private String isActive;
	
	public SysAccessLog() {}
	
	public SysAccessLog(AccessLog accessLog) {
		this.requestTime = accessLog.getRequestTime();
		this.responseTime = accessLog.getResponseTime();
		this.requestUri = accessLog.getRequestUri();
		this.client = accessLog.getClient();
		this.operator = accessLog.getLoginName();
		this.statusCode = accessLog.getResponseStatus();
//		this.payload = accessLog.toString();
		this.requestCode = accessLog.getRstCode();
		this.createdBy = UserContextUtil.getAccountUuid();
		this.createdDate = new Date();
		this.isActive = YesNo.YES;
	}

	public String getLogUuid() {
		return logUuid;
	}

	public void setLogUuid(String logUuid) {
		this.logUuid = logUuid;
	}

	public Date getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(Date requestTime) {
		this.requestTime = requestTime;
	}

	public String getRequestUri() {
		return requestUri;
	}

	public void setRequestUri(String requestUri) {
		this.requestUri = requestUri;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

//	public String getPayload() {
//		return payload;
//	}
//
//	public void setPayload(String payload) {
//		this.payload = payload;
//	} 

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getRequestCode() {
		return requestCode;
	}

	public void setRequestCode(String requestCode) {
		this.requestCode = requestCode;
	}

	public Date getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(Date responseTime) {
		this.responseTime = responseTime;
	}
}

package com.ctop.base.dto;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.ctop.fw.common.model.BaseDto;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * <pre>
 * 功能说明：SysScheduleConfig实体的数据传输对像
 * 创建者：${user}
 * 创建时间：${date}
 * 版本：${version}
 * </pre>
 */
public class SysScheduleConfigDto extends BaseDto implements Serializable {
	private String scheduleId;
	private String bizType;
	private String subType;
	private String parameters;
	private String status;
	private String memo;
	private String cron;
	private String scheduleMethod;
	private String scheduleName;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8") 
	private Date lastRunTime;
	private String lastRunStatus;
	private String lastRunMsg;
	private Integer lastRunSeconds;
	private String serverName;
	private String userDir;
	
	private String currentComputerName; 
	
	private String currentComputerIp;
	
	private String startStatus;
	
	
	public String getStartStatus() {
		return startStatus;
	}

	public void setStartStatus(String startStatus) {
		this.startStatus = startStatus;
	}

	public String getCurrentComputerName() {
		return currentComputerName;
	}

	public void setCurrentComputerName(String currentComputerName) {
		this.currentComputerName = currentComputerName;
	}

	public String getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(String scheduleId) {
		this.scheduleId = scheduleId;
	}
	
	public String getBizType() {
		return bizType;
	}

	public void setBizType(String bizType) {
		this.bizType = bizType;
	}
	
	public String getSubType() {
		return subType;
	}

	public void setSubType(String subType) {
		this.subType = subType;
	}
	
	public String getCurrentComputerIp() {
		return currentComputerIp;
	}

	public void setCurrentComputerIp(String currentComputerIp) {
		this.currentComputerIp = currentComputerIp;
	}

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	public String getCron() {
		return cron;
	}

	public void setCron(String cron) {
		this.cron = cron;
	}
	
	public String getScheduleMethod() {
		return scheduleMethod;
	}

	public void setScheduleMethod(String scheduleMethod) {
		this.scheduleMethod = scheduleMethod;
	}
	
	public String getScheduleName() {
		return scheduleName;
	}

	public void setScheduleName(String scheduleName) {
		this.scheduleName = scheduleName;
	}
	
	public Date getLastRunTime() {
		return lastRunTime;
	}

	public void setLastRunTime(Date lastRunTime) {
		this.lastRunTime = lastRunTime;
	}
	
	public String getLastRunStatus() {
		return lastRunStatus;
	}

	public void setLastRunStatus(String lastRunStatus) {
		this.lastRunStatus = lastRunStatus;
	}
	
	public String getLastRunMsg() {
		return lastRunMsg;
	}

	public void setLastRunMsg(String lastRunMsg) {
		this.lastRunMsg = lastRunMsg;
	}
	
	public Integer getLastRunSeconds() {
		return lastRunSeconds;
	}

	public void setLastRunSeconds(Integer lastRunSeconds) {
		this.lastRunSeconds = lastRunSeconds;
	}
	
	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	
	public String getUserDir() {
		return userDir;
	}

	public void setUserDir(String userDir) {
		this.userDir = userDir;
	}

}


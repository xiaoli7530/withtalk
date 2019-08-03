package com.ctop.base.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "SYS_SCHEDULE_CONFIG")
@BatchSize(size = 20)
public class SysScheduleConfig extends BaseEntity implements Serializable {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "SCHEDULE_ID")
	private String scheduleId;//

	@Column(name = "BIZ_TYPE")
	private String bizType;//业务类型（任务分配器）

	@Column(name = "SUB_TYPE")
	private String subType;//业务子类型（国内整车、国内零件）

	@Column(name = "PARAMETERS")
	private String parameters;//参数

	@Column(name = "STATUS")
	private String status;//有效状态

	@Column(name = "MEMO")
	private String memo;//备注

	@Column(name = "CRON")
	private String cron;//定时规则（1 */5 * * * ?）

	@Column(name = "SCHEDULE_METHOD")
	private String scheduleMethod;//定时器执行方法

	@Column(name = "SCHEDULE_NAME")
	private String scheduleName;//定时器名称

	@Column(name = "LAST_RUN_TIME")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8") 
	private Date lastRunTime;//最后一次运行时间

	@Column(name = "LAST_RUN_STATUS")
	private String lastRunStatus;//最后一次运行状态

	@Column(name = "LAST_RUN_MSG")
	private String lastRunMsg;//最终一次运行返回信息

	@Column(name = "LAST_RUN_SECONDS")
	private Integer lastRunSeconds;//最后一次运行所耗时间(秒)

	@Column(name = "SERVER_NAME")
	private String serverName;//服务能运行的服务器的机器名

	@Column(name = "USER_DIR")
	private String userDir;//服务能运行的工作目录

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


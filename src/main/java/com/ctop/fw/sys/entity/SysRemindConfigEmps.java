package com.ctop.fw.sys.entity;

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
@Table(name = "SYS_REMIND_CONFIG_EMPS")
@BatchSize(size = 20)
public class SysRemindConfigEmps extends BaseEntity implements Serializable {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "RCE_UUID")
	private String rceUuid;//主键

	@Column(name = "RC_UUID")
	private String rcUuid;//主表主键

	@Column(name = "REMIND_USER_TYPE")
	private String remindUserType;//通知人类型（role角色/项目成员/业务人员对应的上级角色/指定人）

	@Column(name = "EMP")
	private String emp;//指定人ID

	@Column(name = "ROLE_UUID")
	private String roleUuid;//角色UUID

	@Column(name = "PROJECT_ROLE_CODES")
	private String projectRoleCodes;//项目成员角色编码（可多选，多个用','分隔）

	@Column(name = "POSITION_NBR_EPMS")
	private String positionNbrEpms;//上级岗位代码,单选

	@Column(name = "IS_DEAL_BTN")
	private String isDealBtn;//是否显示处理按钮

	@Column(name = "EXT1")
	private String ext1;//扩展字段1

	@Column(name = "EXT2")
	private String ext2;//扩展字段2

	@Column(name = "EXT3")
	private String ext3;//扩展字段3

	@Column(name = "EXT4")
	private String ext4;//扩展字段4

	@Column(name = "EXT5")
	private String ext5;//扩展字段5

	public String getRceUuid() {
		return rceUuid;
	}

	public void setRceUuid(String rceUuid) {
		this.rceUuid = rceUuid;
	}
	
	public String getRcUuid() {
		return rcUuid;
	}

	public void setRcUuid(String rcUuid) {
		this.rcUuid = rcUuid;
	}
	
	public String getRemindUserType() {
		return remindUserType;
	}

	public void setRemindUserType(String remindUserType) {
		this.remindUserType = remindUserType;
	}
	
	 
	
	public String getEmp() {
		return emp;
	}

	public void setEmp(String emp) {
		this.emp = emp;
	}

	public String getRoleUuid() {
		return roleUuid;
	}

	public void setRoleUuid(String roleUuid) {
		this.roleUuid = roleUuid;
	}
	
	public String getProjectRoleCodes() {
		return projectRoleCodes;
	}

	public void setProjectRoleCodes(String projectRoleCodes) {
		this.projectRoleCodes = projectRoleCodes;
	}
	
	public String getPositionNbrEpms() {
		return positionNbrEpms;
	}

	public void setPositionNbrEpms(String positionNbrEpms) {
		this.positionNbrEpms = positionNbrEpms;
	}
	
	public String getIsDealBtn() {
		return isDealBtn;
	}

	public void setIsDealBtn(String isDealBtn) {
		this.isDealBtn = isDealBtn;
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


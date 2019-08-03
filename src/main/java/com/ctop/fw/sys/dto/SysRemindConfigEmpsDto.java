package com.ctop.fw.sys.dto;

import com.ctop.fw.common.model.BaseDto;
import java.util.Date;
import java.io.Serializable;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * <pre>
 * 功能说明：SysRemindConfigEmps实体的数据传输对像
 * 创建者：${user}
 * 创建时间：${date}
 * 版本：${version}
 * </pre>
 */
public class SysRemindConfigEmpsDto extends BaseDto implements Serializable {
	private String rceUuid;
	private String rcUuid;
	private String remindUserType;
	private String emp;
	private String roleUuid;
	private String projectRoleCodes;
	private String positionNbrEpms;
	private String isDealBtn;
	private String ext1;
	private String ext2;
	private String ext3;
	private String ext4;
	private String ext5;
	private String _status;
	private String empName;//角色名或者员工名称
	
	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String get_status() {
		return _status;
	}

	public void set_status(String _status) {
		this._status = _status;
	}

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


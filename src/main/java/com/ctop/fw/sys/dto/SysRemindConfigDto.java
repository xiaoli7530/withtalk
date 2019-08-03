package com.ctop.fw.sys.dto;

import com.ctop.fw.common.model.BaseDto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.io.Serializable;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * <pre>
 * 功能说明：SysRemindConfig实体的数据传输对像
 * 创建者：${user}
 * 创建时间：${date}
 * 版本：${version}
 * </pre>
 */
public class SysRemindConfigDto extends BaseDto implements Serializable {
	private String remindFlowType;//流程提醒类型
	private String url;
	private String remindUserType;
	private String isActual;
	private Integer advanceDate;
	private String rcUuid;
	private String inSystem;
	private String bizType;
	private String roleUuid;
	private String remindTitle;
	private String remindDesc;
	private String isReqEmail;
	private Integer reqEmailQty;
	private String ext1;
	private String ext2;
	private String ext3;
	private String ext4;
	private String ext5;
	private String positionNbrEpm;
	private String roleName;
	private String remindEmailDesc;//邮件提醒内容（带参数）
	List<SysRemindConfigEmpsDto> emps = new ArrayList<SysRemindConfigEmpsDto>();	
	private String urgent;//是否催办
	
	
	public String getUrgent() {
		return urgent;
	}

	public void setUrgent(String urgent) {
		this.urgent = urgent;
	}

	public List<SysRemindConfigEmpsDto> getEmps() {
		return emps;
	}

	public void setEmps(List<SysRemindConfigEmpsDto> emps) {
		this.emps = emps;
	}

	public String getRemindEmailDesc() {
		return remindEmailDesc;
	}

	public void setRemindEmailDesc(String remindEmailDesc) {
		this.remindEmailDesc = remindEmailDesc;
	}

	public String getRemindFlowType() {
		return remindFlowType;
	}

	public void setRemindFlowType(String remindFlowType) {
		this.remindFlowType = remindFlowType;
	}

	public String getPositionNbrEpm() {
		return positionNbrEpm;
	}

	public void setPositionNbrEpm(String positionNbrEpm) {
		this.positionNbrEpm = positionNbrEpm;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

 
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getRemindUserType() {
		return remindUserType;
	}

	public void setRemindUserType(String remindUserType) {
		this.remindUserType = remindUserType;
	}
	
	public String getIsActual() {
		return isActual;
	}

	public void setIsActual(String isActual) {
		this.isActual = isActual;
	}
	
	public Integer getAdvanceDate() {
		return advanceDate;
	}

	public void setAdvanceDate(Integer advanceDate) {
		this.advanceDate = advanceDate;
	}
	
	public String getRcUuid() {
		return rcUuid;
	}

	public void setRcUuid(String rcUuid) {
		this.rcUuid = rcUuid;
	}
	
	public String getInSystem() {
		return inSystem;
	}

	public void setInSystem(String inSystem) {
		this.inSystem = inSystem;
	}
	
	public String getBizType() {
		return bizType;
	}

	public void setBizType(String bizType) {
		this.bizType = bizType;
	}
	
	public String getRoleUuid() {
		return roleUuid;
	}

	public void setRoleUuid(String roleUuid) {
		this.roleUuid = roleUuid;
	}
	
	public String getRemindTitle() {
		return remindTitle;
	}

	public void setRemindTitle(String remindTitle) {
		this.remindTitle = remindTitle;
	}
	
	public String getRemindDesc() {
		return remindDesc;
	}

	public void setRemindDesc(String remindDesc) {
		this.remindDesc = remindDesc;
	}
	
	public String getIsReqEmail() {
		return isReqEmail;
	}

	public void setIsReqEmail(String isReqEmail) {
		this.isReqEmail = isReqEmail;
	}
	
	public Integer getReqEmailQty() {
		return reqEmailQty;
	}

	public void setReqEmailQty(Integer reqEmailQty) {
		this.reqEmailQty = reqEmailQty;
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


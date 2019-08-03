package com.ctop.fw.sys.dto;


import java.io.Serializable;

/**
 * <pre>
 * 功能说明：SysRemind实体的数据传输对像
 * 创建者：${user}
 * 创建时间：${date}
 * 版本：${version}
 * </pre>
 */
public class SysRemindUser implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6138150480589206780L;
	
	private String empUuid;//通知人
	private String isDealBtn;//是否显示处理按钮
	private String email;//邮件地址
	private String empName;//通知人姓名
	
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEmpUuid() {
		return empUuid;
	}
	public void setEmpUuid(String empUuid) {
		this.empUuid = empUuid;
	}
	public String getIsDealBtn() {
		return isDealBtn;
	}
	public void setIsDealBtn(String isDealBtn) {
		this.isDealBtn = isDealBtn;
	}
	
	
}


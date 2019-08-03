package com.ctop.base.dto;

import java.util.Date;
import java.io.Serializable;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;

import com.ctop.fw.common.model.BaseDto;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * <pre>
 * 功能说明：BasePoint实体的数据传输对像
 * 创建者：${user}
 * 创建时间：${date}
 * 版本：${version}
 * </pre>
 */
public class BasePointDto extends BaseDto implements Serializable {
	private String pointUuid;
	private String pointCode;
	private String pointName;
	private String cityUuid;
	private String stagingAreaUuid;
	private String contactPerson;
	private String tel;
	private String address;
	private String ext1;
	private String ext2;
	private String ext3;
	private String ext4;
	private String ext5;
	private String isStatic;

	
	public String getPointUuid() {
		return pointUuid;
	}

	public void setPointUuid(String pointUuid) {
		this.pointUuid = pointUuid;
	}
	
	public String getPointCode() {
		return pointCode;
	}

	public void setPointCode(String pointCode) {
		this.pointCode = pointCode;
	}
	
	public String getPointName() {
		return pointName;
	}

	public void setPointName(String pointName) {
		this.pointName = pointName;
	}
	
	public String getCityUuid() {
		return cityUuid;
	}

	public void setCityUuid(String cityUuid) {
		this.cityUuid = cityUuid;
	}
	
	public String getStagingAreaUuid() {
		return stagingAreaUuid;
	}

	public void setStagingAreaUuid(String stagingAreaUuid) {
		this.stagingAreaUuid = stagingAreaUuid;
	}
	
	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}
	
	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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
	
	public String getIsStatic() {
		return isStatic;
	}

	public void setIsStatic(String isStatic) {
		this.isStatic = isStatic;
	}

}


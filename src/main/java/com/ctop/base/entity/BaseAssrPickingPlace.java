package com.ctop.base.entity;

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
@Table(name = "BASE_ASSR_PICKING_PLACE")
@BatchSize(size = 20)
public class BaseAssrPickingPlace extends BaseEntity implements Serializable {

	@Id
	@Column(name = "APP_UUID")
	private String appUuid;//uuid

	@Column(name = "APP_CODE")
	private String appCode;//领料工厂代码

	@Column(name = "APP_NAME")
	private String appName;//领料工厂名称

	@Column(name = "CITY_UUID")
	private String cityUuid;//城市uuid

	@Column(name = "CITY_NAME")
	private String cityName;//城市名称

	@Column(name = "REGION_UUID")
	private String regionUuid;//区域uuid

	@Column(name = "REGION_NAME")
	private String regionName;//区域名称

	@Column(name = "ADDRESS")
	private String address;//地址

	@Column(name = "PINYIN")
	private String pinyin;//拼音

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

	@Column(name = "DEPARTMENT_UUID")
	private String departmentUuid;//对应部门UUID

	@Column(name = "APP_TYPE")
	private String appType;//领料工厂类型（after_sales,outside_plan）

	@Column(name = "ENTER_NO")
	private String enterNo;//领料入口

	@Column(name = "AP_UUID")
	private String apUuid;//领料基地UUID

	@Column(name = "AP_NAME")
	private String apName;//领料基地名称

	public String getAppUuid() {
		return appUuid;
	}

	public void setAppUuid(String appUuid) {
		this.appUuid = appUuid;
	}
	
	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}
	
	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}
	
	public String getCityUuid() {
		return cityUuid;
	}

	public void setCityUuid(String cityUuid) {
		this.cityUuid = cityUuid;
	}
	
	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	
	public String getRegionUuid() {
		return regionUuid;
	}

	public void setRegionUuid(String regionUuid) {
		this.regionUuid = regionUuid;
	}
	
	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
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
	
	public String getDepartmentUuid() {
		return departmentUuid;
	}

	public void setDepartmentUuid(String departmentUuid) {
		this.departmentUuid = departmentUuid;
	}
	
	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}
	
	public String getEnterNo() {
		return enterNo;
	}

	public void setEnterNo(String enterNo) {
		this.enterNo = enterNo;
	}
	
	public String getApUuid() {
		return apUuid;
	}

	public void setApUuid(String apUuid) {
		this.apUuid = apUuid;
	}
	
	public String getApName() {
		return apName;
	}

	public void setApName(String apName) {
		this.apName = apName;
	}
	
}


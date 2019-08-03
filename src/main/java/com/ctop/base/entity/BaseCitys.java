package com.ctop.base.entity;

import java.util.Date;

import java.io.Serializable;import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.format.annotation.DateTimeFormat;
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
@Table(name = "BASE_CITYS")
@BatchSize(size = 20)
public class BaseCitys extends BaseEntity implements Serializable {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "CITY_UUID")
	private String cityUuid;//uuid

	@Column(name = "PROVINCE_UUID")
	private String provinceUuid;//省份

	@Column(name = "COUNTRY_UUID")
	private String countryUuid;//国家

	@Column(name = "CITY_NAME")
	private String cityName;//城市名称

	@Column(name = "CITY_CODE")
	private String cityCode;//城市代码

	@Column(name = "PINYIN")
	private String pinyin;//拼音

	@Column(name = "SEQ_NO")
	private Long seqNo;//排序

	public String getCityUuid() {
		return cityUuid;
	}

	public void setCityUuid(String cityUuid) {
		this.cityUuid = cityUuid;
	}
	
	public String getProvinceUuid() {
		return provinceUuid;
	}

	public void setProvinceUuid(String provinceUuid) {
		this.provinceUuid = provinceUuid;
	}
	
	public String getCountryUuid() {
		return countryUuid;
	}

	public void setCountryUuid(String countryUuid) {
		this.countryUuid = countryUuid;
	}
	
	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	
	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	
	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}
	
	public Long getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Long seqNo) {
		this.seqNo = seqNo;
	}
	
}


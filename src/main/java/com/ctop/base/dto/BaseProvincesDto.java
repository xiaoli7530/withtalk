package com.ctop.base.dto;

import java.io.Serializable;import java.math.BigDecimal;
import java.util.List;

import com.ctop.fw.common.model.BaseDto;

/**
 * <pre>
 * 功能说明：BaseProvinces实体的数据传输对像
 * 创建者：${user}
 * 创建时间：${date}
 * 版本：${version}
 * </pre>
 */
public class BaseProvincesDto extends BaseDto implements Serializable {
	private String provinceUuid;
	private String countryUuid;
	private String provName;
	private String provCode;
	private String pinyin;
	private Long seqNo;
	
	private List<BaseCitysDto> citys;

	
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
	
	public String getProvName() {
		return provName;
	}

	public void setProvName(String provName) {
		this.provName = provName;
	}
	
	public String getProvCode() {
		return provCode;
	}

	public void setProvCode(String provCode) {
		this.provCode = provCode;
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

	public List<BaseCitysDto> getCitys() {
		return citys;
	}

	public void setCitys(List<BaseCitysDto> citys) {
		this.citys = citys;
	}

}


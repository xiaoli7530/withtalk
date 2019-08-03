package com.ctop.base.dto;

import java.io.Serializable;
import java.util.List;

import com.ctop.fw.common.model.BaseDto;
import com.fasterxml.jackson.annotation.JsonView;

/**
 * <pre>
 * 功能说明：BaseCountrys实体的数据传输对像
 * 创建者：${user}
 * 创建时间：${date}
 * 版本：${version}
 * </pre>
 */
public class BaseCountrysDto extends BaseDto implements Serializable {
	public static interface SimplifyView{}
	

	@JsonView(SimplifyView.class)
	private String countryUuid;

	@JsonView(SimplifyView.class)
	private String countryName;

	@JsonView(SimplifyView.class)
	private String countryCode;
	
	@JsonView(SimplifyView.class)
	private String pinyin;

	@JsonView(SimplifyView.class)
	private Long seqNo;

	@JsonView(SimplifyView.class)
	private List<BaseProvincesDto> provinces;
	
	public String getCountryUuid() {
		return countryUuid;
	}

	public void setCountryUuid(String countryUuid) {
		this.countryUuid = countryUuid;
	}
	
	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	
	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
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

	public List<BaseProvincesDto> getProvinces() {
		return provinces;
	}

	public void setProvinces(List<BaseProvincesDto> provinces) {
		this.provinces = provinces;
	}

}


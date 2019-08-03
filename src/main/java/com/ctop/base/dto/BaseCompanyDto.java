package com.ctop.base.dto;

import java.io.Serializable;import java.math.BigDecimal;

import com.ctop.fw.common.model.BaseDto;

/**
 * <pre>
 * 功能说明：BaseCompany实体的数据传输对像
 * 创建者：${user}
 * 创建时间：${date}
 * 版本：${version}
 * </pre>
 */
public class BaseCompanyDto extends BaseDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String companyUuid;
	private String companyPhone;
	private String companyCode;
	private String companyName;
	private String companyNamePy;
	private String companyType;
	private String postalCode;
	private String address;
	private String country;
	private String province;
	private String city;
	private String cityArea;
	private String usableFlag;
	private String contacts;
	private String phone;
	private String tel;
	private String title;
	private String email;
	private String fax;
	private String weixin;
	private String qq;
	private String bspUuid;
	private String bsbcUuid;
	private String wirUuid;
	private String worUuid;
	private String wrrUuid;
	private String wcrUuid;
	private String stlName;
	private String trsName;
	private String inTagTpl;
	private String outTagTpl;
	private String ccyCode;
	private String weightUnit;
	private String volumeUnit;
	private String inUnit;
	private String outUnit;
	private String printUnit;
	private String companyCarryName;
	private int carryCount;//承运数量

	
	public String getCompanyUuid() {
		return companyUuid;
	}

	public void setCompanyUuid(String companyUuid) {
		this.companyUuid = companyUuid;
	}
	
	public String getCompanyPhone() {
		return companyPhone;
	}

	public void setCompanyPhone(String companyPhone) {
		this.companyPhone = companyPhone;
	}
	
	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	public String getCompanyNamePy() {
		return companyNamePy;
	}

	public void setCompanyNamePy(String companyNamePy) {
		this.companyNamePy = companyNamePy;
	}
	
	public String getCompanyType() {
		return companyType;
	}

	public void setCompanyType(String companyType) {
		this.companyType = companyType;
	}
	
	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}
	
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
	public String getCityArea() {
		return cityArea;
	}

	public void setCityArea(String cityArea) {
		this.cityArea = cityArea;
	}
	
	public String getUsableFlag() {
		return usableFlag;
	}

	public void setUsableFlag(String usableFlag) {
		this.usableFlag = usableFlag;
	}
	
	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}
	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}
	
	public String getWeixin() {
		return weixin;
	}

	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}
	
	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}
	
	public String getBspUuid() {
		return bspUuid;
	}

	public void setBspUuid(String bspUuid) {
		this.bspUuid = bspUuid;
	}
	
	public String getBsbcUuid() {
		return bsbcUuid;
	}

	public void setBsbcUuid(String bsbcUuid) {
		this.bsbcUuid = bsbcUuid;
	}
	
	public String getWirUuid() {
		return wirUuid;
	}

	public void setWirUuid(String wirUuid) {
		this.wirUuid = wirUuid;
	}
	
	public String getWorUuid() {
		return worUuid;
	}

	public void setWorUuid(String worUuid) {
		this.worUuid = worUuid;
	}
	
	public String getWrrUuid() {
		return wrrUuid;
	}

	public void setWrrUuid(String wrrUuid) {
		this.wrrUuid = wrrUuid;
	}
	
	public String getWcrUuid() {
		return wcrUuid;
	}

	public void setWcrUuid(String wcrUuid) {
		this.wcrUuid = wcrUuid;
	}
	
	public String getStlName() {
		return stlName;
	}

	public void setStlName(String stlName) {
		this.stlName = stlName;
	}
	
	public String getTrsName() {
		return trsName;
	}

	public void setTrsName(String trsName) {
		this.trsName = trsName;
	}
	
	public String getInTagTpl() {
		return inTagTpl;
	}

	public void setInTagTpl(String inTagTpl) {
		this.inTagTpl = inTagTpl;
	}
	
	public String getOutTagTpl() {
		return outTagTpl;
	}

	public void setOutTagTpl(String outTagTpl) {
		this.outTagTpl = outTagTpl;
	}
	
	public String getCcyCode() {
		return ccyCode;
	}

	public void setCcyCode(String ccyCode) {
		this.ccyCode = ccyCode;
	}
	
	public String getWeightUnit() {
		return weightUnit;
	}

	public void setWeightUnit(String weightUnit) {
		this.weightUnit = weightUnit;
	}
	
	public String getVolumeUnit() {
		return volumeUnit;
	}

	public void setVolumeUnit(String volumeUnit) {
		this.volumeUnit = volumeUnit;
	}
	
	public String getInUnit() {
		return inUnit;
	}

	public void setInUnit(String inUnit) {
		this.inUnit = inUnit;
	}
	
	public String getOutUnit() {
		return outUnit;
	}

	public void setOutUnit(String outUnit) {
		this.outUnit = outUnit;
	}
	
	public String getPrintUnit() {
		return printUnit;
	}

	public void setPrintUnit(String printUnit) {
		this.printUnit = printUnit;
	}

	public String getCompanyCarryName() {
		return companyCarryName;
	}

	public void setCompanyCarryName(String companyCarryName) {
		this.companyCarryName = companyCarryName;
	}

	public int getCarryCount() {
		return carryCount;
	}

	public void setCarryCount(int carryCount) {
		this.carryCount = carryCount;
	}
	
}


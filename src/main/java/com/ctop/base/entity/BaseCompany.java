package com.ctop.base.entity;

import java.io.Serializable;import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.GenericGenerator;

import com.ctop.fw.common.entity.BaseEntity;


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
@Table(name = "BASE_COMPANY")
@BatchSize(size = 20)
public class BaseCompany extends BaseEntity implements Serializable {

	@Id
	@Column(name = "COMPANY_UUID")
	private String companyUuid;//UUID

	@Column(name = "COMPANY_PHONE")
	private String companyPhone;//手机

	@Column(name = "COMPANY_CODE")
	private String companyCode;//客户编号

	@Column(name = "COMPANY_NAME")
	private String companyName;//客户名称

	@Column(name = "COMPANY_NAME_PY")
	private String companyNamePy;//客户拼音(首字母 全拼)

	@Column(name = "COMPANY_TYPE")
	private String companyType;//{label:"客户类型(货主、收货人、供应商、承运人、结算人、下单方、其他)", dict:"COMPANY_TYPE"}

	@Column(name = "POSTAL_CODE")
	private String postalCode;//邮政编码

	@Column(name = "ADDRESS")
	private String address;//地址

	@Column(name = "COUNTRY")
	private String country;//国家

	@Column(name = "PROVINCE")
	private String province;//省份

	@Column(name = "CITY")
	private String city;//城市

	@Column(name = "CITY_AREA")
	private String cityArea;//地区

	@Column(name = "USABLE_FLAG")
	private String usableFlag;//{label:"是否启用", dict:"YES_NO"}

	@Column(name = "CONTACTS")
	private String contacts;//联系人

	@Column(name = "PHONE")
	private String phone;//手机

	@Column(name = "TEL")
	private String tel;//电话

	@Column(name = "TITLE")
	private String title;//职务

	@Column(name = "EMAIL")
	private String email;//电子邮件

	@Column(name = "FAX")
	private String fax;//传真

	@Column(name = "WEIXIN")
	private String weixin;//微信号

	@Column(name = "QQ")
	private String qq;//QQ号

	@Column(name = "BSP_UUID")
	private String bspUuid;//包装属性 BASE_MATEARIAL_PACKAGE

	@Column(name = "BSBC_UUID")
	private String bsbcUuid;//批次属性 BASE_MATEARIAL_BATCH_CONFIG

	@Column(name = "WIR_UUID")
	private String wirUuid;//上架规则 WM_IN_RULE

	@Column(name = "WOR_UUID")
	private String worUuid;//库存周转规则 WM_OUT_RULE

	@Column(name = "WRR_UUID")
	private String wrrUuid;//补货规则 WM_RESTOCK_RULE

	@Column(name = "WCR_UUID")
	private String wcrUuid;//质检规则 WM_CHECK_RULE

//	@Column(name = "STL_NAME")
//	private String stlName;//结算方

//	@Column(name = "TRS_NAME")
//	private String trsName;//承运人

	@Column(name = "IN_TAG_TPL")
	private String inTagTpl;//入库标签

	@Column(name = "OUT_TAG_TPL")
	private String outTagTpl;//出库标签

	@Column(name = "CCY_CODE")
	private String ccyCode;//币种

	@Column(name = "WEIGHT_UNIT")
	private String weightUnit;//重量单位

	@Column(name = "VOLUME_UNIT")
	private String volumeUnit;//体积单位

	@Column(name = "IN_UNIT")
	private String inUnit;//缺省收货单位

	@Column(name = "OUT_UNIT")
	private String outUnit;//缺省发货单位

	@Column(name = "PRINT_UNIT")
	private String printUnit;//打印单位

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
	
}


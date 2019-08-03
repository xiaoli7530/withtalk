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
@Table(name = "BASE_SKU")
@BatchSize(size = 20)
public class BaseSku extends BaseEntity implements Serializable {

	@Column(name = "MUDEL")
	private String mudel;//型号

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "SKU_UUID")
	private String skuUuid;//UUID

	@Column(name = "COMPANY_UUID")
	private String companyUuid;//归属公司

	@Column(name = "ST_UUID")
	private String stUuid;//目录UUID

	@Column(name = "SKU_CODE")
	private String skuCode;//货物编码

	@Column(name = "SKU_NAME")
	private String skuName;//货物名称

	@Column(name = "SKU_NAME_PY")
	private String skuNamePy;//货物名称拼音 (xmsj xiaomishouji)

	@Column(name = "SPEC")
	private String spec;//规格型号

	@Column(name = "SKU_TYPE")
	private String skuType;//{label:"货物类别", dict:"SKU_TYPE"}

	@Column(name = "LENGTH")
	private BigDecimal length;//长 CM

	@Column(name = "WIDTH")
	private BigDecimal width;//宽 CM

	@Column(name = "HEIGHT")
	private BigDecimal height;//高 CM

	@Column(name = "VOLUME")
	private BigDecimal volume;//体积

	@Column(name = "VOLUME_UNIT")
	private String volumeUnit;//体积单位

	@Column(name = "WEIGHT_UNIT")
	private String weightUnit;//重量单位

	@Column(name = "GROSS_WEIGHT")
	private BigDecimal grossWeight;//毛重

	@Column(name = "NET_WEIGHT")
	private BigDecimal netWeight;//净重

	@Column(name = "TARE_WEIGHT")
	private BigDecimal tareWeight;//皮重

	@Column(name = "PRICE")
	private BigDecimal price;//单价

	@Column(name = "ALIAS1")
	private String alias1;//别名1

	@Column(name = "ALIAS2")
	private String alias2;//别名2

	@Column(name = "ALIAS3")
	private String alias3;//别名3

	@Column(name = "ALIAS4")
	private String alias4;//别名4

	@Column(name = "ALIAS5")
	private String alias5;//别名5

	@Column(name = "G1")
	private String g1;//G1 扩展属性

	@Column(name = "G2")
	private String g2;//G2

	@Column(name = "G3")
	private String g3;//G3

	@Column(name = "G4")
	private String g4;//G4

	@Column(name = "G5")
	private String g5;//G5

	@Column(name = "G6")
	private String g6;//G6

	@Column(name = "G7")
	private String g7;//G7

	@Column(name = "G8")
	private String g8;//G8

	@Column(name = "G9")
	private String g9;//G9

	@Column(name = "G10")
	private String g10;//G10

	@Column(name = "G11")
	private String g11;//G11

	@Column(name = "G12")
	private String g12;//G12

	@Column(name = "G13")
	private String g13;//G13

	@Column(name = "G14")
	private String g14;//G14

	@Column(name = "G15")
	private String g15;//G15

	@Column(name = "G16")
	private String g16;//G16

	@Column(name = "G17")
	private String g17;//G17

	@Column(name = "G18")
	private String g18;//G18

	@Column(name = "G19")
	private String g19;//G19

	@Column(name = "G20")
	private String g20;//G20

	@Column(name = "REMARK")
	private String remark;//备注

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

	@Column(name = "ALLOW_OPEN_BOX")
	private String allowOpenBox;//{label:"是否允许拆箱包装", dict:"YES_NO"}

	//@Column(name = "ALLOW_OPEN_INNER_BOX")
	//private String allowOpenInnerBox;//{label:"是否允许拆内包装", dict:"YES_NO"}

	@Column(name = "LIFE_MNG_FLAG")
	private String lifeMngFlag;//{label:"是否进行有效期控制", dict:"YES_NO"}

	@Column(name = "LIFE_TYPE")
	private String lifeType;//{label:"周期类型(按生产日期/按入库日期)", dict:"SKU_LIFE_TYPE"}

	@Column(name = "LIFE_DAYS")
	private BigDecimal lifeDays;//有效期（天）

	@Column(name = "LIFE_WARN_DAYS")
	private BigDecimal lifeWarnDays;//失效期预警（天）

	@Column(name = "LIFE_DAYS_IN")
	private BigDecimal lifeDaysIn;//入库效期（天）

	@Column(name = "LIFE_DAYS_OUT")
	private BigDecimal lifeDaysOut;//出库效期（天）

	@Column(name = "CHECK_CYCLE_DAYS")
	private BigDecimal checkCycleDays;//质检周期（天）

	@Column(name = "DC_TYPE")
	private String dcType;//{label:"危险品标识(参考国标)", dict:"SKU_DC_TYPE"}

	@Column(name = "IN_UNIT")
	private String inUnit;//缺省收货单位

	@Column(name = "OUT_UNIT")
	private String outUnit;//缺省发货单位

	@Column(name = "PRINT_UNIT")
	private String printUnit;//打印单位

	@Column(name = "IS_PRODUCT")
	private String isProduct;//是否为产品组件

	public String getMudel() {
		return mudel;
	}

	public void setMudel(String mudel) {
		this.mudel = mudel;
	}
	
	public String getSkuUuid() {
		return skuUuid;
	}

	public void setSkuUuid(String skuUuid) {
		this.skuUuid = skuUuid;
	}
	
	public String getCompanyUuid() {
		return companyUuid;
	}

	public void setCompanyUuid(String companyUuid) {
		this.companyUuid = companyUuid;
	}
	
	public String getStUuid() {
		return stUuid;
	}

	public void setStUuid(String stUuid) {
		this.stUuid = stUuid;
	}
	
	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	
	public String getSkuName() {
		return skuName;
	}

	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}
	
	public String getSkuNamePy() {
		return skuNamePy;
	}

	public void setSkuNamePy(String skuNamePy) {
		this.skuNamePy = skuNamePy;
	}
	
	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}
	
	public String getSkuType() {
		return skuType;
	}

	public void setSkuType(String skuType) {
		this.skuType = skuType;
	}
	
	public BigDecimal getLength() {
		return length;
	}

	public void setLength(BigDecimal length) {
		this.length = length;
	}
	
	public BigDecimal getWidth() {
		return width;
	}

	public void setWidth(BigDecimal width) {
		this.width = width;
	}
	
	public BigDecimal getHeight() {
		return height;
	}

	public void setHeight(BigDecimal height) {
		this.height = height;
	}
	
	public BigDecimal getVolume() {
		return volume;
	}

	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}
	
	public String getVolumeUnit() {
		return volumeUnit;
	}

	public void setVolumeUnit(String volumeUnit) {
		this.volumeUnit = volumeUnit;
	}
	
	public String getWeightUnit() {
		return weightUnit;
	}

	public void setWeightUnit(String weightUnit) {
		this.weightUnit = weightUnit;
	}
	
	public BigDecimal getGrossWeight() {
		return grossWeight;
	}

	public void setGrossWeight(BigDecimal grossWeight) {
		this.grossWeight = grossWeight;
	}
	
	public BigDecimal getNetWeight() {
		return netWeight;
	}

	public void setNetWeight(BigDecimal netWeight) {
		this.netWeight = netWeight;
	}
	
	public BigDecimal getTareWeight() {
		return tareWeight;
	}

	public void setTareWeight(BigDecimal tareWeight) {
		this.tareWeight = tareWeight;
	}
	
	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
	public String getAlias1() {
		return alias1;
	}

	public void setAlias1(String alias1) {
		this.alias1 = alias1;
	}
	
	public String getAlias2() {
		return alias2;
	}

	public void setAlias2(String alias2) {
		this.alias2 = alias2;
	}
	
	public String getAlias3() {
		return alias3;
	}

	public void setAlias3(String alias3) {
		this.alias3 = alias3;
	}
	
	public String getAlias4() {
		return alias4;
	}

	public void setAlias4(String alias4) {
		this.alias4 = alias4;
	}
	
	public String getAlias5() {
		return alias5;
	}

	public void setAlias5(String alias5) {
		this.alias5 = alias5;
	}
	
	public String getG1() {
		return g1;
	}

	public void setG1(String g1) {
		this.g1 = g1;
	}
	
	public String getG2() {
		return g2;
	}

	public void setG2(String g2) {
		this.g2 = g2;
	}
	
	public String getG3() {
		return g3;
	}

	public void setG3(String g3) {
		this.g3 = g3;
	}
	
	public String getG4() {
		return g4;
	}

	public void setG4(String g4) {
		this.g4 = g4;
	}
	
	public String getG5() {
		return g5;
	}

	public void setG5(String g5) {
		this.g5 = g5;
	}
	
	public String getG6() {
		return g6;
	}

	public void setG6(String g6) {
		this.g6 = g6;
	}
	
	public String getG7() {
		return g7;
	}

	public void setG7(String g7) {
		this.g7 = g7;
	}
	
	public String getG8() {
		return g8;
	}

	public void setG8(String g8) {
		this.g8 = g8;
	}
	
	public String getG9() {
		return g9;
	}

	public void setG9(String g9) {
		this.g9 = g9;
	}
	
	public String getG10() {
		return g10;
	}

	public void setG10(String g10) {
		this.g10 = g10;
	}
	
	public String getG11() {
		return g11;
	}

	public void setG11(String g11) {
		this.g11 = g11;
	}
	
	public String getG12() {
		return g12;
	}

	public void setG12(String g12) {
		this.g12 = g12;
	}
	
	public String getG13() {
		return g13;
	}

	public void setG13(String g13) {
		this.g13 = g13;
	}
	
	public String getG14() {
		return g14;
	}

	public void setG14(String g14) {
		this.g14 = g14;
	}
	
	public String getG15() {
		return g15;
	}

	public void setG15(String g15) {
		this.g15 = g15;
	}
	
	public String getG16() {
		return g16;
	}

	public void setG16(String g16) {
		this.g16 = g16;
	}
	
	public String getG17() {
		return g17;
	}

	public void setG17(String g17) {
		this.g17 = g17;
	}
	
	public String getG18() {
		return g18;
	}

	public void setG18(String g18) {
		this.g18 = g18;
	}
	
	public String getG19() {
		return g19;
	}

	public void setG19(String g19) {
		this.g19 = g19;
	}
	
	public String getG20() {
		return g20;
	}

	public void setG20(String g20) {
		this.g20 = g20;
	}
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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
	
	public String getAllowOpenBox() {
		return allowOpenBox;
	}

	public void setAllowOpenBox(String allowOpenBox) {
		this.allowOpenBox = allowOpenBox;
	}
//	
//	public String getAllowOpenInnerBox() {
//		return allowOpenInnerBox;
//	}
//
//	public void setAllowOpenInnerBox(String allowOpenInnerBox) {
//		this.allowOpenInnerBox = allowOpenInnerBox;
//	}
//	
	public String getLifeMngFlag() {
		return lifeMngFlag;
	}

	public void setLifeMngFlag(String lifeMngFlag) {
		this.lifeMngFlag = lifeMngFlag;
	}
	
	public String getLifeType() {
		return lifeType;
	}

	public void setLifeType(String lifeType) {
		this.lifeType = lifeType;
	}
	
	public BigDecimal getLifeDays() {
		return lifeDays;
	}

	public void setLifeDays(BigDecimal lifeDays) {
		this.lifeDays = lifeDays;
	}
	
	public BigDecimal getLifeWarnDays() {
		return lifeWarnDays;
	}

	public void setLifeWarnDays(BigDecimal lifeWarnDays) {
		this.lifeWarnDays = lifeWarnDays;
	}
	
	public BigDecimal getLifeDaysIn() {
		return lifeDaysIn;
	}

	public void setLifeDaysIn(BigDecimal lifeDaysIn) {
		this.lifeDaysIn = lifeDaysIn;
	}
	
	public BigDecimal getLifeDaysOut() {
		return lifeDaysOut;
	}

	public void setLifeDaysOut(BigDecimal lifeDaysOut) {
		this.lifeDaysOut = lifeDaysOut;
	}
	
	public BigDecimal getCheckCycleDays() {
		return checkCycleDays;
	}

	public void setCheckCycleDays(BigDecimal checkCycleDays) {
		this.checkCycleDays = checkCycleDays;
	}
	
	public String getDcType() {
		return dcType;
	}

	public void setDcType(String dcType) {
		this.dcType = dcType;
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
	
	public String getIsProduct() {
		return isProduct;
	}

	public void setIsProduct(String isProduct) {
		this.isProduct = isProduct;
	}
	
}


package com.ctop.base.dto;

import com.ctop.fw.common.model.BaseDto;
import java.util.Date;
import java.io.Serializable;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * <pre>
 * 功能说明：BaseSku实体的数据传输对像
 * 创建者：${user}
 * 创建时间：${date}
 * 版本：${version}
 * </pre>
 */
public class BaseSkuDto extends BaseDto implements Serializable {
	private String mudel;
	private String skuUuid;
	private String companyUuid;
	private String stUuid;
	private String skuCode;
	private String skuName;
	private String skuNamePy;
	private String spec;
	private String skuType;
	private BigDecimal length;
	private BigDecimal width;
	private BigDecimal height;
	private BigDecimal volume;
	private String volumeUnit;
	private String weightUnit;
	private BigDecimal grossWeight;
	private BigDecimal netWeight;
	private BigDecimal tareWeight;
	private BigDecimal price;
	private String alias1;
	private String alias2;
	private String alias3;
	private String alias4;
	private String alias5;
	private String g1;
	private String g2;
	private String g3;
	private String g4;
	private String g5;
	private String g6;
	private String g7;
	private String g8;
	private String g9;
	private String g10;
	private String g11;
	private String g12;
	private String g13;
	private String g14;
	private String g15;
	private String g16;
	private String g17;
	private String g18;
	private String g19;
	private String g20;
	private String remark;
	private String bspUuid;
	private String bsbcUuid;
	private String wirUuid;
	private String worUuid;
	private String wrrUuid;
	private String wcrUuid;
	private String allowOpenBox;
	//private String allowOpenInnerBox;
	private String lifeMngFlag;
	private String lifeType;
	private BigDecimal lifeDays;
	private BigDecimal lifeWarnDays;
	private BigDecimal lifeDaysIn;
	private BigDecimal lifeDaysOut;
	private BigDecimal checkCycleDays;
	private String dcType;
	private String inUnit;
	private String outUnit;
	private String printUnit;
	private String isProduct;
	private String skuTypeName;

	
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
	
//	public String getAllowOpenInnerBox() {
//		return allowOpenInnerBox;
//	}
//
//	public void setAllowOpenInnerBox(String allowOpenInnerBox) {
//		this.allowOpenInnerBox = allowOpenInnerBox;
//	}
	
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

	public String getSkuTypeName() {
		return skuTypeName;
	}

	public void setSkuTypeName(String skuTypeName) {
		this.skuTypeName = skuTypeName;
	}
	
}


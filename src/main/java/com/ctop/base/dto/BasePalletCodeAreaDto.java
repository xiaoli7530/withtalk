package com.ctop.base.dto;

import java.io.Serializable;import java.math.BigDecimal;

import com.ctop.fw.common.model.BaseDto;

/**
 * <pre>
 * 功能说明：BasePalletCodeArea实体的数据传输对像
 * 创建者：${lishuai}
 * 创建时间：${2016.9.13}
 * 版本：${version}
 * </pre>
 */
public class BasePalletCodeAreaDto extends BaseDto implements Serializable{
	
	private String pcaUuid;//托盘与库区关联表主键
	
	private String palletPreCode;//托盘前缀
	
	private String wwaUuid;//库区UUID
	
	private String ext1;
	
	private String ext2;
	
	private String ext3;
	
	private String ext4;
	
	private String ext5;
	
	private String areaName;
	
	
	
	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getPcaUuid() {
		return pcaUuid;
	}

	public void setPcaUuid(String pcaUuid) {
		this.pcaUuid = pcaUuid;
	}

	public String getPalletPreCode() {
		return palletPreCode;
	}

	public void setPalletPreCode(String palletPreCode) {
		this.palletPreCode = palletPreCode;
	}

	public String getWwaUuid() {
		return wwaUuid;
	}

	public void setWwaUuid(String wwaUuid) {
		this.wwaUuid = wwaUuid;
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
	
	
}

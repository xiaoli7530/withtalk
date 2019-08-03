package com.ctop.base.dto;

import java.util.Date;
import java.io.Serializable;
import org.springframework.format.annotation.DateTimeFormat;

import com.ctop.fw.common.model.BaseDto;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * <pre>
 * 功能说明：BaseSaReceivingPlace实体的数据传输对像
 * 创建者：${user}
 * 创建时间：${date}
 * 版本：${version}
 * </pre>
 */
public class BaseSaReceivingPlaceDto extends BaseDto implements Serializable {
	private String bspUuid;
	private String warehouseUuid;
	private String regionUuid;
	private String bspCode;
	private String bspName;
	private String pinyin;
	private String ext1;
	private String ext2;
	private String ext3;
	private String ext4;
	private String ext5;

	
	public String getBspUuid() {
		return bspUuid;
	}

	public void setBspUuid(String bspUuid) {
		this.bspUuid = bspUuid;
	}
	
	public String getWarehouseUuid() {
		return warehouseUuid;
	}

	public void setWarehouseUuid(String warehouseUuid) {
		this.warehouseUuid = warehouseUuid;
	}
	
	public String getRegionUuid() {
		return regionUuid;
	}

	public void setRegionUuid(String regionUuid) {
		this.regionUuid = regionUuid;
	}
	
	public String getBspCode() {
		return bspCode;
	}

	public void setBspCode(String bspCode) {
		this.bspCode = bspCode;
	}
	
	public String getBspName() {
		return bspName;
	}

	public void setBspName(String bspName) {
		this.bspName = bspName;
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

}


package com.ctop.base.dto;

import java.util.Date;
import java.io.Serializable;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;

import com.ctop.fw.common.model.BaseDto;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * <pre>
 * 功能说明：BaseMaterialSize实体的数据传输对像
 * 创建者：${user}
 * 创建时间：${date}
 * 版本：${version}
 * </pre>
 */
public class BaseMaterialSizeDto extends BaseDto implements Serializable {
	private String bmsUuid;
	private String sizeType;
	private BigDecimal length;
	private BigDecimal width;
	private BigDecimal height;
	private String sizeInfo;
	private String remark;
	private String ext1;
	private String ext2;
	private String ext3;
	private String ext4;
	private String ext5;

	
	public String getBmsUuid() {
		return bmsUuid;
	}

	public void setBmsUuid(String bmsUuid) {
		this.bmsUuid = bmsUuid;
	}
	
	public String getSizeType() {
		return sizeType;
	}

	public void setSizeType(String sizeType) {
		this.sizeType = sizeType;
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
	
	public String getSizeInfo() {
		return sizeInfo;
	}

	public void setSizeInfo(String sizeInfo) {
		this.sizeInfo = sizeInfo;
	}
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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


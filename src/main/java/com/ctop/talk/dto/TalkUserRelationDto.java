package com.ctop.talk.dto;

import com.ctop.fw.common.model.BaseDto;
import java.util.Date;
import java.io.Serializable;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * <pre>
 * 功能说明：TalkUserRelation实体的数据传输对像
 * 创建者：${user}
 * 创建时间：${date}
 * 版本：${version}
 * </pre>
 */
public class TalkUserRelationDto extends BaseDto implements Serializable {
	private String userRelationUuid;
	private String userIdY;
	private String userIdM;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	@JsonFormat(pattern="yyyy-MM-dd HH:mm",timezone = "GMT+8") 
	private Date relationStart;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	@JsonFormat(pattern="yyyy-MM-dd HH:mm",timezone = "GMT+8") 
	private Date relationEnd;
	private String ext1;
	private String ext2;
	private String ext3;
	private String ext4;
	private String ext5;

	
	public String getUserRelationUuid() {
		return userRelationUuid;
	}

	public void setUserRelationUuid(String userRelationUuid) {
		this.userRelationUuid = userRelationUuid;
	}
	
	public String getUserIdY() {
		return userIdY;
	}

	public void setUserIdY(String userIdY) {
		this.userIdY = userIdY;
	}
	
	public String getUserIdM() {
		return userIdM;
	}

	public void setUserIdM(String userIdM) {
		this.userIdM = userIdM;
	}
	
	public Date getRelationStart() {
		return relationStart;
	}

	public void setRelationStart(Date relationStart) {
		this.relationStart = relationStart;
	}
	
	public Date getRelationEnd() {
		return relationEnd;
	}

	public void setRelationEnd(Date relationEnd) {
		this.relationEnd = relationEnd;
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


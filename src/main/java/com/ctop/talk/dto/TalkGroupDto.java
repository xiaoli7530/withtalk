package com.ctop.talk.dto;

import com.ctop.fw.common.model.BaseDto;
import java.util.Date;
import java.io.Serializable;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * <pre>
 * 功能说明：TalkGroup实体的数据传输对像
 * 创建者：${user}
 * 创建时间：${date}
 * 版本：${version}
 * </pre>
 */
public class TalkGroupDto extends BaseDto implements Serializable {
	private String groupUuid;
	private String groupName;
	private BigDecimal groupCreaterUuid;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	@JsonFormat(pattern="yyyy-MM-dd HH:mm",timezone = "GMT+8") 
	private Date groupCreateTime;
	private String groupIntroduction;
	private BigDecimal groupTotalCount;
	private BigDecimal groupUserCount;
	private BigDecimal groupManagementUuid;
	private String groupManagementName;
	private BigDecimal groupMasterUuid;
	private String groupMasterName;
	private String ext1;
	private String ext2;
	private String ext3;
	private String ext4;
	private String ext5;

	
	public String getGroupUuid() {
		return groupUuid;
	}

	public void setGroupUuid(String groupUuid) {
		this.groupUuid = groupUuid;
	}
	
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	public BigDecimal getGroupCreaterUuid() {
		return groupCreaterUuid;
	}

	public void setGroupCreaterUuid(BigDecimal groupCreaterUuid) {
		this.groupCreaterUuid = groupCreaterUuid;
	}
	
	public Date getGroupCreateTime() {
		return groupCreateTime;
	}

	public void setGroupCreateTime(Date groupCreateTime) {
		this.groupCreateTime = groupCreateTime;
	}
	
	public String getGroupIntroduction() {
		return groupIntroduction;
	}

	public void setGroupIntroduction(String groupIntroduction) {
		this.groupIntroduction = groupIntroduction;
	}
	
	public BigDecimal getGroupTotalCount() {
		return groupTotalCount;
	}

	public void setGroupTotalCount(BigDecimal groupTotalCount) {
		this.groupTotalCount = groupTotalCount;
	}
	
	public BigDecimal getGroupUserCount() {
		return groupUserCount;
	}

	public void setGroupUserCount(BigDecimal groupUserCount) {
		this.groupUserCount = groupUserCount;
	}
	
	public BigDecimal getGroupManagementUuid() {
		return groupManagementUuid;
	}

	public void setGroupManagementUuid(BigDecimal groupManagementUuid) {
		this.groupManagementUuid = groupManagementUuid;
	}
	
	public String getGroupManagementName() {
		return groupManagementName;
	}

	public void setGroupManagementName(String groupManagementName) {
		this.groupManagementName = groupManagementName;
	}
	
	public BigDecimal getGroupMasterUuid() {
		return groupMasterUuid;
	}

	public void setGroupMasterUuid(BigDecimal groupMasterUuid) {
		this.groupMasterUuid = groupMasterUuid;
	}
	
	public String getGroupMasterName() {
		return groupMasterName;
	}

	public void setGroupMasterName(String groupMasterName) {
		this.groupMasterName = groupMasterName;
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


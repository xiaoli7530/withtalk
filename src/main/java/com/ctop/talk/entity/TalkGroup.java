package com.ctop.talk.entity;

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
@Table(name = "TALK_GROUP")
@BatchSize(size = 20)
public class TalkGroup extends BaseEntity implements Serializable {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "GROUP_UUID")
	private String groupUuid;//主键

	@Column(name = "GROUP_NAME")
	private String groupName;//群名字

	@Column(name = "GROUP_CREATER_UUID")
	private BigDecimal groupCreaterUuid;//创建人user_uuid

	@Column(name = "GROUP_CREATE_TIME")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8") 
	private Date groupCreateTime;//创建人名字

	@Column(name = "GROUP_INTRODUCTION")
	private String groupIntroduction;//群介绍

	@Column(name = "GROUP_TOTAL_COUNT")
	private BigDecimal groupTotalCount;//群可以容纳人的总数

	@Column(name = "GROUP_USER_COUNT")
	private BigDecimal groupUserCount;//群人数的数量

	@Column(name = "GROUP_MANAGEMENT_UUID")
	private BigDecimal groupManagementUuid;//群管理员user_uuid

	@Column(name = "GROUP_MANAGEMENT_NAME")
	private String groupManagementName;//群管理员名字

	@Column(name = "GROUP_MASTER_UUID")
	private BigDecimal groupMasterUuid;//群主user_uuid

	@Column(name = "GROUP_MASTER_NAME")
	private String groupMasterName;//群主名字

	@Column(name = "EXT1")
	private String ext1;//备注1

	@Column(name = "EXT2")
	private String ext2;//备注2

	@Column(name = "EXT3")
	private String ext3;//备注3

	@Column(name = "EXT4")
	private String ext4;//备注4

	@Column(name = "EXT5")
	private String ext5;//备注5

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


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
@Table(name = "TALK_USER_GROUP_RELATION")
@BatchSize(size = 20)
public class TalkUserGroupRelation extends BaseEntity implements Serializable {

	@Column(name = "GROUP_UUID")
	private String groupUuid;//group主键

	@Column(name = "GROUP_LEVEL")
	private String groupLevel;//群等级

	@Column(name = "RELATION_START")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8") 
	private Date relationStart;//建立联系时间

	@Column(name = "RELATION_END")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8") 
	private Date relationEnd;//结束联系时间

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

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "USER_GROUP_RELATION_UUIS")
	private String userGroupRelationUuis;//主键

	@Column(name = "USER_UUID")
	private String userUuid;//user主键

	public String getGroupUuid() {
		return groupUuid;
	}

	public void setGroupUuid(String groupUuid) {
		this.groupUuid = groupUuid;
	}
	
	public String getGroupLevel() {
		return groupLevel;
	}

	public void setGroupLevel(String groupLevel) {
		this.groupLevel = groupLevel;
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
	
	public String getUserGroupRelationUuis() {
		return userGroupRelationUuis;
	}

	public void setUserGroupRelationUuis(String userGroupRelationUuis) {
		this.userGroupRelationUuis = userGroupRelationUuis;
	}
	
	public String getUserUuid() {
		return userUuid;
	}

	public void setUserUuid(String userUuid) {
		this.userUuid = userUuid;
	}
	
}


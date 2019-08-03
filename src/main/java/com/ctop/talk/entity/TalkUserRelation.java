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
@Table(name = "TALK_USER_RELATION")
@BatchSize(size = 20)
public class TalkUserRelation extends BaseEntity implements Serializable {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "USER_RELATION_UUID")
	private String userRelationUuid;//主键

	@Column(name = "USER_ID_Y")
	private String userIdY;//talk_user主键Y

	@Column(name = "USER_ID_M")
	private String userIdM;//talk_user主键M

	@Column(name = "RELATION_START")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8") 
	private Date relationStart;//建立联系时间

	@Column(name = "RELATION_END")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8") 
	private Date relationEnd;//删除联系时间

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


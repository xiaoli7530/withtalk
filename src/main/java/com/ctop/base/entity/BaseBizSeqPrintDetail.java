package com.ctop.base.entity;

import java.util.Date;

import java.io.Serializable;import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat;

import com.ctop.fw.common.entity.BaseEntity;
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
@Table(name = "BASE_BIZ_SEQ_PRINT_DETAIL")
@BatchSize(size = 20)
public class BaseBizSeqPrintDetail extends BaseEntity implements Serializable {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "BBSPD_UUID")
	private String bbspdUuid;//主键

	@Column(name = "BBSP_UUID", insertable=false, updatable=false)
	private String bbspUuid;//单据号打印头表UUID

	@Column(name = "BBS_UUID")
	private String bbsUuid;//业务单据定义UUID

	@Column(name = "BIZ_SEQ_NO")
	private String bizSeqNo;//单据号

	@Column(name = "USED")
	private String used;//是否已使用

	public String getBbspdUuid() {
		return bbspdUuid;
	}

	public void setBbspdUuid(String bbspdUuid) {
		this.bbspdUuid = bbspdUuid;
	}
	
	public String getBbspUuid() {
		return bbspUuid;
	}

	public void setBbspUuid(String bbspUuid) {
		this.bbspUuid = bbspUuid;
	}
	
	public String getBbsUuid() {
		return bbsUuid;
	}

	public void setBbsUuid(String bbsUuid) {
		this.bbsUuid = bbsUuid;
	}
	
	public String getBizSeqNo() {
		return bizSeqNo;
	}

	public void setBizSeqNo(String bizSeqNo) {
		this.bizSeqNo = bizSeqNo;
	}
	
	public String getUsed() {
		return used;
	}

	public void setUsed(String used) {
		this.used = used;
	}
	
}


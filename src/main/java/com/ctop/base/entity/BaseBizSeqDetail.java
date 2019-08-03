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
@Table(name = "BASE_BIZ_SEQ_DETAIL")
@BatchSize(size = 20)
public class BaseBizSeqDetail extends BaseEntity implements Serializable {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "BBSD_UUID")
	private String bbsdUuid;//主键UUID

	@Column(name = "BBS_UUID")
	private String bbsUuid;//业务单据规则定义UUID

	@Column(name = "BIZ_SEQ_HOLDER")
	private String bizSeqHolder;//只缺失了流水号的业务单据号， 它跟BULE_UUID, BIZ_SEQ_HOLDER, IS_ACTIVE 确定唯一记录

	@Column(name = "SEQUENCE")
	private Long sequence;//用来自增的流水号
	
	/**增加序列号*/
	public void increaseSequence(int num) {
		this.sequence = this.sequence == null ? num : this.sequence + num;
	}

	public String getBbsdUuid() {
		return bbsdUuid;
	}

	public void setBbsdUuid(String bbsdUuid) {
		this.bbsdUuid = bbsdUuid;
	}
	
	public String getBbsUuid() {
		return bbsUuid;
	}

	public void setBbsUuid(String bbsUuid) {
		this.bbsUuid = bbsUuid;
	}
	
	public String getBizSeqHolder() {
		return bizSeqHolder;
	}

	public void setBizSeqHolder(String bizSeqHolder) {
		this.bizSeqHolder = bizSeqHolder;
	}
	
	public Long getSequence() {
		return sequence;
	}

	public void setSequence(Long sequence) {
		this.sequence = sequence;
	}
	
}


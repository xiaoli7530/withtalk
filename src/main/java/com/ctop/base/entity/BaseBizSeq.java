package com.ctop.base.entity;

import java.io.Serializable;import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.GenericGenerator;

import com.ctop.fw.common.entity.BaseEntity;


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
@Table(name = "BASE_BIZ_SEQ")
@BatchSize(size = 20)
public class BaseBizSeq extends BaseEntity implements Serializable {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "BBS_UUID")
	private String bbsUuid;//主键UUID

	@Column(name = "CODE")
	private String code;//业务单据规则号

	@Column(name = "NAME")
	private String name;//业务单规则名称

	@Column(name = "FULL_DEFINITION")
	private String fullDefinition;//业务单据规则定义

	@Column(name = "HOLDER_DEFINITION")
	private String holderDefinition;//业务单据规则定义

	@Column(name = "SEQ_LENGTH")
	private Integer seqLength;//用来生成单据号的流水号的长度

	@Column(name = "MAX_LENGTH")
	private Integer maxLength;//整个业务单据号的长度
	
	@Column(name = "INITIAL_VALUE")
	private Integer initialValue;//初始值

	public Integer getInitialValue() {
		return initialValue;
	}

	public void setInitialValue(Integer initialValue) {
		this.initialValue = initialValue;
	}

	public String getBbsUuid() {
		return bbsUuid;
	}

	public void setBbsUuid(String bbsUuid) {
		this.bbsUuid = bbsUuid;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getFullDefinition() {
		return fullDefinition;
	}

	public void setFullDefinition(String fullDefinition) {
		this.fullDefinition = fullDefinition;
	}
	
	public String getHolderDefinition() {
		return holderDefinition;
	}

	public void setHolderDefinition(String holderDefinition) {
		this.holderDefinition = holderDefinition;
	}
	
	public Integer getSeqLength() {
		return seqLength;
	}

	public void setSeqLength(Integer seqLength) {
		this.seqLength = seqLength;
	}
	
	public Integer getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(Integer maxLength) {
		this.maxLength = maxLength;
	}
	
}


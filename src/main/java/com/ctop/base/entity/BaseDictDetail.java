package com.ctop.base.entity;

import java.io.Serializable;import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

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
@Table(name = "BASE_DICT_DETAIL")
@Where( clause = "IS_ACTIVE = 'Y' " )
@BatchSize(size = 20)
public class BaseDictDetail extends BaseEntity implements Serializable {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "BDL_UUID")
	private String bdlUuid;//UUID

	@Column(name = "DICT_CODE")
	private String dictCode;//字典代码

	@Column(name = "CODE")
	private String code;//明细代码

	@Column(name = "NAME")
	private String name;//明细名称

	@Column(name = "SEQ_NO")
	private Long seqNo;//排序值

	@Column(name = "REMARK")
	private String remark;//备注

	public String getBdlUuid() {
		return bdlUuid;
	}

	public void setBdlUuid(String bdlUuid) {
		this.bdlUuid = bdlUuid;
	}
	
	public String getDictCode() {
		return dictCode;
	}

	public void setDictCode(String dictCode) {
		this.dictCode = dictCode;
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
	
	public Long getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Long seqNo) {
		this.seqNo = seqNo;
	}
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}


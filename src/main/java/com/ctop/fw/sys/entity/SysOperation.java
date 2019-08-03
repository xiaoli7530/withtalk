package com.ctop.fw.sys.entity;

import java.io.Serializable;

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
@Table(name = "SYS_OPERATION")
@BatchSize(size = 20)
public class SysOperation extends BaseEntity implements Serializable {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "OPERATION_UUID")
	private String operationUuid;//主键ID

	@Column(name = "OPERATION_CODE")
	private String operationCode;//按钮代码

	@Column(name = "NAME")
	private String name;//类型

	@Column(name = "SEQ_NO")
	private Long seqNo;//序号

	@Column(name = "REMARK")
	private String remark;//备注

	public String getOperationUuid() {
		return operationUuid;
	}

	public void setOperationUuid(String operationUuid) {
		this.operationUuid = operationUuid;
	}
	
	public String getOperationCode() {
		return operationCode;
	}

	public void setOperationCode(String operationCode) {
		this.operationCode = operationCode;
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


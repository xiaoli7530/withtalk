package com.ctop.fw.sys.entity;

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
@Table(name = "SYS_ACCOUNT_WM")
@BatchSize(size = 20)
public class SysAccountWm extends BaseEntity implements Serializable {

	@Column(name = "SEQ_NO")
	private Long seqNo;//排序

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "AW_UUID")
	private String awUuid;//主键UUID

	@Column(name = "ACCOUNT_UUID")
	private String accountUuid;//账号ID

	@Column(name = "WAREHOUSE_UUID")
	private String warehouseUuid;//仓库ID

	public Long getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Long seqNo) {
		this.seqNo = seqNo;
	}
	
	public String getAwUuid() {
		return awUuid;
	}

	public void setAwUuid(String awUuid) {
		this.awUuid = awUuid;
	}
	
	public String getAccountUuid() {
		return accountUuid;
	}

	public void setAccountUuid(String accountUuid) {
		this.accountUuid = accountUuid;
	}
	
	public String getWarehouseUuid() {
		return warehouseUuid;
	}

	public void setWarehouseUuid(String warehouseUuid) {
		this.warehouseUuid = warehouseUuid;
	}
	
}


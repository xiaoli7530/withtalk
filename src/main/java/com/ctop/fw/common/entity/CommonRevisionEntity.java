package com.ctop.fw.common.entity;

import java.io.Serializable;import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;


@Entity
@Table(name = "BASE_COMMON_REV_INFO")
@RevisionEntity( CommonRevisionListener.class )
public class CommonRevisionEntity implements Serializable {
	 
	private static final long serialVersionUID = 5035292144244765270L;

	@Id
	@GeneratedValue
	@RevisionNumber
	@Column(name="REV")
	private int id;

	@RevisionTimestamp
	@Column(name = "REVTSTMP")
	private long timestamp;
	
	@Column(name = "ACCOUNT_UUID")
	private String accountUuid;
	@Column(name = "ACCOUNT_NAME")
	private String accountName;
	@Column(name = "TENANT_UUID")
	private String tenantUuid;
	@Column(name = "WAREHOUSE_UUID")
	private String warehouseUuid;
	@Column(name = "REMOTE_IP")
	private String remoteIp;
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Transient
	public Date getRevisionDate() {
		return new Date( timestamp );
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public boolean equals(Object o) {
		if ( this == o ) {
			return true;
		}
		if ( !(o instanceof CommonRevisionEntity) ) {
			return false;
		}

		final CommonRevisionEntity that = (CommonRevisionEntity) o;
		return id == that.id
				&& timestamp == that.timestamp;
	}

	@Override
	public int hashCode() {
		int result;
		result = id;
		result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));
		return result;
	}

	@Override
	public String toString() {
		return "DefaultRevisionEntity(id = " + id
				+ ", revisionDate = " + DateFormat.getDateTimeInstance().format( getRevisionDate() ) + ")";
	}
	
	public String getAccountUuid() {
		return accountUuid;
	}
	public void setAccountUuid(String accountUuid) {
		this.accountUuid = accountUuid;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getTenantUuid() {
		return tenantUuid;
	}
	public void setTenantUuid(String tenantUuid) {
		this.tenantUuid = tenantUuid;
	}
	public String getWarehouseUuid() {
		return warehouseUuid;
	}
	public void setWarehouseUuid(String warehouseUuid) {
		this.warehouseUuid = warehouseUuid;
	}
	public String getRemoteIp() {
		return remoteIp;
	}
	public void setRemoteIp(String remoteIp) {
		this.remoteIp = remoteIp;
	}
}

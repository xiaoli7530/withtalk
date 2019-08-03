package com.ctop.fw.sys.entity;

import java.util.Date;

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
@Table(name = "SYS_ROLE")
@BatchSize(size = 20)
public class SysRole extends BaseEntity implements Serializable {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ROLE_UUID")
	private String roleUuid;//主键ID

	@Column(name = "NAME")
	private String name;//角色名称

	@Column(name = "ROLE_CODE")
	private String roleCode;//角色代码

	@Column(name = "REMARK")
	private String remark;//备注
	
	@Column(name = "COMPANY_UUID")
	private String companyUuid;
	
	@Column(name = "ROLE_ORIGIN")//角色归属
	private String roleOrigin;
	
	@Column(name = "SQL")//sql-add
	private String sql;
	
	@Column(name = "FROM_TYPE")//来源类型（wms/edv/ps/ppobuild）
	private String fromType;
	

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getRoleOrigin() {
		return roleOrigin;
	}

	public void setRoleOrigin(String roleOrigin) {
		this.roleOrigin = roleOrigin;
	}

	public String getRoleUuid() {
		return roleUuid;
	}

	public void setRoleUuid(String roleUuid) {
		this.roleUuid = roleUuid;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCompanyUuid() {
		return companyUuid;
	}

	public void setCompanyUuid(String companyUuid) {
		this.companyUuid = companyUuid;
	}

	public String getFromType() {
		return fromType;
	}

	public void setFromType(String fromType) {
		this.fromType = fromType;
	}
	
	
}


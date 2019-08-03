package com.ctop.fw.sys.entity;

import java.io.Serializable;import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.GenericGenerator;

import com.ctop.fw.common.entity.BaseEntity;
import com.ctop.fw.common.utils.StringUtil;


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
@Table(name = "SYS_RESOURCE")
@BatchSize(size = 20)
public class SysResource extends BaseEntity implements Serializable {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "RESOURCE_UUID")
	private String resourceUuid;//UUID

	@Column(name = "COMPANY_UUID")
	private String companyUuid;//货主

	@Column(name = "RESOURCE_CODE")
	private String resourceCode;//菜单编号

	@Column(name = "RESOURCE_NAME")
	private String resourceName;//菜单名称

	@Column(name = "RESOURCE_ORIGIN")
	private String resourceOrigin;//资源来源(管理/业务操作,预留)

	@Column(name = "URL")
	private String url;//链接地址

	@Column(name = "IS_SHOW")
	private String isShow;//{label:"是否显示", dict:"YES_NO"}

	@Column(name = "PARENT_UUID")
	private String parentUuid;//父级菜单

	@Column(name = "RESOURCE_LEVEL")
	private String resourceLevel;//资源级别，从0开始

	@Column(name = "TREE_CODE")
	private String treeCode;//树代码

	@Column(name = "TREE_SEQ")
	private Long treeSeq;//树序号

	@Column(name = "REMARK")
	private String remark;//备注

	@Column(name = "IMG")
	private String img;//图标(预留)

        public void buildTreeCode(SysResource parent) {
		String prefix = parent != null ? parent.getTreeCode() : "";
		this.treeCode = prefix + StringUtil.leftPad("" + treeSeq, 3, '0');
	}

	public String getResourceUuid() {
		return resourceUuid;
	}

	public void setResourceUuid(String resourceUuid) {
		this.resourceUuid = resourceUuid;
	}
	
	public String getCompanyUuid() {
		return companyUuid;
	}

	public void setCompanyUuid(String companyUuid) {
		this.companyUuid = companyUuid;
	}
	
	public String getResourceCode() {
		return resourceCode;
	}

	public void setResourceCode(String resourceCode) {
		this.resourceCode = resourceCode;
	}
	
	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	
	public String getResourceOrigin() {
		return resourceOrigin;
	}

	public void setResourceOrigin(String resourceOrigin) {
		this.resourceOrigin = resourceOrigin;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}
	
	public String getParentUuid() {
		return parentUuid;
	}

	public void setParentUuid(String parentUuid) {
		this.parentUuid = parentUuid;
	}
	
	public String getResourceLevel() {
		return resourceLevel;
	}

	public void setResourceLevel(String resourceLevel) {
		this.resourceLevel = resourceLevel;
	}
	
	public String getTreeCode() {
		return treeCode == null ? "" : treeCode;
	}

	public void setTreeCode(String treeCode) {
		this.treeCode = treeCode;
	}
	
	public Long getTreeSeq() {
		return treeSeq;
	}

	public void setTreeSeq(Long treeSeq) {
		this.treeSeq = treeSeq;
	}
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}
	
}


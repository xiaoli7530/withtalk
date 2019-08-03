package com.ctop.fw.sys.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.io.Serializable;import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;

import com.ctop.fw.common.model.BaseDto;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * <pre>
 * 功能说明：${table.className}实体的数据传输对像
 * 创建者：${user}
 * 创建时间：${date}
 * 版本：${version}
 * </pre>
 */
public class SysResourceDto extends BaseDto implements Serializable {
	private String resourceUuid;
	private String companyUuid;
	private String resourceCode;
	private String resourceName;
	private String resourceOrigin;
	private String url;
	private String isShow;
	private String parentUuid;
	private String resourceLevel;
	private String treeCode;
	private Long treeSeq;
	private String remark;
	private String img;
	private String operationUuids;
	private List<String> operationUuidList = new ArrayList<String>();
	
	public List<String> getOperationUuidList() {
		return operationUuidList;
	}

	public void setOperationUuidList(List<String> operationUuidList) {
		this.operationUuidList = operationUuidList;
	}

	public String getOperationUuids() {
		return operationUuids;
	}

	public void setOperationUuids(String operationUuids) {
		this.operationUuids = operationUuids;
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
		return treeCode;
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


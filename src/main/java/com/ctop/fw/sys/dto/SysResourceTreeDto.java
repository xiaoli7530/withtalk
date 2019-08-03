package com.ctop.fw.sys.dto;

import com.ctop.fw.common.model.BaseTreeDto;

public class SysResourceTreeDto extends BaseTreeDto {

	private static final long serialVersionUID = 3151804345615692705L;
	protected String resourceUuid;
	protected String companyUuid;
	protected String resourceCode;
	protected String resourceName;
	protected String resourceOrigin;
	protected String url;
	protected String isShow;
	protected String parentUuid;
	protected String resourceLevel; 
	protected Long treeSeq;
	protected String remark;
	protected String img;
	 
	
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

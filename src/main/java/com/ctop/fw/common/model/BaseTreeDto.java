package com.ctop.fw.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.ctop.fw.common.utils.StringUtil;
import com.ctop.fw.sys.dto.SysResourceDto;

public class BaseTreeDto implements Serializable {
	private static final long serialVersionUID = 7845399417169626874L;
	protected String id;
	protected String parentId;
	protected String text;
	protected String state;
	protected Boolean checked;
	protected String iconCls;
	protected List<BaseTreeDto> children;
	protected String treeCode;
	protected String resourceCode;
	protected String isShow;
	protected String url;
	protected Long treeSeq;
	protected String remark;

	
	public static BaseTreeDto buildFromSysResource(SysResourceDto resource) {
		BaseTreeDto tree = new BaseTreeDto();
		tree.id = resource.getResourceUuid();
		tree.parentId = resource.getParentUuid();
		tree.text = resource.getResourceName();
		tree.treeCode = resource.getTreeCode();
		tree.resourceCode = resource.getResourceCode();
		tree.isShow = resource.getIsShow();
		tree.url = resource.getUrl();
		tree.treeSeq = resource.getTreeSeq();
		tree.remark = resource.getRemark();
		return tree;
	}
	
	public static <T extends BaseTreeDto> List<T> buildCascadeList(List<T> list) {
		List<T> tops = new ArrayList<T>();
		for(T sub : list) {
			sub.children = new ArrayList<BaseTreeDto>();
		}
		for (T sub : list) {
			Optional<T> parent = list.stream().filter(p -> p.id.equals(sub.parentId)).findFirst();
			if (parent.isPresent()) {
				parent.get().addChild(sub);
			} else {
				tops.add(sub);
			}
		}
		for (T item : list) {
			item.state = StringUtil.isEmpty(item.state) ? (item.getChildren() != null && !item.getChildren().isEmpty() ? "closed" : "open") : item.state;
		}
		return tops;
	}

	public void addChild(BaseTreeDto child) {
		if (this.children == null) {
			this.children = new ArrayList<BaseTreeDto>();
		}
		this.children.add(child);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public List<BaseTreeDto> getChildren() {
		return children;
	}

	public void setChildren(List<BaseTreeDto> children) {
		this.children = children;
	}

	public String getTreeCode() {
		return treeCode;
	}

	public void setTreeCode(String treeCode) {
		this.treeCode = treeCode;
	}

	public String getResourceCode() {
		return resourceCode;
	}

	public void setResourceCode(String resourceCode) {
		this.resourceCode = resourceCode;
	}

	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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
	
	
}

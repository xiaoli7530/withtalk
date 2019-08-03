package com.ctop.fw.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.ctop.fw.common.utils.StringUtil;

public class ComboTreeDto implements Serializable {
	private static final long serialVersionUID = 7845399417169626874L;
	protected String id;
	protected String parentId;
	protected String text;
	protected String state;
	protected Boolean checked;
	protected String iconCls;
	protected List<ComboTreeDto> children;

	public static <T extends ComboTreeDto> List<T> buildCascadeList(List<T> list) {
		List<T> tops = new ArrayList<T>();
		for(T sub : list) {
			sub.children = new ArrayList<ComboTreeDto>();
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

	public void addChild(ComboTreeDto child) {
		if (this.children == null) {
			this.children = new ArrayList<ComboTreeDto>();
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

	public List<ComboTreeDto> getChildren() {
		return children;
	}

	public void setChildren(List<ComboTreeDto> children) {
		this.children = children;
	}
	
	
}

package com.ctop.fw.common.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.ctop.fw.common.utils.StringUtil;
import com.ctop.fw.hr.dto.HrDepartmentDto;

public class DepartmentTreeDto {
	private static final long serialVersionUID = 7845399417169626874L;
	protected String id;
	protected Long seqNo;
	protected String parentid;
	protected String departmentNo;
	protected String text;
	protected String companyUuid;
	protected String orgPhone;
	protected String orgManager;
	protected String address;
	protected String remark;
	protected String state;
	private Long empNum;
	protected List<DepartmentTreeDto> children;
	
	public static DepartmentTreeDto buildFromDepartment(HrDepartmentDto deparment) {
		DepartmentTreeDto tree = new DepartmentTreeDto();
		tree.id = deparment.getDepartmentUuid();
		tree.seqNo = deparment.getSeqNo();
		tree.parentid = deparment.getParentUuid();
		tree.departmentNo = deparment.getDepartmentNo();
		tree.text = deparment.getDepartmentName();
		tree.companyUuid = deparment.getCompanyUuid();
		tree.orgPhone = deparment.getOrgPhone();
		tree.orgManager = deparment.getOrgManager();
		tree.address = deparment.getAddress();
		tree.remark = deparment.getRemark();
		tree.empNum = deparment.getEmpNum();
		return tree;
	}

	public static <T extends DepartmentTreeDto> List<T> buildCascadeList(List<T> list) {
		List<T> tops = new ArrayList<T>();
		for(T sub : list) {
			sub.children = new ArrayList<DepartmentTreeDto>();
		}
		for (T sub : list) {
			Optional<T> parent = list.stream().filter(p -> p.id.equals(sub.parentid)).findFirst();
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

	public void addChild(DepartmentTreeDto child) {
		if (this.children == null) {
			this.children = new ArrayList<DepartmentTreeDto>();
		}
		this.children.add(child);
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Long getSeqNo() {
		return seqNo;
	}
	public void setSeqNo(Long seqNo) {
		this.seqNo = seqNo;
	}
	
	public String getParentid() {
		return parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	public String getDepartmentNo() {
		return departmentNo;
	}
	public void setDepartmentNo(String departmentNo) {
		this.departmentNo = departmentNo;
	}

	public String getCompanyUuid() {
		return companyUuid;
	}
	public void setCompanyUuid(String companyUuid) {
		this.companyUuid = companyUuid;
	}
	public String getOrgPhone() {
		return orgPhone;
	}
	public void setOrgPhone(String orgPhone) {
		this.orgPhone = orgPhone;
	}
	public String getOrgManager() {
		return orgManager;
	}
	public void setOrgManager(String orgManager) {
		this.orgManager = orgManager;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public List<DepartmentTreeDto> getChildren() {
		return children;
	}

	public void setChildren(List<DepartmentTreeDto> children) {
		this.children = children;
	}

	public Long getEmpNum() {
		return empNum;
	}

	public void setEmpNum(Long empNum) {
		this.empNum = empNum;
	}
	
	
}

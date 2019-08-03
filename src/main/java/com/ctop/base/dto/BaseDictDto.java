package com.ctop.base.dto;

import java.io.Serializable;
import java.util.List;

import com.ctop.fw.common.model.BaseDto;
import com.fasterxml.jackson.annotation.JsonView;

/**
 * <pre>
 * 功能说明：${table.className}实体的数据传输对像
 * 创建者：${user}
 * 创建时间：${date}
 * 版本：${version}
 * </pre>
 */
public class BaseDictDto extends BaseDto implements Serializable {
	
	public static interface SimplifyView {}
	
	@JsonView(SimplifyView.class)
	private String dictUuid;
	@JsonView(SimplifyView.class)
	private String dictCode;
	@JsonView(SimplifyView.class)
	private String dictName;
	@JsonView(SimplifyView.class)
	private String type;
	private String remark;
	@JsonView(SimplifyView.class)
	private String editAble;

	@JsonView(SimplifyView.class)
	private List<BaseDictDetailDto> details;

	@JsonView(SimplifyView.class)
	private BaseDictTableDto baseDictTable;

	
	public String getDictUuid() {
		return dictUuid;
	}

	public void setDictUuid(String dictUuid) {
		this.dictUuid = dictUuid;
	}
	
	public String getDictCode() {
		return dictCode;
	}

	public void setDictCode(String dictCode) {
		this.dictCode = dictCode;
	}
	
	public String getDictName() {
		return dictName;
	}

	public void setDictName(String dictName) {
		this.dictName = dictName;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public String getEditAble() {
		return editAble;
	}

	public void setEditAble(String editAble) {
		this.editAble = editAble;
	}

	public List<BaseDictDetailDto> getDetails() {
		return details;
	}

	public void setDetails(List<BaseDictDetailDto> details) {
		this.details = details;
	}

	public BaseDictTableDto getBaseDictTable() {
		return baseDictTable;
	}

	public void setBaseDictTable(BaseDictTableDto baseDictTable) {
		this.baseDictTable = baseDictTable;
	}

}


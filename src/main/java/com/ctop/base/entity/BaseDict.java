package com.ctop.base.entity;

import java.util.Date;

import java.io.Serializable;import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat;

import com.ctop.fw.common.entity.BaseEntity;
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
@Table(name = "BASE_DICT")
@BatchSize(size = 20)
public class BaseDict extends BaseEntity implements Serializable {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "DICT_UUID")
	private String dictUuid;//UUID

	@Column(name = "DICT_CODE")
	private String dictCode;//字典代码

	@Column(name = "DICT_NAME")
	private String dictName;//字典名称

	@Column(name = "TYPE")
	private String type;//{label:"类型(列表/表关联)", dict:"DICTIONARY_TYPE"}

	@Column(name = "REMARK")
	private String remark;//备注

	@Column(name = "EDIT_ABLE")
	private String editAble;//是否开放维护(Y/N)

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
	
}


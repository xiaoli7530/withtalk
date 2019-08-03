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
@Table(name = "BASE_PINYIN")
@BatchSize(size = 20)
public class BasePinyin extends BaseEntity implements Serializable {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "CHS")
	private String chs;//汉字

	@Column(name = "PY")
	private String py;//拼音

	@Column(name = "FPY")
	private String fpy;//首字母
	

	public char getFirstChar() {
		return chs != null ? chs.charAt(0) : '\0';
	}
	

	public String getChs() {
		return chs;
	}

	public void setChs(String chs) {
		this.chs = chs;
	}
	
	public String getPy() {
		return py;
	}

	public void setPy(String py) {
		this.py = py;
	}
	
	public String getFpy() {
		return fpy;
	}

	public void setFpy(String fpy) {
		this.fpy = fpy;
	}
	
}


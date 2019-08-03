package com.ctop.fw.sys.dto;

import java.util.Date;
import java.io.Serializable;import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;

import com.ctop.fw.common.model.BaseDto;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * <pre>
 * 功能说明：SysExcelImport实体的数据传输对像
 * 创建者：${user}
 * 创建时间：${date}
 * 版本：${version}
 * </pre>
 */
public class SysExcelImportDto extends BaseDto implements Serializable {
	private Long startRowIndex;
	private String targetTable;
	private String tempTable;
	private String processScript;
	private String transferScript;
	private Long pageSize;
	private Long nameRowIndex;
	private String importType;
	private String ignoreRowMode;
	private Long seq;
	private String tplUrl;
	private String module;
	private String updatable;
	private String updatableDefault;
	private String importUuid;
	private String importName;

	
	public Long getStartRowIndex() {
		return startRowIndex;
	}

	public void setStartRowIndex(Long startRowIndex) {
		this.startRowIndex = startRowIndex;
	}
	
	public String getTargetTable() {
		return targetTable;
	}

	public void setTargetTable(String targetTable) {
		this.targetTable = targetTable;
	}
	
	public String getTempTable() {
		return tempTable;
	}

	public void setTempTable(String tempTable) {
		this.tempTable = tempTable;
	}
	
	public String getProcessScript() {
		return processScript;
	}

	public void setProcessScript(String processScript) {
		this.processScript = processScript;
	}
	
	public String getTransferScript() {
		return transferScript;
	}

	public void setTransferScript(String transferScript) {
		this.transferScript = transferScript;
	}
	
	public Long getPageSize() {
		return pageSize;
	}

	public void setPageSize(Long pageSize) {
		this.pageSize = pageSize;
	}
	
	public Long getNameRowIndex() {
		return nameRowIndex;
	}

	public void setNameRowIndex(Long nameRowIndex) {
		this.nameRowIndex = nameRowIndex;
	}
	
	public String getImportType() {
		return importType;
	}

	public void setImportType(String importType) {
		this.importType = importType;
	}
	
	public String getIgnoreRowMode() {
		return ignoreRowMode;
	}

	public void setIgnoreRowMode(String ignoreRowMode) {
		this.ignoreRowMode = ignoreRowMode;
	}
	
	public Long getSeq() {
		return seq;
	}

	public void setSeq(Long seq) {
		this.seq = seq;
	}
	
	public String getTplUrl() {
		return tplUrl;
	}

	public void setTplUrl(String tplUrl) {
		this.tplUrl = tplUrl;
	}
	
	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}
	
	public String getUpdatable() {
		return updatable;
	}

	public void setUpdatable(String updatable) {
		this.updatable = updatable;
	}
	
	public String getUpdatableDefault() {
		return updatableDefault;
	}

	public void setUpdatableDefault(String updatableDefault) {
		this.updatableDefault = updatableDefault;
	}
	
	public String getImportUuid() {
		return importUuid;
	}

	public void setImportUuid(String importUuid) {
		this.importUuid = importUuid;
	}
	
	public String getImportName() {
		return importName;
	}

	public void setImportName(String importName) {
		this.importName = importName;
	}

}


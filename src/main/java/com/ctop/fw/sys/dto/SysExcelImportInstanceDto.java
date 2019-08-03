package com.ctop.fw.sys.dto;

import java.io.Serializable;import java.math.BigDecimal;
import java.util.Map;

import javax.persistence.Transient;

import com.ctop.fw.common.model.BaseDto;
import com.ctop.fw.sys.entity.SysExcelImport;

/**
 * <pre>
 * 功能说明：SysExcelImportInstance实体的数据传输对像
 * 创建者：${user}
 * 创建时间：${date}
 * 版本：${version}
 * </pre>
 */
public class SysExcelImportInstanceDto extends BaseDto implements Serializable {
	private String instanceUuid;
	private String companyUuid;
	private String accountUuid;
	private String importUuid;
	private String refType;
	private String refUuid;
	private String message;
	private String filePath;
	private String sheetName;
	private String stage;
	private String columnIndex;
	private Long nameRowIndex;
	private Long startRowIndex;
	private Long pageSize;
	private Long importedNum;
	private Long importedRealNum;
	private Long invalidNum;
	private String fileName;
	private String allColMatched;
	private String basicValid;
	private String advanceValid;
	private String transfered;
	private String tempTable;
	private String updateExist;

	private SysExcelImport excelImport;
	private Map<String, Object> paramsMap;

	
	public String getInstanceUuid() {
		return instanceUuid;
	}

	public void setInstanceUuid(String instanceUuid) {
		this.instanceUuid = instanceUuid;
	}
	
	public String getCompanyUuid() {
		return companyUuid;
	}

	public void setCompanyUuid(String companyUuid) {
		this.companyUuid = companyUuid;
	}
	
	public String getAccountUuid() {
		return accountUuid;
	}

	public void setAccountUuid(String accountUuid) {
		this.accountUuid = accountUuid;
	}
	
	public String getImportUuid() {
		return importUuid;
	}

	public void setImportUuid(String importUuid) {
		this.importUuid = importUuid;
	}
	
	public String getRefType() {
		return refType;
	}

	public void setRefType(String refType) {
		this.refType = refType;
	}
	
	public String getRefUuid() {
		return refUuid;
	}

	public void setRefUuid(String refUuid) {
		this.refUuid = refUuid;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}
	
	public String getStage() {
		return stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}
	
	public String getColumnIndex() {
		return columnIndex;
	}

	public void setColumnIndex(String columnIndex) {
		this.columnIndex = columnIndex;
	}
	
	public Long getNameRowIndex() {
		return nameRowIndex;
	}

	public void setNameRowIndex(Long nameRowIndex) {
		this.nameRowIndex = nameRowIndex;
	}
	
	public Long getStartRowIndex() {
		return startRowIndex;
	}

	public void setStartRowIndex(Long startRowIndex) {
		this.startRowIndex = startRowIndex;
	}
	
	public Long getPageSize() {
		return pageSize;
	}

	public void setPageSize(Long pageSize) {
		this.pageSize = pageSize;
	}
	
	public Long getImportedNum() {
		return importedNum;
	}

	public void setImportedNum(Long importedNum) {
		this.importedNum = importedNum;
	}
	
	public Long getImportedRealNum() {
		return importedRealNum;
	}

	public void setImportedRealNum(Long importedRealNum) {
		this.importedRealNum = importedRealNum;
	}
	
	public Long getInvalidNum() {
		return invalidNum;
	}

	public void setInvalidNum(Long invalidNum) {
		this.invalidNum = invalidNum;
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getAllColMatched() {
		return allColMatched;
	}

	public void setAllColMatched(String allColMatched) {
		this.allColMatched = allColMatched;
	}
	
	public String getBasicValid() {
		return basicValid;
	}

	public void setBasicValid(String basicValid) {
		this.basicValid = basicValid;
	}
	
	public String getAdvanceValid() {
		return advanceValid;
	}

	public void setAdvanceValid(String advanceValid) {
		this.advanceValid = advanceValid;
	}
	
	public String getTransfered() {
		return transfered;
	}

	public void setTransfered(String transfered) {
		this.transfered = transfered;
	}
	
	public String getTempTable() {
		return tempTable;
	}

	public void setTempTable(String tempTable) {
		this.tempTable = tempTable;
	}
	
	public String getUpdateExist() {
		return updateExist;
	}

	public void setUpdateExist(String updateExist) {
		this.updateExist = updateExist;
	}

	public SysExcelImport getExcelImport() {
		return excelImport;
	}

	public void setExcelImport(SysExcelImport excelImport) {
		this.excelImport = excelImport;
	}

	public Map<String, Object> getParamsMap() {
		return paramsMap;
	}

	public void setParamsMap(Map<String, Object> paramsMap) {
		this.paramsMap = paramsMap;
	}

}


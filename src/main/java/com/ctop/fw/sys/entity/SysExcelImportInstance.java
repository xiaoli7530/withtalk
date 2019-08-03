package com.ctop.fw.sys.entity;

import java.io.Serializable;import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.GenericGenerator;

import com.ctop.core.pdf.parser.TableColumn;
import com.ctop.core.pdf.parser.TableExtractor;
import com.ctop.fw.common.entity.BaseEntity;
import com.ctop.fw.common.utils.ListUtil;
import com.ctop.fw.sys.excelImport.support.ExcelImportRow;
import com.ctop.fw.sys.excelImport.support.ImportDataPageIterator;


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
@Table(name = "SYS_EXCEL_IMPORT_INSTANCE")
@BatchSize(size = 20)
public class SysExcelImportInstance extends BaseEntity implements Serializable {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "INSTANCE_UUID")
	private String instanceUuid;//主键

	@Column(name = "COMPANY_UUID")
	private String companyUuid;//货主UUID

	@Column(name = "ACCOUNT_UUID")
	private String accountUuid;//用户UUID

	@Column(name = "IMPORT_UUID")
	private String importUuid;//excel导入配置UUID

	@Column(name = "REF_TYPE")
	private String refType;//引用的类型

	@Column(name = "REF_UUID")
	private String refUuid;//引用的表的主键

	@Column(name = "MESSAGE")
	private String message;//上载导入时的校验消息

	@Column(name = "FILE_PATH")
	private String filePath;//上载的excel文件路径

	@Column(name = "SHEET_NAME")
	private String sheetName;//要导入的sheet的名称

	@Column(name = "STAGE")
	private String stage;//导入步骤

	@Column(name = "COLUMN_INDEX")
	private String columnIndex;//临时表的列在EXCEL中的顺序，按column1Uuid,index1,column2Uuid, index2的方式保存

	@Column(name = "NAME_ROW_INDEX")
	private Long nameRowIndex;//excel中标题行序号

	@Column(name = "START_ROW_INDEX")
	private Long startRowIndex;//数据开始行序号

	@Column(name = "PAGE_SIZE")
	private Long pageSize;//页大小

	@Column(name = "IMPORTED_NUM")
	private Long importedNum;//导入的数量

	@Column(name = "IMPORTED_REAL_NUM")
	private Long importedRealNum;//真正的数量，excel一条可拆成多条

	@Column(name = "INVALID_NUM")
	private Long invalidNum;//校验未通过的数量

	@Column(name = "FILE_NAME")
	private String fileName;//上载的文件名

	@Column(name = "ALL_COL_MATCHED")
	private String allColMatched;//excel中所有列都能匹配上

	@Column(name = "BASIC_VALID")
	private String basicValid;//基本校验是否通过

	@Column(name = "ADVANCE_VALID")
	private String advanceValid;//高级校验（SQL）是否通过

	@Column(name = "TRANSFERED")
	private String transfered;//是否导入正式表

	@Column(name = "TEMP_TABLE")
	private String tempTable;//临时表

	@Column(name = "UPDATE_EXIST")
	private String updateExist;//是否更新已存在数据
	
	@Column(name = "EXT1")
	private String ext1;
	
	@Column(name = "EXT2")
	private String ext2;
	
	@Column(name = "EXT3")
	private String ext3;
	
	@Column(name = "EXT4")
	private String ext4;
	
	@Column(name = "EXT5")
	private String ext5;
	
	@Column(name = "EXT6")
	private String ext6;
	
	@Column(name = "EXT7")
	private String ext7;
	
	@Column(name = "EXT8")
	private String ext8;
	
	@Column(name = "EXT9")
	private String ext9;
	
	@Column(name = "EXT10")
	private String ext10;
	
	@Transient
	private SysExcelImport excelImport;
	/**校验和transfer时的params在调用后放入该属性中；*/
	@Transient
	private Map<String, Object> paramsMap;

	public void setExcelImport(SysExcelImport excelImport) {
		if(excelImport == null) {
			return;
		}
		this.excelImport = excelImport;
		this.importUuid = excelImport.getImportUuid();
		this.nameRowIndex = getFirstNotNull(this.nameRowIndex, excelImport.getNameRowIndex(), new Long(0));
		this.startRowIndex = getFirstNotNull(this.startRowIndex, excelImport.getStartRowIndex(), 1L);
		this.pageSize = getFirstNotNull(this.pageSize != null && this.pageSize > 0 ? this.pageSize : null, excelImport.getPageSize(), 10L);
		this.tempTable = excelImport.getTempTable();
		if(!"Y".equals(excelImport.getUpdatable())) {
			this.updateExist = "N";
		} else {
			this.updateExist = this.updateExist == null || "".equals(this.updateExist) ? excelImport.getUpdatableDefault() : this.updateExist;
		}
	}

	private <T> T getFirstNotNull(T... args) {
		for(T item : args) {
			if(item != null) {
				return item;
			}
		}
		return null;
	}
	
	/**将excel中的列名与模板中的列名匹配，记录匹配的列号，及未匹配的列名*/
	private void processColumnNameIndex(Sheet sheet) {
		Row titleRow = sheet.getRow(this.nameRowIndex.intValue());
		int minNum = titleRow.getFirstCellNum();
		int maxNum = titleRow.getLastCellNum();
		List<String> notMatched = new ArrayList<String>();
		StringBuilder columnIndex = new StringBuilder();
		for(int i=minNum; i<maxNum; i++) {
			Cell cell = titleRow.getCell(i);
			if(cell == null) {
				continue;
			}
			String title = cell.getStringCellValue();
			title = title != null ? title.trim() : "";
			title = title.replace("\n", "");
			SysExcelImportColumn matched = excelImport.markColumnIndexByColumnName(title, i);
			if(matched == null) {
				notMatched.add(title);
			} else {
				if(columnIndex.length() > 0) {
					columnIndex.append(",");
				}
				columnIndex.append(matched.getTempColumn()).append(",").append(title).append(",").append(i);
			}
		}
		this.columnIndex = columnIndex.toString();
		this.recordUnmatchedColumnInExcel(notMatched);
	}
	
	private void processColumnNameIndex(List<TableColumn> gridColumns) {
		int i=0;
		List<String> notMatched = new ArrayList<String>();
		StringBuilder columnIndex = new StringBuilder();
		for(TableColumn gc : gridColumns) {
			String title = gc.getTitlesText();
			title = title != null ? title.trim() : "";
			SysExcelImportColumn matched = excelImport.markColumnIndexByColumnName(gc, i);
			if(matched == null) {
				notMatched.add(title);
			} else {
				if(columnIndex.length() > 0) {
					columnIndex.append(",");
				}
				columnIndex.append(matched.getTempColumn()).append(",").append(title).append(",").append(i);
			}
			i++;
		}
		this.columnIndex = columnIndex.toString();
		this.recordUnmatchedColumnInExcel(notMatched);
	} 
	
	public DataIterable buildExceTableIterable(Workbook wb) {
		return new ExcelTableDataIterable(wb);
	}
	
	public DataIterable buildPdfTableIterable(TableExtractor extractor) {
		return new PdfTableDataIterable(extractor);
	}
	
	public static interface DataIterable {
		public Iterator<List<ExcelImportRow>> iterate(SysExcelImportInstance instance);
	}
	
	public static class ExcelTableDataIterable implements DataIterable {
		private Workbook wb; 
		
		public ExcelTableDataIterable(Workbook wb) {
			this.wb = wb;
		}
 
		public Iterator<List<ExcelImportRow>> iterate(SysExcelImportInstance instance) {
			instance.excelImport.buildColumnValidator(); 
			instance.importedNum = 0L;
			instance.importedRealNum = 0L;
			instance.invalidNum = 0L;
			instance.basicValid = "Y";
			instance.advanceValid = null;
			instance.transfered = "N";
			instance.stage = "to_temp_table";
			Sheet sheet = wb.getSheet(instance.sheetName);
			instance.processColumnNameIndex(sheet); 
			return new ImportDataPageIterator(sheet, instance);
		}
	}
	
	public static class PdfTableDataIterable implements DataIterable {
		private TableExtractor tableExtractor; 
		
		public PdfTableDataIterable(TableExtractor extractor) {
			this.tableExtractor = extractor;
		}
 
		public Iterator<List<ExcelImportRow>> iterate(SysExcelImportInstance instance) {
			SysExcelImport excelImport = instance.getExcelImport();
			excelImport.buildColumnValidator(); 
			instance.importedNum = 0L;
			instance.importedRealNum = 0L;
			instance.invalidNum = 0L;
			instance.basicValid = "Y";
			instance.advanceValid = null;
			instance.transfered = "N";
			instance.stage = "to_temp_table"; 
			instance.processColumnNameIndex(tableExtractor.getColumns()); 
			List<ExcelImportRow> rows = tableExtractor.readAccordingToSysExcelColumns(instance.excelImport.getColumns());
			
			instance.setImportedRealNum(instance.getImportedRealNum() + rows.size());
			
			//校验数据
			boolean valid = true;
			for(ExcelImportRow row : rows) {
				row.setInstanceUuid(instance.getInstanceUuid());
				if(!excelImport.validateRowData(row)) {
					instance.setInvalidNum(instance.getInvalidNum() + 1);
					valid = false;
				} 
			}
			if(!"Y".equals(instance.getBasicValid()) && !valid) {
				instance.setBasicValid("N");
				instance.appendMessage("基本校验未通过，请在明细中查看; ");
			}
			return Arrays.asList(rows).iterator();
		}
	}
	 
	
	public void appendMessage(String message) { 
		DateFormat format = new SimpleDateFormat("yyyyMMdd HH:mm:ss ");
		String time = format.format(new Date());
		if(this.message == null) {
			this.message = time + message + "\n";
		} else { 
			this.message += (time + message + "\n");
		}
		
	}
	
	private void recordUnmatchedColumnInExcel(List<String> unmatcheds) {
		if(unmatcheds.isEmpty()) {
			return;
		}
		String str = ListUtil.join(unmatcheds, ",");
		if(this.message != null) {
			this.message += " ";
		} else {
			this.message = "";
		}
		this.message = this.message + ("导入文件中的列" + str + "在导入模板中未定义.\n");
	}
	
//	public ExcelBuilder buildExcelBuilder() {
//		ExcelBuilder builder = new ExcelBuilder();
//		builder.appendExcelColumn("原行号", "IMPORT_ROW_NUM");
//		builder.appendExcelColumn("校验信息", "IMPORT_MESSAGE");
//		if(this.columnIndex != null) {
//			String[] arr = this.columnIndex.split("\\s*,\\s*");
//			for(int i=0; i<arr.length/3; i++) {
//				String tempColumn = arr[3*i];
//				SysExcelImportColumn column = excelImport.getColumnByTempColumn(tempColumn);
//				if(column == null) {
//					continue;
//				}
//				builder.appendExcelColumn(arr[3*i+1], tempColumn);
//			}
//		}
//		builder.createSheet(null);
//		builder.buildTitleRows();
//		return builder;
//	}
	

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
	
	public String getExt1() {
		return ext1;
	}

	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}

	public String getExt2() {
		return ext2;
	}

	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}

	public String getExt3() {
		return ext3;
	}

	public void setExt3(String ext3) {
		this.ext3 = ext3;
	}

	public String getExt4() {
		return ext4;
	}

	public void setExt4(String ext4) {
		this.ext4 = ext4;
	}

	public String getExt5() {
		return ext5;
	}

	public void setExt5(String ext5) {
		this.ext5 = ext5;
	}

	public String getExt6() {
		return ext6;
	}

	public void setExt6(String ext6) {
		this.ext6 = ext6;
	}

	public String getExt7() {
		return ext7;
	}

	public void setExt7(String ext7) {
		this.ext7 = ext7;
	}

	public String getExt8() {
		return ext8;
	}

	public void setExt8(String ext8) {
		this.ext8 = ext8;
	}

	public String getExt9() {
		return ext9;
	}

	public void setExt9(String ext9) {
		this.ext9 = ext9;
	}

	public String getExt10() {
		return ext10;
	}

	public void setExt10(String ext10) {
		this.ext10 = ext10;
	}

	public SysExcelImport getExcelImport() {
		return excelImport;
	}

	public Map<String, Object> getParamsMap() {
		return paramsMap;
	}

	public void setParamsMap(Map<String, Object> paramsMap) {
		this.paramsMap = paramsMap;
	}
	
}


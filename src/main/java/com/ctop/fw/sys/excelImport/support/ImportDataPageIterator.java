package com.ctop.fw.sys.excelImport.support;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.ctop.fw.sys.entity.SysExcelImport;
import com.ctop.fw.sys.entity.SysExcelImportInstance;

public class ImportDataPageIterator implements Iterator<List<ExcelImportRow>> {
	private SysExcelImport excelImport;
	private Row row;  
	private Iterator<Row> it;
	private boolean requiredExpand = false;
	private boolean requiredCheckIgnoreRow = false;
	private SysExcelImportInstance instance;
	
	
	public ImportDataPageIterator(Sheet sheet, SysExcelImportInstance instance) {
		this.excelImport = instance.getExcelImport();
		this.requiredExpand = excelImport.isExpandRowRequired();
		this.requiredCheckIgnoreRow = excelImport.isCheckIgnoreRowRequired();
		this.it = sheet.iterator();
		Long startRowIndex = excelImport.getStartRowIndex();
		//定位到要读的第一行之前
		if(startRowIndex == null || startRowIndex <= 0) {
			return;
		}
		long preIndex = startRowIndex - 1;
		this.instance = instance;
		//一直移动cursor直到数据行前一行
		while(it.hasNext() && it.next().getRowNum() != preIndex) {}
	}

	public boolean hasNext() {
		return it.hasNext();
	}

	public List<ExcelImportRow> next() {  
		int pageSize = excelImport.getPageSize() != null ? excelImport.getPageSize().intValue() : 1000;
		List<ExcelImportRow> rows = new ArrayList<ExcelImportRow>(pageSize);
		//读数据
		if(row != null) {
			rows.add(excelImport.readExcelRow(row));
			row = null;
		}
		int i=1;
		while(it.hasNext()) {
			rows.add(excelImport.readExcelRow(it.next()));
			if((++i) == pageSize) {
				break;
			}
		}
		instance.setImportedNum(instance.getImportedNum() +  rows.size());
		//需要校验是否有空白行
		int ignoreRowNum = 0;
		if(requiredCheckIgnoreRow) {
			Iterator<ExcelImportRow> it = rows.iterator();
			while(it.hasNext()) {
				ExcelImportRow rowData = it.next();
				if(excelImport.shouldIgnoreRowData(rowData)) {
					it.remove(); 
					ignoreRowNum = 0;
				}
			} 
			if(ignoreRowNum > 0) {
				instance.appendMessage(ignoreRowNum + "行空白行数据已识别，未导入。");
			}
		}
		//一行扩展到多行
		if(this.requiredExpand) {
			Iterator<ExcelImportRow> it = rows.iterator();
			List<ExcelImportRow> tempList = new ArrayList<ExcelImportRow>();
			while(it.hasNext()) {
				ExcelImportRow row = it.next();
				List<ExcelImportRow> subRows = excelImport.expandMultiRow(row);
				if(subRows.size() > 1) {
					it.remove();
					tempList.addAll(subRows);
				}
			}
			rows.addAll(tempList);
		}
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
		return rows;
	}

	public void remove() {
	}
	
}

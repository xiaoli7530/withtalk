package com.ctop.fw.common.excelexport;

import java.io.InputStream;

import org.apache.poi.ss.usermodel.Workbook;

public class ExcelBuilder {
	
	private Workbook wb;

	public ExcelBuilder(InputStream is) {
		this.wb = ExcelUtil.readExcelTemplate4Export(is);
	}
	
	
}

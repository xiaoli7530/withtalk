package com.ctop.fw.sys.excelImport.support;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.ctop.fw.common.utils.BusinessException;

public class ExcelUtil {
 
	public static Workbook createWorkBook(String filePath) {
		File file = new File(filePath);
		try {
			return WorkbookFactory.create(file);
		} catch (EncryptedDocumentException | InvalidFormatException | IOException e) {
			throw BusinessException.template("");
		}
	}
	 
	public static void close(Workbook wb) {
		try {
			wb.close();
		} catch (IOException e) {
			//
		}
	}
	
	 
	public static List<String> getAllSheetNames(Workbook workbook) {
		int num = workbook.getNumberOfSheets();
		List<String> list = new ArrayList<String>(num);
		for(int i=0; i<num; i++) {
			list.add(workbook.getSheetName(i));
		}
		return list;
	}
}
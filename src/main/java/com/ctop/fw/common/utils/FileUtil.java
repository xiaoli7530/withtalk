package com.ctop.fw.common.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import com.itextpdf.io.IOException;

public class FileUtil {

	/**
	 * 写文本文件
	 * @param dirs
	 * @param fileName
	 * @param content
	 */
	public static boolean writeTextFile(String dirs, String fileName, String content) {
		if(dirs == null || "".equals(dirs.trim()) 
				|| fileName == null || "".equals(fileName.trim())) {
			return false;
		}
		File file = new File(dirs);
		if(!file.exists()) {
			file.mkdirs();	
		}
		
		String path = dirs + fileName;
		try {
			OutputStreamWriter out = new OutputStreamWriter( new FileOutputStream(path),"UTF-8");
			out.write(content);
			out.flush();
			out.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (java.io.IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
}

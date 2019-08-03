package com.ctop.fw.common.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;

/**
 * 
 * @author Administrator
 *
 */
public class ZipCompressor {

	public static void compress(OutputStream os, Map<String, File> fileNameMap) throws IOException {
		ZipOutputStream zos = new ZipOutputStream(os);
		int count, bufferLen = 1024;
		byte data[] = new byte[bufferLen];

		for (Map.Entry<String, File> mapEntry : fileNameMap.entrySet()) {
			String fileName = mapEntry.getKey();
			File file = mapEntry.getValue();
			if (file.isDirectory()) {
				throw new BusinessException("不支持文件夹压缩!");
			}
			if (!file.exists()) {
				throw new BusinessException("无效的文件!" + file.getAbsolutePath());
			}
			ZipEntry entry = new ZipEntry(fileName);
			zos.putNextEntry(entry);
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			while ((count = bis.read(data, 0, bufferLen)) != -1) {
				zos.write(data, 0, count);
			}
			bis.close();
		}
		zos.closeEntry();
		zos.close();
	}
	public static void compressByStream(OutputStream os, Map<String, InputStream> fileNameMap) throws IOException {
		ZipOutputStream zos = new ZipOutputStream(os);

		for (Map.Entry<String, InputStream> mapEntry : fileNameMap.entrySet()) {
			String fileName = mapEntry.getKey();
			
			ZipEntry entry = new ZipEntry(fileName);
			zos.putNextEntry(entry);
			//BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			IOUtils.copy(mapEntry.getValue(), zos);
			mapEntry.getValue().close();
		}
		zos.closeEntry();
		zos.close();
	}
}
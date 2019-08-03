package com.ctop.fw.common.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Map;

import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.pdf.BaseFont;

public class PdfUtil {
	
	
	public static void main(String[] args) throws FileNotFoundException {
		PdfUtil pdfUtil = new PdfUtil();
		FileOutputStream os = new FileOutputStream(new File("d://pdf1.pdf"));
		pdfUtil.generatePdfWriter(null, os, "inventory_out.tpl");
	}

	public static String getClassPath() {
		URL url = PdfUtil.class.getClassLoader().getResource("");
		System.out.println("url------------" + url.getPath());
		return url.getPath();
	}

	public static void generatePdfWriter(Map context, OutputStream os,
			String tplFileName) {
		try {
			System.out.println("----------------------content begin");
			ITextRenderer renderer = new ITextRenderer();
			ITextFontResolver fontResolver = renderer.getFontResolver();
			// 修改为当前classpath的目录
			fontResolver.addFont(getClassPath() + "ARIALUNI.TTF",
					BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
			// 生成文件内容
			String content = FreemarkerUtil.getInstance(getClassPath())
					.process(context, tplFileName);
			 //content = StringUtils.replace(content, "&", "&amp;");
			//content = StringUtils.replace(content, "&nbsp;", "&#160;");
			// content = StringUtils.replace(content, "<", "&lt;");
			// content = StringUtils.replace(content, ">", "&gt;");
			// content = StringUtils.replace(content, "\"", "&quot;");
			// content = StringUtils.replace(content, "'", "&apos;");
			renderer.setDocumentFromString(content);
			renderer.layout();
			renderer.createPDF(os);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != os) {
				try {
					os.flush();
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}

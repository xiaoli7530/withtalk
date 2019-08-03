package com.ctop.core.pdf.parser;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import com.ctop.fw.common.utils.BusinessException;
import com.ctop.fw.common.utils.StringUtil;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.geom.Vector;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfCanvasProcessor;
import com.itextpdf.kernel.pdf.canvas.parser.listener.LocationTextExtractionStrategy;
import com.itextpdf.kernel.pdf.canvas.parser.listener.LocationTextExtractionStrategy.ITextChunkLocation;
import com.itextpdf.kernel.pdf.canvas.parser.listener.LocationTextExtractionStrategy.TextChunk;

/**
 * PDF文本处理程序；
 * 可提供根据文本位置读取文本的方法；
 * 根据列表的列位置范围取列表的方法；
 * @author 龚建军
 *
 */
public class PdfTextChunkExtractor implements Iterable<List<TextChunk>>{
	static Logger logger = LoggerFactory.getLogger(PdfTextChunkExtractor.class); 
	private List<List<TextChunk>> allTextChunkList;
	
	public PdfTextChunkExtractor(List<TextChunk> textChunkList) {
		this.allTextChunkList = Arrays.asList(textChunkList);
	}
	 
	
	public PdfTextChunkExtractor(InputStream is) {
		PdfReader pdfReader;
		try {
			pdfReader = new PdfReader(is);
		} catch (Exception e) {
			throw new BusinessException(e, "无法读取PDF文件, 请确认是否为PDF文件!", new Object[]{});
		}
		PdfDocument pdf = new PdfDocument(pdfReader);
		this.buildTextChunk4AllPages(pdf);
		pdf.close();
	}
	 
	
	@SuppressWarnings("unchecked")
	private void buildTextChunk4AllPages(PdfDocument pdf) {
		Field field = ReflectionUtils.findField(LocationTextExtractionStrategy.class, "locationalResult");
		field.setAccessible(true);
		
		LocationTextExtractionStrategy listener = new LocationTextExtractionStrategy();
		PdfCanvasProcessor parser = new PdfCanvasProcessor(listener); 
		
		allTextChunkList = new ArrayList<List<TextChunk>>();
		for(int i=1; i <= pdf.getNumberOfPages(); i++) {
			PdfPage page = pdf.getPage(i);
			parser.processPageContent(page);
			List<TextChunk> list = (List<TextChunk>) ReflectionUtils.getField(field, listener); 
			list = this.removeDuplicatedChunks(list);
			this.allTextChunkList.add(list);
			parser.reset();
			ReflectionUtils.setField(field, listener, new ArrayList<TextChunk>());
		}
	}  
	
	/**
	 * 返回按页面遍历
	 */
	public Iterator<List<TextChunk>> iterator() {
		return this.allTextChunkList.iterator();
	}
	
	/**
	 * 删除重复的文本， 根据坐标及文本内容；
	 */
	private List<TextChunk> removeDuplicatedChunks(List<TextChunk> list) {
		Collections.sort(list);
		Iterator<TextChunk> it = list.iterator();
		//先到第二个chunk
		TextChunk pre = null, next = null;
		if(it.hasNext()) {
			pre = it.next();
		}
		while(it.hasNext()) {
			next = it.next(); 
			//如个是一样的则删除一个；
			if(this.isSameTextChunk(pre, next)) {
				it.remove();
			} else {
				pre = next;
			}
		}
		return list;
	}
	
	/** 判断是否是同一个文本 */
	private boolean isSameTextChunk(TextChunk pre, TextChunk next) {
		if(!StringUtil.equals(pre.getText(), next.getText())) {
			return false;
		}
		ITextChunkLocation pl = pre.getLocation();
		ITextChunkLocation nl = next.getLocation();
		
		Vector preStartLoc = pl.getStartLocation();
		Vector preEndLoc = pl.getEndLocation();
		float x = preStartLoc.get(Vector.I1), y = preStartLoc.get(Vector.I2) - 1;
		float width = preEndLoc.get(Vector.I1) - x, height = 2;
		Rectangle rect = new Rectangle(x, y, width, height);
		
		Vector nextStartLoc = nl.getStartLocation();
		Vector nextEndLoc = nl.getEndLocation();
		return rect.intersectsLine(nextStartLoc.get(Vector.I1), nextStartLoc.get(Vector.I2), 
				nextEndLoc.get(Vector.I1), nextEndLoc.get(Vector.I2));
	}
	
	/**
	 * Used for debugging only
	 */
	public void dumpState() {
		for(List<TextChunk> textChunkList : this.allTextChunkList) {
			this.dumpState(textChunkList);
		}
	}
	
	private void dumpState(List<TextChunk> list) {
		Method method = ReflectionUtils.findMethod(TextChunk.class, "printDiagnostics");
		method.setAccessible(true);
		for (TextChunk location : list) {
			ReflectionUtils.invokeMethod(method, location);
			System.out.println("\n");
		}
	} 
	
	public TableExtractor toExtractList() {
		return new TableExtractor(this);
	}
	
	public TextExtractor toExtractText() {
		return new TextExtractor(this, 1);
	}
}

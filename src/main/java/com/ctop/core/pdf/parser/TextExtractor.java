package com.ctop.core.pdf.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

import com.ctop.fw.common.utils.ListUtil;
import com.itextpdf.kernel.pdf.canvas.parser.listener.LocationTextExtractionStrategy.TextChunk;

public class TextExtractor {
	
	private Map<String, TextLocator> textMap = new LinkedHashMap<String, TextLocator>();
	private PdfTextChunkExtractor processor;
	private float scale;
	
	public TextExtractor(PdfTextChunkExtractor processor, float scale) {
		this.processor = processor;
		this.scale = scale;
	}
	
	public TextExtractor registerTextBetween(String field, String beforeRegExp, String afterRegExp) {
		this.textMap.put(field, new BetweenTextLocator(beforeRegExp, afterRegExp));
		return this;
	}
	
	public TextExtractor registerNextToText(String field, String labelRegExp, 
			float gapX1, float gapY1, 
			float width, float height) {
		gapX1 = gapX1 * this.scale;
		gapY1 = gapY1 * this.scale;
		width = width * this.scale;
		height = height * this.scale;
		this.textMap.put(field, new NextToTextLocator(labelRegExp, gapX1, gapY1, width, height));
		return this;
	}
	
	public TextExtractor registerNextToText(String field, String labelRegExp, 
			float gapX1, float gapY1, 
			float width, float height,
			TextConverter converter) {
		gapX1 = gapX1 * this.scale;
		gapY1 = gapY1 * this.scale;
		width = width * this.scale;
		height = height * this.scale;
		this.textMap.put(field, new NextToTextLocator(labelRegExp, gapX1, gapY1, width, height, converter));
		return this;
	}
	
	
	private Object getObject(TextLocator text) { 
		for(List<TextChunk> textChunkList : processor) {
			Object value = text.getConvertedObject(textChunkList);
			if(value != null) {
				return value;
			}
		}
		return null;
	}
	
	public Map<String, Object> extractMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		for(Map.Entry<String, TextLocator> entry : this.textMap.entrySet()) {
			map.put(entry.getKey(), this.getObject(entry.getValue()));
		}
		return map;
	}
	
	public <T> T extractBean(Class<T> cls) {
		T bean = BeanUtils.instantiate(cls);
		BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(bean);
		for(Map.Entry<String, TextLocator> entry : this.textMap.entrySet()) {
			bw.setPropertyValue(entry.getKey(), this.getObject(entry.getValue()));
		}
		return bean;
	}
	
	public void logMap(Map<String, Object> textMap) {
		List<String> list = new ArrayList<String>();
		StringBuilder buff = new StringBuilder();
		for(Map.Entry<String, TextLocator> entry: this.textMap.entrySet()) {
			buff.append("\t")
			.append(entry.getKey())
			.append(": \"")
			.append(textMap.get(entry.getKey()))
			.append("\"");
			list.add(buff.toString());
			buff.delete(0, buff.length());
		}
		System.out.println("{");
		System.out.println(ListUtil.join(list, ",\n"));
		System.out.println("}");
	}
}

package com.ctop.core.pdf.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ctop.fw.common.utils.BusinessException;
import com.itextpdf.kernel.pdf.canvas.parser.listener.LocationTextExtractionStrategy.TextChunk;

public class BetweenTextLocator extends TextLocator {
	static Logger logger = LoggerFactory.getLogger(BetweenTextLocator.class);
	private String beforeRegExp;
	private String afterRegExp;
	
	public BetweenTextLocator(String beforeRegExp, String afterRegExp) {
		this.beforeRegExp = beforeRegExp;
		this.afterRegExp = afterRegExp;
	}
	
	public BetweenTextLocator(String beforeRegExp, String afterRegExp, TextConverter converter) {
		this.beforeRegExp = beforeRegExp;
		this.afterRegExp = afterRegExp;
		super.converter = converter;
	}

	/**
	 * 取两个字段中的值
	 * 
	 * @param beforeRegExp
	 * @param afterRegExp
	 * @return
	 */
	public String getText(List<TextChunk> textChunkList) {
		List<TextChunk> beforeList = new ArrayList<TextChunk>();
		List<TextChunk> afterList = new ArrayList<TextChunk>();
		for (TextChunk item : textChunkList) {
			if (item.getText().matches(beforeRegExp)) {
				beforeList.add(item);
			}
			if (item.getText().matches(afterRegExp)) {
				afterList.add(item);
			}
		}
		if (beforeList.size() > 1) {
			throw new BusinessException("根据{0}找到多个匹配的串!", new Object[] { beforeRegExp });
		}
		if (afterList.size() > 1) {
			throw new BusinessException("根据{0}找到多个匹配的串!", new Object[] { afterRegExp });
		}
		TextChunk before = beforeList.get(0), after = afterList.get(0);
		int index1 = textChunkList.indexOf(before), index2 = textChunkList.indexOf(after);
		List<TextChunk> subList = textChunkList.subList(index1 + 1, index2);

		String text = PdfTextChunkUtil.getText(subList);
		if(PdfTextChunkUtil.DEBUG) {
			logger.info("[{} ... {}] > {}", beforeRegExp, afterRegExp, text);
		}
		return text;
	}

}
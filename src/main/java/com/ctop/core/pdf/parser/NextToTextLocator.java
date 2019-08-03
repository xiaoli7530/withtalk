package com.ctop.core.pdf.parser;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.ctop.fw.common.utils.BusinessException;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.geom.Vector;
import com.itextpdf.kernel.pdf.canvas.parser.listener.LocationTextExtractionStrategy.ITextChunkLocation;
import com.itextpdf.kernel.pdf.canvas.parser.listener.LocationTextExtractionStrategy.TextChunk;

public class NextToTextLocator extends TextLocator {

	static Logger logger = LoggerFactory.getLogger(NextToTextLocator.class);
	private String labelRegExp;
	private float gapX1;
	private float gapY1;
	private float width;
	private float height; 

	public NextToTextLocator(String labelRegExp, float gapX1, float gapY1, float width, float height) {
		this.labelRegExp = labelRegExp;
		this.gapX1 = gapX1;
		this.gapY1 = gapY1;
		this.width = width;
		this.height = height;
		this.converter = TextConverter.NOOP;
	}
	
	public NextToTextLocator(String labelRegExp, float gapX1, float gapY1, float width, float height, TextConverter converter) {
		this.labelRegExp = labelRegExp;
		this.gapX1 = gapX1;
		this.gapY1 = gapY1;
		this.width = width;
		this.height = height;
		this.converter = converter;
	}

	/**
	 * 取指定字段后面或前面的的字段值；
	 * 
	 * @param labelRegExp
	 * @param gapX1
	 * @param gapY1
	 * @param width
	 * @param height
	 * @return
	 */
	public String getText(List<TextChunk> textChunkList) {
		Assert.notNull(labelRegExp, "定位的匹配字段正则表达式不能为空!");
		List<TextChunk> list = new ArrayList<TextChunk>();
		for (TextChunk item : textChunkList) {
			if (item.getText().matches(labelRegExp)) {
				list.add(item);
			}
		}
		if (list.isEmpty()) {
			if(PdfTextChunkUtil.DEBUG) {
				logger.info("{} > {}", labelRegExp, "null");
			}
			return null;
		}
		if (list.size() > 1) {
			//throw new BusinessException("根据{0}找到多个匹配的串!", new Object[] { labelRegExp });
		}
		TextChunk item = list.get(0);
		ITextChunkLocation loc = item.getLocation();
		Vector startLoc = loc.getStartLocation();
		Vector endLoc = loc.getEndLocation();
		// float x = (endLoc.get(Vector.I1) + startLoc.get(Vector.I1) ) / 2
		// + gapX1,
		// y = (endLoc.get(Vector.I2) + startLoc.get(Vector.I2) ) / 2 +
		// gapY1;
		//
		// Rectangle rect = new Rectangle(x - (width / 2), y - (height / 2),
		// width, height);
		float maxX = Math.max(endLoc.get(Vector.I1), startLoc.get(Vector.I1)),
				minX = Math.min(endLoc.get(Vector.I1), startLoc.get(Vector.I1)),
				maxY = Math.max(endLoc.get(Vector.I2), startLoc.get(Vector.I2)),
				minY = Math.min(endLoc.get(Vector.I2), startLoc.get(Vector.I2));
		float x = (gapX1 > 0 ? maxX : minX) + gapX1, y = (gapY1 > 0 ? maxY : minY) + gapY1;
		Rectangle rect = new Rectangle(x, y - height / 2, width, height);

		List<TextChunk> list2 = new ArrayList<TextChunk>();
		for (TextChunk temp : textChunkList) {
			ITextChunkLocation loc1 = temp.getLocation();
			Vector start = loc1.getStartLocation(), end = loc1.getEndLocation();
			if (rect.intersectsLine(start.get(Vector.I1), start.get(Vector.I2), end.get(Vector.I1),
					end.get(Vector.I2))) {
				list2.add(temp);
			}
		}
		String text = PdfTextChunkUtil.getText(list2);
		if(PdfTextChunkUtil.DEBUG) {
			logger.info("{} > {}", labelRegExp, text);
		}
		return text;
	}
}
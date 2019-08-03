package com.ctop.core.pdf.parser;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ctop.fw.common.utils.ListUtil;
import com.ctop.fw.common.utils.StringUtil;
import com.itextpdf.kernel.geom.Vector;
import com.itextpdf.kernel.pdf.canvas.parser.listener.LocationTextExtractionStrategy.TextChunk;

public class TableColumn {
	static Logger logger = LoggerFactory.getLogger(TableColumn.class);
	private String[] titles;
	private String field;
	private float startX;
	private float endX;
	private TextConverter converter;
	
	public TableColumn(String field, float startX, float endX, String[] titles) {
		this.titles = titles;
		this.field = field;
		this.startX = startX;
		this.endX = endX;
		this.converter = TextConverter.NOOP;
	}
	
	public TableColumn(String field, float startX, float endX, String[] titles, TextConverter converter) {
		this.titles = titles;
		this.field = field;
		this.startX = startX;
		this.endX = endX;
		this.converter = converter;
	}
	
	public static void logColumns(List<TableColumn> list) {
		for(TableColumn column : list) {
			System.out.println(column);
		}
	}
	
	/** 判断chunk的X是在column指定的范围内 */
	public boolean isTextChunkInColumnRange(TextChunk chunk) {
		float cx1 = chunk.getLocation().getStartLocation().get(Vector.I1),
			  cx2 = chunk.getLocation().getEndLocation().get(Vector.I1);
		if(PdfTextChunkUtil.DEBUG) {
			logger.info("[{}: {}, {}] ---- ({}: {}, {}) ", chunk.getText(), cx1, cx2, this.field, startX, endX);
		}
		return startX < cx1 && endX > cx2;
	}
	
	/**
	 * 判断column的titles是否与list的尾部匹配；
	 * @param list
	 * @return
	 */
	public boolean matchTitlesWithListTails(List<String> list) {
		int length = this.titles.length, size = list.size();
		if(size < length) {
			return false;
		}
		for(int i=0; i< length; i++) {
			if(!StringUtil.equals(this.titles[length - 1 - i], list.get(size - 1 -i))) {
				return false;
			}
		}
		return true;
	}
	
	public String getTitlesText() {
		return ListUtil.join(Arrays.asList(this.titles), " ");
	}
	
	public String toString() {
		return new StringBuilder()
				.append("{field:").append(this.field)
				.append(", startX:").append(this.startX)
				.append(", endX:").append(this.endX)
				.append(", title").append(ListUtil.join(Arrays.asList(this.titles), ","))
				.append("}")
				.toString();
	}
	
	public TextConverter getConverter() {
		return converter;
	}

	public void setConverter(TextConverter converter) {
		this.converter = converter;
	}

	public String[] getTitles() {
		return titles;
	}
	public void setTitles(String[] titles) {
		this.titles = titles;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public float getStartX() {
		return startX;
	}
	public void setStartX(float startX) {
		this.startX = startX;
	}
	public float getEndX() {
		return endX;
	}
	public void setEndX(float endX) {
		this.endX = endX;
	}
}
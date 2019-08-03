package com.ctop.core.pdf.parser;

import java.util.List;

import com.itextpdf.kernel.pdf.canvas.parser.listener.LocationTextExtractionStrategy.TextChunk;

public abstract class TextLocator {
	protected TextConverter converter;
	public abstract String getText(List<TextChunk> textChunkList);
	public Object getConvertedObject(List<TextChunk> textChunkList) {
		String text = this.getText(textChunkList);
		if(this.converter == null) {
			return text;
		}
		return this.converter.convert(text);
	}
}
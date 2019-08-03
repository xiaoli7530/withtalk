package com.ctop.core.pdf.parser;

import java.util.List;

import com.itextpdf.kernel.pdf.canvas.parser.listener.LocationTextExtractionStrategy.TextChunk;

public class PdfTextChunkUtil {
	public static final boolean DEBUG = false;

	public static String getText(List<TextChunk> textChunks) {
		StringBuilder sb = new StringBuilder();
		TextChunk lastChunk = null;
		for (TextChunk chunk : textChunks) {

			if (lastChunk == null) {
				sb.append(chunk.getText());
			} else {
				if (sameLine(chunk, lastChunk)) {
					// we only insert a blank space if the trailing character of
					// the previous string wasn't a space, and the leading
					// character of the current string isn't a space
					if (isChunkAtWordBoundary(chunk, lastChunk) && !startsWithSpace(chunk.getText())
							&& !endsWithSpace(lastChunk.getText())) {
						sb.append(' ');
					}

					sb.append(chunk.getText());
				} else {
					sb.append('\n');
					sb.append(chunk.getText());
				}
			}
			lastChunk = chunk;
		}
		return sb.toString().trim();
	}
	
	public static boolean isChunkAtWordBoundary(TextChunk chunk, TextChunk previousChunk) {
		return chunk.getLocation().isAtWordBoundary(previousChunk.getLocation());
	}
	
	/**
	 * Checks if the string starts with a space character, false if the string
	 * is empty or starts with a non-space character.
	 *
	 * @param str
	 *            the string to be checked
	 * @return true if the string starts with a space character, false if the
	 *         string is empty or starts with a non-space character
	 */
	public static boolean startsWithSpace(String str) {
		return str.length() != 0 && str.charAt(0) == ' ';
	}

	/**
	 * Checks if the string ends with a space character, false if the string is
	 * empty or ends with a non-space character
	 *
	 * @param str
	 *            the string to be checked
	 * @return true if the string ends with a space character, false if the
	 *         string is empty or ends with a non-space character
	 */
	public static boolean endsWithSpace(String str) {
		return str.length() != 0 && str.charAt(str.length() - 1) == ' ';
	}
	
	public static boolean sameLine(TextChunk chunk, TextChunk lastChunk) {
        return chunk.getLocation().sameLine(lastChunk.getLocation());
    }
}

package com.ctop.core.pdf.parser;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.ctop.fw.common.utils.StringUtil;

public class TextConverter {
	
	public Object convert(String text) {
		return text;
	}
	
	public static TextConverter NOOP = new TextConverter();
	public static TextConverter date(String format) {
		return new DateTextConverter(format);
	}
	public static TextConverter date(String... formats) {
		return new AutoDateConverter(Arrays.asList(formats));
	}
	public static TextConverter bigDecimal() {
		return new BigDecimalConverter();
	}
	
	public static class DateTextConverter extends TextConverter {
		private SimpleDateFormat dateFormat;
		public DateTextConverter(String format) {
			this.dateFormat = new SimpleDateFormat(format);
		}
		
		public Object convert(String text) {
			if(StringUtil.isEmpty(text)) {
				return null;
			}
			try {
				return this.dateFormat.parse(text);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
	}
	
	public static class AutoDateConverter extends TextConverter {
		private List<DateTextConverter> converters; 
		
		public AutoDateConverter(List<String> formats) {
			this.converters = formats.stream().map(f -> new DateTextConverter(f)).collect(Collectors.toList());
		}
		
		public Object convert(String text) {
			if(StringUtil.isEmpty(text)) {
				return null;
			}
			for(DateTextConverter c : this.converters) {
				Object value = c.convert(text);
				if( value != null) {
					return value;
				}
			}
			return null;
		}
	}
	
	public static class BigDecimalConverter extends TextConverter {
		public Object convert(String text) {
			if(StringUtil.isEmpty(text)) {
				return null;
			}
			text = text.replace(",", "");
			return new BigDecimal(text);
		}
	}
}

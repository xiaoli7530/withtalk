package com.ctop.fw.sys.excelImport.support.validate;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DateValidator implements VTypeValidator, Serializable  {
	private static final long serialVersionUID = 7047640434504252763L;
	private SimpleDateFormat format;
	private List<SimpleDateFormat> formats = new ArrayList<>();
	
	public DateValidator(String dateFormat) {
		this.format = new SimpleDateFormat(dateFormat);
		String[] formats = new String[] {"yyyy/MM/dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss", "yyyy/MM/dd", "yyyy-MM-dd"};
		for(String format : formats) {
			this.formats.add(new SimpleDateFormat(format));
		}
	}

	public ValidateResult validate(String text) {
		if(text != null && !"".equals(text)) { 
			Date date = null;
			for(SimpleDateFormat format : this.formats) {
				try {
					date = format.parse(text);
					break;
				} catch(ParseException e) {
					//  
				}
			}
			if (date == null) {
				return ValidateResult.buildInvalidResult("无法解析的日期：" + text); 
			}
			String dateText = this.format.format(date);
			return new ValidateResult(true, dateText);
		}
		return ValidateResult.VALID;
	}

}

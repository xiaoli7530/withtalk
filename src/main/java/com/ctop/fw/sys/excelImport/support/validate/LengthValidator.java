package com.ctop.fw.sys.excelImport.support.validate;

import java.io.Serializable;
import java.nio.charset.Charset;


public class LengthValidator implements VTypeValidator, Serializable  {
	private static final long serialVersionUID = 6545835942797166670L;
	private int length;
	
	public LengthValidator(int length) {
		this.length = length;
	}

	public ValidateResult validate(String text) { 
		Charset set = Charset.forName("UTF-8");
		int tempLength = text != null ? text.getBytes(set).length : 0;
		return length < tempLength ? ValidateResult.buildInvalidResult("字段超长") : ValidateResult.VALID;
	}

}

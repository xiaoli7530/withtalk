package com.ctop.fw.sys.excelImport.support.validate;

import java.io.Serializable;

public class RequiredValidator implements VTypeValidator, Serializable {
	private static final long serialVersionUID = -4579761170266308571L;
	public static RequiredValidator INSTANCE = new RequiredValidator();
	
	private RequiredValidator(){}

	public ValidateResult validate(String text) {
		if(text == null || text.isEmpty()) {
			return ValidateResult.buildInvalidResult("值不能为空");
		}
		return ValidateResult.VALID;
	}

}

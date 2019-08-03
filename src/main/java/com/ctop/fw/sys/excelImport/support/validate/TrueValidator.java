package com.ctop.fw.sys.excelImport.support.validate;

import java.io.Serializable;

public class TrueValidator implements VTypeValidator, Serializable {
	private static final long serialVersionUID = 5977439165728754974L;
	public static TrueValidator INSTANCE = new TrueValidator();

	public ValidateResult validate(String text) {
		return ValidateResult.VALID;
	}

}

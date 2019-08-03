package com.ctop.fw.sys.excelImport.support.validate;

import java.io.Serializable;

public class FalseValidator implements VTypeValidator, Serializable {
	private static final long serialVersionUID = -8433388383771825825L;
	private String message;
	public FalseValidator(String message) {
		this.message = message;
	}
	public ValidateResult validate(String text) {
		return ValidateResult.buildInvalidResult(message);
	}
}

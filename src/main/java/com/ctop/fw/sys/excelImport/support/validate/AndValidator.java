package com.ctop.fw.sys.excelImport.support.validate;

import java.io.Serializable;

public class AndValidator implements VTypeValidator, Serializable {
	private static final long serialVersionUID = -1152092931328648236L;
	private VTypeValidator[] typeValidators;
	public AndValidator(VTypeValidator...typeValidators) {
		this.typeValidators = typeValidators;
	}

	public ValidateResult validate(String text) {
		ValidateResult result = ValidateResult.VALID;
		for(VTypeValidator validator : typeValidators) {
			result = result.and(validator.validate(text));
		}
		return result;
	}

}

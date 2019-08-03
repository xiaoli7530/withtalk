package com.ctop.fw.sys.excelImport.support.validate;

import java.io.Serializable;

public interface VTypeValidator extends Serializable  {

	public ValidateResult validate(String text);
}

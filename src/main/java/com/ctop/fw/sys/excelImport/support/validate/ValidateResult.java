package com.ctop.fw.sys.excelImport.support.validate;

public class ValidateResult {
	
	public static ValidateResult VALID = new ValidateResult(true, null);
	
	public ValidateResult(){}
	public ValidateResult(boolean valid, String message) {
		this.valid = valid;
		this.message = message;
	}
	
	public static ValidateResult buildInvalidResult(String message) {
		return new ValidateResult(false, message);
	}

	private boolean valid;
	private String message;
	
	public ValidateResult and(ValidateResult other) {
		ValidateResult another = new ValidateResult();
		another.valid = this.valid && other.valid;
		if(!another.valid) {
			StringBuilder buff = new StringBuilder();
			buff.append(this.valid ? "" : this.message);
			buff.append(other.valid ? "" : other.message);
			another.message = buff.toString();
		}
		return another;
	}
	
	public boolean isValid() {
		return valid;
	}
	public void setValid(boolean valid) {
		this.valid = valid;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
}

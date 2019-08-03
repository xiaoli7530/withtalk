package com.ctop.fw.sys.excelImport.support.validate;

import java.io.Serializable;
import java.util.Optional;

import com.ctop.base.dto.BaseDictDetailDto;
import com.ctop.base.dto.BaseDictDto;
import com.ctop.fw.common.utils.StringUtil;

public class DictValidator implements VTypeValidator, Serializable  {
	 
	private static final long serialVersionUID = -734341616094024459L;
	private String dictNo;
	private BaseDictDto dict;
	
	public DictValidator(BaseDictDto dict, String dictNo) {
		this.dictNo = dictNo;
		this.dict = dict;
	}

	public ValidateResult validate(String text) {
		if(StringUtil.isEmpty(text)) {
			return ValidateResult.VALID;
		}
		Optional<BaseDictDetailDto> detail = Optional.empty();
		if(dict != null && dict.getDetails() != null) {
			detail = dict.getDetails().stream().filter(item -> item.getCode().equals(text) || item.getName().equals(text)).findFirst();
		}
		if(detail.isPresent()) {
			return ValidateResult.VALID;
		}
		return ValidateResult.buildInvalidResult("数据字典" + dictNo + "中找不到定义的值或名称" + text);
	}

	public String getCode(String text) {
		Optional<BaseDictDetailDto> detail = Optional.empty();
		if(dict != null && dict.getDetails() != null) {
			detail = dict.getDetails().stream().filter(item -> item.getCode().equals(text) || item.getName().equals(text)).findFirst();
		}
		return detail.isPresent() ? detail.get().getCode() : null;
	}
}

package com.ctop.fw.sys.excelImport.support.validate;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ctop.base.dto.BaseDictDto;
import com.ctop.base.service.BaseDictService;
import com.ctop.fw.common.utils.MathUtil;
import com.ctop.fw.common.utils.StringUtil;

@Component
public class VTypeValidatorBuilder {
	
	public static VTypeValidatorBuilder INSTANCE;
	
	@Autowired
	private BaseDictService baseDictService;
	
	@Autowired
	public void setVTypeValidateBuilder(VTypeValidatorBuilder builder) {
		INSTANCE = builder;
	}
	
	/**注册的解析vtype的方法*/
	private Map<String, Method> vtypeRegister = new HashMap<String, Method>();
	
	private VTypeValidatorBuilder() {
		Method[] methods = this.getClass().getDeclaredMethods();
		for(Method method : methods) {
			if(method.isAnnotationPresent(VType.class)) {
				vtypeRegister.put(method.getAnnotation(VType.class).value(), method);
			}
		}
		
	}

	/**根据配置的规则，生成校验器
	 * 规则： vtype1:validatorParam1;vtype2:validatorParam2;
	 * */
	public VTypeValidator buidlValidator(String validateRule) {
		String[] arr = validateRule.split("\\s*;\\s*");
		VTypeValidator[] validators = new VTypeValidator[arr.length];
		int i = 0;
		for(String rule : arr) {
			int index = rule.indexOf(":");
			String vtype = rule;
			String param = "";
			if(index != -1) {
				vtype = rule.substring(0, index);
				param = rule.substring(index + 1);
			}
			Method method = vtypeRegister.get(vtype);
			try {
				validators[i] = (VTypeValidator) method.invoke(this, param);
			} catch (Exception e) {
				validators[i] = new FalseValidator("配置" + rule + "有误，未能生成相应的校验器.");
			}
		}
		return new AndValidator(validators);
	}
	
	@VType("length")
	public VTypeValidator buildLengthValidator(String param) {
		LengthValidator validator = new LengthValidator(Integer.parseInt(param));
		return validator;
	}
	
	@VType("positiveFloat")
	public VTypeValidator buildPositiveFloatValidator(String param) {
		return new VTypeValidator(){
			private static final long serialVersionUID = -1950811993281592996L;

			public ValidateResult validate(String text) {
				if(text == null || text.isEmpty()) {
					return ValidateResult.VALID;
				}
				try {
					BigDecimal d = new BigDecimal(text);
					if(MathUtil.le(d , BigDecimal.ZERO)) {
						return ValidateResult.buildInvalidResult(text + "小于等于0");
					}
					return ValidateResult.VALID;
				} catch (Exception ex) {
					return ValidateResult.buildInvalidResult(text + "不是数字.");
				}
			}
		};
	}
	
	/**校验指定数字的精度*/
	@VType("precisionScale")
	public VTypeValidator buildPrecisionScaleValidator(final String param) {
		int index = param.indexOf(",");
		int num1 = Integer.parseInt(param.substring(0, index));
		int num2 = index != -1 ? Integer.parseInt(param.substring(index + 1)) : 0;
		final int precision = num1 - num2;
		final int scale = num2;
		return new VTypeValidator(){
			private static final long serialVersionUID = 1945384046606555362L;

			public ValidateResult validate(String text) {
				if(text == null || text.isEmpty()) {
					return ValidateResult.VALID;
				}
				text = text.replace(",", "");
				int dotIndex = text.indexOf(".");
				// 整数部分
				String intPart = text.substring(0, dotIndex != -1 ? dotIndex : text.length());
				// 小数部分
				String floatPart = dotIndex != -1  ? text.substring(dotIndex + 1) : "";
				// 去除小数后面的0
				int index = floatPart.length() - 1;
				while(index >=0 && floatPart.charAt(index) == '0') {
					index--;
				}
				if (index != -1) {
					floatPart = floatPart.substring(0, index + 1);
				} else {
					floatPart = "";
				}
				String full = intPart + (StringUtil.isEmpty(floatPart) ? "" : "." + floatPart);
				try {
					// 是数字就能parse
					Double.parseDouble(full);
					// 精度十分超出；
					if(intPart.length() > precision || (floatPart.length() > scale)) {
						return ValidateResult.buildInvalidResult(text + "超出指定的精度范围" + param);
					}
					return ValidateResult.VALID;
				} catch (Exception ex) {
					return ValidateResult.buildInvalidResult(text + "不是数字.");
				}
			}
		};
	}
	 
	@VType("required")
	public VTypeValidator buildRequiredValidator(final String param) {
		return RequiredValidator.INSTANCE;
	}
	
	@VType("dict")
	public DictValidator buildDictValidator(String param) {
		BaseDictDto dict = this.baseDictService.findBaseDictWithDetails(param);
		return new DictValidator(dict, param);
	}
}

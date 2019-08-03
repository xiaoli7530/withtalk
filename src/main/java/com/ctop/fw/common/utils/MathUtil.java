package com.ctop.fw.common.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public abstract class MathUtil {

	/**
	 * 加：value1 + value2
	 * @param value1
	 * @param value2
	 * @return
	 */
	public static BigDecimal add(BigDecimal value1, BigDecimal value2) { 
		return add(value1, value2, 2);
	}
	
	/**
	 * 加：value1 + value2
	 * @param value1
	 * @param value2
	 * @param scale	保留的小数位
	 * @return
	 */
	public static BigDecimal add(BigDecimal value1, BigDecimal value2, int scale) { 
		value1 = value1 == null ? BigDecimal.ZERO : value1;
		value2 = value2 == null ? BigDecimal.ZERO : value2;
		return value1.add(value2).setScale(scale, RoundingMode.HALF_UP);
	}
	
	/**
	 * 减：value - value2
	 * @param value1
	 * @param value2
	 * @return
	 */
	public static BigDecimal subtract(BigDecimal value1, BigDecimal value2) {
		value1 = value1 == null ? BigDecimal.ZERO : value1;
		value2 = value2 == null ? BigDecimal.ZERO : value2;
		return value1.subtract(value2).setScale(2, RoundingMode.HALF_UP);
	}
 
	/**
	 * 乘：value1 * value2
	 * @param value1
	 * @param value2
	 * @return
	 */
	public static BigDecimal multiply(BigDecimal value1, BigDecimal value2) {
		value1 = value1 == null ? BigDecimal.ZERO : value1;
		value2 = value2 == null ? BigDecimal.ZERO : value2;
		return value1.multiply(value2).setScale(2, RoundingMode.HALF_UP);
	}
	 
	/**
	 * 乘：value1 * value2
	 * @param value1
	 * @param value2
	 * @return
	 */
	public static BigDecimal multiply(BigDecimal value1, int value2) {
		value1 = value1 == null ? BigDecimal.ZERO : value1;
		return value1.multiply(new BigDecimal(value2)).setScale(2, RoundingMode.HALF_UP);
	}
	
	/**
	 * 乘：value1 * value2
	 * @param value1
	 * @param value2
	 * @param scale	保留的小数位
	 * @return
	 */
	public static BigDecimal multiply(BigDecimal value1, BigDecimal value2, int scale) {
		value1 = value1 == null ? BigDecimal.ZERO : value1;
		value2 = value2 == null ? BigDecimal.ZERO : value2;
		return value1.subtract(value2).setScale(scale, RoundingMode.HALF_UP);
	}
	
	/**
	 * 除：value/value2
	 * @param value1
	 * @param value2
	 * @return
	 */
	public static BigDecimal divide(BigDecimal value1, BigDecimal value2) { 
		return divide(value1, value2, 2).setScale(2, RoundingMode.HALF_UP);
	}
	 
	/**
	 * 除：value/value2
	 * @param value1
	 * @param value2
	 * @param scale 保留的小数位
	 * @return
	 */
	public static BigDecimal divide(BigDecimal value1, BigDecimal value2, int scale) {
		value1 = value1 == null ? BigDecimal.ZERO : value1;
		if(BigDecimal.ZERO.compareTo(value1) == 0) {
			return BigDecimal.ZERO;
		}
		value2 = value2 == null ? BigDecimal.ONE : value2;
		return value1.divide(value2, scale, RoundingMode.HALF_UP);
	}
	 
	/**
	 * 两个数取较小的那个
	 * @param value1
	 * @param value2
	 * @return
	 */
	public static BigDecimal min(BigDecimal value1, BigDecimal value2) {
		value1 = value1 == null ? BigDecimal.ZERO : value1;
		value2 = value2 == null ? BigDecimal.ZERO : value2;
		return value1.compareTo(value2) > 0 ? value2 : value1;
	}
	 
	/**
	 * 三个数取最小的那个
	 * @param value1
	 * @param value2
	 * @param value3
	 * @return
	 */
	public static BigDecimal min(BigDecimal value1, BigDecimal value2, BigDecimal value3) {
		return min(min(value1, value2), value3);
	}
	 
	/**
	 * 两个数取较大的那个
	 * @param value1
	 * @param value2
	 * @return
	 */
	public static BigDecimal max(BigDecimal value1, BigDecimal value2) {
		value1 = value1 == null ? BigDecimal.ZERO : value1;
		value2 = value2 == null ? BigDecimal.ZERO : value2;
		return value1.compareTo(value2) > 0 ? value1 : value2;
	}
	 
	/**
	 * 等于
	 * @param value1
	 * @param value2
	 * @return
	 */
	public static boolean eq(BigDecimal value1, BigDecimal value2) {
		value1 = value1 == null ? BigDecimal.ZERO : value1;
		value2 = value2 == null ? BigDecimal.ZERO : value2;
		return value1.compareTo(value2) == 0 ;
	}
	
	/**
	 * 等于
	 * @param value1
	 * @param value2
	 * @return
	 */
	public static boolean eq(Double value1, Double value2)  {
		value1 = value1 == null ? 0 : value1;
		value2 = value2 == null ? 0 : value2;
		return value1.compareTo(value2) == 0 ;
	}
	
	/**
	 * 等于
	 * @param value1
	 * @param value2
	 * @return
	 */
	public static boolean eq(Number value1, Number value2) {
		double v1 = value1 == null ? 0 : value1.doubleValue();
		double v2 = value2 == null ? 0 : value2.doubleValue();
		return v1 == v2 ;
	}
	
	/**
	 * 大于
	 * @param value1
	 * @param value2
	 * @return
	 */
	public static boolean gt(BigDecimal value1, BigDecimal value2) {
		value1 = value1 == null ? BigDecimal.ZERO : value1;
		value2 = value2 == null ? BigDecimal.ZERO : value2;
		return value1.compareTo(value2) > 0 ;
	}
	
	/**
	 * 大于等于
	 * @param value1
	 * @param value2
	 * @return
	 */
	public static boolean ge(BigDecimal value1, BigDecimal value2) {
		value1 = value1 == null ? BigDecimal.ZERO : value1;
		value2 = value2 == null ? BigDecimal.ZERO : value2;
		return value1.compareTo(value2) >= 0 ;
	}
	
	/**
	 * 大于0
	 * @param value
	 * @return
	 */
	public static boolean gtZero(BigDecimal value) {
		value = value == null ? BigDecimal.ZERO : value;
		return value.compareTo(BigDecimal.ZERO) > 0 ;
	} 
	
	/**
	 * 小于
	 * @param value1
	 * @param value2
	 * @return
	 */
	public static boolean lt(BigDecimal value1, BigDecimal value2) {
		value1 = value1 == null ? BigDecimal.ZERO : value1;
		value2 = value2 == null ? BigDecimal.ZERO : value2;
		return value1.compareTo(value2) < 0 ;
	}
	
	/**
	 * 小于等于
	 * @param value1
	 * @param value2
	 * @return
	 */
	public static boolean le(BigDecimal value1, BigDecimal value2) {
		value1 = value1 == null ? BigDecimal.ZERO : value1;
		value2 = value2 == null ? BigDecimal.ZERO : value2;
		return value1.compareTo(value2) <= 0 ;
	}
	 
	public static String toString(BigDecimal value) {
		value = value == null ? BigDecimal.ZERO : value;
		value = value.setScale(2, RoundingMode.HALF_UP);
		return value.toString();
	}
	
	public static String toPenny(BigDecimal value) {
		value = value == null ? BigDecimal.ZERO : value;
		value = value.setScale(2, RoundingMode.HALF_UP);
		BigDecimal bei= new BigDecimal(100);
		BigDecimal pennyValue = value.multiply(bei).setScale(0, RoundingMode.HALF_UP);
		return pennyValue.toString();
	}
	 
	public static Long toPennyLongValue(BigDecimal value) {
		value = value == null ? BigDecimal.ZERO : value;
		value = value.setScale(2, RoundingMode.HALF_UP);
		BigDecimal bei= new BigDecimal(100);
		BigDecimal pennyValue = value.multiply(bei).setScale(0, RoundingMode.HALF_UP);
		
		return pennyValue.longValue();
	} 
	
	public static String fenToYuan(String value){
		Double amount = new Double(value);
		amount =amount/100;
		if(amount>=1){
			DecimalFormat format = new DecimalFormat("###.00");
			return format.format(amount);
		}
		return String.valueOf(amount);
	}
	
	
	public static void main(String[] args) {
		BigDecimal v1 = new BigDecimal(10);
		BigDecimal v2 = new BigDecimal(10);
		System.out.println(MathUtil.le(v1, v2));
		
		BigDecimal v3 = new BigDecimal(10);
		BigDecimal v4 = new BigDecimal(3);
		System.out.println(MathUtil.min(v3, v4));
		
	}
}

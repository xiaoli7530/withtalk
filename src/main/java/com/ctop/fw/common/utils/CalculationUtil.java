package com.ctop.fw.common.utils;

import java.math.BigDecimal;

public class CalculationUtil {
	/**
	 * 提供精确的加法运算。
	 * 
	 * @param v1
	 *            被加数
	 * @param v2
	 *            加数
	 * @return 两个参数的和
	 */

	public static double add(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.add(b2).doubleValue();
	}
	
	public static BigDecimal add(BigDecimal v1,BigDecimal v2) {
		return MathUtil.add(v1, v2);
	}
	
    /**
     * 提供精确的减法运算
     * @param d1
     *        
     * @param d2
     *        
     * @return
     */
	public static double sub(double d1, double d2) {
		BigDecimal b1 = new BigDecimal(d1);
		BigDecimal b2 = new BigDecimal(d2);
		return b1.subtract(b2).doubleValue();
	}

	public static BigDecimal sub(BigDecimal d1, BigDecimal d2) {
		return MathUtil.subtract(d1, d2);
	}
	/**
	 * 提供精确的乘法运算。
	 * 
	 * @param v1
	 *            被乘数
	 * @param v2
	 *            乘数
	 * @return 两个参数的积
	 */

	public static double mul(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.multiply(b2).doubleValue();
	}
	
	public static BigDecimal mul(BigDecimal v1, BigDecimal v2) {
		return MathUtil.multiply(v1, v2);
	}
	/**
	 * 提供精确的除法运算
	 * @param d1
	 * 
	 * @param d2
	 * 
	 * @param len
	 * @return
	 */
	public  static double div(double d1,double d2,int len) {
        BigDecimal b1 = new BigDecimal(d1);
	    BigDecimal b2 = new BigDecimal(d2);
	    return b1.divide(b2,len,BigDecimal.ROUND_HALF_UP).doubleValue();
    }
	
	public static BigDecimal div(BigDecimal v1, BigDecimal v2,int len) {
		return MathUtil.divide(v1, v2, len);
	}
	/**
	 * 进行四舍五入操作
	 * @param d
	 * @param len
	 * @return
	 */
	public static double round(double d,int len) { 
	    BigDecimal b1 = new BigDecimal(d);
	    BigDecimal b2 = new BigDecimal(1);
		// 任何一个数字除以1都是原数字
	    // ROUND_HALF_UP是BigDecimal的一个常量，表示进行四舍五入的操作
        return b1.divide(b2, len,BigDecimal.ROUND_HALF_UP).doubleValue();
	}
}

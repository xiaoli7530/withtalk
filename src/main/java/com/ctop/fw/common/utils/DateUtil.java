package com.ctop.fw.common.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateFormatUtils;

public class DateUtil extends org.apache.commons.lang.time.DateUtils {

	/**
	 * 把自定日期格式化指定的格式的字符串
	 * @param date		指定的日期
	 * @param format	指定的格式
	 * @return
	 */
	public static String getDateStr(Date date, String format) {
		return format(date,format);
	};


	/**
	 * 按默认格式格式化日期时间，到分
	 * @param date
	 * @return
	 */
	public static String format(Date date) {
		return format(date,"yyyy-MM-dd HH:mm");
	}
	/**
	 * 格式化日期
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String format(Date date, String pattern) {
		if(date==null){
			return "";
		}
		return DateFormatUtils.format(date, pattern);
	}
	
	
	/**
	 * 把指定的日期增加年数
	 * @param date	指定的日期
	 * @param year	年数，可为负数
	 * @return
	 */
	public static Date addYear(Date date, int year) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.YEAR, year);
		return date = calendar.getTime();
	}
	
	/**
	 * 把指定的日期增加月数
	 * @param date	指定的日期
	 * @param month	月数，可为负数
	 * @return
	 */
	public static Date addMonth(Date date, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, month);
		return date = calendar.getTime();
	}
	
	/**
	 * 把指定的日期增加天数
	 * @param date	指定的日期
	 * @param day	天数，可为负数
	 * @return
	 */
	public static Date addDay(Date date, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, day);
		return date = calendar.getTime();
	}

	public static void main(String[] args) {
		//System.out.println(getDateStr(new Date(), "yyyy-MM-dd"));
		
		/*System.out.println(getDateStr(addYear(new Date(), 1), "yyyy-MM-dd"));
		System.out.println(getDateStr(addYear(new Date(), 12), "yyyy-MM-dd"));
		System.out.println(getDateStr(addYear(new Date(), -2), "yyyy-MM-dd"));*/
		
		/*System.out.println(getDateStr(addMonth(new Date(), 7), "yyyy-MM-dd"));
		System.out.println(getDateStr(addMonth(new Date(), 9), "yyyy-MM-dd"));
		System.out.println(getDateStr(addMonth(new Date(), -2), "yyyy-MM-dd"));*/
		
		//System.out.println(getDateStr(addDay(new Date(), 24), "yyyy-MM-dd"));
	}
	
	 /**
	  * 获得当天零时零分零秒
	  * @return
	  */
	public static Date miniDailyDate(Date date){
		Calendar calendar = Calendar.getInstance();
		//calendar.setTime(new Date());
		if(date != null){        
	         calendar.setTime(date);      
	    } 
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0); 
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	
	 /**
	  * 获得Date最大时间
	  * @return
	  */
	public static Date getDailyMostDate(Date date){
		Calendar calendar = Calendar.getInstance();
		if(date != null){        
	         calendar.setTime(date);      
	    } 
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		return calendar.getTime();
	}
	
	/**
	 * 获取指定日期是星期几
	 * @param date
	 * @return
	 */
	public static String getWeekOfDate(Date date) {      
		String[] weekOfDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};  
	    Calendar calendar = Calendar.getInstance();      
	    if(date != null){        
	         calendar.setTime(date);      
	    }        
	    int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;      
	    if (w < 0){        
	        w = 0;      
	    }      
	    return weekOfDays[w];    
	}
	
	/**
     * 获取系统当前时间
     * @return
     */
    public static String getNowTime(String type) {
        SimpleDateFormat df = new SimpleDateFormat(type);
        return df.format(new Date());
    }
}

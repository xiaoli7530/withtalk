package com.ctop.base.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import java.util.UUID;

import org.apache.commons.lang.time.DateUtils;

import oracle.net.aso.n;

public class DateUtil {
	
	public static Date dateAddDay(Date startdate,int day){
		     Calendar   calendar   =   new   GregorianCalendar(); 
		     calendar.setTime(startdate); 
		     calendar.add(calendar.DATE,day);//把日期往后增加天数
		     return startdate=calendar.getTime();   
	}
	
	/**定义常量**/
    public static final String DATE_JFP_STR="yyyyMM";
    public static final String DATE_FULL_STR = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_SMALL_STR = "yyyy-MM-dd";
    public static final String DATE_KEY_STR = "yyMMddHHmmss";
     
    /**
     * 使用预设格式提取字符串日期
     * @param strDate 日期字符串
     * @return
     */
    public static Date parse(String strDate) {
        return parse(strDate,DATE_FULL_STR);
    }
     
    /**
     * 使用用户格式提取字符串日期
     * @param strDate 日期字符串
     * @param pattern 日期格式
     * @return
     */
    public static Date parse(String strDate, String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        try {
            return df.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
     
    /**
     * 两个时间比较
     * @param date
     * @return
     */
    public static int compareDateWithNow(Date date1){
        Date date2 = new Date();
        int rnum =date1.compareTo(date2);
        return rnum;
    }
     
    /**
     * 两个时间比较(时间戳比较)
     * @param date
     * @return
     */
    public static int compareDateWithNow(long date1){
        long date2 = dateToUnixTimestamp();
        if(date1>date2){
            return 1;
        }else if(date1<date2){
            return -1;
        }else{
            return 0;
        }
    }
     
 
    /**
     * 获取系统当前时间
     * @return
     */
    public static String getNowTime() {
        SimpleDateFormat df = new SimpleDateFormat(DATE_FULL_STR);
        return df.format(new Date());
    }
     
    /**
     * 获取系统当前时间
     * @return
     */
    public static String getNowTime(String type) {
        SimpleDateFormat df = new SimpleDateFormat(type);
        return df.format(new Date());
    }
     
    /**
     * 获取yyyyMM
     * @return
     */
    public static String getJFPTime() {
        SimpleDateFormat df = new SimpleDateFormat(DATE_JFP_STR);
        return df.format(new Date());
    }
     
    /**
     * 将指定的日期转换成Unix时间戳
     * @param String date 需要转换的日期 yyyy-MM-dd HH:mm:ss
     * @return long 时间戳
     */
    public static long dateToUnixTimestamp(String date) {
        long timestamp = 0;
        try {
            timestamp = new SimpleDateFormat(DATE_FULL_STR).parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timestamp;
    }
     
    /**
     * 将指定的日期转换成Unix时间戳
     * @param String date 需要转换的日期 yyyy-MM-dd
     * @return long 时间戳
     */
    public static long dateToUnixTimestamp(String date, String dateFormat) {
        long timestamp = 0;
        try {
            timestamp = new SimpleDateFormat(dateFormat).parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timestamp;
    }
     
    /**
     * 将当前日期转换成Unix时间戳
     * @return long 时间戳
     */
    public static long dateToUnixTimestamp() {
        long timestamp = new Date().getTime();
        return timestamp;
    }
     
     
    /**
     * 将Unix时间戳转换成日期
     * @param long timestamp 时间戳
     * @return String 日期字符串
     */
    public static String unixTimestampToDate(long timestamp) {
        SimpleDateFormat sd = new SimpleDateFormat(DATE_FULL_STR);
        sd.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return sd.format(new Date(timestamp));
    }
    
    /**
     * 随机生成UUID
     * @return
     */
    public static synchronized String getUUID(){
      UUID uuid=UUID.randomUUID();
      String str = uuid.toString(); 
      String uuidStr=str.replace("-", "");
      return uuidStr;
    }
    /**
     * 日期相减得到小时
     * @param date1
     * @param date2
     * @return
     */
    public static int subtractGetHour(Date date1,Date date2){
    	return (int)((date1.getTime()-date2.getTime())/(60*60*1000));
    }
    /**
     * 日期相减得到天数
     * @param date1
     * @param date2
     * @return
     */
    public static int subtractGetDay(Date date1,Date date2){
    	return (int)(date1.getTime()-date2.getTime())/(24*60*60*1000);
    }
    /**
     * 获取月份最后一天
     * @param from
     * @return
     */
	public static Date monthLastDate(Date from) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(from);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return calendar.getTime();
	}
	/**
     * 获取系统当前时间
     * @return
     */
    public static String toDateString(Date date,String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        return df.format(date);
    }
    
    /**
	 * 字符串转换为日期java.util.Date
	 * @param dateText 字符串
	 * @param format 日期格式
	 * @return
	 */
	public static Date valueOf(String dateText, String format) {
		if (dateText == null) {
			return null;
		}
		DateFormat df = null;
		try {
			if (format == null) {
				df = new SimpleDateFormat();
			} else {
				df = new SimpleDateFormat(format);
			}
			df.setTimeZone(new SimpleTimeZone(28800000, "GMT"));
			df.setLenient(false);

			return df.parse(dateText);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 获取一年的最后一天
	 * @param from
	 * @return
	 */
	public static Date yearLastDate(Date from) {
		Calendar instance = Calendar.getInstance();
		instance.setTime(from);
		instance.set(Calendar.MONTH, 12);
		return DateUtil.monthLastDate(instance.getTime());
	}
	/**
	 * 获取日期星期一
	 * @param date
	 * @return
	 */
	public static Date[] getDateWeekMondayAndSunDay(Date date) {
		Date[] result = new Date[2];
		result[0] = getThisWeekSunDay(date);
		result[1] = getThisWeekSaturdayDay(date);
		return result;
		/*Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"),Locale.CHINA);
		cal.setFirstDayOfWeek(Calendar.SUNDAY);
		cal.setTime(date);
		// 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
		int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
		if (1 == dayWeek) {
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}
		// 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
		//cal.setFirstDayOfWeek(Calendar.MONDAY);
		// 获得当前日期是一个星期的第几天
		int day = cal.get(Calendar.DAY_OF_WEEK);
		// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
		cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		// 星期天
		result[0] = cal.getTime();

		cal.add(Calendar.DATE, 5);
		cal.set(Calendar.HOUR, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);

		//星期六
		result[1] = cal.getTime();
		return result;*/
	}
	
	public static Date getThisWeekSunDay(Date date){
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"),Locale.CHINA);
		cal.setTime(date);
		cal.setFirstDayOfWeek(Calendar.SUNDAY);
		int weekNum = cal.get(Calendar.DAY_OF_WEEK) - 1;
		cal.add(Calendar.DATE,  (7-weekNum) - 7);
		return cal.getTime();
	}
	public static Date getThisWeekSaturdayDay(Date date){
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"),Locale.CHINA);
		cal.setTime(date);
		cal.setFirstDayOfWeek(Calendar.SUNDAY);
		int weekNum = cal.get(Calendar.DAY_OF_WEEK);
		cal.add(Calendar.DATE, 7 - weekNum);
		return cal.getTime();
	}
	
	public static boolean isBewteen(Date beginTime, Date endTime, Date val) {
		Calendar date = Calendar.getInstance();
		date.setTime(val);

		Calendar begin = Calendar.getInstance();
		begin.setTime(beginTime);

		Calendar end = Calendar.getInstance();
		end.setTime(endTime);
		
		if(DateUtils.isSameDay(begin, date) || DateUtils.isSameDay(end, date)){
			return true;
		} 

		if (date.after(begin) && date.before(end)) {
			return true;
		} else {
			return false;
		}
		
	}
	/**
	 * 最大日期
	 * @param dates
	 * @return
	 */
	public static Date max(Date... dates){
		Date max = dates[0];
		for(int i = 0 ;i < dates.length ;i ++){
			if(dates[i] == null){
				continue;
			}
			if(max == null){
				max = dates[i];
				continue;
			}
			if(max.before(dates[i])){
				max = dates[i];
			}
		}
		return max;
	}
	/**
	 * 最小日期
	 * @param dates
	 * @return
	 */
	public static Date min(Date... dates){
		Date min = dates[0];
		for(int i = 0 ;i < dates.length ;i ++){
			if(dates[i] == null){
				continue;
			}
			if(min == null){
				min = dates[i];
				continue;
			}
			if(min.after(dates[i])){
				min = dates[i];
			}
		}
		return min;
	}
	public static void main(String[] args) throws ParseException {
		 DateFormat datetimeDf = new SimpleDateFormat("yyyy-MM-dd");  
		 Date begin = datetimeDf.parse("2018-03-11");
		 
		/* Date[] dateWeekMondayAndSunDay = getDateWeekMondayAndSunDay(begin);
		 System.out.println("begin:" + datetimeDf.format(dateWeekMondayAndSunDay[0]) + " end:" +datetimeDf.format(dateWeekMondayAndSunDay[1]) );
		*/ 
		/* LocalDate localDate = LocalDate.parse("2018-03-11");
		 LocalDate nextSunday = localDate.with(TemporalAdjusters.previous(DayOfWeek.SUNDAY));
		 System.out.println("begin:" + nextSunday.getYear() + "-" + nextSunday.getMonthValue() + "-" + nextSunday.getDayOfMonth());
		 LocalDate saturday = localDate.with(TemporalAdjusters.previous(DayOfWeek.SATURDAY));
		 System.out.println("end:" + saturday.getYear() + "-" + saturday.getMonthValue() + "-" + saturday.getDayOfMonth());
*/
		 
		System.out.println("begin:" + datetimeDf.format(getThisWeekSunDay(begin)) + " end:" +datetimeDf.format(getThisWeekSaturdayDay(begin)) );
	}
	
}

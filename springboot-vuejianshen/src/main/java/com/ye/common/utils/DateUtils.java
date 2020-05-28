package com.ye.common.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期处理
 * 
 */
public class DateUtils {
	/** 时间格式(yyyy-MM-dd) */
	public final static String DATE_PATTERN = "yyyy-MM-dd";
	/** 时间格式(yyyy-MM-dd HH:mm:ss) */
	public final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	
	public static String format(Date date) {
        return format(date, DATE_PATTERN);
    }

    public static String format(Date date, String pattern) {
        if(date != null){
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.format(date);
        }
        return null;
    }
    
    public static String getLastDayOfMonth(int year, int month) {     
        Calendar cal = Calendar.getInstance();     
        cal.set(Calendar.YEAR, year);     
        cal.set(Calendar.MONTH, month-1);     
        cal.set(Calendar.DAY_OF_MONTH,cal.getActualMaximum(Calendar.DATE));  
       return  new   SimpleDateFormat( "yyyy-MM-dd ").format(cal.getTime()); 
       
    }
    public static String getFirstDayOfMonth(int year, int month) {     
        Calendar cal = Calendar.getInstance();     
        cal.set(Calendar.YEAR, year);     
        cal.set(Calendar.MONTH, month-1);  
        cal.set(Calendar.DAY_OF_MONTH,cal.getMinimum(Calendar.DATE));  
       return   new   SimpleDateFormat( "yyyy-MM-dd ").format(cal.getTime());  
    }
}

package com.commons.orm.sql;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @Description: 日期工具类
 * @author hang
 * @date 2015-3-27 下午6:28:31
 * @version V1.7
 */
public class SqlDateUtil {

	private static final String date_format = "yyyy-MM-dd HH:mm:ss";
	private static ThreadLocal<DateFormat> threadLocal = new ThreadLocal<DateFormat>();

	public static DateFormat getDateFormat() {
		DateFormat df = threadLocal.get();
		if (df == null) {
			df = new SimpleDateFormat(date_format);
			threadLocal.set(df);
		}
		return df;
	}
	
	public static void removeThreadLocal(){
		threadLocal.remove();
	}

	public static long getformatDate(String time) {
		try {
			return getDateFormat().parse(time).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static String getformatString(long time) throws ParseException {
		Date date = new Date(time);
		return getDateFormat().format(date);
	}

	public static String formatDate(Date date) throws ParseException {
		return getDateFormat().format(date);
	}

	public static Date parse(String strDate) throws ParseException {
		return getDateFormat().parse(strDate);
	}

	public static Date parseCsharpDate(String str) throws ParseException {
		String formatStr = "MM/dd/yyyy HH:mm:ss";
		if (str.indexOf("T") > 0) {
			formatStr = "yyyy-MM-dd'T'HH:mm:ss";
		} else if (str.toUpperCase().indexOf("M") > 0) {
			final SimpleDateFormat format = new SimpleDateFormat(
					"MM/dd/yyyy hh:mm:ss a", Locale.US);
			return format.parse(str);
		}else if(str.indexOf("-") != -1){
			formatStr = "yyyy-MM-dd HH:mm:ss";
		}
//		System.out.println(formatStr);
		final SimpleDateFormat format = new SimpleDateFormat(formatStr);
		return format.parse(str);
	}
	
	public static Date parseTicks(long ticks) {
		// long milli = System.currentTimeMillis() + 8*3600*1000;
		// long ticks = (milli*10000)+621355968000000000L;
		if (ticks > 0) {
			long milli = (ticks - 621355968000000000L) / 10000;
			milli -= 8 * 3600 * 1000;
			return new Date(milli);
		}
		return null;
	}

	public static void main(String[] args) throws ParseException {
		System.err.println(parseCsharpDate("1/11/2015 8:5:01 PM"));
	}
}

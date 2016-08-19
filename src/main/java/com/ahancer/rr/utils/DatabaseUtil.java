package com.ahancer.rr.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DatabaseUtil {
	
	private static String DATABASE_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	
	public static String getDateTime(Date date){
		SimpleDateFormat sf = new SimpleDateFormat(DATABASE_DATE_FORMAT);
		return sf.format(date);
	}

}

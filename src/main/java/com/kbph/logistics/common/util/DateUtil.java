package com.kbph.logistics.common.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import lombok.experimental.UtilityClass;

@UtilityClass
public class DateUtil {
	public static String getDate(String... patterns) {
		String pattern = "yyyy-MM-dd";
		if(patterns.length!=0) {
			pattern= patterns[0];
		}
		return LocalDate.now().format(DateTimeFormatter.ofPattern(pattern));
	}

	public static String getTime(String...patterns) {
		String pattern = "HH:mm:ss";
		if(patterns.length!=0) {
			pattern= patterns[0];
		}
		return LocalTime.now().format(DateTimeFormatter.ofPattern(pattern));
	}
	
	public static String getDateTime(String... patterns) {
	    String pattern = "yyyy-MM-dd HH:mm:ss"; 
	    if (patterns.length != 0) {
	        pattern = patterns[0];
	    }
	    return LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));
	}
	
	public static List<String> getDateRange(String fromDate, String toDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		LocalDate confirmDateFrom = LocalDate.parse(fromDate);
		LocalDate confirmDateTo = LocalDate.parse(toDate);
		
		int dateDiffrence = confirmDateTo.getDayOfYear() - confirmDateFrom.getDayOfYear();
		
		List<String> confirmDates = new ArrayList<>();
		for(int i=0; i<=dateDiffrence; i++) {
			confirmDates.add(confirmDateFrom.plusDays(i).format(formatter));
		}
		return confirmDates;
	}
}

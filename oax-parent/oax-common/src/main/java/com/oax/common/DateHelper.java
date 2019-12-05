package com.oax.common;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateHelper {

    public static final DateTimeFormatter FULL = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static String format(Date date) {
        return format(date, FULL);
    }

    public static long currentTimeSeconds(){
        return System.currentTimeMillis()/1000;
    }

    public static String format(Long timestamp) {
        Date date = new Date(timestamp);
        return format(date);
    }

    public static String format(Date date, DateTimeFormatter formatter) {
        if (date == null) {
            return null;
        }
        LocalDateTime ld = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return ld.format(formatter);
    }

    public static Date parse(String str) {
        return parse(str, FULL);
    }

    public static Date parse(String str, DateTimeFormatter formatter) {
        LocalDateTime ldt = LocalDateTime.parse(str, formatter);
        return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 今天的开始时间，凌晨
     * @return
     */
    public static Date getTodayStart(){
        ZoneId zoneId = ZoneId.systemDefault();
        LocalTime midnight = LocalTime.MIDNIGHT;
        LocalDate today = LocalDate.now();
        LocalDateTime todayMidnight = LocalDateTime.of(today, midnight);
        ZonedDateTime zdt = todayMidnight.atZone(zoneId);
        return Date.from(zdt.toInstant());
    }

    /**
     * 明天的开始时间，凌晨
     * @return
     */
    public static Date getTomorrowStart(){
        ZoneId zoneId = ZoneId.systemDefault();
        LocalTime midnight = LocalTime.MIDNIGHT;
        LocalDate today = LocalDate.now();
        LocalDateTime todayMidnight = LocalDateTime.of(today, midnight);
        LocalDateTime yesterdayMidnight = todayMidnight.plusDays(1);
        ZonedDateTime zdt = yesterdayMidnight.atZone(zoneId);
        return Date.from(zdt.toInstant());
    }

}

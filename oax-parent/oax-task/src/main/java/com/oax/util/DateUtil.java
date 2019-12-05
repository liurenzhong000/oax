package com.oax.util;


import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2017/11/13 0013.
 */
public class DateUtil {

    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * 根据当前日期获得所在周的日期区间（周一和周日日期）
     * @return
     */
    public static String getTimeInterval(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        // System.out.println("要计算日期为:" + sdf.format(cal.getTime())); // 输出要计算日期
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // 获得当前日期是一个星期的第几天
        int day = cal.get(Calendar.DAY_OF_WEEK);
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
        String imptimeBegin = sdf.format(cal.getTime());
        // System.out.println("所在周星期一的日期：" + imptimeBegin);
        cal.add(Calendar.DATE, 6);
        String imptimeEnd = sdf.format(cal.getTime());
        // System.out.println("所在周星期日的日期：" + imptimeEnd);
        return imptimeBegin+ "," + imptimeEnd;
    }


    /**
     * 根据当前日期获得上周的日期区间（上周周一和周日日期）
     *
     * @return
     * @author zhaoxuepu
     */
    public static String getLastTimeInterval(Date date) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date);
        calendar1.add(Calendar.DAY_OF_WEEK,-1);
        Calendar calendar2 = Calendar.getInstance();
        int dayOfWeek = calendar1.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
        int offset1 = 2 - dayOfWeek;
        int offset2 = 7 - dayOfWeek;
        calendar1.add(Calendar.DATE, offset1 - 7);
        calendar2.add(Calendar.DATE, offset2 - 7);
        String lastBeginDate = sdf.format(calendar1.getTime());
        String lastEndDate = sdf.format(calendar2.getTime());
        return lastBeginDate+ "," + lastEndDate;
    }

    public static String parseDateToYYYYMMDD (Date date){
        return sdf.format(date);
    }
    public static String parseDateToYYYYMMDDHHMMSS (Date date){
        return simpleDateFormat.format(date);
    }
    /**
     * 获取当天开始时间
     * @return
     */
    public static String getStartTime(){
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        return simpleDateFormat.format(todayStart.getTime());
    }

    /**
     * 获取指定日期的开始时间
     * @param date
     * @return
     */
    public static String getStartTime(String date){
        Calendar startDate = Calendar.getInstance();
        startDate.setTime(parseStringToData(date));
        startDate.set(Calendar.HOUR_OF_DAY, 0);
        startDate.set(Calendar.MINUTE, 0);
        startDate.set(Calendar.SECOND, 0);
        startDate.set(Calendar.MILLISECOND, 0);
        return simpleDateFormat.format(startDate.getTime());
    }

    /**
     * 获取指定日期的开始时间
     * @param date
     * @return
     */
    public static Date getStartTimeDate(String date){
        Calendar startDate = Calendar.getInstance();
        startDate.setTime(parseStringToData(date));
        startDate.set(Calendar.HOUR_OF_DAY, 0);
        startDate.set(Calendar.MINUTE, 0);
        startDate.set(Calendar.SECOND, 0);
        startDate.set(Calendar.MILLISECOND, 0);
        return startDate.getTime();
    }

    /**
     * 获取当天结束时间
     * @return
     */
    public static String getnowEndTime() {
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.set(Calendar.HOUR_OF_DAY, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);
        return simpleDateFormat.format(todayEnd.getTime());
    }
    /**
     * 获取指定日期结束时间
     * @return
     */
    public static String getnowEndTime(String date) {
        System.out.println(date);
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.setTime(parseStringToData(date));
        todayEnd.set(Calendar.HOUR_OF_DAY, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);
        System.out.println(todayEnd.getTime());
        return simpleDateFormat.format(todayEnd.getTime());
    }

    /**
     * 获取指定日期结束时间
     * @return
     */
    public static Date getnowEndTimeDate(String date) {
        System.out.println(date);
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.setTime(parseStringToData(date));
        todayEnd.set(Calendar.HOUR_OF_DAY, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);
        System.out.println(todayEnd.getTime());
        return todayEnd.getTime();
    }

    /**
     * String to Date
     * @param date
     * @return
     */
    public static Date parseStringToData(String date){
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * String to Date
     * @param date
     * @return
     */
    public static Date parseStringToDataHMS(String date){
        try {
            return simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 获取i天后的日期
     * @param date
     * @param i
     * @return
     */
    public static String getTomorrow(String date,int i){
        Date today = parseStringToData(date);
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        c.add(Calendar.DAY_OF_MONTH, i);// 今天+1天
        Date tomorrow = c.getTime();
        return sdf.format(tomorrow);
    }

    /**
     * 获取i天后的日期
     * @param date
     * @param i
     * @return
     */
    public static String getLast(String date,int i){
        Date today = parseStringToData(date);
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        c.add(Calendar.DAY_OF_MONTH, -i);// 昨天-1天
        Date tomorrow = c.getTime();
        return sdf.format(tomorrow);
    }

    /**
     * 获取i年前的日期
     * @param date
     * @param i
     * @return
     */
    public static Date getDate(int i ){
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.YEAR, -i);// 今天+1天
        Date date = c.getTime();
        return date;
    }

    /**
     * 毫秒转时间
     * @param time
     * @return
     */
    public static Date getDate(Long time){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        Date date = c.getTime();
        return date;
    }
}

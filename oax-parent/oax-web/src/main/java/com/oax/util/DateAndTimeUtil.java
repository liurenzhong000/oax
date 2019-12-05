package com.oax.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateAndTimeUtil {

    /***
     * 日期减一天、加一天
     *
     * @param option
     *            传入类型 pro：日期减一天，next：日期加一天
     * @param _date
     *            2014-11-24
     * @return 减一天：2014-11-23或(加一天：2014-11-25)
     */
    public static String checkOption(String option, String _date,int amount) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cl = Calendar.getInstance();
        Date date = null;

        try {
            date = (Date) sdf.parse(_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cl.setTime(date);
        if ("pre".equals(option)) {
            // 时间减几天
            cl.add(Calendar.DAY_OF_MONTH, -amount);

        } else if ("next".equals(option)) {
            // 时间加几天
            cl.add(Calendar.DAY_OF_YEAR, amount);
        } else {
            // do nothing
        }
        date = cl.getTime();
        return sdf.format(date);
    }



}

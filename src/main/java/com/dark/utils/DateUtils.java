package com.dark.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by tengxue on 16-9-7.
 * 这个工具类主要是为了生成启动时间使用的
 */
public class DateUtils {

    private static final SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");

    public static String  getTime(Date time, int offset){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(time);
        calendar.add(Calendar.DAY_OF_MONTH,offset);
        return sdf.format(calendar.getTime());
    }

}

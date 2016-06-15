/**
 * Project Name:suzume-search
 * File Name:TimeUtil.java
 * Package Name:com.suzume.search.util
 * Date:2016年4月4日上午9:58:05
 * Copyright (c) 2016, All Rights Reserved.
 *
 */

package com.oneapm.es.util;

/**
 * ClassName:TimeUtil <br/>
 * Function: <br/>
 * Date: 2016年4月4日 上午9:58:05 <br/>
 * 
 * @author shengjie
 * @version
 * @since JDK 1.7
 * @see
 */
public class TimeUtil {
    
    /**
     * humanTime: <br/>
     * 
     * @author shengjie
     * @param date
     * @return
     * @since JDK 1.7
     */
    public static String humanTime(long date) {
        if (date /
            1000L /
            60L /
            60L /
            24L > 0L) {
            return String.valueOf(date /
                                  1000L /
                                  60L /
                                  60L /
                                  24L) +
                   "天" +
                   humanTime(date %
                             (1000L * 60L * 60L * 24L));
        }
        if (date / 1000L / 60L / 60L > 0L) {
            return String.valueOf(date / 1000L / 60L / 60L) +
                   "小时" +
                   humanTime(date %
                             (1000L * 60L * 60L));
        }
        if (date / 1000L / 60L > 0L) {
            return String.valueOf(date / 1000L / 60L) +
                   "分钟" +
                   humanTime(date %
                             (1000L * 60L));
        }
        if (date / 1000L > 0L) {
            return String.valueOf(date / 1000L) +
                   "秒" +
                   humanTime(date % 1000L);
        }
        return String.valueOf(date) +
               "毫秒";
    }
    
    /**
     * calcDuration: <br/>
     * 
     * @author xushjie
     * @param duration
     * @return
     * @since JDK 1.7
     */
    public static Long calcDuration(String duration) {
        //
        if (null == duration ||
            "".equals(duration) ||
            duration.length() < 2) {
            return 0L;
        }
        //
        String l = duration.substring(0,
                                      duration.length() - 1);
        Long base = null;
        try {
            base = Long.valueOf(l);
        } catch (Exception e) {
            return 0L;
        }
        String u = duration.substring(duration.length() - 1);
        //
        switch (u) {
            case "y":
            case "Y":
                return Long.valueOf(base *
                                    12 *
                                    30 *
                                    24 *
                                    60 *
                                    60 *
                                    1000);
            case "M":
                return Long.valueOf(base *
                                    30 *
                                    24 *
                                    60 *
                                    60 *
                                    1000);
            case "w":
            case "W":
                return Long.valueOf(base *
                                    7 *
                                    24 *
                                    60 *
                                    60 *
                                    1000);
            case "d":
            case "D":
                return Long.valueOf(base *
                                    24 *
                                    60 *
                                    60 *
                                    1000);
            case "h":
            case "H":
                return Long.valueOf(base * 60 * 60 * 1000);
            case "m":
                return Long.valueOf(base * 60 * 1000);
            case "s":
            case "S":
                return Long.valueOf(base * 1000);
            default:
                return Long.valueOf(base * 1000);
        }
    }
    
    public static void main(String[] args) {
        {
            System.out.println(calcDuration("1y"));
        }
        {
            System.out.println(calcDuration("1M"));
        }
        {
            System.out.println(calcDuration("1w"));
        }
        {
            System.out.println(calcDuration("1d"));
        }
        {
            System.out.println(calcDuration("1h"));
        }
        {
            System.out.println(calcDuration("1m"));
        }
        {
            System.out.println(calcDuration("1s"));
        }
    }
    
}

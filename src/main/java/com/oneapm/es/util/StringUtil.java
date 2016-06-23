/**
 * Project Name:elasticsearch-demos
 * File Name:StringUtil.java
 * Package Name:com.oneapm.es.util
 * Date:2016年6月15日下午2:52:37
 * Copyright (c) 2016, All Rights Reserved.
 *
 */

package com.oneapm.es.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * ClassName:StringUtil <br/>
 * Function: <br/>
 * Date: 2016年6月15日 下午2:52:37 <br/>
 * @author xushjie
 * @version
 * @since JDK 1.7
 * @see
 */
public class StringUtil {
    
    public static final DateFormat AKKA_LOG_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    
    /**
     * startWithLogLevel: <br/>
     * @author xushjie
     * @param str
     * @return
     * @since JDK 1.7
     */
    public static boolean startWithLogLevel(String str) {
        //
        if (null == str ||
            "".equals(str)) {
            return false;
        }
        //
        return startWith(str,
                         "INFO") ||
               startWith(str,
                         "DEBUG") ||
               startWith(str,
                         "ERROR") ||
               startWith(str,
                         "WARN") ||
               startWith(str,
                         "TRACE");
    }
    
    private static boolean startWith(String source,
                                     String target) {
        if (source.length() < target.length()) {
            return false;
        }
        String sub = source.substring(0,
                                      target.length());
        return target.equalsIgnoreCase(sub);
    }
    
    /**
     * startWithTimestamp: <br/>
     * @author xushjie
     * @param line
     * @param prefix
     * @param pattern
     * @return
     * @since JDK 1.7
     */
    public static boolean startWithTimestamp(String line,
                                             Integer prefix,
                                             DateFormat pattern) {
        if (line == null ||
            "".equals(line) ||
            line.length() < prefix.intValue() ||
            prefix.intValue() <= 0) {
            return false;
        }
        try {
            pattern.parse(line.substring(0,
                                         prefix.intValue()));
        } catch (ParseException e) {
            return false;
        }
        return true;
    }
    
    /**
     * validKeepAlive: <br/>
     * @author xushjie
     * @param keepAlive
     * @return
     * @since JDK 1.7
     */
    public static boolean validKeepAlive(String keepAlive) {
        //
        if (keepAlive == null ||
            "".equals(keepAlive)) {
            return false;
        }
        //
        return keepAlive.matches("\\d+[yMwdhms]");
    }
    
    public static void main(String[] args) {
        System.out.println(validKeepAlive("3d"));
        System.out.println(validKeepAlive("d"));
        System.out.println(validKeepAlive("3"));
        System.out.println(validKeepAlive("ad"));
        System.out.println(validKeepAlive(""));
    }
    
    /**
     * parseBatchSize: <br/>
     * @author xushjie
     * @param batchSize
     * @return
     * @since JDK 1.7
     */
    public static Long parseBatchSize(String batchSize) {
        assert batchSize != null;
        assert !"".equals(batchSize);
        assert batchSize.length() > 1;
        //
        String prefix = "";
        switch (batchSize.substring(batchSize.length() - 1,
                                    batchSize.length())) {
            case "k":
            case "K":
                prefix = batchSize.substring(0,
                                             batchSize.length() - 1);
                return (long) (Double.parseDouble(prefix) * 1024L);
            case "m":
            case "M":
                prefix = batchSize.substring(0,
                                             batchSize.length() - 1);
                return (long) (Double.parseDouble(prefix) * 1024L * 1024L);
            case "g":
            case "G":
                prefix = batchSize.substring(0,
                                             batchSize.length() - 1);
                return (long) (Double.parseDouble(prefix) * 1024L * 1024L * 1024L);
            default:
                prefix = batchSize.substring(0,
                                             batchSize.length() - 1);
                return (long) (Double.parseDouble(prefix));
        }
    }
    
}

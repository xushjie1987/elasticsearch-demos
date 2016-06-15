/**
 * Project Name:elasticsearch-demos
 * File Name:StringUtil.java
 * Package Name:com.oneapm.es.util
 * Date:2016年6月15日下午2:52:37
 * Copyright (c) 2016, All Rights Reserved.
 *
 */

package com.oneapm.es.util;

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

}

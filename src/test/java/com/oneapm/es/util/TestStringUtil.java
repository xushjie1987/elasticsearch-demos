/**
 * Project Name:elasticsearch-demos
 * File Name:TestStringUtil.java
 * Package Name:com.oneapm.es.util
 * Date:2016年6月21日上午10:34:31
 * Copyright (c) 2016, All Rights Reserved.
 *
 */

package com.oneapm.es.util;

import org.junit.Test;

/**
 * ClassName:TestStringUtil <br/>
 * Function: <br/>
 * Date: 2016年6月21日 上午10:34:31 <br/>
 * @author xushjie
 * @version
 * @since JDK 1.7
 * @see
 */
public class TestStringUtil {

    @Test
    public void testStringSplit() {
        String s = "abc   \t\t\t\n\nljljljsfds   \t\t   fds";
        String[] p = s.split("\\s+");
        System.out.println(p.length);
    }

}

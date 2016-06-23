/**
 * Project Name:elasticsearch-demos
 * File Name:TestFile.java
 * Package Name:com.oneapm.es.util
 * Date:2016年6月21日下午4:14:08
 * Copyright (c) 2016, All Rights Reserved.
 *
 */

package com.oneapm.es.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;

/**
 * ClassName:TestFile <br/>
 * Function: <br/>
 * Date: 2016年6月21日 下午4:14:08 <br/>
 * @author xushjie
 * @version
 * @since JDK 1.7
 * @see
 */
public class TestFile {
    
    private StringBuffer sb = new StringBuffer();
    
    @Test
    public void testRead() throws IOException {
        File f = new File("F:/logs/cassandra/97/system.log");
        FileReader fr = new FileReader(f);
        BufferedReader br = new BufferedReader(fr);
        Long num = 0L;
        while (true) {
            String line = getUnit(br);
            if (null == line ||
                "".equals(line)) {
                break;
            }
            num++;
            System.out.println(num +
                               "\t" +
                               line);
        }
    }
    
    private String getUnit(BufferedReader reader) {
        String line;
        try {
            while (true) {
                // read
                line = reader.readLine();
                // end of file
                if (line == null ||
                    "".equals(line)) {
                    if (null == sb.toString() ||
                        "".equals(sb.toString())) {
                        return "";
                    } else {
                        String tmp = sb.toString();
                        sb = new StringBuffer();
                        return tmp;
                    }
                }
                // normal
                if (StringUtil.startWithLogLevel(line)) {
                    if (null == sb.toString() ||
                        "".equals(sb.toString())) {
                        sb.append(line);
                    } else {
                        String tmp = sb.toString();
                        sb = new StringBuffer();
                        sb.append(tmp);
                        return tmp;
                    }
                    continue;
                }
                // exception
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (null == sb.toString() ||
                "".equals(sb.toString())) {
                return "";
            } else {
                String tmp = sb.toString();
                sb = new StringBuffer();
                return tmp;
            }
        }
    }
    
}

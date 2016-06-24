/**
 * Project Name:elasticsearch-demos
 * File Name:TestGrok.java
 * Package Name:com.oneapm.es.grok
 * Date:2016年6月24日下午7:07:35
 * Copyright (c) 2016, All Rights Reserved.
 *
 */

package com.oneapm.es.grok;

import oi.thekraken.grok.api.Grok;
import oi.thekraken.grok.api.Match;
import oi.thekraken.grok.api.exception.GrokException;

import org.junit.Test;

/**
 * ClassName:TestGrok <br/>
 * Function: <br/>
 * Date: 2016年6月24日 下午7:07:35 <br/>
 * @author xushjie
 * @version
 * @since JDK 1.7
 * @see
 */
public class TestGrok {
    
    @Test
    public void test01() throws GrokException {
        Grok g = new Grok();
        g.addPatternFromFile(this.getClass()
                                 .getResource("/")
                                 .getPath() +
                             "/pattern/patterns");
        g.compile("%{USERNAME:username}");
        Match gm = g.match("zhangsan");
        gm.captures();
        // See the result
        System.out.println(gm.toJson(true));
    }
    
    @Test
    public void test02() throws GrokException {
        Grok g = new Grok();
        g.compile("%{XSJ:xsj=\\w+}");
        Match gm = g.match("zhangsan");
        gm.captures();
        // See the result
        System.out.println(gm.toJson(true));
    }
    
    /**
     * test03: <br/>
     * 这里定义的正则表达式，禁止出现{} <br>
     * @author xushjie
     * @throws GrokException
     * @since JDK 1.7
     */
    @Test
    public void test03() throws GrokException {
        Grok g = new Grok();
        g.compile("%{XSJ:xsj=[\\{\\}]+}");
        Match gm = g.match("{}{}{}{}{}");
        gm.captures();
        // See the result
        System.out.println(gm.toJson(true));
    }
    
    @Test
    public void test04() throws GrokException {
        Grok g = new Grok();
        g.compile("%{XSJ:xsj=.*}");
        Match gm = g.match(".a.");
        gm.captures();
        // See the result
        System.out.println(gm.toJson(true));
    }
    
    @Test
    public void test05() throws GrokException {
        Grok g = new Grok();
        g.addPattern("XXX",
                     "a{3}");
        g.addPattern("YYY",
                     "b{3}");
        g.addPattern("ZZZ",
                     "%{XXX:x}%{YYY:y}");
        g.compile("%{ZZZ:z}");
        Match gm = g.match("aaabbb");
        gm.captures();
        // See the result
        System.out.println(gm.toJson(true));
    }
    
    @Test
    public void test06() throws GrokException {
        Grok g = new Grok();
        g.addPattern("XXX",
                     "a{3}");
        g.addPattern("YYY",
                     "b{3}");
        g.addPattern("ZZZ",
                     "(?:%{XXX:x}%{YYY:y})");
        g.compile("%{ZZZ:z}");
        Match gm = g.match("aaabbb");
        gm.captures();
        // See the result
        System.out.println(gm.toJson(true));
    }
    
    /**
     * test07: <br/>
     * (?:(?<name0>(?:(?<name1>a{3})(?<name2>b{3})))) <br>
     * @author xushjie
     * @throws GrokException
     * @since JDK 1.7
     */
    @Test
    public void test07() throws GrokException {
        Grok g = new Grok();
        g.addPattern("XXX",
                     "a{3}");
        g.addPattern("YYY",
                     "b{3}");
        g.addPattern("ZZZ",
                     "(?:%{XXX:x}%{YYY:y})");
        g.compile("(?:%{ZZZ})");
        System.out.println(g.getNamedRegex());
        Match gm = g.match("aaabbb");
        gm.captures();
        // See the result
        System.out.println(gm.toJson(true));
    }
    
    @Test
    public void test08() throws GrokException {
        Grok g = new Grok();
        g.addPattern("XXX",
                     "a{3}");
        g.addPattern("YYY",
                     "b{3}");
        g.addPattern("ZZZ",
                     "(?:%{XXX:x}%{YYY:y})");
        g.compile("%{XXX:x}%{YYY:y}");
        System.out.println(g.getNamedRegex());
        Match gm = g.match("aaabbb");
        gm.captures();
        // See the result
        System.out.println(gm.toJson(true));
    }
    
    @Test
    public void test09() throws GrokException {
        Grok g = new Grok();
        g.addPattern("XXX",
                     "a{3}");
        g.addPattern("YYY",
                     "b{3}");
        g.compile("%{XXX:x}\n\n\\{%{YYY:y}");
        System.out.println(g.getNamedRegex());
        Match gm = g.match("aaa\n\n{bbb");
        gm.captures();
        // See the result
        System.out.println(gm.toJson(true));
    }
    
    @Test
    public void test10() throws GrokException {
        System.out.println("a\na".matches("a\na"));
        System.out.println("a\na".matches("a\\na"));
    }
    
}

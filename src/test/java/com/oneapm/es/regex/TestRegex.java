/**
 * Project Name:elasticsearch-demos
 * File Name:TestRegex.java
 * Package Name:com.oneapm.es.regex
 * Date:2016年6月23日下午5:19:07
 * Copyright (c) 2016, All Rights Reserved.
 *
 */

package com.oneapm.es.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

/**
 * ClassName:TestRegex <br/>
 * Function: <br/>
 * Date: 2016年6月23日 下午5:19:07 <br/>
 * @author xushjie
 * @version
 * @since JDK 1.7
 * @see
 */
public class TestRegex {
    
    @Test
    public void testMatch01() {
        //
        String s = "ABC";
        Pattern p = Pattern.compile("^ABC$");
        Matcher m = p.matcher(s);
        //
        while (m.find()) {
            System.out.println(m.start());
            System.out.println(m.end());
            System.out.println("***********************************");
        }
    }
    
    @Test
    public void testMatch02() {
        //
        String s = "ABC";
        Pattern p = Pattern.compile("^abc$");
        Matcher m = p.matcher(s);
        //
        while (m.find()) {
            System.out.println(m.start());
            System.out.println(m.end());
            System.out.println("***********************************");
        }
    }
    
    /**
     * testMatch03: <br/>
     * 进行多行跨行匹配，因此能够匹配成功 <br>
     * 将就是将多行视作一行处理 <br>
     * @author xushjie
     * @since JDK 1.7
     */
    @Test
    public void testMatch03() {
        //
        String s = "abc\r\n\r\n\n\n\nabc";
        Pattern p = Pattern.compile("^abc$",
                                    Pattern.MULTILINE);
        Matcher m = p.matcher(s);
        //
        while (m.find()) {
            System.out.println(m.start());
            System.out.println(m.end());
            System.out.println("***********************************");
        }
    }
    
    /**
     * testMatch04: <br/>
     * 默认按照每行进行匹配 <br>
     * 匹配失败 <br>
     * @author xushjie
     * @since JDK 1.7
     */
    @Test
    public void testMatch04() {
        //
        String s = "abc\r\n\r\n\n\n\nabc";
        Pattern p = Pattern.compile("^abc$");
        Matcher m = p.matcher(s);
        //
        while (m.find()) {
            System.out.println(m.start());
            System.out.println(m.end());
            System.out.println("***********************************");
        }
    }
    
    /**
     * testMatch05: <br/>
     * 匹配失败 <br>
     * @author xushjie
     * @since JDK 1.7
     */
    @Test
    public void testMatch05() {
        //
        String s = "abc\r\n\r\n\n\n\n";
        Pattern p = Pattern.compile("^abc$");
        Matcher m = p.matcher(s);
        //
        while (m.find()) {
            System.out.println(m.start());
            System.out.println(m.end());
            System.out.println("***********************************");
        }
    }
    
    /**
     * testMatch06: <br/>
     * 匹配成功 <br>
     * @author xushjie
     * @since JDK 1.7
     */
    @Test
    public void testMatch06() {
        //
        String s = "abc\r\n\r\n\n\n\n";
        Pattern p = Pattern.compile("^abc$",
                                    Pattern.MULTILINE);
        Matcher m = p.matcher(s);
        //
        while (m.find()) {
            System.out.println(m.start());
            System.out.println(m.end());
            System.out.println("***********************************");
        }
    }
    
    /**
     * testMatch07: <br/>
     * 匹配失败 <br>
     * @author xushjie
     * @since JDK 1.7
     */
    @Test
    public void testMatch07() {
        //
        String s = "ab\r\n\r\n\n\n\nc";
        Pattern p = Pattern.compile("^abc$",
                                    Pattern.MULTILINE);
        Matcher m = p.matcher(s);
        //
        while (m.find()) {
            System.out.println(m.start());
            System.out.println(m.end());
            System.out.println("***********************************");
        }
    }
    
    // 小结：多行模式匹配每一个分隔符划分的区间的开头和结尾的区间，而普通模式则匹配整个输入序列
    
    /**
     * testMatch08: <br/>
     * 匹配失败 <br>
     * @author xushjie
     * @since JDK 1.7
     */
    @Test
    public void testMatch08() {
        //
        String s = "ab\nc";
        Pattern p = Pattern.compile("^abc$",
                                    Pattern.MULTILINE);
        Matcher m = p.matcher(s);
        //
        while (m.find()) {
            System.out.println(m.start());
            System.out.println(m.end());
            System.out.println("***********************************");
        }
    }
    
    /**
     * testMatch09: <br/>
     * 匹配失败 <br>
     * @author xushjie
     * @since JDK 1.7
     */
    @Test
    public void testMatch09() {
        //
        String s = "ab\nc";
        Pattern p = Pattern.compile("^abc$");
        Matcher m = p.matcher(s);
        //
        while (m.find()) {
            System.out.println(m.start());
            System.out.println(m.end());
            System.out.println("***********************************");
        }
    }
    
    /**
     * testMatch10: <br/>
     * 匹配失败 <br>
     * @author xushjie
     * @since JDK 1.7
     */
    @Test
    public void testMatch10() {
        //
        String s = "abc\nabc";
        Pattern p = Pattern.compile("^abc$");
        Matcher m = p.matcher(s);
        //
        while (m.find()) {
            System.out.println(m.start());
            System.out.println(m.end());
            System.out.println("***********************************");
        }
    }
    
    /**
     * testMatch11: <br/>
     * 匹配成功，匹配到2个match <br>
     * @author xushjie
     * @since JDK 1.7
     */
    @Test
    public void testMatch11() {
        //
        String s = "abc\nabc";
        Pattern p = Pattern.compile("^abc$",
                                    Pattern.MULTILINE);
        Matcher m = p.matcher(s);
        //
        while (m.find()) {
            System.out.println(m.start());
            System.out.println(m.end());
            System.out.println("***********************************");
        }
    }
    
    // 小结：多行模式和普通模式区别：多行模式针对每一行分别认定^和$符号，表示行首和行尾，但是普通模式以输入串的开启和输入结束作为^和$
    
    /**
     * testMatch12: <br/>
     * 匹配成功，说明Pattern可以同时应用多个，以按位或操作符|作为多个Pattern的结合操作符 <br>
     * @author xushjie
     * @since JDK 1.7
     */
    @Test
    public void testMatch12() {
        //
        String s = "abc\nabc";
        Pattern p = Pattern.compile("^ABC$",
                                    Pattern.MULTILINE |
                                            Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(s);
        //
        while (m.find()) {
            System.out.println(m.start());
            System.out.println(m.end());
            System.out.println("***********************************");
        }
    }
    
    /**
     * testMatch13: <br/>
     * 先尽可能多匹配，再回溯 <br>
     * 先尽可能少匹配，再回溯 <br>
     * 先尽可能多匹配，不回溯 <br>
     * @author xushjie
     * @since JDK 1.7
     */
    @Test
    public void testMatch13() {
        // Greedy quantifiers
        {
            //
            String s = "xfooxxxfoo";
            Pattern p = Pattern.compile(".*foo");
            Matcher m = p.matcher(s);
            //
            System.out.println("Greedy quantifiers");
            while (m.find()) {
                System.out.print(m.start());
                System.out.print("\t\t");
                System.out.println(m.end());
            }
        }
        // Reluctant quantifiers
        {
            //
            String s = "xfooxxxfoo";
            Pattern p = Pattern.compile(".*?foo");
            Matcher m = p.matcher(s);
            //
            System.out.println("Reluctant quantifiers");
            while (m.find()) {
                System.out.print(m.start());
                System.out.print("\t\t");
                System.out.println(m.end());
            }
        }
        // Possessive quantifiers
        {
            //
            String s = "xfooxxxfoo";
            Pattern p = Pattern.compile(".*+foo");
            Matcher m = p.matcher(s);
            //
            System.out.println("Possessive quantifiers");
            while (m.find()) {
                System.out.print(m.start());
                System.out.print("\t\t");
                System.out.println(m.end());
            }
        }
    }
    
    /**
     * testMatch14: <br/>
     * 默认数字分组group <br>
     * 名称分组group <br>
     * @author xushjie
     * @since JDK 1.7
     */
    @Test
    public void testMatch14() {
        // (X) X, as a capturing group
        {
            //
            String s = "fooxxfooxx";
            Pattern p = Pattern.compile("((foo){1}(xx){1})(fooxx)");
            Matcher m = p.matcher(s);
            //
            System.out.println("(X) X, as a capturing group");
            while (m.find()) {
                System.out.println("group 1: " +
                                   m.group(1));
                System.out.println("group 2: " +
                                   m.group(2));
                System.out.println("group 3: " +
                                   m.group(3));
                System.out.println("group 4: " +
                                   m.group(4));
            }
            System.out.println();
            System.out.println();
        }
        // (?<name>X) X, as a named-capturing group
        {
            //
            String s = "fooxxfooxx";
            Pattern p = Pattern.compile("(?<g1>(?<g2>foo){1}(?<g3>xx){1})(?<g4>fooxx)");
            Matcher m = p.matcher(s);
            //
            System.out.println("(?<name>X)  X, as a named-capturing group");
            while (m.find()) {
                System.out.println("group g1: " +
                                   m.group("g1"));
                System.out.println("group g2: " +
                                   m.group("g2"));
                System.out.println("group g3: " +
                                   m.group("g3"));
                System.out.println("group g4: " +
                                   m.group("g4"));
            }
            System.out.println();
            System.out.println();
        }
    }
    
    /**
     * testMatch15: <br/>
     * 非捕获组 <br>
     * @author xushjie
     * @since JDK 1.7
     */
    @Test
    public void testMatch15() {
        // (?:X) X, as a non-capturing group
        {
            //
            String s = "fooxxfooxx";
            Pattern p = Pattern.compile("(?<g1>(?<g2>foo){1}(?:xx){1})(?<g4>fooxx)");
            Matcher m = p.matcher(s);
            //
            System.out.println("(?:X)   X, as a non-capturing group");
            while (m.find()) {
                System.out.println("group g1: " +
                                   m.group("g1"));
                System.out.println("group g2: " +
                                   m.group("g2"));
                System.out.println("group g4: " +
                                   m.group("g4"));
            }
            System.out.println();
            System.out.println();
        }
    }
    
    /**
     * testMatch16: <br/>
     * 向右看，不捕获，多重验证 <br>
     * 向左看，不捕获，多重验证 <br>
     * 不捕获，不回溯 <br>
     * @author xushjie
     * @since JDK 1.7
     */
    @Test
    public void testMatch16() {
        // (?=X) X, via zero-width positive lookahead
        {
            //
            String s = "xxfoo";
            Pattern p = Pattern.compile("xx(?=[a-z]{3}$)(foo){1}");
            Matcher m = p.matcher(s);
            //
            System.out.println("(?=X) X, via zero-width positive lookahead");
            while (m.find()) {
                System.out.println(m.group());
            }
            System.out.println();
            System.out.println();
        }
        // (?=X) X, via zero-width positive lookahead
        {
            //
            String s = "xxfoo";
            Pattern p = Pattern.compile("xx(?=[a-z]{2}$)(foo){1}");
            Matcher m = p.matcher(s);
            //
            System.out.println("(?=X) X, via zero-width positive lookahead");
            while (m.find()) {
                System.out.println(m.group());
            }
            System.out.println();
            System.out.println();
        }
        // (?=X) X, via zero-width positive lookahead
        {
            //
            String s = "xxfoo";
            Pattern p = Pattern.compile("xx(?=[a-z]{3}$)(.*?o.*?)");
            Matcher m = p.matcher(s);
            //
            System.out.println("(?=X) X, via zero-width positive lookahead");
            while (m.find()) {
                System.out.println(m.group(1));
            }
            System.out.println();
            System.out.println();
        }
        // (?!X) X, via zero-width negative lookahead
        {
            //
            String s = "xxfoo";
            Pattern p = Pattern.compile("xx(?![a-z]{2}$)(.*?o.*?)");
            Matcher m = p.matcher(s);
            //
            System.out.println("(?!X)   X, via zero-width negative lookahead");
            while (m.find()) {
                System.out.println(m.group(1));
            }
            System.out.println();
            System.out.println();
        }
        // (?<=X) X, via zero-width positive lookbehind
        {
            //
            String s = "foofoo";
            Pattern p = Pattern.compile("foo(?<=^[a-z]{3})(.*?o.*?)");
            Matcher m = p.matcher(s);
            //
            System.out.println("(?<=X)  X, via zero-width positive lookbehind");
            while (m.find()) {
                System.out.println(m.group(1));
            }
            System.out.println();
            System.out.println();
        }
        // (?<!X) X, via zero-width negative lookbehind
        {
            //
            String s = "foofoo";
            Pattern p = Pattern.compile("foo(?<!^[a-z]{2})(.*?o.*?)");
            Matcher m = p.matcher(s);
            //
            System.out.println("(?<!X)  X, via zero-width negative lookbehind");
            while (m.find()) {
                System.out.println(m.group(1));
            }
            System.out.println();
            System.out.println();
        }
        // (?>X) X, as an independent, non-capturing group
        {
            //
            String s = "fooxx";
            Pattern p = Pattern.compile("^(?>foo|foox)x$");
            Matcher m = p.matcher(s);
            //
            System.out.println("(?>X)   X, as an independent, non-capturing group");
            while (m.find()) {
                System.out.println(m.group(1));
            }
            System.out.println();
            System.out.println();
        }
        // (?>X) X, as an independent, non-capturing group
        {
            //
            String s = "fooxx";
            Pattern p = Pattern.compile("^(.*)x$");
            Matcher m = p.matcher(s);
            //
            System.out.println("正常可回溯场景，(?>X)   X, as an independent, non-capturing group");
            while (m.find()) {
                System.out.println(m.group(1));
            }
            System.out.println();
            System.out.println();
        }
        // (?>X) X, as an independent, non-capturing group
        {
            //
            String s = "fooxx";
            Pattern p = Pattern.compile("^(?>.*)x$");
            Matcher m = p.matcher(s);
            //
            System.out.println("不可回溯，(?>X)   X, as an independent, non-capturing group");
            while (m.find()) {
                System.out.println(m.group(1));
            }
            System.out.println();
            System.out.println();
        }
    }
    
    /**
     * testMatch17: <br/>
     * 或者关系 <br>
     * @author xushjie
     * @since JDK 1.7
     */
    @Test
    public void testMatch17() {
        // X|Y Either X or Y
        {
            //
            String s = "xxfoo";
            Pattern p = Pattern.compile("xx|foo");
            Matcher m = p.matcher(s);
            //
            System.out.println("X|Y Either X or Y");
            while (m.find()) {
                System.out.print(m.start());
                System.out.print("\t\t");
                System.out.print(m.end());
                System.out.print("\t\t");
                System.out.print(m.group());
                System.out.print("\t\t");
                System.out.println(m.toString());
            }
            System.out.println();
            System.out.println();
        }
    }
    
    /**
     * testMatch18: <br/>
     * 正则regex中的空白字符对正则的含义是有影响的 <br>
     * @author xushjie
     * @since JDK 1.7
     */
    @Test
    public void testMatch18() {
        // X|Y Either X or Y
        {
            //
            String s = "xxfoo";
            Pattern p = Pattern.compile(" x "
                                        + "x | f o "
                                        + " o ");
            Matcher m = p.matcher(s);
            //
            System.out.println("X|Y Either X or Y");
            while (m.find()) {
                System.out.print(m.start());
                System.out.print("\t\t");
                System.out.print(m.end());
                System.out.print("\t\t");
                System.out.print(m.group());
                System.out.print("\t\t");
                System.out.println(m.toString());
            }
            System.out.println();
            System.out.println();
        }
    }
    
    @Test
    public void testMatch19() {
        //
        {
            //
            String s = "xfooxxxfoo";
            Pattern p = Pattern.compile(".*foo");
            Matcher m = p.matcher(s);
            //
            System.out.println("");
            while (m.find()) {
                System.out.print(m.start());
                System.out.print("\t\t");
                System.out.println(m.end());
            }
        }
    }
    
    @Test
    public void testMatch20() {
        //
        {
            //
            String s = "xfooxxxfoo";
            Pattern p = Pattern.compile(".*foo");
            Matcher m = p.matcher(s);
            //
            System.out.println("");
            while (m.find()) {
                System.out.print(m.start());
                System.out.print("\t\t");
                System.out.println(m.end());
            }
        }
    }
    
    @Test
    public void testMatch21() {
        //
        {
            //
            String s = "xfooxxxfoo";
            Pattern p = Pattern.compile(".*foo");
            Matcher m = p.matcher(s);
            //
            System.out.println("");
            while (m.find()) {
                System.out.print(m.start());
                System.out.print("\t\t");
                System.out.println(m.end());
            }
        }
    }
    
    @Test
    public void testMatch22() {
        //
        {
            //
            String s = "xfooxxxfoo";
            Pattern p = Pattern.compile(".*foo");
            Matcher m = p.matcher(s);
            //
            System.out.println("");
            while (m.find()) {
                System.out.print(m.start());
                System.out.print("\t\t");
                System.out.println(m.end());
            }
        }
    }
    
    @Test
    public void testMatch23() {
        //
        {
            //
            String s = "xfooxxxfoo";
            Pattern p = Pattern.compile(".*foo");
            Matcher m = p.matcher(s);
            //
            System.out.println("");
            while (m.find()) {
                System.out.print(m.start());
                System.out.print("\t\t");
                System.out.println(m.end());
            }
        }
    }
    
    @Test
    public void testMatch24() {
        //
        {
            //
            String s = "xfooxxxfoo";
            Pattern p = Pattern.compile(".*foo");
            Matcher m = p.matcher(s);
            //
            System.out.println("");
            while (m.find()) {
                System.out.print(m.start());
                System.out.print("\t\t");
                System.out.println(m.end());
            }
        }
    }
    
}

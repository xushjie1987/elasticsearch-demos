/**
 * Project Name:elasticsearch-demos
 * File Name:RESTfulMain.java
 * Package Name:com.oneapm.es.logs.rest
 * Date:2016年6月22日上午10:56:38
 * Copyright (c) 2016, All Rights Reserved.
 *
 */

package com.oneapm.es.logs.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.oneapm.es.logs.rest.srvc.ElasticSearchEngine;

/**
 * ClassName:RESTfulMain <br/>
 * Function: <br/>
 * Date: 2016年6月22日 上午10:56:38 <br/>
 * @author xushjie
 * @version
 * @since JDK 1.7
 * @see
 */
@SpringBootApplication(scanBasePackages = { "com.oneapm.es.logs.rest" })
public class RESTfulMain {
    
    /**
     * main: <br/>
     * @author xushjie
     * @param args
     * @since JDK 1.7
     */
    public static void main(String[] args) {
        SpringApplication.run(new Object[] { RESTfulMain.class },
                              args);
    }
    
    @Bean(name = "engine",
          initMethod = "init",
          destroyMethod = "destroy")
    public ElasticSearchEngine getEngine() {
        ElasticSearchEngine engine = new ElasticSearchEngine();
        engine.setIp("10.45.10.213");
        engine.setPort(9300);
        engine.setCluster("cluster_1");
        return engine;
    }
    
}

/**
 * Project Name:elasticsearch-demos
 * File Name:Demo1.java
 * Package Name:com.oneapm.es.connect
 * Date:2016年4月15日上午11:08:20
 * Copyright (c) 2016, All Rights Reserved.
 *
 */

package com.oneapm.es.connect;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import com.oneapm.es.util.ToString;

/**
 * ClassName:Demo1 <br/>
 * Function: <br/>
 * Date: 2016年4月15日 上午11:08:20 <br/>
 * @author xushjie
 * @version
 * @since JDK 1.7
 * @see
 */
public class Demo1 {
    
    /**
     * main: <br/>
     * @author xushjie
     * @param args
     * @throws UnknownHostException
     * @since JDK 1.7
     */
    public static void main(String[] args) throws UnknownHostException {
        //
        Settings settings = Settings.settingsBuilder()
                                    .put("cluster.name",
                                         "x_es_cluster")
                                    .build();
        //
        Client client = TransportClient.builder()
                                       .settings(settings)
                                       .build()
                                       .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("10.128.7.130"),
                                                                                           9500));
        //
        GetResponse response = client.prepareGet("restored_test_1",
                                                 "external",
                                                 "1")
                                     .setFields("name",
                                                "_source")
                                     .execute()
                                     .actionGet();
        //
        System.out.println(ToString.toStringHelper(response));
    }
}

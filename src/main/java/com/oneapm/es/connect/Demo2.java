/**
 * Project Name:elasticsearch-demos
 * File Name:Demo2.java
 * Package Name:com.oneapm.es.connect
 * Date:2016年6月14日上午9:58:33
 * Copyright (c) 2016, All Rights Reserved.
 *
 */

package com.oneapm.es.connect;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;

/**
 * ClassName:Demo2 <br/>
 * Function: <br/>
 * Date: 2016年6月14日 上午9:58:33 <br/>
 * @author xushjie
 * @version
 * @since JDK 1.7
 * @see
 */
public class Demo2 {
    //@formatter:off
    /*
    {
      "from" : 0,
      "size" : 1,
      "query" : {
        "term" : {
          "hostname" : "iZ25979ub3rZ"
        }
      }
    }
    */
    //@formatter:on
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
                                         "xintest")
                                    .build();
        //
        TransportClient client = TransportClient.builder()
                                                .settings(settings)
                                                .build()
                                                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("10.45.39.191"),
                                                                                                    9300));
        //
        SearchRequestBuilder request = client.prepareSearch();
        //
        request.setIndices("agent10_index-2016-06-13");
        //
        request.setQuery(QueryBuilders.termQuery("hostname",
                                                 "iZ25979ub3rZ"));
        //
        request.setFrom(0);
        //
        request.setSize(1);
        //
        System.out.println(request.toString());
    }

}

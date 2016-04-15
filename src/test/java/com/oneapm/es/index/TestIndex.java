/**
 * Project Name:elasticsearch-demos
 * File Name:TestIndex.java
 * Package Name:com.oneapm.es.index
 * Date:2016年4月15日下午6:22:28
 * Copyright (c) 2016, All Rights Reserved.
 *
 */

package com.oneapm.es.index;

import java.io.IOException;
import java.net.InetAddress;

import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.junit.Assert;
import org.junit.Test;

/**
 * ClassName:TestIndex <br/>
 * Function: <br/>
 * Date: 2016年4月15日 下午6:22:28 <br/>
 * @author xushjie
 * @version
 * @since JDK 1.7
 * @see
 */
public class TestIndex {
    
    /**
     * testCreateIndex: <br/>
     * @author xushjie
     * @throws IOException
     * @since JDK 1.7
     */
    @Test
    public void testCreateIndex() throws IOException {
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
        CreateIndexResponse response = client.admin()
                                             .indices()
                                             .prepareCreate("index_1")
                                             .setSettings(Settings.builder()
                                                                  .put("number_of_shards",
                                                                       5)
                                                                  .put("number_of_replicas",
                                                                       1)
                                                                  .build())
                                             .addMapping("index_1",
                                                         XContentFactory.jsonBuilder()
                                                                        .startObject()
                                                                        .startObject("external")
                                                                        .startObject("properties")
                                                                        .startObject("event_id")
                                                                        .field("type",
                                                                               "string")
                                                                        .endObject()
                                                                        .startObject("event_time")
                                                                        .field("type",
                                                                               "string")
                                                                        .endObject()
                                                                        .endObject()
                                                                        .endObject()
                                                                        .endObject())
                                             .execute()
                                             .actionGet();
        //
        Assert.assertTrue(response.isAcknowledged());
    }
    
}

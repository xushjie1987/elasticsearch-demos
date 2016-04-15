/**
 * Project Name:elasticsearch-demos
 * File Name:TestRepository.java
 * Package Name:com.oneapm.es.repository
 * Date:2016年4月15日下午4:53:22
 * Copyright (c) 2016, All Rights Reserved.
 *
 */

package com.oneapm.es.repository;

import java.io.FileNotFoundException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.action.admin.cluster.repositories.delete.DeleteRepositoryResponse;
import org.elasticsearch.action.admin.cluster.repositories.put.PutRepositoryResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.settings.SettingsException;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.junit.Assert;
import org.junit.Test;

/**
 * ClassName:TestRepository <br/>
 * Function: <br/>
 * Date: 2016年4月15日 下午4:53:22 <br/>
 * @author xushjie
 * @version
 * @since JDK 1.7
 * @see
 */
public class TestRepository {
    
    /**
     * testCreateRepository: <br/>
     * @author xushjie
     * @throws UnknownHostException
     * @throws SettingsException
     * @throws FileNotFoundException
     * @since JDK 1.7
     */
    @Test
    public void testCreateRepository() throws UnknownHostException, SettingsException, FileNotFoundException {
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
        PutRepositoryResponse response = client.admin()
                                               .cluster()
                                               .preparePutRepository("backup_2")
                                               .setType("hdfs")
                                               .setSettings(Settings.builder()
                                                                    .put("path",
                                                                         "/elasticsearch_1/backup_2")
                                                                    .put("uri",
                                                                         "hdfs://x86-ubuntu:9000/")
                                                                    .put("conf_location",
                                                                         "/home/appd/app/hadoop/hadoop-2.7.1/etc/hadoop/hdfs-site.xml"
                                                                                 + ","
                                                                                 + "/home/appd/app/hadoop/hadoop-2.7.1/etc/hadoop/core-site.xml")
                                                                    .build())
                                               .execute()
                                               .actionGet();
        //
        Assert.assertTrue(response.isAcknowledged());
    }
    
    /**
     * testDeleteRepository: <br/>
     * @author xushjie
     * @throws UnknownHostException
     * @since JDK 1.7
     */
    @Test
    public void testDeleteRepository() throws UnknownHostException {
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
        DeleteRepositoryResponse response = client.admin()
                                                  .cluster()
                                                  .prepareDeleteRepository("backup_2")
                                                  .execute()
                                                  .actionGet();
        //
        Assert.assertTrue(response.isAcknowledged());
    }
    
}

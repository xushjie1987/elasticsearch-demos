/**
 * Project Name:elasticsearch-demos
 * File Name:ElasticSearchEngine.java
 * Package Name:com.oneapm.es.logs.rest.srvc
 * Date:2016年6月22日上午11:28:07
 * Copyright (c) 2016, All Rights Reserved.
 *
 */

package com.oneapm.es.logs.rest.srvc;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.search.sort.SortParseElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * ClassName:ElasticSearchEngine <br/>
 * Function: <br/>
 * Date: 2016年6月22日 上午11:28:07 <br/>
 * @author xushjie
 * @version
 * @since JDK 1.7
 * @see
 */
@Getter
@Setter
@Component(value = "engine")
public class ElasticSearchEngine {
    
    public static final Logger log = LoggerFactory.getLogger(ElasticSearchEngine.class);
    
    private String             ip;
    
    private Integer            port;
    
    private String             cluster;
    
    private Settings           settings;
    
    private TransportClient    client;
    
    public void init() {
        //
        settings = Settings.settingsBuilder()
                           .put("cluster.name",
                                cluster)
                           .build();
        //
        try {
            client = TransportClient.builder()
                                    .settings(settings)
                                    .build()
                                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(ip),
                                                                                        port));
        } catch (UnknownHostException e) {
            e.printStackTrace();
            client = null;
        }
    }
    
    private GetRequestBuilder getGetRequest(String index,
                                            String type,
                                            String id) {
        return client.prepareGet(index,
                                 type,
                                 id);
    }
    
    private SearchRequestBuilder getSearchRequest(String... indices) {
        return client.prepareSearch(indices);
    }
    
    public String getDocumentById(String index,
                                  String type,
                                  String id) {
        GetRequestBuilder request = getGetRequest(index,
                                                  type,
                                                  id);
        System.out.println(request.toString());
        GetResponse response = request.get();
        if (response.isSourceEmpty()) {
            return "";
        } else {
            return response.getSourceAsString();
        }
    }
    
    public List<String> searchByLevelAndContent(String index,
                                                String level,
                                                String content) {
        // request
        SearchRequestBuilder request = getSearchRequest(index);
        request.setFrom(0)
               .setSize(10)
               .addSort(SortBuilders.fieldSort(SortParseElement.SCORE_FIELD_NAME)
                                    .order(SortOrder.DESC))
               .setQuery(QueryBuilders.boolQuery()
                                      .should(QueryBuilders.matchQuery("log_content",
                                                                       content))
                                      .must(QueryBuilders.matchQuery("log_level",
                                                                     level)))
               .addHighlightedField("log_content",
                                    100,
                                    100);
        System.out.println(request.toString());
        // get hits
        SearchResponse response = request.get();
        SearchHits hits = response.getHits();
        List<String> result = new ArrayList<String>();
        for (SearchHit hit : hits.hits()) {
            Text[] fragments = hit.getHighlightFields()
                                  .get("log_content")
                                  .fragments();
            String highlight = "";
            for (Text f : fragments) {
                highlight += f.string();
            }
            result.add(highlight);
        }
        return result;
    }
    
    public void destroy() {
        // skip
    }
    
}

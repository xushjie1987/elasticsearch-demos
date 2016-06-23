/**
 * Project Name:elasticsearch-demos
 * File Name:MatchService.java
 * Package Name:com.oneapm.es.logs.rest.srvc
 * Date:2016年6月22日上午11:52:20
 * Copyright (c) 2016, All Rights Reserved.
 *
 */

package com.oneapm.es.logs.rest.srvc;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.oneapm.es.logs.rest.bean.CassandraIndexMapping;
import com.oneapm.es.logs.rest.param.ParamBean;

/**
 * import com.oneapm.es.logs.rest.bean.CassandraIndexMapping;
 * 
 * ClassName:MatchService <br/>
 * Function: <br/>
 * Date: 2016年6月22日 上午11:52:20 <br/>
 * @author xushjie
 * @version
 * @since JDK 1.7
 * @see
 */
@Service(value = "matchService")
public class MatchService implements ElasticSearchService<String, ParamBean> {
    
    public static final Logger log  = LoggerFactory.getLogger(MatchService.class);
    
    private Gson               gson = new Gson();
    
    @Resource(name = "engine")
    public ElasticSearchEngine engine;
    
    @Override
    public List<String> matchLevelAndContent(ParamBean from,
                                             ParamBean to) {
        // find document by id
        String document = engine.getDocumentById(from.getIndexName(),
                                                 from.getTypeName(),
                                                 from.getDocumentId());
        if ("".equals(document)) {
            return Arrays.asList();
        }
        // convert
        CassandraIndexMapping cass = gson.fromJson(document,
                                                   CassandraIndexMapping.class);
        System.out.println(cass.getLog_content());
        // match full-text query
        return engine.searchByLevelAndContent(to.getIndexName(),
                                              cass.getLog_level(),
                                              cass.getLog_content());
    }
}

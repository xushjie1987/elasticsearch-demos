/**
 * Project Name:elasticsearch-demos
 * File Name:ElasticSearchService.java
 * Package Name:com.oneapm.es.logs.rest.srvc
 * Date:2016年6月22日上午11:08:20
 * Copyright (c) 2016, All Rights Reserved.
 *
 */

package com.oneapm.es.logs.rest.srvc;

import java.util.List;

/**
 * ClassName:ElasticSearchService <br/>
 * Function: <br/>
 * Date: 2016年6月22日 上午11:08:20 <br/>
 * @author xushjie
 * @version
 * @since JDK 1.7
 * @see
 */
public interface ElasticSearchService<T, I> {
    
    List<T> matchLevelAndContent(I from,
                                 I to);
    
}

/**
 * Project Name:elasticsearch-demos
 * File Name:MatchController.java
 * Package Name:com.oneapm.es.logs.rest.ctrl
 * Date:2016年6月22日上午11:07:02
 * Copyright (c) 2016, All Rights Reserved.
 *
 */

package com.oneapm.es.logs.rest.ctrl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.oneapm.es.logs.rest.param.MatchParamBean;
import com.oneapm.es.logs.rest.srvc.ElasticSearchService;

/**
 * ClassName:MatchController <br/>
 * Function: <br/>
 * Date: 2016年6月22日 上午11:07:02 <br/>
 * @author xushjie
 * @version
 * @since JDK 1.7
 * @see
 */
@RestController
@RequestMapping(path = { "/match" })
public class MatchController {
    
    @Resource(name = "matchService")
    public ElasticSearchService matchService;
    
    @RequestMapping(path = { "/{index}/{type}/{id}/content" },
                    method = { RequestMethod.GET })
    @ResponseBody
    public List<String> getMatch(@PathVariable(value = "index") String indexFrom,
                                 @PathVariable(value = "type") String typeFrom,
                                 @PathVariable(value = "id") String idFrom,
                                 @RequestParam(name = "index",
                                               defaultValue = "akka-2016-06-20") String indexTo) {
        //
        MatchParamBean from = new MatchParamBean();
        from.setIndexName(indexFrom);
        from.setTypeName(typeFrom);
        from.setDocumentId(idFrom);
        MatchParamBean to = new MatchParamBean();
        to.setIndexName(indexTo);
        //
        return matchService.matchLevelAndContent(from,
                                                 to);
    }
    
}

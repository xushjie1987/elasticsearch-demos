/**
 * Project Name:elasticsearch-demos
 * File Name:MatchParamBean.java
 * Package Name:com.oneapm.es.logs.rest.param
 * Date:2016年6月22日下午1:31:43
 * Copyright (c) 2016, All Rights Reserved.
 *
 */

package com.oneapm.es.logs.rest.param;

import lombok.Getter;
import lombok.Setter;

/**
 * ClassName:MatchParamBean <br/>
 * Function: <br/>
 * Date: 2016年6月22日 下午1:31:43 <br/>
 * @author xushjie
 * @version
 * @since JDK 1.7
 * @see
 */
@Getter
@Setter
public class MatchParamBean implements ParamBean {
    
    private String indexName;
    
    private String typeName;
    
    private String DocumentId;
    
}

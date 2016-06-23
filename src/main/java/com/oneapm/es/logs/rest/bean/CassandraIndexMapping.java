/**
 * Project Name:elasticsearch-demos
 * File Name:CassandraIndexMapping.java
 * Package Name:com.oneapm.es.logs.rest.bean
 * Date:2016年6月22日下午2:47:50
 * Copyright (c) 2016, All Rights Reserved.
 *
 */

package com.oneapm.es.logs.rest.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * ClassName:CassandraIndexMapping <br/>
 * Function: <br/>
 * Date: 2016年6月22日 下午2:47:50 <br/>
 * @author xushjie
 * @version
 * @since JDK 1.7
 * @see
 */
@Getter
@Setter
public class CassandraIndexMapping {
    private String host_ip;
    private String log_level;
    private String timestamp;
    private String log_content;
}

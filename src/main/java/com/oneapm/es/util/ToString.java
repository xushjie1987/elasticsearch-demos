/**
 * Project Name:elasticsearch-demos
 * File Name:ToString.java
 * Package Name:com.oneapm.es.util
 * Date:2016年4月15日上午11:45:01
 * Copyright (c) 2016, All Rights Reserved.
 *
 */

package com.oneapm.es.util;

import org.elasticsearch.action.admin.cluster.repositories.put.PutRepositoryResponse;
import org.elasticsearch.action.get.GetResponse;

import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;

/**
 * ClassName:ToString <br/>
 * Function: <br/>
 * Date: 2016年4月15日 上午11:45:01 <br/>
 * @author xushjie
 * @version
 * @since JDK 1.7
 * @see
 */
public class ToString {
    
    /**
     * toStringHelper: <br/>
     * @author xushjie
     * @param o
     * @return
     * @since JDK 1.7
     */
    public static String toStringHelper(GetResponse o) {
        ToStringHelper tsh = MoreObjects.toStringHelper(o);
        tsh.add("version",
                o.getVersion())
           .add("id",
                o.getId())
           .add("index",
                o.getIndex());
        for (String k : o.getFields()
                         .keySet()) {
            tsh.add(k,
                    o.getField(k)
                     .getValue());
        }
        return tsh.toString();
    }
    
    /**
     * toStringHelper: <br/>
     * @author xushjie
     * @param o
     * @return
     * @since JDK 1.7
     */
    public static String toStringHelper(PutRepositoryResponse o) {
        ToStringHelper tsh = MoreObjects.toStringHelper(o);
        tsh.add("acknowledged",
                o.isAcknowledged())
           .add("contextEmpty",
                o.isContextEmpty());
        return tsh.toString();
        
    }
}

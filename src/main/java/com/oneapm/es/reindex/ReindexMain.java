/**
 * Project Name:elasticsearch-demos
 * File Name:ReindexMain.java
 * Package Name:com.oneapm.es.reindex
 * Date:2016年6月15日上午11:25:56
 * Copyright (c) 2016, All Rights Reserved.
 *
 */

package com.oneapm.es.reindex;

import io.airlift.airline.Cli;
import io.airlift.airline.Cli.CliBuilder;
import io.airlift.airline.Command;
import io.airlift.airline.Help;
import io.airlift.airline.Option;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.LongAdder;

import lombok.Getter;
import lombok.Setter;

import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortParseElement;

import com.oneapm.es.util.StringUtil;
import com.oneapm.es.util.TimeUtil;

/**
 * ClassName:ReindexMain <br/>
 * Function: <br/>
 * Date: 2016年6月15日 上午11:25:56 <br/>
 * @author xushjie
 * @version
 * @since JDK 1.7
 * @see
 */
public class ReindexMain {

    /**
     * ClassName: ReindexCmd <br/>
     * Function: <br/>
     * date: 2016年6月15日 下午2:09:45 <br/>
     *
     * @author xushjie
     * @version ReindexMain
     * @since JDK 1.7
     */
    @Getter
    @Setter
    @Command(name = "reindex",
             description = "Reindex索引")
    public static class ReindexCmd implements Runnable {

        @Option(name = { "-a", "--address" },
                required = false,
                description = "Reindex集群IP地址，默认127.0.0.l")
        public String    address     = "127.0.0.l";

        @Option(name = { "-b", "--batch" },
                required = false,
                description = "Reindex的scroll批量，默认500")
        public Long      batch       = 500L;

        @Option(name = { "-c", "--cluster" },
                required = false,
                description = "Reindex集群名称，默认test")
        public String    clusterName = "test";

        @Option(name = { "-k", "--bulk" },
                required = false,
                description = "Reindex的bulk批量，默认50")
        public Long      bulk        = 50L;

        @Option(name = { "-v", "--live" },
                required = false,
                description = "Reindex执行scroll的window窗口存活时间，默认5m")
        public String    keepAlive   = "5m";

        @Option(name = { "-n", "--new" },
                required = false,
                description = "Reindex目标索引名称，默认new")
        public String    newIndex    = "new";

        @Option(name = { "-o", "--old" },
                required = false,
                description = "Reindex原始索引名称，默认old")
        public String    oldIndex    = "old";

        @Option(name = { "-p", "--port" },
                required = false,
                description = "Reindex集群port端口号，默认9300")
        public Integer   port        = 9300;

        @Option(name = { "-t", "--ntype" },
                required = false,
                description = "Reindex目标类型，默认new")
        public String    newType     = "new";

        @Option(name = { "-y", "--otype" },
                required = false,
                description = "Reindex原始类型，默认*")
        public String    oldType     = "*";

        public LongAdder total       = new LongAdder();

        public LongAdder real        = new LongAdder();

        /**
         * 执行reindex
         * @see java.lang.Runnable#run()
         */
        @Override
        public void run() {
            //
            long start = System.currentTimeMillis();
            // show process
            Thread monitor = new Thread(new Runnable() {
                @Override
                public void run() {
                    //
                    while (!Thread.interrupted()) {
                        long current = real.longValue();
                        System.out.println("[" +
                                           current +
                                           "]条文档已经被Reindex，当前已经耗时[" +
                                           TimeUtil.humanTime(System.currentTimeMillis() -
                                                              start) +
                                           "]，平均速度[" +
                                           ((double) current / (double) (System.currentTimeMillis() - start)) *
                                           1000L +
                                           "条/秒]，Reindex比率：[" +
                                           (total.longValue() > 0L
                                                                  ? (((double) current / (double) total.longValue()) * 100.0) +
                                                                    "%"
                                                                  : "0.0%") +
                                           "].");
                        try {
                            Thread.sleep(2000L);
                        } catch (InterruptedException e) {
                            System.out.println("Monitor监控线程已经被中断，退出!");
                            return;
                        }
                    }
                }
            });
            // 建立连接
            Settings settings = Settings.settingsBuilder()
                                        .put("cluster.name",
                                             clusterName)
                                        .build();
            TransportClient client = null;
            try {
                client = TransportClient.builder()
                                        .settings(settings)
                                        .build()
                                        .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(address),
                                                                                            port));
            } catch (UnknownHostException e) {
                e.printStackTrace();
                monitor.interrupt();
                System.exit(-1);
            }
            // 开启monitor监控
            monitor.start();
            // 执行scroll
            SearchRequestBuilder request = client.prepareSearch(oldIndex);
            if (!"*".equals(oldType)) {
                request.setTypes(oldType);
            }
            SearchResponse scrollResp = request.setScroll(StringUtil.validKeepAlive(keepAlive)
                                                                                              ? keepAlive
                                                                                              : "5m")
                                               .setQuery(QueryBuilders.matchAllQuery())
                                               .addSort(SortBuilders.fieldSort(SortParseElement.DOC_FIELD_NAME))
                                               .setSize(batch.intValue())
                                               .execute()
                                               .actionGet();
            // next batch
            while (true) {
                // Break condition: No hits are returned
                if (scrollResp.getHits()
                              .getHits().length == 0) {
                    break;
                }
                //
                total.add(scrollResp.getHits()
                                    .getHits().length);
                // 执行bulk
                BulkRequestBuilder bulkRequest = client.prepareBulk();
                long count = 0L;
                for (SearchHit hit : scrollResp.getHits()
                                               .getHits()) {
                    //
                    if (!hit.isSourceEmpty()) {
                        bulkRequest.add(client.prepareIndex(newIndex,
                                                            newType)
                                              .setSource(hit.getSourceRef()));
                    }
                    //
                    count++;
                    //
                    if (count >= ((bulk.longValue() > batch.longValue())
                                                                        ? batch.longValue()
                                                                        : bulk.longValue())) {
                        BulkResponse resp = bulkRequest.get();
                        // has failures
                        if (resp.hasFailures()) {
                            long failCount = 0L;
                            for (BulkItemResponse item : resp.getItems()) {
                                if (item.isFailed()) {
                                    failCount++;
                                }
                            }
                            real.add(bulkRequest.numberOfActions() -
                                     failCount);
                            System.out.println("bulk文档数：[" +
                                               bulkRequest.numberOfActions() +
                                               "]，失败文档数：[" +
                                               failCount +
                                               "]");
                        } else {
                            real.add(bulkRequest.numberOfActions());
                        }
                        // re-init
                        bulkRequest = client.prepareBulk();
                        count = 0L;
                    }
                }
                // rest
                if (bulkRequest.numberOfActions() > 0) {
                    BulkResponse resp = bulkRequest.get();
                    // has failures
                    if (resp.hasFailures()) {
                        long failCount = 0L;
                        for (BulkItemResponse item : resp.getItems()) {
                            if (item.isFailed()) {
                                failCount++;
                            }
                        }
                        real.add(bulkRequest.numberOfActions() -
                                 failCount);
                        System.out.println("bulk文档数：[" +
                                           bulkRequest.numberOfActions() +
                                           "]，失败文档数：[" +
                                           failCount +
                                           "]");
                    } else {
                        real.add(bulkRequest.numberOfActions());
                    }
                }
                // next _search/scroll
                scrollResp = client.prepareSearchScroll(scrollResp.getScrollId())
                                   .setScroll(StringUtil.validKeepAlive(keepAlive)
                                                                                  ? keepAlive
                                                                                  : "5m")
                                   .execute()
                                   .actionGet();
            }
            // reindex finish
            long end = System.currentTimeMillis();
            monitor.interrupt();
            //
            try {
                monitor.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //
            System.out.println("预计Reindex[" +
                               total.longValue() +
                               "]条文档，实际Reindex[" +
                               real.longValue() +
                               "]条文档，耗时[" +
                               TimeUtil.humanTime(end -
                                                  start) +
                               "]，平均速度[" +
                               (double) (real.longValue()) /
                               (double) (end - start) *
                               1000L +
                               "条/秒]，Reindex成功比率：[" +
                               (total.longValue() > 0L
                                                      ? (((double) real.longValue() / (double) total.longValue()) * 100.0) +
                                                        "%"
                                                      : "0.0%") +
                               "]，失败比率：[" +
                               (total.longValue() > 0L
                                                      ? (((double) (total.longValue() - real.longValue()) / (double) total.longValue()) * 100.0) +
                                                        "%"
                                                      : "0.0%") +
                               "].");
        }
    }

    /**
     * main: <br/>
     * @author xushjie
     * @param args
     * @since JDK 1.7
     */
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        //
        CliBuilder<Runnable> builder = Cli.<Runnable> builder("XCli")
                                          .withDescription("XCli客户端")
                                          .withDefaultCommand(Help.class)
                                          .withCommands(Help.class,
                                                        ReindexCmd.class);
        //
        Cli<Runnable> gitParser = builder.build();
        //
        gitParser.parse(args)
                 .run();
    }
}

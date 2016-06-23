/**
 * Project Name:elasticsearch-demos
 * File Name:CassandraLogMain.java
 * Package Name:com.oneapm.es.logs
 * Date:2016年6月20日下午4:37:42
 * Copyright (c) 2016, All Rights Reserved.
 *
 */

package com.oneapm.es.logs;

import io.airlift.airline.Cli;
import io.airlift.airline.Cli.CliBuilder;
import io.airlift.airline.Command;
import io.airlift.airline.Help;
import io.airlift.airline.Option;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.LongAdder;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.io.FileUtils;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentFactory;

import com.oneapm.es.util.StringUtil;
import com.oneapm.es.util.TimeUtil;

/**
 * ClassName:CassandraLogMain <br/>
 * Function: <br/>
 * Date: 2016年6月20日 下午4:37:42 <br/>
 * @author xushjie
 * @version
 * @since JDK 1.7
 * @see
 */
public class LogsMain {
    
    public static enum LogLevel {
        INFO("INFO"),
        DEBUG("DEBUG"),
        ERROR("ERROR"),
        TRACE("TRACE"),
        WARN("WARN");
        private String level;
        
        private LogLevel(String level) {
            this.level = level;
        }
        
        @Override
        public String toString() {
            return level;
        }
        
        public static LogLevel match(String level) {
            if (level == null ||
                "".equals(level)) {
                return null;
            }
            level = level.trim();
            if (INFO.toString()
                    .equalsIgnoreCase(level)) {
                return INFO;
            }
            if (DEBUG.toString()
                     .equalsIgnoreCase(level)) {
                return DEBUG;
            }
            if (ERROR.toString()
                     .equalsIgnoreCase(level)) {
                return ERROR;
            }
            if (TRACE.toString()
                     .equalsIgnoreCase(level)) {
                return TRACE;
            }
            if (WARN.toString()
                    .equalsIgnoreCase(level)) {
                return WARN;
            }
            return null;
        }
    }
    
    public static interface LogParser<O, I> {
        O parse(I line);
    }
    
    @Getter
    @Setter
    public static class CassandraLogBean {
        private String   timestamp;
        private String   logContent;
        private String   hostIp;
        private LogLevel logLevel;
    }
    
    public static class CassandraLogParser implements LogParser<CassandraLogBean, String> {
        
        @Override
        public CassandraLogBean parse(String line) {
            //
            if (null == line ||
                "".equals(line)) {
                System.out.println("Cassandra日志line为null或空串");
                return null;
            }
            //
            String[] pieces = line.split("\\s+");
            CassandraLogBean log = new CassandraLogBean();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-hh HH:mm:ss,SSS");
            String timestamp = pieces[2] +
                               " " +
                               pieces[3];
            try {
                sdf.parse(timestamp);
            } catch (ParseException e) {
                System.out.println("解析Cassandra日志时间字段失败：" +
                                   line);
                e.printStackTrace();
                return null;
            }
            log.setLogContent(line);
            log.setTimestamp(timestamp);
            log.setLogLevel(LogLevel.match(pieces[0]));
            return log;
        }
        
    }
    
    @Getter
    @Setter
    public static class AkkaLogBean {
        private String   timestamp;
        private String   logContent;
        private String   hostIp;
        private LogLevel logLevel;
    }
    
    public static class AkkaLogParser implements LogParser<AkkaLogBean, String> {
        
        @Override
        public AkkaLogBean parse(String line) {
            //
            if (null == line ||
                "".equals(line)) {
                System.out.println("Akka日志line为null或空串");
                return null;
            }
            //
            String[] pieces = line.split("\\s+");
            AkkaLogBean log = new AkkaLogBean();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-hh HH:mm:ss.SSS");
            String timestamp = pieces[0] +
                               " " +
                               pieces[1];
            try {
                sdf.parse(timestamp);
            } catch (ParseException e) {
                System.out.println("解析Akka日志时间字段失败：" +
                                   line);
                e.printStackTrace();
                return null;
            }
            log.setLogContent(line);
            log.setTimestamp(timestamp);
            log.setLogLevel(LogLevel.match(pieces[3]));
            return log;
        }
        
    }
    
    public static interface LogReader<O, I> {
        O read(I input);
    }
    
    public static class CassandraLogReader implements LogReader<CassandraLogStream, File> {
        
        @Override
        public CassandraLogStream read(File input) {
            try {
                if (input.isDirectory()) {
                    Collection<File> files = FileUtils.listFiles(input,
                                                                 new String[] { "log" },
                                                                 true);
                    return new CassandraLogStream(files);
                } else {
                    BufferedReader br = new BufferedReader(new FileReader(input));
                    return new CassandraLogStream(br);
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("打开Cassandra日志文件失败");
            }
            return null;
        }
        
    }
    
    public static class AkkaLogReader implements LogReader<AkkaLogStream, File> {
        
        @Override
        public AkkaLogStream read(File input) {
            try {
                if (input.isDirectory()) {
                    Collection<File> files = FileUtils.listFiles(input,
                                                                 new String[] { "log" },
                                                                 true);
                    return new AkkaLogStream(files);
                } else {
                    BufferedReader br = new BufferedReader(new FileReader(input));
                    return new AkkaLogStream(br);
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("打开Akka日志文件失败");
            }
            return null;
        }
        
    }
    
    public static interface LogStream<O> extends AutoCloseable {
        O getUnit();
    }
    
    public static class CassandraLogStream implements LogStream<String> {
        BufferedReader reader  = null;
        List<File>     files   = null;
        int            current = 0;
        StringBuffer   sb      = new StringBuffer();
        
        public CassandraLogStream(BufferedReader reader) {
            this.reader = reader;
        }
        
        public CassandraLogStream(Collection<File> files) {
            this.files = new ArrayList<File>(files);
        }
        
        private BufferedReader getNextReader() {
            try {
                if (files == null ||
                    files.size() == 0 ||
                    current >= files.size()) {
                    return null;
                }
                return new BufferedReader(new FileReader(files.get(current++)));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }
        
        private String getNextLine() throws IOException {
            if (reader == null) {
                reader = getNextReader();
            }
            //
            if (reader != null) {
                String line = reader.readLine();
                if (line == null) {
                    reader = null;
                    return getNextLine();
                } else {
                    return line;
                }
            }
            return null;
        }
        
        @Override
        public String getUnit() {
            if (reader == null &&
                (files == null || files.size() < 1)) {
                System.out.println("文件不存在，或者目录为空");
                System.exit(-1);
            }
            String line;
            try {
                while (true) {
                    // read
                    line = getNextLine();
                    // end of all files
                    if (line == null) {
                        if (null == sb.toString() ||
                            "".equals(sb.toString())) {
                            return "";
                        } else {
                            String tmp = sb.toString();
                            sb = new StringBuffer();
                            return tmp;
                        }
                    }
                    // blank line
                    if ("".equals(line)) {
                        if (null != sb.toString() &&
                            !"".equals(sb.toString())) {
                            sb.append("\n");
                        }
                        continue;
                    }
                    // normal
                    if (StringUtil.startWithLogLevel(line)) {
                        if (null == sb.toString() ||
                            "".equals(sb.toString())) {
                            sb.append(line);
                        } else {
                            String tmp = sb.toString();
                            sb = new StringBuffer();
                            sb.append(line);
                            return tmp;
                        }
                        continue;
                    }
                    // exception
                    sb.append("\n");
                    sb.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
                if (null == sb.toString() ||
                    "".equals(sb.toString())) {
                    return "";
                } else {
                    String tmp = sb.toString();
                    sb = new StringBuffer();
                    return tmp;
                }
            }
        }
        
        @Override
        public void close() throws Exception {
            if (reader != null) {
                reader.close();
            }
        }
        
    }
    
    public static class AkkaLogStream implements LogStream<String> {
        BufferedReader reader  = null;
        List<File>     files   = null;
        int            current = 0;
        StringBuffer   sb      = new StringBuffer();
        
        public AkkaLogStream(BufferedReader reader) {
            this.reader = reader;
        }
        
        public AkkaLogStream(Collection<File> files) {
            this.files = new ArrayList<File>(files);
        }
        
        private BufferedReader getNextReader() {
            try {
                if (files == null ||
                    files.size() == 0 ||
                    current >= files.size()) {
                    return null;
                }
                return new BufferedReader(new FileReader(files.get(current++)));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }
        
        private String getNextLine() throws IOException {
            if (reader == null) {
                reader = getNextReader();
            }
            //
            if (reader != null) {
                String line = reader.readLine();
                if (line == null) {
                    reader = null;
                    return getNextLine();
                } else {
                    return line;
                }
            }
            return null;
        }
        
        @Override
        public String getUnit() {
            if (reader == null &&
                (files == null || files.size() < 1)) {
                System.out.println("文件不存在，或者目录为空");
                System.exit(-1);
            }
            String line;
            try {
                while (true) {
                    // read
                    line = getNextLine();
                    // end of all files
                    if (line == null) {
                        if (null == sb.toString() ||
                            "".equals(sb.toString())) {
                            return "";
                        } else {
                            String tmp = sb.toString();
                            sb = new StringBuffer();
                            return tmp;
                        }
                    }
                    // blank line
                    if ("".equals(line)) {
                        if (null != sb.toString() &&
                            !"".equals(sb.toString())) {
                            sb.append("\n");
                        }
                        continue;
                    }
                    // normal
                    if (StringUtil.startWithTimestamp(line,
                                                      23,
                                                      StringUtil.AKKA_LOG_FORMAT)) {
                        if (null == sb.toString() ||
                            "".equals(sb.toString())) {
                            sb.append(line);
                        } else {
                            String tmp = sb.toString();
                            sb = new StringBuffer();
                            sb.append(line);
                            return tmp;
                        }
                        continue;
                    }
                    // exception
                    sb.append("\n");
                    sb.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
                if (null == sb.toString() ||
                    "".equals(sb.toString())) {
                    return "";
                } else {
                    String tmp = sb.toString();
                    sb = new StringBuffer();
                    return tmp;
                }
            }
        }
        
        @Override
        public void close() throws Exception {
            if (reader != null) {
                reader.close();
            }
        }
        
    }
    
    @Getter
    @Setter
    @Command(name = "cassandra",
             description = "Cassandra日志")
    public static class CassandraLogCmd implements Runnable {
        
        @Option(name = { "-a", "--address" },
                required = false,
                description = "Cassandra日志集群IP地址，默认127.0.0.l")
        public String                              address     = "127.0.0.l";
        
        @Option(name = { "-b", "--batch" },
                required = false,
                description = "Akka日志的batch大小，默认6.4K，支持单位：b/B，k/K，m/M，g/G")
        public String                              batch       = "6.4K";
        
        @Option(name = { "-c", "--cluster" },
                required = false,
                description = "Cassandra日志集群名称，默认cluster")
        public String                              clusterName = "cluster";
        
        @Option(name = { "-i", "--index" },
                required = false,
                description = "Cassandra日志目标索引名称，默认cassandra-")
        public String                              indexName   = "cassandra-";
        @Option(name = { "-h", "--host" },
                required = false,
                description = "Cassandra日志存储主机的IP，默认127.0.0.1")
        public String                              hostIp      = "127.0.0.1";
        
        @Option(name = { "-k", "--bulk" },
                required = false,
                description = "Cassandra日志的bulk批量，默认3")
        public Long                                bulk        = 3L;
        
        @Option(name = { "-l", "--log" },
                required = true,
                description = "Cassandra日志的位置，绝对路径")
        public String                              logPath;
        
        @Option(name = { "-p", "--port" },
                required = false,
                description = "Cassandra日志集群port端口号，默认9300")
        public Integer                             port        = 9300;
        
        @Option(name = { "-t", "--type" },
                required = false,
                description = "Cassandra日志目标类型，默认logs")
        public String                              typeName    = "logs";
        
        public LogParser<CassandraLogBean, String> parser      = new CassandraLogParser();
        
        public LogReader<CassandraLogStream, File> reader      = new CassandraLogReader();
        
        public LongAdder                           total       = new LongAdder();
        
        public LongAdder                           real        = new LongAdder();
        
        /**
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
                                           "]条文档已经被index，当前已经耗时[" +
                                           TimeUtil.humanTime(System.currentTimeMillis() -
                                                              start) +
                                           "]，平均速度[" +
                                           ((double) current / (double) (System.currentTimeMillis() - start)) *
                                           1000L +
                                           "条/秒]，index比率：[" +
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
            // connect
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
            // monitor
            monitor.start();
            // stream
            long batchSize = StringUtil.parseBatchSize(batch);
            assert batchSize > 0L;
            try (CassandraLogStream stream = reader.read(new File(logPath))) {
                // index
                while (true) {
                    // prepare
                    BulkRequestBuilder bulkRequest = client.prepareBulk();
                    long count = 0L;
                    long bat = 0L;
                    // batch
                    while (count < bulk &&
                           bat < batchSize) {
                        // read line
                        String unit = stream.getUnit();
                        CassandraLogBean line = parser.parse(unit);
                        // check
                        if (line == null) {
                            break;
                        }
                        line.setHostIp(hostIp);
                        // add request
                        bulkRequest.add(client.prepareIndex(indexName,
                                                            typeName)
                                              .setSource(XContentFactory.jsonBuilder()
                                                                        .startObject()
                                                                        .field("host_ip",
                                                                               line.getHostIp())
                                                                        .field("log_level",
                                                                               line.getLogLevel())
                                                                        .field("timestamp",
                                                                               line.getTimestamp())
                                                                        .field("log_content",
                                                                               line.getLogContent())
                                                                        .endObject()));
                        //
                        count++;
                        bat += unit.getBytes("UTF-8").length;
                    }
                    // send
                    if (bulkRequest.numberOfActions() > 0) {
                        // do
                        BulkResponse resp = bulkRequest.get();
                        // add
                        total.add(bulkRequest.numberOfActions());
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
                                               "]，错误信息：[" +
                                               resp.buildFailureMessage() +
                                               "].");
                        } else {
                            real.add(bulkRequest.numberOfActions());
                        }
                    }
                    // end of file
                    if (count < bulk &&
                        bat < batchSize) {
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            // finish
            long end = System.currentTimeMillis();
            monitor.interrupt();
            //
            try {
                monitor.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //
            System.out.println("预计index[" +
                               total.longValue() +
                               "]条文档，实际index[" +
                               real.longValue() +
                               "]条文档，耗时[" +
                               TimeUtil.humanTime(end -
                                                  start) +
                               "]，平均速度[" +
                               (double) (real.longValue()) /
                               (double) (end - start) *
                               1000L +
                               "条/秒]，index成功比率：[" +
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
    
    @Getter
    @Setter
    @Command(name = "akka",
             description = "Akka日志")
    public static class AkkaLogCmd implements Runnable {
        
        @Option(name = { "-a", "--address" },
                required = false,
                description = "Akka日志集群IP地址，默认127.0.0.l")
        public String                         address     = "127.0.0.l";
        
        @Option(name = { "-b", "--batch" },
                required = false,
                description = "Akka日志的batch大小，默认6.4K，支持单位：b/B，k/K，m/M，g/G")
        public String                         batch       = "6.4K";
        
        @Option(name = { "-c", "--cluster" },
                required = false,
                description = "Akka日志集群名称，默认cluster")
        public String                         clusterName = "cluster";
        
        @Option(name = { "-i", "--index" },
                required = false,
                description = "Akka日志目标索引名称，默认akka-")
        public String                         indexName   = "akka-";
        @Option(name = { "-h", "--host" },
                required = false,
                description = "Akka日志存储主机的IP，默认127.0.0.1")
        public String                         hostIp      = "127.0.0.1";
        
        @Option(name = { "-k", "--bulk" },
                required = false,
                description = "Akka日志的bulk批量，默认20")
        public Long                           bulk        = 20L;
        
        @Option(name = { "-l", "--log" },
                required = true,
                description = "Akka日志的位置，绝对路径")
        public String                         logPath;
        
        @Option(name = { "-p", "--port" },
                required = false,
                description = "Akka日志集群port端口号，默认9300")
        public Integer                        port        = 9300;
        
        @Option(name = { "-t", "--type" },
                required = false,
                description = "Akka日志目标类型，默认logs")
        public String                         typeName    = "logs";
        
        public LogParser<AkkaLogBean, String> parser      = new AkkaLogParser();
        
        public LogReader<AkkaLogStream, File> reader      = new AkkaLogReader();
        
        public LongAdder                      total       = new LongAdder();
        
        public LongAdder                      real        = new LongAdder();
        
        /**
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
                                           "]条文档已经被index，当前已经耗时[" +
                                           TimeUtil.humanTime(System.currentTimeMillis() -
                                                              start) +
                                           "]，平均速度[" +
                                           ((double) current / (double) (System.currentTimeMillis() - start)) *
                                           1000L +
                                           "条/秒]，index比率：[" +
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
            // connect
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
            // monitor
            monitor.start();
            // stream
            long batchSize = StringUtil.parseBatchSize(batch);
            assert batchSize > 0L;
            try (AkkaLogStream stream = reader.read(new File(logPath))) {
                // index
                while (true) {
                    // prepare
                    BulkRequestBuilder bulkRequest = client.prepareBulk();
                    long count = 0L;
                    long bat = 0L;
                    // batch
                    while (count < bulk &&
                           bat < batchSize) {
                        // read line
                        String unit = stream.getUnit();
                        AkkaLogBean line = parser.parse(unit);
                        // check
                        if (line == null) {
                            break;
                        }
                        line.setHostIp(hostIp);
                        // add request
                        bulkRequest.add(client.prepareIndex(indexName,
                                                            typeName)
                                              .setSource(XContentFactory.jsonBuilder()
                                                                        .startObject()
                                                                        .field("host_ip",
                                                                               line.getHostIp())
                                                                        .field("log_level",
                                                                               line.getLogLevel())
                                                                        .field("timestamp",
                                                                               line.getTimestamp())
                                                                        .field("log_content",
                                                                               line.getLogContent())
                                                                        .endObject()));
                        //
                        count++;
                        bat += unit.getBytes("UTF-8").length;
                    }
                    // send
                    if (bulkRequest.numberOfActions() > 0) {
                        // do
                        BulkResponse resp = bulkRequest.get();
                        // add
                        total.add(bulkRequest.numberOfActions());
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
                                               "]，错误信息：[" +
                                               resp.buildFailureMessage() +
                                               "].");
                        } else {
                            real.add(bulkRequest.numberOfActions());
                        }
                    }
                    // end of file
                    if (count < bulk &&
                        bat < batchSize) {
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            // finish
            long end = System.currentTimeMillis();
            monitor.interrupt();
            //
            try {
                monitor.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //
            System.out.println("预计index[" +
                               total.longValue() +
                               "]条文档，实际index[" +
                               real.longValue() +
                               "]条文档，耗时[" +
                               TimeUtil.humanTime(end -
                                                  start) +
                               "]，平均速度[" +
                               (double) (real.longValue()) /
                               (double) (end - start) *
                               1000L +
                               "条/秒]，index成功比率：[" +
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
                                                        CassandraLogCmd.class,
                                                        AkkaLogCmd.class);
        //
        Cli<Runnable> gitParser = builder.build();
        //
        gitParser.parse(args)
                 .run();
    }
    
}

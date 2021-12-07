package com.study.design.metrics.version1.reporter;

import com.google.gson.Gson;
import com.study.design.metrics.version1.Aggregator;
import com.study.design.metrics.version1.entity.RequestInfo;
import com.study.design.metrics.version1.entity.RequestStat;
import com.study.design.metrics.version1.metrics.MetricsStorage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 说明:
 * 发送统计数据至命令行
 *
 * @USER: Cgh
 * @DATE: 2021/12/7
 */
public class ConsoleReporter {
    private MetricsStorage metricsStorage;
    private ScheduledExecutorService executor;

    public ConsoleReporter(MetricsStorage metricsStorage) {
        this.metricsStorage = metricsStorage;
        this.executor = Executors.newSingleThreadScheduledExecutor();
    }

    public void startRepeatedReport(long periodInSeconds, long durationInSeconds) {
        executor.scheduleAtFixedRate(() -> {
            long durationInMills = durationInSeconds * 1000;
            long endTimeInMills = System.currentTimeMillis();
            long startTimeInMills = endTimeInMills - durationInMills;
            Map<String, List<RequestInfo>> requestInfos = metricsStorage.getRequestInfos(startTimeInMills, endTimeInMills);
            Map<String, RequestStat> stats = new HashMap<>();
            for (Map.Entry<String, List<RequestInfo>> entry : requestInfos.entrySet()) {
                String apiName = entry.getKey();
                List<RequestInfo> requestInfosPerApi = entry.getValue();
                RequestStat requestStat = Aggregator.aggreagte(requestInfosPerApi, durationInMills);
                stats.put(apiName, requestStat);
            }

            System.out.println("Time Span: [" + startTimeInMills + ", " + endTimeInMills + "]");
            Gson gson = new Gson();
            System.out.println(gson.toJson(stats));
        }, 0, periodInSeconds, TimeUnit.SECONDS);

    }
}

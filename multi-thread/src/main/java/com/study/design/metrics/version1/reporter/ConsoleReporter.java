package com.study.design.metrics.version1.reporter;

import com.study.design.metrics.version1.metrics.MetricsStorage;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

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

    }
}

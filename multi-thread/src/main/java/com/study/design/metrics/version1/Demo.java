package com.study.design.metrics.version1;

import com.study.design.metrics.version1.entity.RequestInfo;
import com.study.design.metrics.version1.metrics.MetricsStorage;
import com.study.design.metrics.version1.metrics.RedisMetricsStorage;
import com.study.design.metrics.version1.reporter.ConsoleReporter;
import com.study.design.metrics.version1.reporter.EmailReporter;

/**
 * 说明:
 *
 * @USER: Cgh
 * @DATE: 2021/12/8
 */
public class Demo {
    public static void main(String[] args) {
        MetricsStorage storage = new RedisMetricsStorage();
        ConsoleReporter consoleReporter = new ConsoleReporter(storage);
        consoleReporter.startRepeatedReport(60, 60);

        EmailReporter emailReporter = new EmailReporter(storage);
        emailReporter.addToAddress("caoguanghua@ddd.com");
        emailReporter.startDailyReport();

        MetricsCollector collector = new MetricsCollector(storage);
        collector.recordRequest(new RequestInfo("register",123,12441));
        collector.recordRequest(new RequestInfo("register",223,12541));
        collector.recordRequest(new RequestInfo("register",323,12641));
        collector.recordRequest(new RequestInfo("login",23,12441));
        collector.recordRequest(new RequestInfo("login",1223,13244));

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

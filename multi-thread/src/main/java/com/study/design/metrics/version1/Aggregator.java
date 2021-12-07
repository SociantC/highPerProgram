package com.study.design.metrics.version1;

import com.study.design.metrics.version1.entity.RequestInfo;
import com.study.design.metrics.version1.entity.RequestStat;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 说明:
 * 根据原始数据计算统计数据
 *
 * @USER: Cgh
 * @DATE: 2021/12/7
 */
public class Aggregator {
    public static RequestStat aggreagte(List<RequestInfo> requestInfos, long duringInMills) {
        double maxRespTime = Double.MIN_VALUE;
        double minRespTime = Double.MAX_VALUE;

        double avgRespTime = -1;
        double p999RespTime = -1;
        double p99RespTime = -1;
        double sumRespTime = 0;

        long count = 0;
        for (RequestInfo requestInfo : requestInfos) {
            ++count;
            double respTime = requestInfo.getResponseTime();
            if (respTime > maxRespTime) {
                maxRespTime = respTime;
            }

            if (respTime < minRespTime) {
                minRespTime = respTime;
            }

            sumRespTime += respTime;
        }

        if (count != 0) {
            avgRespTime = sumRespTime / count;
        }

        long tps = (long) (count / duringInMills * 1000);
        Collections.sort(requestInfos, new Comparator<RequestInfo>() {
            @Override
            public int compare(RequestInfo o1, RequestInfo o2) {
                double diff = o1.getResponseTime() - o2.getResponseTime();
                return Double.compare(diff, 0.0d);
            }
        });

        int idx999 = (int) (count * 0.999);
        int idx99 = (int) (count * 0.99);
        if (count != 0) {
            p999RespTime = requestInfos.get(idx999).getResponseTime();
            p99RespTime = requestInfos.get(idx99).getResponseTime();
        }

        return new RequestStat(maxRespTime, minRespTime, avgRespTime, p999RespTime, p99RespTime, count, tps);


    }


}

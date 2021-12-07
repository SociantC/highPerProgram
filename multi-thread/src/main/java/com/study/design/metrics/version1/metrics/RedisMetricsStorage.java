package com.study.design.metrics.version1.metrics;

import com.study.design.metrics.version1.entity.RequestInfo;

import java.util.List;
import java.util.Map;

/**
 * 说明:
 *
 * @USER: Cgh
 * @DATE: 2021/12/7
 */
public class RedisMetricsStorage implements MetricsStorage{
    @Override
    public void saveRequestInfo(RequestInfo requestInfo) {

    }

    @Override
    public List<RequestInfo> getRequestInfos(String apiName, long startTimeInMills, long endTimeInMills) {
        return null;
    }

    @Override
    public Map<String, List<RequestInfo>> getRequestInfos(long startTimeInMil, long endTimeInMills) {
        return null;
    }
}

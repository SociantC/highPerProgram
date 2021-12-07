package com.study.design.metrics.version1.metrics;

import com.study.design.metrics.version1.entity.RequestInfo;

import java.util.List;
import java.util.Map;

/**
 * 说明:
 * 原始数据存储
 * @USER: Cgh
 * @DATE: 2021/12/7
 */
public interface MetricsStorage {
    void saveRequestInfo(RequestInfo requestInfo);

    List<RequestInfo> getRequestInfos(String apiName, long startTimeInMills, long endTimeInMills);

    Map<String, List<RequestInfo>> getRequestInfos(long startTimeInMil, long endTimeInMills);

}

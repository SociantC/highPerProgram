package com.study.design.metrics.version1;

import com.study.design.metrics.version1.entity.RequestInfo;
import com.study.design.metrics.version1.metrics.MetricsStorage;
import org.springframework.util.StringUtils;

/**
 * 说明:
 * 提供API。采集接口请求的原始数据。
 *
 * @USER: Cgh
 * @DATE: 2021/12/7
 */
public class MetricsCollector {
    private MetricsStorage metricsStorage;

    public MetricsCollector(MetricsStorage metricsStorage) {
        this.metricsStorage = metricsStorage;
    }

    public void recordRequest(RequestInfo requestInfo) {
        if (requestInfo == null || StringUtils.hasText(requestInfo.getApiName())) {
            return;
        }
        metricsStorage.saveRequestInfo(requestInfo);
    }

}

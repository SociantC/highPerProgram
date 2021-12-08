package com.study.design.metrics.version1.entity;

/**
 * 说明:
 *
 * @USER: Cgh
 * @DATE: 2021/12/7
 */
public class RequestInfo {
    private String apiName;
    private double responseTime;
    private long timeStamp;

    public RequestInfo(String apiName, double responseTime, long timeStamp) {
        this.apiName = apiName;
        this.responseTime = responseTime;
        this.timeStamp = timeStamp;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public double getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(double responseTime) {
        this.responseTime = responseTime;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}

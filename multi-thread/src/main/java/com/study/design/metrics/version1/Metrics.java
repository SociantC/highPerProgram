package com.study.design.metrics.version1;

import com.google.gson.Gson;

import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 说明:
 *
 * @USER: Cgh
 * @DATE: 2021/12/7
 */
public class Metrics {
    private Map<String, List<Double>> responseTimes = new HashMap<>();
    private Map<String, List<Double>> timeStamps = new HashMap<>();
    private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    public void recordResponseTime(String apiTime, double responseTime) {
        responseTimes.putIfAbsent(apiTime, new ArrayList<>());
        responseTimes.get(apiTime).add(responseTime);
    }

    public void recordTimeStamp(String apiName, double timeStamp) {
        timeStamps.putIfAbsent(apiName, new ArrayList<>());
        timeStamps.get(apiName).add(timeStamp);
    }

    public void startRepeatedReport(long period, TimeUnit unit) {
        executor.scheduleAtFixedRate(() -> {
            Gson gson = new Gson();
            Map<String, Map<String, Double>> stats = new HashMap<>();
            for (Map.Entry<String, List<Double>> entry : responseTimes.entrySet()) {
                String apiName = entry.getKey();
                List<Double> respsontimes = entry.getValue();
                stats.putIfAbsent(apiName, new HashMap<>());
                stats.get(apiName).put("max", max(respsontimes));
                stats.get(apiName).put("min", min(respsontimes));
            }

            for (Map.Entry<String, List<Double>> entry : timeStamps.entrySet()) {
                String apiName = entry.getKey();
                List<Double> timestamps = entry.getValue();
                stats.putIfAbsent(apiName, new HashMap<>());
                stats.get(apiName).put("count", (double) timestamps.size());
            }

            System.out.println(gson.toJson(stats));
        }, 0, period, unit);

    }

    private double max(List<Double> time) {
        OptionalDouble max = time.stream().mapToDouble(i -> i).max();
        return max.isPresent()?max.getAsDouble():-1d;
    }

    private double min(List<Double> time) {
        OptionalDouble min = time.stream().mapToDouble(i -> i).min();
        return min.isPresent()?min.getAsDouble():-1d;
    }



}

package com.khokhlov.tendermonitoring.service;

import com.khokhlov.tendermonitoring.model.entity.TrackedKeyword;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Service
@RequiredArgsConstructor
public class DynamicMonitoringService {

    private final ThreadPoolTaskScheduler scheduler;
    private final RssMonitorService rssMonitorService;
    //todo: after restart of the application - data will be lost
    private final Map<Long, ScheduledFuture<?>> runningTasks = new ConcurrentHashMap<>();

    public void schedule(TrackedKeyword keyword) {
        Runnable task = () -> rssMonitorService.checkFeeds(keyword);

        ScheduledFuture<?> future = scheduler.scheduleWithFixedDelay(task, Duration.ofMinutes(keyword.getCheckedIntervalMinutes()));
        runningTasks.put(keyword.getId(), future);
    }

    public void cancel(Long keywordId) {
        ScheduledFuture<?> future = runningTasks.get(keywordId);
        if (future != null) {
            future.cancel(true);
            runningTasks.remove(keywordId);
        }
    }

    public boolean isTracking(Long keywordId) {
        return runningTasks.containsKey(keywordId);
    }
}

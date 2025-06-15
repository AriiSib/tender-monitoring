package com.khokhlov.tendermonitoring.service;

import com.khokhlov.tendermonitoring.model.entity.TrackedKeyword;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Service
@RequiredArgsConstructor
public class DynamicMonitoringService {

    private final ThreadPoolTaskScheduler scheduler;
    private final RssMonitorService rssMonitorService;
    private final Map<Long, ScheduledFuture<?>> runningTasks = new ConcurrentHashMap<>();

    public void schedule(TrackedKeyword keyword) {
        cancel(keyword.getId());

        Runnable task = () -> rssMonitorService.checkFeeds(keyword);
        ScheduledFuture<?> scheduledFuture =
                scheduler.scheduleWithFixedDelay(
                        task,
                        Duration.ofMinutes(keyword.getCheckedIntervalMinutes())
                );
        runningTasks.put(keyword.getId(), scheduledFuture);
    }

    public void cancel(Long keywordId) {
        Optional.ofNullable(runningTasks.remove(keywordId))
                .ifPresent(f -> f.cancel(true));
    }

    public boolean isTracking(Long keywordId) {
        return runningTasks.containsKey(keywordId);
    }
}

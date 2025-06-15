package com.khokhlov.tendermonitoring.service;

import com.khokhlov.tendermonitoring.repository.TrackedKeywordRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MonitoringBootstrap implements ApplicationListener<ApplicationReadyEvent> {

    private final TrackedKeywordRepository trackedKeywordRepository;
    private final DynamicMonitoringService monitoringService;

    @Override
    public void onApplicationEvent(@NotNull ApplicationReadyEvent e) {
        trackedKeywordRepository.findAllByActiveTrue()
                .forEach(monitoringService::schedule);
    }
}

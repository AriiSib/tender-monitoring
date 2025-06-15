package com.khokhlov.tendermonitoring.service;

import com.khokhlov.tendermonitoring.repository.TrackedTenderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TenderCleanupService {

    private final TrackedTenderRepository trackedTenderRepository;

    @Scheduled(cron = "0 1 5 * * *")
    public void deleteOldTenders() {
        trackedTenderRepository.deleteAll();
    }
}

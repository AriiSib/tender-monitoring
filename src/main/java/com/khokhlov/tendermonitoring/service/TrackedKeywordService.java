package com.khokhlov.tendermonitoring.service;

import com.khokhlov.tendermonitoring.model.entity.TrackedKeyword;
import com.khokhlov.tendermonitoring.repository.TrackedKeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TrackedKeywordService {

    private final TrackedKeywordRepository trackedKeywordRepository;

    @Transactional
    public void updateInterval(Long id, int minutes) {
        TrackedKeyword kw = trackedKeywordRepository.findById(id).orElseThrow();
        kw.setCheckedIntervalMinutes(minutes);
    }
}

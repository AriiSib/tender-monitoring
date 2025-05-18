package com.khokhlov.tendermonitoring.service;

import com.khokhlov.tendermonitoring.model.dto.TrackedKeywordViewDTO;
import com.khokhlov.tendermonitoring.model.dto.TrackedTenderViewDTO;
import com.khokhlov.tendermonitoring.model.dto.UserDTO;
import com.khokhlov.tendermonitoring.model.entity.TrackedKeyword;
import com.khokhlov.tendermonitoring.model.entity.TrackedTender;
import com.khokhlov.tendermonitoring.model.entity.User;
import com.khokhlov.tendermonitoring.repository.TrackedKeywordRepository;
import com.khokhlov.tendermonitoring.repository.TrackedTenderRepository;
import com.khokhlov.tendermonitoring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrackingViewService {

    private final TrackedKeywordRepository trackedKeywordRepository;
    private final TrackedTenderRepository trackedTenderRepository;
    private final DynamicMonitoringService dynamicMonitoringService;
    private final UserRepository userRepository;

    public List<TrackedKeywordViewDTO> getAllForUser(UserDTO userDTO) {
        User user = userRepository.getUsersByIdAndUsername(userDTO.id(), userDTO.username());
        List<TrackedKeyword> keywords = trackedKeywordRepository.findAllByUser(user);

        return keywords.stream()
                .map(kw -> new TrackedKeywordViewDTO(
                        kw.getId(),
                        kw.getKeyword(),
                        kw.getCheckedIntervalMinutes(),
                        kw.getLastPublishedDate(),
                        kw.getLastUpdateFoundAt(),
                        dynamicMonitoringService.isTracking(kw.getId())
                ))
                .toList();
    }

    public List<TrackedTenderViewDTO> getNewTendersForUser(UserDTO userDTO) {
        User user = userRepository.getUsersByIdAndUsername(userDTO.id(), userDTO.username());
        List<TrackedTender> tenders = trackedTenderRepository.findAllForUser(user);

        return tenders.stream()
                .map(tender -> new TrackedTenderViewDTO(
                        tender.getTrackedKeyword().getKeyword(),
                        tender.getTitle(),
                        tender.getLink(),
                        tender.getPrice(),
                        tender.getExpirationDate(),
                        tender.getPublishedDate()
                ))
                .sorted(Comparator.comparing(TrackedTenderViewDTO::publishedDate).reversed())
                .toList();
    }

    public void pause(Long id) {
        dynamicMonitoringService.cancel(id);
    }

    public void resume(Long id) {
        TrackedKeyword keyword = trackedKeywordRepository.findById(id).orElseThrow();
        dynamicMonitoringService.schedule(keyword);
    }

    public void updateInterval(Long id, int interval) {
        TrackedKeyword keyword = trackedKeywordRepository.findById(id).orElseThrow();
        keyword.setCheckedIntervalMinutes(interval);
        trackedKeywordRepository.save(keyword);

        dynamicMonitoringService.cancel(id);
        dynamicMonitoringService.schedule(keyword);
    }

    public void delete(Long id) {
        dynamicMonitoringService.cancel(id);
        trackedKeywordRepository.deleteById(id);
    }
}

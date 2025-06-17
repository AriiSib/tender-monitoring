package com.khokhlov.tendermonitoring.service;

import com.khokhlov.tendermonitoring.mapper.TenderMapper;
import com.khokhlov.tendermonitoring.model.dto.*;
import com.khokhlov.tendermonitoring.model.entity.TrackedKeyword;
import com.khokhlov.tendermonitoring.model.entity.TrackedTender;
import com.khokhlov.tendermonitoring.model.entity.User;
import com.khokhlov.tendermonitoring.repository.TrackedKeywordRepository;
import com.khokhlov.tendermonitoring.repository.TrackedTenderRepository;
import com.khokhlov.tendermonitoring.repository.UserRepository;
import com.khokhlov.tendermonitoring.telegram.service.TelegramNotificationService;
import com.khokhlov.tendermonitoring.util.RssParser;
import com.khokhlov.tendermonitoring.util.RssUrlBuilder;
import com.khokhlov.tendermonitoring.util.TenderCardParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RssMonitorService {

    private final TrackedKeywordRepository trackedKeywordRepository;
    private final TrackedTenderRepository trackedTenderRepository;
    private final TelegramNotificationService notificationService;
    private final TenderMapper tenderMapper;
    private final UserRepository userRepository;

    @Transactional
    public TrackedKeyword startMonitoring(UserDTO userDTO, TrackingAttributeDTO attribute) {
        User user = userRepository.getUsersByIdAndUsername(userDTO.id(), userDTO.username());
        if (trackedKeywordRepository.existsByUserAndKeyword(user, attribute.keyword())) {
            throw new IllegalArgumentException("Keyword already tracked");
        }
        TrackedKeyword keyword = trackedKeywordRepository.save(
                TrackedKeyword.builder()
                        .user(user)
                        .keyword(attribute.keyword())
                        .checkedIntervalMinutes(attribute.interval())
                        .rssUrl(RssUrlBuilder.build(attribute))
                        .build()
        );

        List<RssItemDTO> firstPage = RssParser.parse(keyword.getRssUrl());
        List<RssItemDTO> referencePoint = firstPage.stream()
                .filter(i -> i.purchaseCode().equals(attribute.purchaseCode()))
                .toList();

        saveAndNotify(keyword, referencePoint);

        return keyword;
    }

    @Transactional
    public void checkFeeds(TrackedKeyword keyword) {
        List<RssItemDTO> rssItems = RssParser.parse(keyword.getRssUrl());

        List<RssItemDTO> fresh = keyword.getLastPublishedDate() == null
                ? Collections.emptyList()
                : rssItems.stream()
                .takeWhile(i -> i.publishedDate()
                        .isAfter(keyword.getLastPublishedDate()))
                .toList();

        saveAndNotify(keyword, fresh);
        keyword.setLastUpdate(LocalDateTime.now());
        trackedKeywordRepository.save(keyword);
    }

    private void saveAndNotify(TrackedKeyword keyword, List<RssItemDTO> newItems) {
        if (newItems.isEmpty()) return;

        List<TrackedTender> toSave = new ArrayList<>();

        for (RssItemDTO item : newItems) {
            boolean exists = trackedTenderRepository
                    .existsByTrackedKeyword_IdAndPurchaseCode(keyword.getId(), item.purchaseCode());
            if (exists) continue;

            SpecifiedDate dates = TenderCardParser.parseDate(item.link());

            TrackedTender tender = tenderMapper.toTrackedTender(item, dates);
            tender.setTrackedKeyword(keyword);
            tender.setNotified(false);
            toSave.add(tender);
        }

        if (toSave.isEmpty()) return;

        trackedTenderRepository.saveAll(toSave);

        keyword.setLastPublishedDate(newItems.getFirst().publishedDate());
        trackedKeywordRepository.save(keyword);

        Long chat = keyword.getUser().getTelegramChatId();
        if (chat != null) {
            notificationService.notifyUser(toSave, chat);
            toSave.forEach(t -> t.setNotified(true));
        }
    }
}

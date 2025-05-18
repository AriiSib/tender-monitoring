package com.khokhlov.tendermonitoring.service;

import com.khokhlov.tendermonitoring.mapper.TenderMapper;
import com.khokhlov.tendermonitoring.model.dto.*;
import com.khokhlov.tendermonitoring.model.entity.TrackedKeyword;
import com.khokhlov.tendermonitoring.model.entity.TrackedTender;
import com.khokhlov.tendermonitoring.model.entity.User;
import com.khokhlov.tendermonitoring.repository.TrackedKeywordRepository;
import com.khokhlov.tendermonitoring.repository.TrackedTenderRepository;
import com.khokhlov.tendermonitoring.repository.UserRepository;
import com.khokhlov.tendermonitoring.util.RssParser;
import com.khokhlov.tendermonitoring.util.RssUrlBuilder;
import com.khokhlov.tendermonitoring.util.TenderCardParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RssMonitorService {

    private final TrackedKeywordRepository trackedKeywordRepository;
    private final TrackedTenderRepository trackedTenderRepository;
    private final TenderMapper tenderMapper;
    private final UserRepository userRepository;


    public TrackedKeyword startMonitoring(UserDTO userDTO, TrackingAttributeDTO attribute) {
        User user = userRepository.getUsersByIdAndUsername(userDTO.id(), userDTO.username());
        if (trackedKeywordRepository.existsByUserAndKeyword(user, attribute.keyword())) {
            throw new RuntimeException("Keyword already tracked");
        }
//        else if (attribute.keyword() == null || attribute.keyword().isBlank()) {
//            throw new RuntimeException("Keyword cannot be empty");
//        }
        TrackedKeyword keyword = new TrackedKeyword();
        keyword.setUser(user);
        keyword.setKeyword(attribute.keyword());
        keyword.setCheckedIntervalMinutes(attribute.interval());
        keyword.setRssUrl(RssUrlBuilder.build(attribute));

        List<RssItemDTO> referencePoint = filterByPurchaseCode(attribute, keyword);
        track(keyword, referencePoint);

        return keyword;
    }

    public List<RssItemDTO> checkFeeds(TrackedKeyword keyword) {
        keyword.setLastUpdateFoundAt(LocalDateTime.now());
        List<RssItemDTO> rssItems = RssParser.parse(keyword.getRssUrl());
        if (keyword.getLastPublishedDate() == null) {
            return rssItems;
        } else {
            List<RssItemDTO> newItems = filterAfterLastPublished(rssItems, keyword.getLastPublishedDate());
            if (!newItems.isEmpty()) {
                track(keyword, newItems);
            }
            return newItems;
        }

    }

    private void track(TrackedKeyword keyword, List<RssItemDTO> newItems) {
        List<TrackedTender> trackedTenders = new ArrayList<>();
        for (RssItemDTO item : newItems) {
            SpecifiedDate specifiedDate = TenderCardParser.parseDate(item.link());
            //todo: возможна проблема в сравнении с ZonedDateTime.now()
            if (specifiedDate.expirationDate() == null || specifiedDate.expirationDate().isBefore(ZonedDateTime.now())) {
                continue;
            }

            TrackedTender tender = tenderMapper.toTrackedTender(item, specifiedDate);
            tender.setTrackedKeyword(keyword);
            trackedTenders.add(tender);
        }
        trackedTenderRepository.saveAll(trackedTenders);

        if (!newItems.isEmpty()) {
            keyword.setLastPublishedDate(newItems.getFirst().publishedDate());
            trackedKeywordRepository.save(keyword);
        }
    }

    private List<RssItemDTO> filterByPurchaseCode(TrackingAttributeDTO attribute, TrackedKeyword keyword) {
        return checkFeeds(keyword)
                .stream()
                .filter(item -> item.purchaseCode().equals(attribute.purchaseCode()))
                .toList();
    }

    private List<RssItemDTO> filterAfterLastPublished(List<RssItemDTO> items, ZonedDateTime lastDate) {
        return items.stream()
                .takeWhile(item -> item.publishedDate().isAfter(lastDate))
                .toList();
    }
}

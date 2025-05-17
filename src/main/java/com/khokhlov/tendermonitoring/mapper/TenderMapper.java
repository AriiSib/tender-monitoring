package com.khokhlov.tendermonitoring.mapper;

import com.khokhlov.tendermonitoring.model.dto.RssItemDTO;
import com.khokhlov.tendermonitoring.model.dto.SpecifiedDate;
import com.khokhlov.tendermonitoring.model.entity.TrackedTender;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TenderMapper {

    @Mapping(source = "rss.title", target = "title")
    @Mapping(source = "rss.link", target = "link")
    @Mapping(source = "rss.procurementLaws", target = "procurementLaws")
    @Mapping(source = "rss.stage", target = "stage")
    @Mapping(source = "rss.purchaseObject", target = "purchaseObject")
    @Mapping(source = "rss.price", target = "price")
    @Mapping(source = "rss.currency", target = "currency")
    @Mapping(source = "rss.publishedDate", target = "publishedDate")
    @Mapping(source = "rss.postedDate", target = "postedDate", qualifiedByName = "toZonedDateTime")
    @Mapping(source = "rss.updatedDate", target = "updatedDate", qualifiedByName = "toZonedDateTime")
    @Mapping(source = "rss.purchaseCode", target = "purchaseCode")
    @Mapping(source = "rss.author", target = "author")
    @Mapping(source = "date.expirationDate", target = "expirationDate")
    TrackedTender toTrackedTender(RssItemDTO rss, SpecifiedDate date);

    @Named("toZonedDateTime")
    static ZonedDateTime toZonedDateTime(LocalDate localDate) {
        return localDate != null ? localDate.atStartOfDay(ZoneId.systemDefault()) : null;
    }
}

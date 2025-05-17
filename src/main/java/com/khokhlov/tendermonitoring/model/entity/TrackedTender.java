package com.khokhlov.tendermonitoring.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Table(name = "tracked_tenders")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrackedTender {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(columnDefinition = "TEXT")
    private String title;

    @Column(columnDefinition = "TEXT")
    private String link;

    @Column(columnDefinition = "TEXT")
    private String procurementLaws;

    @Column(columnDefinition = "TEXT")
    private String stage;

    @Column(columnDefinition = "TEXT")
    private String purchaseObject;

    private BigDecimal price;

    private String currency;

    private ZonedDateTime publishedDate;

    private ZonedDateTime postedDate;

    private ZonedDateTime updatedDate;

    private ZonedDateTime expirationDate;

    @Column(columnDefinition = "TEXT")
    private String purchaseCode;

    @Column(columnDefinition = "TEXT")
    private String author;

    private boolean notified;

    @ManyToOne
    @JoinColumn(name = "tracked_keyword_id")
    private TrackedKeyword trackedKeyword;
}
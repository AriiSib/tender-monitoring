package com.khokhlov.tendermonitoring.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tracked_keywords")
public class TrackedKeyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String keyword;

    @Column(columnDefinition = "TEXT")
    private String rssUrl;

    private ZonedDateTime lastPublishedDate;

    private int checkedIntervalMinutes;

    private LocalDateTime lastUpdateFoundAt;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "trackedKeyword", cascade = CascadeType.ALL)
    private List<TrackedTender> trackedTenders = new ArrayList<>();
}

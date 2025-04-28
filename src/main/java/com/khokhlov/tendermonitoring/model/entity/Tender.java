package com.khokhlov.tendermonitoring.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;

@Entity
@Table(name = "tenders")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tender {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String title;

    @Column(columnDefinition = "TEXT")
    private String link;

    @Column(columnDefinition = "TEXT")
    private String searchParams;

    @Column(columnDefinition = "TEXT")
    private String stage;

    @Column(columnDefinition = "TEXT")
    private String purchaseObject;

    private BigDecimal price;

    private LocalDate publishedDate;
    private LocalDate updatedDate;
    private LocalDate deadline;

    @Column(columnDefinition = "TEXT")
    private String purchaseCode;

    @Column(columnDefinition = "TEXT")
    private String author;
}

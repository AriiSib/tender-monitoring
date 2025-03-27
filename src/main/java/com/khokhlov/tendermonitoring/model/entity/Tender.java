package com.khokhlov.tendermonitoring.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
//    @Column(columnDefinition = "TEXT")
//    private String description;
    @Column(columnDefinition = "TEXT")
    private String searchParams;
    @Column(columnDefinition = "TEXT")
    private String procurementLaws;
    @Column(columnDefinition = "TEXT")
    private String stage;
//    @Column(columnDefinition = "TEXT")
//    private String foundResult;
    @Column(columnDefinition = "TEXT")
    private String purchaseObject;
//    @Column(columnDefinition = "TEXT")
//    private String customer;
    @Column(columnDefinition = "TEXT")
    private String price;
    @Column(columnDefinition = "TEXT")
    private String currency;
    @Column(columnDefinition = "TEXT")
    private String publishedDate;
    @Column(columnDefinition = "TEXT")
    private String updatedDate;
    private String deadline;
    @Column(columnDefinition = "TEXT")
    private String purchaseCode;
    @Column(columnDefinition = "TEXT")
    private String author;
}

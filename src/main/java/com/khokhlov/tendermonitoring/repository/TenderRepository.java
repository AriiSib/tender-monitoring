package com.khokhlov.tendermonitoring.repository;

import com.khokhlov.tendermonitoring.model.entity.Tender;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenderRepository extends JpaRepository<Tender, Long> {
}

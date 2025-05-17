package com.khokhlov.tendermonitoring.repository;

import com.khokhlov.tendermonitoring.model.entity.TrackedTender;
import org.springframework.data.repository.CrudRepository;

public interface TrackedTenderRepository extends CrudRepository<TrackedTender, Long> {
}

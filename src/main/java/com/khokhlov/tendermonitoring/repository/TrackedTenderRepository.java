package com.khokhlov.tendermonitoring.repository;

import com.khokhlov.tendermonitoring.model.entity.TrackedTender;
import com.khokhlov.tendermonitoring.model.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TrackedTenderRepository extends CrudRepository<TrackedTender, Long> {

    @Query("SELECT t FROM TrackedTender t WHERE t.trackedKeyword.user = :user")
    List<TrackedTender> findAllForUser(@Param("user") User user);

    void deleteAll();
}

package com.khokhlov.tendermonitoring.repository;

import com.khokhlov.tendermonitoring.model.entity.TrackedKeyword;
import com.khokhlov.tendermonitoring.model.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TrackedKeywordRepository extends CrudRepository<TrackedKeyword, Long> {
    @Override
    List<TrackedKeyword> findAll();

    boolean existsByUserAndKeyword(User user, String keyword);
}

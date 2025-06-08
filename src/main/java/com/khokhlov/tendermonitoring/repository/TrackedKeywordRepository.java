package com.khokhlov.tendermonitoring.repository;

import com.khokhlov.tendermonitoring.model.entity.TrackedKeyword;
import com.khokhlov.tendermonitoring.model.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TrackedKeywordRepository extends CrudRepository<TrackedKeyword, Long> {
    boolean existsByUserAndKeyword(User user, String keyword);

    List<TrackedKeyword> findAllByUser(User user);

    List<TrackedKeyword> findAllByUserId(Long id);
}

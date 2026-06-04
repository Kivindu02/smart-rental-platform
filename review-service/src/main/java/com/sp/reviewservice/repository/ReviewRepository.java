package com.sp.reviewservice.repository;

import com.sp.reviewservice.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {
    boolean existsByPropertyIdAndUserId(UUID propertyId, UUID userId);
    List<Review> findByPropertyId(UUID propertyId);
}

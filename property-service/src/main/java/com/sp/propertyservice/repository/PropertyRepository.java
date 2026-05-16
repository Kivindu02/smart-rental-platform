package com.sp.propertyservice.repository;

import com.sp.propertyservice.model.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PropertyRepository extends JpaRepository<Property, UUID> {
    // This tells JPA: "Only grab the ID column, nothing else."
    @Query("SELECT p.id FROM Property p")
    List<UUID> findAllIds();
}

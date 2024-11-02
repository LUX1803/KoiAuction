package com.mnky.kas.repository;

import com.mnky.kas.model.Farm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface FarmRepository extends JpaRepository<Farm, Short> {
    @Modifying
    @Query(value = "INSERT INTO farm (name, description, image, owner_id,sent) " +
            "VALUES (:name, :description, :image, :owner, :sent)", nativeQuery = true)
    void saveFarm(@Param("name") String name, @Param("description") String description,
                  @Param("image") String image, @Param("owner") short owner,
                  @Param("sent")Timestamp sent);

    Farm findByOwnerId(short owner);

    List<Farm> findByStatus(Farm.FarmStatus status);

    @Query(value = "UPDATE farm SET status = :status WHERE id = :farmId", nativeQuery = true)
    int updateStatus(short farmId, String status);


}
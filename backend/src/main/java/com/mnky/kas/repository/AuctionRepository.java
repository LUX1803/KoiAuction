package com.mnky.kas.repository;


import com.mnky.kas.model.Auction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Short> {

    List<Auction> getAuctionByAuctionStatus(Auction.AuctionStatus status);

    Auction findAuctionById(Short auctionId);

    @Modifying
    @Query(value = "INSERT INTO auction (started, ended, staff_id) " +
            "VALUES (:name, :description, :started, :ended)", nativeQuery = true)
    void saveAuction(@Param("started") Timestamp started,
                     @Param("ended") Timestamp ended);

    Auction findFirstByOrderByIdDesc();

    @Query(value = "insert into auction (started, ended) values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) returning id", nativeQuery = true)
    short save();

    @Query(value = "update auction set status = :status where id = :id", nativeQuery = true)
    int updateAuctionById(@Param("id") short id,@Param("status") String status);
}

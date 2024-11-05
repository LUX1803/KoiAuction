package com.mnky.kas.repository;

import com.mnky.kas.model.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface BidRepository extends JpaRepository<Bid, Short> {
    @Query("SELECT b FROM Bid b WHERE b.lot.id = :lotId ORDER BY b.time DESC")
    List<Bid> findByLotId(short lotId);

    @Query("SELECT COALESCE(MAX(b.amount), 0) FROM Bid b WHERE b.lot.id = :lotId")
    double findHighestBidByLotId(@Param("lotId") short lotId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO bid (amount,highest, bidder_id,lot_id,time) " +
            "VALUES (:amount, :highest, :bidder_id, :lot_id, :time)", nativeQuery = true)
    void saveBid(@Param("amount") double amount,
                 @Param("highest") boolean highest, @Param("bidder_id") short bidder_id,
                 @Param("lot_id") short lot_id, @Param("time") Timestamp time);

    @Modifying
    @Query(value = "SELECT * FROM Bid b WHERE amount = (SELECT MAX(amount) FROM Bid b1 WHERE b1.lot_id = :lot_id) AND b.lot_id = :lot_id", nativeQuery = true)
    List<Bid> findBidsWithMaxAmount(short lot_id);


    @Modifying
    @Transactional
    @Query("UPDATE Bid b SET b.isHighest = true WHERE b.id = :id")
    void updateById(short id);

    List<Bid> findAllByIsHighestFalseAndLotId(short lot_id);

    Bid findByLotIdAndBidderId(short lotId, short bidderId);

    @Query("SELECT b FROM Bid b WHERE b.lot.id = :lotId AND b.isHighest = true")
    Bid findBidIsHighestByLotId(@Param("lotId") short lotId);

    Bid findFirstByLotIdOrderByAmountDesc(short lotId);

    Bid findFirstByLotIdOrderByTimeDesc(short lotId);

    @Query("SELECT b FROM Bid b " +
            "WHERE b.lot.id = :lotId " +
            "AND b.amount = ( " +
            "    SELECT MAX(b2.amount) FROM Bid b2 " +
            "    WHERE b2.lot.id = :lotId AND b2.bidder.id = b.bidder.id " +
            ")")
    List<Bid> findAllHighestBidsByLotId(@Param("lotId") short lotId);

}

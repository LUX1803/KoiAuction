package com.mnky.kas.repository;

import com.mnky.kas.dto.response.LotListViewResponse;
import com.mnky.kas.model.Auction;
import com.mnky.kas.model.Lot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.List;

@Repository
public interface LotRepository extends JpaRepository<Lot, Short>, JpaSpecificationExecutor<Lot> {
//    @Query("SELECT new com.mnky.kas.dto.response.LotListViewResponse(" +
//            "a.id, l.id, l.started, l.ended, l.startingPrice, l.status, " +
//            "k.id, k.length, k.yob, k.farm.name, k.variety, " +
//            "(SELECT COALESCE(MAX(b.amount), l.startingPrice) FROM Bid b WHERE b.lot.id = l.id)) " +
//            "FROM Lot l " +
//            "JOIN l.auction a " +
//            "JOIN l.koi k " +
//            "WHERE a.id = :auctionId")
    @Query("SELECT l FROM Lot l WHERE l.auction.id = :auctionId ORDER BY l.id ASC")
    List<Lot> findByAuctionId(short auctionId);

    Lot findByAuctionIdAndId(short auctionId, short lotId);



    Lot findById(short id);

    @Transactional
    @Query(value = "insert into lot (started, ended, starting_price, estimated_price, reserve_price, buy_now_price, price_interval, time_interval, seller_commission, buyer_premium, auction_id, koi_id, method_id, staff_id) " +
            "values (:started, :ended, :startingPrice, :estimatedPrice, :reservePrice, :buyNowPrice, :priceInterval, :timeInterval,  :sellerCommission, :buyerPremium, :auctionId, :koiId, :methodId, :eeid) RETURNING id", nativeQuery = true)
    short save(Timestamp started,
         Timestamp ended,
         double startingPrice,
         double estimatedPrice,
         double reservePrice,
         double buyNowPrice,
         double priceInterval,
         Duration timeInterval,
         double sellerCommission,
         double buyerPremium,
         short auctionId,
         short koiId,
         short methodId,
         String eeid);

    @Query(value = "update lot set buy_now_price = :newBuyNowPrice where id = :id ", nativeQuery = true)
    int updateLotById(short id, double newBuyNowPrice);

    @Query(value = "update lot set status = :status where id = :id", nativeQuery = true)
    int updateLotById(short id, String status);

    @Query(value = "SELECT * FROM lot WHERE id = LAST_INSERT_ID()", nativeQuery = true)
    Lot findLastInsertedLot();

    Lot findAuctionById(short lotId);

    Lot findByInvoice_Id(short invoiceId);

    List<Lot> findByKoiId(short koiId);

    Lot findFirstByKoiIdOrderByIdDesc(short koiId);
}

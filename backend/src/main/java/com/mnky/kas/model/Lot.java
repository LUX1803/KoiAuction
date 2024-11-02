package com.mnky.kas.model;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "lot")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Lot {

    public enum LotStatus {
        PENDING,
        UPCOMING,
        LIVE,
        AWARDED,
        MISSED,
        CANCELLED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "smallint")
    private short id;
    @Column(name = "started", columnDefinition = "TIMESTAMPtz")
    private Timestamp started;
    @Column(name = "ended", columnDefinition = "TIMESTAMPtz")
    private Timestamp ended;
    @Column(name = "starting_price", columnDefinition = "decimal(10,2) default 0")
    private double startingPrice;
    @Column(name = "estimated_price", columnDefinition = "decimal(10,2) default 0")
    private double estimatedPrice;
    @Column(name = "reserve_price", columnDefinition = "decimal(10,2) default 0.00")
    private double reservePrice;
    @Column(name = "buy_now_price", columnDefinition = "decimal(10,2) default 0.00")
    private double buyNowPrice;
    @Column(name = "price_interval", columnDefinition = "decimal(10,2) default 0.00")
    private double priceInterval;
    @Column(name = "time_interval")
    private Duration timeInterval;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @ColumnDefault("'PENDING'")
    private LotStatus status;
    @Column(name = "seller_commission", columnDefinition = "decimal(3,2) default 0.00")
    private double sellerCommission;
    @Column(name = "buyer_premium", columnDefinition = "decimal(3,2) default 0.00")
    private double buyerPremium;
    @ManyToOne
    @JoinColumn(name = "koi_id")
    private Koi koi;
    @ManyToOne
    @JoinColumn(name = "auction_id")
    private Auction auction;
    @ManyToOne
    @JoinColumn(name = "method_id")
    private AuctionMethod method;
    @ManyToOne
    @JoinColumn(name = "staff_id", referencedColumnName = "eeid")
    private Employee staff;


    @OneToMany(mappedBy = "lot")
    private Set<Bid> bids = new HashSet<>();
    @OneToOne(mappedBy = "lot")
    private Invoice invoice;

}

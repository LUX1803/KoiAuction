package com.mnky.kas.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "automatic_bidding")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AutomaticBidding {
    @Id
    @Column(name = "id", nullable = false, columnDefinition = "SMALLserial")
    private short id;
    @Column(name = "trigger_amount", nullable = false, columnDefinition = "decimal(10,2)")
    private double trigerAmount;
    @Column(name = "increment_amount", nullable = false, columnDefinition = "decimal(10,2)")
    private double incrementAmount;
    @Column(name = "max_amount", columnDefinition = "decimal(10,2)")
    private double maxAmount;
    @Column(name = "activated", nullable = false, columnDefinition = "boolean default true")
    private boolean isActivated;
    @ManyToOne
    @JoinColumn(name = "bidder_id", nullable = false)
    private Member bidder;
    @ManyToOne
    @JoinColumn(name = "lot_id", nullable = false)
    private Lot lot;

    public AutomaticBidding(double trigerAmount, double incrementAmount, double maxAmount, boolean isActivated, Member bidder, Lot lot) {
        this.trigerAmount = trigerAmount;
        this.incrementAmount = incrementAmount;
        this.maxAmount = maxAmount;
        this.isActivated = isActivated;
        this.bidder = bidder;
        this.lot = lot;
    }
}

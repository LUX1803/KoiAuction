package com.mnky.kas.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "smallint")
    private short id;
    @Column(name = "amount", columnDefinition = "decimal(10,2)")
    private double amount;
    @Column(name = "time", columnDefinition = "TIMESTAMPtz default current_timestamp")
    @CreationTimestamp
    private Timestamp time;
    @Column(name = "highest", columnDefinition = "boolean")
    private boolean isHighest;
    @ManyToOne
    @JoinColumn(name = "bidder_id")
    private Member bidder;
    @ManyToOne
    @JoinColumn(name = "lot_id")
    private Lot lot;

}

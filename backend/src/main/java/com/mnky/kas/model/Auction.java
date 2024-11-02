package com.mnky.kas.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "auction")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Auction {

     public enum AuctionStatus {
         PENDING,
        UPCOMING,
        ONGOING,
        CLOSED,
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
    @Column(name = "created", columnDefinition = "TIMESTAMPtz DEFAULT CURRENT_TIMESTAMP")
    @CreationTimestamp
    private Timestamp created;
    @Column(name = "updated", columnDefinition = "TIMESTAMPtz")
    @UpdateTimestamp
    private Timestamp updated;
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "status")
    @ColumnDefault("'PENDING'")
    private AuctionStatus auctionStatus;
    @ManyToOne
    @JoinColumn(name = "staff_id", referencedColumnName = "eeid")
    private Employee staff;

    @OneToMany(mappedBy = "auction")
    private Set<Lot> lots = new HashSet<>();

}

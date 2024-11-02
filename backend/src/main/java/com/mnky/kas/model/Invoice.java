package com.mnky.kas.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {
    public enum InvoiceStatus {
        PENDING,
        PAID,
        UNPAID,
        CANCELLED
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "smallint")
    private short id;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @ColumnDefault("'PENDING'")
    private InvoiceStatus status;
    @Column(name = "hammer_price", columnDefinition = "decimal(10,2)")
    private double hammerPrice;
    @Column(name = "shipping_cost", columnDefinition = "decimal(10,2)")
    private double shippingCost;
    @Column(name = "buyer_premium", columnDefinition = "decimal(3,2)")
    private double buyerPremium;
    @Column(name = "tax", columnDefinition = "decimal(3,2) default 0.1")
    private double tax;
    @Column(name = "buyer_total", columnDefinition = "decimal(10,2)")
    private double buyerTotal; // hammer price * (1 + buyer premium + tax) + shipping cost
    @Column(name = "description", columnDefinition = "text")
    private String description;
    @Column(name = "created", columnDefinition = "TIMESTAMPtz default current_timestamp")
    @CreationTimestamp
    private Timestamp created;
    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private Member recipient;
    @OneToOne
    @JoinColumn(name = "lot_id")
    private Lot lot;

    @OneToOne(mappedBy = "invoice",
            orphanRemoval = true,
            cascade = CascadeType.ALL)
    private Transaction transaction;

}

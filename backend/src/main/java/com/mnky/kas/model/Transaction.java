package com.mnky.kas.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.sql.Timestamp;

@Entity
@Table(name = "transaction")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Transaction {
    public enum PaymentType {
        WALLET, BANK
    }
    public enum TransactionStatus {
        PENDING, SUCCESS, FAILED
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "smallint")
    private short id;
    @Column(name = "amount", columnDefinition = "decimal(10,2)")
    private double amount;
    @Column(name = "description", columnDefinition = "text")
    private String description;
    @Column(name = "payment_type")
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private PaymentType paymentType;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @ColumnDefault("'PENDING'")
    private TransactionStatus status;
    @Column(name = "created", columnDefinition = "TIMESTAMPTZ")
    @CreationTimestamp
    private Timestamp created;
    @Column(name = "updated", columnDefinition = "TIMESTAMPTZ")
    @UpdateTimestamp
    private Timestamp updated;
    @Column(name = "closed", columnDefinition = "TIMESTAMPTZ")
    private Timestamp closed;
    @OneToOne
    @JoinColumn(name = "invoice_id", nullable = true)
    private Invoice invoice;
    @ManyToOne
    @JoinColumn(name = "payment_id", nullable = true)
    private Payment payment;
    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

}

package com.mnky.kas.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "payment")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "smallint")
    private short id;

    @Column(name = "vnp_transaction_id")
    private String vnpTransactionId;  // VNPay transaction reference ID

    @Column(name = "vnp_response_code")
    private String vnpResponseCode;  // VNPay response code

    @Column(name = "vnp_bank_code")
    private String vnpBankCode;  // Bank code used in the transaction

    @Column(name = "vnp_card_type")
    private String vnpCardType;  // Card type used (if applicable)

    @Column(name = "vnp_amount", columnDefinition = "decimal(10,2)")
    private double vnpAmount;  // Amount processed via VNPay

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Transaction.TransactionStatus status;  // PENDING, SUCCESS, FAILED

    @Column(name = "created", columnDefinition = "TIMESTAMPTZ")
    @CreationTimestamp
    private Timestamp created;

    @Column(name = "updated", columnDefinition = "TIMESTAMPTZ")
    @UpdateTimestamp
    private Timestamp updated;

    @OneToMany(mappedBy = "payment")
    private Set<Transaction> transaction = new HashSet<>();
}


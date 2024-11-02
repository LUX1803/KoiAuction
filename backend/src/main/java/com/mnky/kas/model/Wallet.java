package com.mnky.kas.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "wallet")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "smallint")
    private short id;
    @Column(name = "balance", nullable = false, columnDefinition = "decimal(10,2) default 0.00")
    private double balance;

    @OneToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private Member owner;

}

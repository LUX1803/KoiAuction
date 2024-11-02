package com.mnky.kas.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "member")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

    public enum Role {
        BIDDER,
        BREEDER,
        SHIPPER,
        STAFF,
        MANAGER,
        ADMIN
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "smallint")
    private short id;
    @Column(name = "username", nullable = false, unique = true, columnDefinition = "VARCHAR(50)")
    private String username;
    // Bcrypt hash length is 60
    @Column(name = "password", nullable = false, columnDefinition = "VARCHAR(60)")
    private String password;
    @Column(name = "email", nullable = false, unique = true, columnDefinition = "VARCHAR(50)")
    private String email;
    @Column(name = "phone", columnDefinition = "VARCHAR(10)")
    private String phone;
    @Column(name = "address", columnDefinition = "VARCHAR(100)")
    private String address;
    @Column(name = "firstname", nullable = false, columnDefinition = "VARCHAR(50)")
    private String firstname;
    @Column(name = "lastname", nullable = false, columnDefinition = "VARCHAR(50)")
    private String lastname;
    @Column(name = "gender", length = 7)
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private Gender gender;
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @ColumnDefault("'BIDDER'")
    private Role role;

    @OneToOne(mappedBy = "member")
    private Employee employee;
    @OneToOne(mappedBy = "owner")
    private Farm farm;
    @OneToMany(mappedBy = "bidder")
    private Set<Bid> bids = new HashSet<>();
    @OneToMany(mappedBy = "member")
    private Set<Transaction> transactions = new HashSet<>();
    @OneToOne(mappedBy = "owner")
    private Wallet wallet;
    @OneToMany(mappedBy = "recipient")
    private Set<Invoice> invoices = new HashSet<>();

}

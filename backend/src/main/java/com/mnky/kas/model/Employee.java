package com.mnky.kas.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "employee")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Employee {
    @Id
    @Column(name = "eeid", nullable = false, unique = true, columnDefinition = "char(5)")
    private String eeid;
    @OneToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "staff")
    private Set<Auction> auctions = new HashSet<>();
    @OneToMany(mappedBy = "staff")
    private Set<Lot> lots = new HashSet<>();
    @OneToMany(mappedBy = "staff")
    private Set<Koi> kois = new HashSet<>();
    @OneToMany(mappedBy = "staff")
    private Set<Farm> farms = new HashSet<>();

    public Employee(Member member, String eeid) {
        this.member = member;
        this.eeid = eeid;
    }
}

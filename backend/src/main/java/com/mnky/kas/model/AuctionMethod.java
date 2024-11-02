package com.mnky.kas.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AuctionMethod {
    @Id
    @Column(name = "id", nullable = false, columnDefinition = "smallserial")
    private short id;
    @Column(name = "name", nullable = false, columnDefinition = "varchar(50)")
    private String name;
    @Column(name = "description", nullable = false, columnDefinition = "text")
    private String description;

    @OneToMany(mappedBy = "method")
    private Set<Lot> lots = new HashSet<>();

    public AuctionMethod(String name, String description) {
        this.name = name;
        this.description = description;
    }
}

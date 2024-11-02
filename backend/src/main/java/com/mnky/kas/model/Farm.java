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
@Table(name = "farm")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Farm {
    public enum FarmStatus {
        PENDING,
        APPROVED,
        REJECTED
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "smallint")
    private short id;
    @Column(name = "name", nullable = false, columnDefinition = "varchar(50)")
    private String name;
    @Column(name = "description", columnDefinition = "text")
    private String description;
    @Column(name = "status", nullable = false)
    @ColumnDefault("'PENDING'")
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private FarmStatus status;
    @Column(name = "image", columnDefinition = "text")
    private String image;
    @Column(name = "sent", nullable = false, columnDefinition = "TIMESTAMPtz default current_timestamp")
    @CreationTimestamp
    private Timestamp sent;
    @Column(name = "replied", columnDefinition = "TIMESTAMPtz")
    @UpdateTimestamp
    private Timestamp replied;
    @OneToOne
    @JoinColumn(name = "owner_id", nullable = false, referencedColumnName = "id")
    private Member owner;
    @ManyToOne
    @JoinColumn(name = "staff_id", referencedColumnName = "eeid")
    private Employee staff;

    @OneToMany(mappedBy = "farm")
    private Set<Koi> kois = new HashSet<>();

}

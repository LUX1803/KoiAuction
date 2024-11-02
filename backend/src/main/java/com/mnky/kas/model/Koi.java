package com.mnky.kas.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "koi")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Koi {

    public enum KoiStatus {
        PENDING, // koi is not yet ready for auction
        REJECTED, // koi is rejected by auction company
        FREE, // auction company has not yet scheduled this koi
        QUEUED, // koi has been scheduled for auction, waiting for auction
        AUCTIONING, // next auction has this koi
        HELD, // koi is bid on, but not sold ( delivery not yet done )
        SHIPPING, // koi is sold, but not yet delivered
        SOLD // breeder has been paid
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "smallint")
    private short id;
    @Column(name = "yob", columnDefinition = "char(4)")
    private short yob;
    @Column(name = "length", columnDefinition = "smallint")
    private short length;
    @Column(name = "gender", length = 7)
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private Gender gender;
    @Column(name = "image", columnDefinition = "text")
    private String image;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @ColumnDefault("'PENDING'")
    private KoiStatus status;
    @Column(name = "sent", columnDefinition = "TIMESTAMPTZ")
    @CreationTimestamp
    private Timestamp sent;
    @Column(name = "replied", columnDefinition = "TIMESTAMPTZ")
    private Timestamp replied;

    @Column(name = "message", columnDefinition = "text")
    private String message;
    @Column(name = "note", columnDefinition = "text")
    private String note;

    @ManyToOne
    @JoinColumn(name = "variety_id")
    private Variety variety;
    @ManyToOne
    @JoinColumn(name = "farm_id")
    private Farm farm;
    @ManyToOne
    @JoinColumn(name = "eeid", referencedColumnName = "eeid")
    private Employee staff;

    @OneToMany(mappedBy = "koi")
    private Set<Lot> lots = new HashSet<>();

}

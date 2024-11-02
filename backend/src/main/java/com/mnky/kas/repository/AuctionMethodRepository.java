package com.mnky.kas.repository;

import com.mnky.kas.model.AuctionMethod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuctionMethodRepository extends JpaRepository<AuctionMethod, Short> {
    AuctionMethod findById(short id);
}

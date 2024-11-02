package com.mnky.kas.repository;

import com.mnky.kas.model.Member;
import com.mnky.kas.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Short> {

    @Modifying
    @Query(value = "INSERT INTO wallet (balance, owner_id) VALUES (:balance, :owner_id)", nativeQuery = true)
    void saveWallet(@Param("balance") double balance, @Param("owner_id") short owner_id);

    Wallet findByOwnerId(short owner_id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Wallet w SET w.balance = :balance WHERE w.owner.id = :ownerId")
    void updateByOwnerId(@Param("ownerId") short owner_id, @Param("balance") double balance);

    Wallet findByOwner(Member owner);
//    @Modifying
//    @Query(value = "INSERT INTO wallet (balance, id, owner_id) VALUES (:balance, :id, :owner_id)", nativeQuery = true)
//    void saveWallet(@Param("balance") double balance, @Param("id") short id, @Param("owner_id") short owner_id);
}

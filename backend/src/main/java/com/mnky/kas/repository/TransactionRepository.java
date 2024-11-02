package com.mnky.kas.repository;

import com.mnky.kas.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Short> {
    Transaction findById(short id);
    Transaction findByInvoice_Id(short invoiceId);
    List<Transaction> findByMemberId(short bidderId);
    List<Transaction> findByMemberIdAndStatus(short bidderId, Transaction.TransactionStatus status);

    @Query(value = "insert into transaction (member_id, amount) values (:memberId, :amount)", nativeQuery = true)
     int createTransaction(short memberId, double amount);
}

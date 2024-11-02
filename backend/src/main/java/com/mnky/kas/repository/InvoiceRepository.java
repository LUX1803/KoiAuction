package com.mnky.kas.repository;

import com.mnky.kas.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, Short> {
    Invoice findById(short id);
    Invoice findByLotId(short lotId);
    Invoice findByLotIdAndRecipientId(short lotId, short recipientId);
}

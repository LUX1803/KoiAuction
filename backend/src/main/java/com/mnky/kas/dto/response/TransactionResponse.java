package com.mnky.kas.dto.response;

import com.mnky.kas.model.Transaction;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionResponse {
    private short id;
    private double amount;
    private String description;
    private Transaction.PaymentType paymentType;
    private Transaction.TransactionStatus status;
    private Timestamp created;
    private Timestamp updated;
    private Timestamp closed;
    private short memberId;    // Assuming the member ID is a Long
    private short invoiceId;  // Assuming the invoice ID is a short
    private short paymentId;

}

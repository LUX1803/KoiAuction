package com.mnky.kas.dto.response;

import com.mnky.kas.model.Invoice;
import lombok.*;

import java.sql.Timestamp;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvoiceResponse {
    private short id;
    private Invoice.InvoiceStatus status;
    private double hammerPrice;
    private double shippingCost;
    private double buyerPremium;
    private double tax;
    private double buyerTotal;
    private String description;
    private Timestamp created;
    private Long recipientId;  // Assuming the recipient ID is a Long
    private Long lotId;
}

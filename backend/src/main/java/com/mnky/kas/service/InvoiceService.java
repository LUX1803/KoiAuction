package com.mnky.kas.service;

import com.mnky.kas.dto.response.InvoiceResponse;

import java.text.ParseException;
import java.util.List;

public interface InvoiceService {
    InvoiceResponse getInvoiceByLotIdAndRecipientId(short lotId, String token) throws ParseException;

    InvoiceResponse getInvoiceByLotId(short lotId);

    InvoiceResponse getInvoiceById(short invoiceId);

    List<InvoiceResponse> getInvoiceByRecipientId(String token);
}

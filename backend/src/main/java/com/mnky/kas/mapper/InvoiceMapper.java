package com.mnky.kas.mapper;

import com.mnky.kas.dto.response.InvoiceResponse;
import com.mnky.kas.model.Invoice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface InvoiceMapper {
    @Mapping(target = "recipientId", source = "recipient.id")
    InvoiceResponse toInvoiceResponse(Invoice invoice);
}

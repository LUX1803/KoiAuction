package com.mnky.kas.mapper;

import com.mnky.kas.dto.response.TransactionResponse;
import com.mnky.kas.model.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface TransactionMapper {
    @Mapping(source = "member.id", target = "memberId")
    @Mapping(source = "amount", target = "amount")
    @Mapping(source = "invoice.id", target = "invoiceId")
    TransactionResponse toTransactionResponse(Transaction transaction);
}

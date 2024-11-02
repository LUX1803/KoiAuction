package com.mnky.kas.service;

import com.mnky.kas.dto.response.InvoiceResponse;
import com.mnky.kas.mapper.InvoiceMapper;
import com.mnky.kas.repository.InvoiceRepository;
import com.mnky.kas.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final InvoiceMapper invoiceMapper;

    @Override
    public InvoiceResponse getInvoiceByLotIdAndRecipientId(short lotId, String token) throws ParseException {
        short recipientId = JWTUtil.getMemberIdFromToken(token.substring(7));
        return invoiceMapper.toInvoiceResponse(invoiceRepository.findByLotIdAndRecipientId(lotId, recipientId));
    }

    @Override
    public InvoiceResponse getInvoiceByLotId(short lotId) {
        return null;
    }

    @Override
    public List<InvoiceResponse> getInvoiceByRecipientId(String token) {
        return null;
    }

    @Override
    public InvoiceResponse getInvoiceById(short invoiceId) {
        return invoiceMapper.toInvoiceResponse(invoiceRepository.findById(invoiceId));
    }
}

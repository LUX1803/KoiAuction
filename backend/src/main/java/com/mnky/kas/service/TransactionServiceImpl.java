package com.mnky.kas.service;

import com.mnky.kas.dto.response.TransactionResponse;
import com.mnky.kas.mapper.TransactionMapper;
import com.mnky.kas.model.Transaction;
import com.mnky.kas.repository.TransactionRepository;
import com.mnky.kas.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
        private final TransactionRepository transactionRepository;
        private final TransactionMapper transactionMapper;

        @Override
        public List<TransactionResponse> getTransactionByMemberId(String token) throws ParseException {
            List<TransactionResponse> res = new ArrayList<>();

            short memberId = JWTUtil.getMemberIdFromToken(token.substring(7));
            List<Transaction> transactions = transactionRepository.findByMemberId(memberId);

            if (transactions != null && !transactions.isEmpty()) {
                for (Transaction transaction : transactions) {
                    res.add(transactionMapper.toTransactionResponse(transaction));
                }
            }

            return res;
        }

        @Override
        public List<TransactionResponse> getTransactionByMemberIdAndStatus(String token, String status) throws ParseException {
            List<TransactionResponse> res = new ArrayList<>();

            short memberId = JWTUtil.getMemberIdFromToken(token.substring(7));
            List<Transaction> transactions = transactionRepository.findByMemberIdAndStatus(memberId, Transaction.TransactionStatus.valueOf(status));

            if (transactions != null && !transactions.isEmpty()) {
                for (Transaction transaction : transactions) {
                    res.add(transactionMapper.toTransactionResponse(transaction));
                }
            }

            return res;
        }
}

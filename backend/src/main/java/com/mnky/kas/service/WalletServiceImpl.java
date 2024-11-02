package com.mnky.kas.service;

import com.mnky.kas.dto.request.WalletRegisterRequest;
import com.mnky.kas.dto.response.TransactionResponse;
import com.mnky.kas.dto.response.WalletResponse;
import com.mnky.kas.mapper.TransactionMapper;
import com.mnky.kas.model.Member;
import com.mnky.kas.model.Transaction;
import com.mnky.kas.model.Wallet;
import com.mnky.kas.repository.MemberRepository;
import com.mnky.kas.repository.TransactionRepository;
import com.mnky.kas.repository.WalletRepository;
import com.mnky.kas.util.JWTUtil;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class WalletServiceImpl implements WalletService {
    private final WalletRepository walletRepository;
    private final MemberRepository memberRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    @Autowired
    public WalletServiceImpl(WalletRepository walletRepository, MemberRepository memberRepository, TransactionRepository transactionRepository, TransactionMapper transactionMapper) {
        this.walletRepository = walletRepository;
        this.memberRepository = memberRepository;
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
    }

    @Override
    @Transactional
    public void createWallet(WalletRegisterRequest walletRegisterRequest) {
        walletRepository.saveWallet(walletRegisterRequest.getBalance(), walletRegisterRequest.getOwnerId());

    }
    public WalletResponse getMemberAndWallet(String bearerToken) throws ParseException {
        // Parse token để lấy thông tin người dùng
        //SignedJWT jwt = SignedJWT.parse(bearerToken);

        String username = JWTUtil.getUserNameFromToken(bearerToken.substring(7));

        // Tìm thành viên và ví của họ
        Member member = memberRepository.findByUsername(username);
        Wallet wallet = walletRepository.findByOwner(member);

        WalletResponse res = new WalletResponse();
        res.setBalance(wallet.getBalance());

        return res;
    }

    @Override
    @Transactional
    public TransactionResponse addBalanceTransaction(String bearerToken, Double balance) throws ParseException {
        String username = JWTUtil.getUserNameFromToken(bearerToken.substring(7));
        Member member = memberRepository.findByUsername(username);

        //Create Transactions
        Transaction transaction = new Transaction();
        transaction.setAmount(balance);
        transaction.setDescription(new Date().getTime() + "_" + "ADD_" + balance);
        transaction.setPaymentType(Transaction.PaymentType.WALLET);
        transaction.setClosed(null);
        transaction.setCreated(new Timestamp(System.currentTimeMillis()));
        transaction.setMember(member);

//        doesnt need invoice and payment
        transaction.setInvoice(null);
        transaction.setPayment(null);
        transaction.setStatus(Transaction.TransactionStatus.PENDING);

//        System.out.println("trans: "+ transaction.toString());
        return transactionMapper.toTransactionResponse(transactionRepository.save(transaction));


    }

    @Override
    public void addBalance(Member owner , Double balance) {

        Wallet wallet = walletRepository.findByOwner(owner);
        wallet.setBalance(wallet.getBalance() + balance);
        walletRepository.save(wallet);

    }

}

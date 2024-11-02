package com.mnky.kas.service;

import com.mnky.kas.dto.request.WalletRegisterRequest;
import com.mnky.kas.dto.response.WalletResponse;
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
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private TransactionRepository transactionRepository;

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
    public void addBalance(String bearerToken, Integer balance) throws ParseException {
        String username = JWTUtil.getUserNameFromToken(bearerToken.substring(7));
        Member member = memberRepository.findByUsername(username);

        //Create Transactions
        Transaction transaction = new Transaction();
        transaction.setAmount(balance);
        transaction.setDescription(new Date().getTime() + "_" + "ADD_WALLET_BALANCE" + balance);
        transaction.setPaymentType(Transaction.PaymentType.WALLET);
        transaction.setClosed(null);
        transaction.setCreated(new Timestamp(System.currentTimeMillis()));
        transaction.setMember(member);

//        doesnt need invoice and payment
        transaction.setInvoice(null);
        transaction.setPayment(null);
        transaction.setStatus(Transaction.TransactionStatus.PENDING);

//        System.out.println("trans: "+ transaction.toString());
        transactionRepository.save(transaction);


    }

}

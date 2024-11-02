package com.mnky.kas.service;

import com.mnky.kas.dto.request.WalletRegisterRequest;
import com.mnky.kas.dto.response.TransactionResponse;
import com.mnky.kas.dto.response.WalletResponse;
import com.mnky.kas.mapper.TransactionMapper;
import com.mnky.kas.model.*;
import com.mnky.kas.repository.*;
import com.mnky.kas.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class WalletServiceImpl implements WalletService {
    private final WalletRepository walletRepository;
    private final MemberRepository memberRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final InvoiceRepository invoiceRepository;
    private final LotRepository lotRepository;
    private final KoiRepository koiRepository;
    private final PaymentRepository paymentRepository;
    @Autowired
    public WalletServiceImpl(WalletRepository walletRepository, MemberRepository memberRepository, TransactionRepository transactionRepository, TransactionMapper transactionMapper, InvoiceRepository invoiceRepository, LotRepository lotRepository, KoiRepository koiRepository, PaymentRepository paymentRepository) {
        this.walletRepository = walletRepository;
        this.memberRepository = memberRepository;
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
        this.invoiceRepository = invoiceRepository;
        this.lotRepository = lotRepository;
        this.koiRepository = koiRepository;
        this.paymentRepository = paymentRepository;
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

        // Tìm ví của họ
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
    @Transactional
    public void addBalance(Member owner , Double balance) {

        Wallet wallet = walletRepository.findByOwner(owner);
        wallet.setBalance(wallet.getBalance() + balance);
        walletRepository.save(wallet);

    }

    @Override
    public void paymentWithWallet(String bearerToken, List<Integer> transIdList) throws ParseException {
        //getUser
        String username = JWTUtil.getUserNameFromToken(bearerToken.substring(7));
        Member member = memberRepository.findByUsername(username);
        Wallet wallet = walletRepository.findByOwner(member);

        System.out.println("Current Money: " + wallet.getBalance());

        long amount = 0;

        for ( Integer transId : transIdList ) {
            //getTransaction
            Transaction transaction = transactionRepository.findById((short) Integer.parseInt(String.valueOf(transId)));

            //cal all amount
            amount += transaction.getAmount();

            transaction.setStatus(Transaction.TransactionStatus.SUCCESS);
            System.out.println("Current Amount: "+ transaction.getAmount());

            //update Transactions
            transaction.setDescription(transaction.getDescription() + "_" + "REM_" + transaction.getAmount());
            transaction.setPaymentType(Transaction.PaymentType.WALLET);

            //Invoice and others
            Invoice invoice = invoiceRepository.findById(transaction.getInvoice().getId());
            invoice.setStatus(Invoice.InvoiceStatus.PAID);
            Lot lot = lotRepository.findByInvoice_Id(invoice.getId());
            Koi koi = lot.getKoi();
            koi.setStatus(Koi.KoiStatus.SHIPPING);
//        koiRepository.save(koi); ====================
//        invoiceRepository.save(invoice); ===================

//        transactionRepository.save(transaction);  =============================

        }


        //Create Set<Trans> => add to Payment
        Set<Transaction> transactions = new HashSet<>();
        for (Integer transId : transIdList) {
            transactions.add(transactionRepository.findById(Short.parseShort(transId+"")));
        }
        //create payment
        Payment payment = new Payment();

        amount *= 100L;
        payment.setVnpAmount(amount);
        payment.setTransaction(transactions);
//        Payment savedPayment = paymentRepository.save(payment); ==============

        //setType to WALLET
        for (Integer transId : transIdList) {
            Transaction trans = transactionRepository.findById(Short.parseShort(transId+""));
            //        transaction.setPayment(savedPayment);=======================
            trans.setPaymentType(Transaction.PaymentType.WALLET);
            //update transaction
//        transactionRepository.save(transaction);  =============================
        }





        //CATCH WHEN WALLET KHONG DU
    }

}

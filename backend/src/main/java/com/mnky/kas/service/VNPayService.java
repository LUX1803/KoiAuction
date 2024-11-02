package com.mnky.kas.service;

import com.mnky.kas.config.VNPayConfig;
import com.mnky.kas.dto.response.VNPayResponse;
import com.mnky.kas.model.*;
import com.mnky.kas.repository.*;
import com.mnky.kas.util.VNPayUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.*;

@Service
@RequiredArgsConstructor
public class VNPayService {

    private final TransactionRepository transactionRepository;
    private final InvoiceRepository invoiceRepository;
    private final PaymentRepository paymentRepository;
    private final LotRepository lotRepository;
    private final KoiRepository koiRepository;
    private final WalletService walletService;

    public VNPayResponse createVnPayPayment(HttpServletRequest request) {
        String[] transactionIds = request.getParameterValues("transactionIds");

        long amount = 0;
        for (String transactionId : transactionIds) {
            System.out.println(transactionId);
            amount += transactionRepository.findById(Short.parseShort(transactionId)).getAmount();
        }

        amount *= 100L;
        String bankCode = "NCB";
        Map<String, String> vnpParamsMap = VNPayConfig.getVNPayConfig();
        vnpParamsMap.put("vnp_Amount", String.valueOf(amount));
        vnpParamsMap.put("vnp_IpAddr", request.getRemoteAddr());
        vnpParamsMap.put("vnp_BankCode", bankCode);
        vnpParamsMap.put("vnp_TxnRef", String.join(",", transactionIds));
        String queryUrl = VNPayUtil.getPaymentURL(vnpParamsMap, true);
        String hashData = VNPayUtil.getPaymentURL(vnpParamsMap, false);
        String vnpSecureHash = VNPayUtil.hmacSHA512(VNPayUtil.getSecretKey(), hashData);

        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
        String paymentUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html" + "?" + queryUrl;


        return new VNPayResponse(200, "Success", paymentUrl);
    }

    public String handleVnPayCallback(HttpServletRequest request) {

        String responseCode = request.getParameter("vnp_ResponseCode");
        boolean isAddBalance = true;


        if (responseCode.equals("00")) {
            String[] transactionIds = request.getParameter("vnp_TxnRef").split(",");
            for (String transactionId : transactionIds) {
                System.out.println(transactionId);
                Transaction trans = transactionRepository.findById(Short.parseShort(transactionId));
                trans.setStatus(Transaction.TransactionStatus.SUCCESS);

                if (trans.getInvoice() != null) {
                    isAddBalance = false;
                    Invoice invoice = invoiceRepository.findById(trans.getInvoice().getId());
                    invoice.setStatus(Invoice.InvoiceStatus.PAID);
                    Lot lot = lotRepository.findByInvoice_Id(invoice.getId());
                    Koi koi = lot.getKoi();
                    koi.setStatus(Koi.KoiStatus.SHIPPING);
                    koiRepository.save(koi);
                    invoiceRepository.save(invoice);
                } else {
                    Member member = trans.getMember();
                    walletService.addBalance(member, trans.getAmount());
                }

                transactionRepository.save(trans);
            }
            Set<Transaction> transactions = new HashSet<>();
            for (String transactionId : transactionIds) {
                transactions.add(transactionRepository.findById(Short.parseShort(transactionId)));
            }

            Payment payment = new Payment();
            payment.setVnpAmount(Double.parseDouble(request.getParameter("vnp_Amount")));
            payment.setVnpBankCode(request.getParameter("vnp_BankCode"));
            payment.setVnpCardType(request.getParameter("vnp_CardType"));
            payment.setTransaction(transactions);

            Payment savedPayment = null;
            if (!isAddBalance) {
                savedPayment = paymentRepository.save(payment);
            }


            for (String transactionId : transactionIds) {
                Transaction trans = transactionRepository.findById(Short.parseShort(transactionId));

                // isAddBalance == false
                if (savedPayment != null) {
                    trans.setPayment(savedPayment);
                    trans.setPaymentType(Transaction.PaymentType.BANK);
                }else{
                    trans.setPaymentType(Transaction.PaymentType.WALLET);
                }

                transactionRepository.save(trans);
            }

            if(isAddBalance) {

                return "http://localhost:5173/wallet";
            }else {
                return "http://localhost:5173/billing";
            }


        } else {
            // Failure
            System.out.println("Fail");
            return "http://localhost:5173/billing"; //temp
        }
    }

}

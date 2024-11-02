package com.mnky.kas.service;

import com.mnky.kas.dto.request.KoiRequest;
import com.mnky.kas.dto.response.KoiResponse;
import com.mnky.kas.mapper.KoiMapper;
import com.mnky.kas.model.*;
import com.mnky.kas.repository.*;
import com.mnky.kas.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KoiServiceImpl implements KoiService {

    private final FarmRepository farmRepository;
    private final KoiRepository koiRepository;
    private final KoiMapper koiMapper;
    private final LotRepository lotRepository;
    private final InvoiceRepository invoiceRepository;
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;

    @Override
    public void createKoi(KoiRequest koiRequest, String token) throws ParseException {
        String jwtToken = token.substring(7);
        short memberId = JWTUtil.getMemberIdFromToken(jwtToken);
        Farm farm = farmRepository.findByOwnerId(memberId);
        if (farm == null) {
            throw new RuntimeException("Farm not found");
        }
        Koi koi = new Koi();
        koi.setYob(koiRequest.getYob());
        koi.setLength(koiRequest.getLength());
        koi.setGender(Gender.valueOf(koiRequest.getGender()));
        koi.setImage(koiRequest.getImage());
        koi.setMessage(koiRequest.getMessage());
        koi.setVariety(new Variety());
        koi.getVariety().setId(koiRequest.getVarietyId());
        koi.setFarm(farm);
        koi.setStatus(Koi.KoiStatus.PENDING);

        koiRepository.save(koi);

        //koiRepository.saveKoi(koiRequest.getYob(), koiRequest.getLength(), Gender.valueOf(koiRequest.getGender()), koiRequest.getImage(), koiRequest.getMessage(), koiRequest.getVarietyId(), farm.getId());
    }

    @Override
    public List<KoiResponse> getKoiByStatus(String koiStatus) {
        List<KoiResponse> koiResponses = new ArrayList<>();
        List<Koi> lists= koiRepository.findAllByStatus(Koi.KoiStatus.valueOf(koiStatus));
        for(Koi koi: lists ){
            koiResponses.add(koiMapper.toKoiResponse(koi));
        }
        return koiResponses;
    }

    @Override
    public List<KoiResponse> getKoiByFarmId(short farmId) {
        List<KoiResponse> koiResponses = new ArrayList<>();
        List<Koi> lists= koiRepository.findAllByFarmId(farmId);
        for(Koi koi: lists ){
            koiResponses.add(koiMapper.toKoiResponse(koi));
        }
        return koiResponses;
    }

    @Override
    public List<KoiResponse> getKoiByVariety(short varietyId) {
        List<KoiResponse> koiResponses = new ArrayList<>();
        List<Koi> lists= koiRepository.findAllByVarietyId(varietyId);
        for(Koi koi: lists ){
            koiResponses.add(koiMapper.toKoiResponse(koi));
        }
        return koiResponses;
    }

    @Override
    public void updateKoi(KoiRequest koiRequest) {
        Koi koi = koiRepository.findById(koiRequest.getId());


        if (koiRequest.getNote() != null && koi.getReplied() == null) {
            koi.setNote(koiRequest.getNote());
            koi.setStatus(Koi.KoiStatus.valueOf(koiRequest.getStatus()));
            koi.setReplied(new Timestamp(System.currentTimeMillis()));
        }

        if (koiRequest.getStatus() != null && koiRequest.getStatus().equalsIgnoreCase("SOLD")) {
            koi.setStatus(Koi.KoiStatus.SOLD);

            Transaction trans = new Transaction();
            trans.setMember(koi.getFarm().getOwner());
            trans.setStatus(Transaction.TransactionStatus.SUCCESS);

            Lot lot = lotRepository.findFirstByKoiIdOrderByIdDesc(koi.getId());
            Invoice invoice = invoiceRepository.findByLotId(lot.getId());
            double sellerTotal = invoice.getHammerPrice() * (1 - lot.getSellerCommission());
            trans.setAmount(sellerTotal);
            trans.setUpdated(new Timestamp(System.currentTimeMillis()));
            trans.setStatus(Transaction.TransactionStatus.SUCCESS);
            Wallet wallet = walletRepository.findByOwnerId(koi.getFarm().getOwner().getId());
            double balance = wallet.getBalance();
            wallet.setBalance(balance + sellerTotal);
            trans.setDescription("Sold koi " + koi.getId());
            walletRepository.save(wallet);
            transactionRepository.save(trans);
        }

        koiRepository.save(koi);
    }

    @Override
    public List<KoiResponse> getFarmKoi(String token) throws ParseException {
        String jwtToken = token.substring(7);
        short memberId = JWTUtil.getMemberIdFromToken(jwtToken);
        Farm farm = farmRepository.findByOwnerId(memberId);
        List<KoiResponse> koiResponses = new ArrayList<>();
        List<Koi> lists= koiRepository.findAllByFarmId(farm.getId());
        for(Koi koi: lists ){
            koiResponses.add(koiMapper.toKoiResponse(koi));
        }
        return koiResponses;
    }

    @Override
    public KoiResponse getFarmKoiDetail(String token, short koiId) throws ParseException {
        String jwtToken = token.substring(7);
        short memberId = JWTUtil.getMemberIdFromToken(jwtToken);
        Farm farm = farmRepository.findByOwnerId(memberId);
        Koi koi = koiRepository.findByFarmIdAndId(farm.getId(), koiId);
        if (koi.getFarm().getId() != farm.getId()) {
            throw new RuntimeException("Koi not found");
        }
        return koiMapper.toKoiResponse(koi);
    }
}

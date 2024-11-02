package com.mnky.kas.service;

import com.mnky.kas.dto.request.BreederRegisterRequest;
import com.mnky.kas.dto.request.MemberRegisterRequest;
import com.mnky.kas.dto.request.WalletRegisterRequest;
import com.mnky.kas.dto.response.BreederRegisterResponse;
import com.mnky.kas.dto.response.MemberProfileViewResponse;
import com.mnky.kas.dto.response.MemberRegisterResponse;
import com.mnky.kas.exception.AppException;
import com.mnky.kas.exception.ErrorCode;
import com.mnky.kas.mapper.MemberMapper;
import com.mnky.kas.model.Invoice;
import com.mnky.kas.model.Koi;
import com.mnky.kas.model.Lot;
import com.mnky.kas.model.Member;
import com.mnky.kas.repository.FarmRepository;
import com.mnky.kas.repository.KoiRepository;
import com.mnky.kas.repository.LotRepository;
import com.mnky.kas.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@Service
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    WalletService walletService;
    @Autowired
    private FarmServiceImpl farmServiceImpl;
    @Autowired
    private FarmRepository farmRepository;
    @Autowired
    private KoiRepository koiRepository;
    @Autowired
    private LotRepository lotRepository;

    @Override
    @Transactional
    public MemberRegisterResponse register(MemberRegisterRequest request) {
        if (memberRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USERNAME_ALREADY_EXISTS);
        }

        memberRepository.saveMember(request.getUsername(), passwordEncoder.encode(request.getPassword()),
                                    request.getEmail(), request.getPhone(), request.getAddress(),
                                    request.getFirstname(), request.getLastname(),
                                    request.getGender().name());

        Member member = memberRepository.findByUsername(request.getUsername());

        walletService.createWallet(WalletRegisterRequest.builder()
                .ownerId(member.getId())
                .build());

        return memberMapper.toMemberRegisterResponse(member);
    }

    @Override
    @Transactional
    public BreederRegisterResponse registerBreeder(BreederRegisterRequest request) {
        Member member = memberRepository.findByUsername(request.getUsername());
        farmServiceImpl.createFarm(BreederRegisterRequest.builder().owner(member.getId())
                .name(request.getName())
                .image(request.getImage())
                .description(request.getDescription())
                .sent(new Timestamp(System.currentTimeMillis()))
                .build());
        member.setFarm(farmRepository.findByOwnerId(member.getId()));

        return memberMapper.toBreederRegisterResponse(member);
    }

    @Override
    public MemberProfileViewResponse viewProfile(short memberId) {
        Member member = memberRepository.findById(memberId);
        if(member == null){
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        return memberMapper.toMemberProfileViewResponse(member);
    }

    @Override
    public MemberProfileViewResponse viewRecipientProfile(short koiId) {
        Lot lot = lotRepository.findFirstByKoiIdOrderByIdDesc(koiId);
        Member member = memberRepository.findById(lot.getInvoice().getRecipient().getId());
        if(member == null){
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        return memberMapper.toMemberProfileViewResponse(member);
    }
}

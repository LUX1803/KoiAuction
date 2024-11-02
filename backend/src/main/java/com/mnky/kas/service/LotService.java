package com.mnky.kas.service;


import com.mnky.kas.dto.request.LotRequest;
import com.mnky.kas.dto.request.LotUpdateRequest;
import com.mnky.kas.dto.response.LotDetailViewResponse;
import com.mnky.kas.dto.response.LotListViewResponse;
import com.mnky.kas.dto.response.LotSearchResponse;
import com.mnky.kas.model.Lot;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.text.ParseException;
import java.time.Duration;
import java.util.List;

public interface LotService {
    List<LotListViewResponse> viewLotList(short auctionId);

    LotDetailViewResponse viewLotDetail(short auctionId, short lotId);

    List<LotSearchResponse> searchLots(Short auctionId, String varietyName,
                                                           Short length, String gender,
                                                           String farmName, String methodName,
                                                           Double minPrice, Double maxPrice);


    void createLot(LotRequest dto, String token) throws ParseException;

    LotDetailViewResponse updateLot(short lotId, LotUpdateRequest request, String token) throws ParseException;

    List<LotDetailViewResponse> getLotByKoiId(short koiId);
}


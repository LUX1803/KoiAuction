package com.mnky.kas.service;

import com.mnky.kas.dto.request.KoiRequest;
import com.mnky.kas.dto.response.KoiResponse;
import com.mnky.kas.model.Koi;

import java.text.ParseException;
import java.util.List;

public interface KoiService {

    void createKoi(KoiRequest koiRequest, String token) throws ParseException;
    List<KoiResponse> getKoiByStatus(String koiStatus);
    List<KoiResponse> getKoiByFarmId(short farmId);
    List<KoiResponse> getFarmKoi(String token) throws ParseException;
    KoiResponse getFarmKoiDetail(String token, short koiId) throws ParseException;
    List<KoiResponse> getKoiByVariety(short varietyId);
    void updateKoi(KoiRequest koiRequest);

}

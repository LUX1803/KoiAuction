package com.mnky.kas.service;

import com.mnky.kas.dto.request.BreederRegisterRequest;
import com.mnky.kas.dto.response.FarmResponse;

import java.util.List;

public interface FarmService {
    void createFarm(BreederRegisterRequest breederRegisterRequest);

    List<FarmResponse> getFarmWithStatus(String status);

    void updateFarmStatus(short farmId, String status);

    FarmResponse getFarmByOwnerId(short ownerId);
}
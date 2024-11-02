package com.mnky.kas.service;

import com.mnky.kas.dto.request.BreederRegisterRequest;
import com.mnky.kas.dto.response.FarmResponse;
import com.mnky.kas.mapper.FarmMapper;
import com.mnky.kas.model.Farm;
import com.mnky.kas.repository.FarmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class FarmServiceImpl implements FarmService {
    @Autowired
    private FarmRepository farmRepository;
    @Autowired
    private FarmMapper farmMapper;

    @Override
    @Transactional
    public void createFarm(BreederRegisterRequest breederRegisterRequest) {
        farmRepository.saveFarm(breederRegisterRequest.getName(),breederRegisterRequest.getDescription(),breederRegisterRequest.getImage(),breederRegisterRequest.getOwner(),breederRegisterRequest.getSent());
    }

    @Override
    public List<FarmResponse> getFarmWithStatus(String status) {
        List<Farm> list = farmRepository.findByStatus(Farm.FarmStatus.valueOf(status));
        List<FarmResponse> farmResponseList = new ArrayList<>();
        if (list != null || !list.isEmpty()) {
            for (Farm farm : list) {
                farmResponseList.add(farmMapper.toFarmResponse(farm));
            }
        }

        return farmResponseList;
    }

    @Override
    public void updateFarmStatus(short farmId, String status) {
        farmRepository.updateStatus(farmId, status);
    }

    @Override
    public FarmResponse getFarmByOwnerId(short ownerId) {
        return farmMapper.toFarmResponse(farmRepository.findByOwnerId(ownerId)) ;
    }
}
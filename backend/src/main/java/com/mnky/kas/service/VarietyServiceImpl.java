package com.mnky.kas.service;

import com.mnky.kas.dto.response.VarietyResponse;
import com.mnky.kas.mapper.VarietyMapper;
import com.mnky.kas.model.Variety;
import com.mnky.kas.repository.VarietyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VarietyServiceImpl implements VarietyService {
    @Autowired
    private VarietyRepository varietyRepository;

    @Autowired
    private VarietyMapper varietyMapper;

    @Override
    public List<VarietyResponse> getAllVarieties() {
        List<Variety> varieties = varietyRepository.findAll();
        List<VarietyResponse> response = new ArrayList<>();
        for (Variety variety : varieties) {
           response.add(varietyMapper.toVarietyResponse(variety));
        }
        return response;
    }
}

package com.mnky.kas.controller;

import com.mnky.kas.dto.response.ApiResponse;
import com.mnky.kas.dto.response.VarietyResponse;
import com.mnky.kas.service.VarietyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class VarietyController {
    @Autowired
    VarietyService varietyService;

    @GetMapping("/variety")
    public ApiResponse<List<VarietyResponse>> getAllVarieties (){
        List<VarietyResponse> varieties = varietyService.getAllVarieties();
        ApiResponse<List<VarietyResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(varieties);
        return apiResponse;
    }
}

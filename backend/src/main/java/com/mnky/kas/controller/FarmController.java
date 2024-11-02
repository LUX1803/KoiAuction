package com.mnky.kas.controller;

import com.mnky.kas.dto.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FarmController {

    @GetMapping("/farm/status/{status}")
    public ApiResponse<?> getFarmWithStatus(@PathVariable String status) {
        ApiResponse<?> response = new ApiResponse<>();
        return response;
    }

    @PatchMapping("/farm/{farmId}/status/{status}")
    public ApiResponse<?> updateFarmStatus(@PathVariable short farmId, @PathVariable String status) {
        ApiResponse<?> response = new ApiResponse<>();
        return response;
    }
}

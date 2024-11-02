package com.mnky.kas.controller;

import com.mnky.kas.dto.request.KoiRequest;
import com.mnky.kas.dto.response.ApiResponse;
import com.mnky.kas.dto.response.KoiResponse;
import com.mnky.kas.model.Koi;
import com.mnky.kas.service.KoiService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/koi")
@RequiredArgsConstructor
public class KoiController {

    private final KoiService koiService;

    @PostMapping
    @PreAuthorize("hasRole('BREEDER')")
    public ApiResponse<?> createKoi(@RequestBody KoiRequest koiRequest, @RequestHeader("Authorization") String token) throws ParseException {
        koiService.createKoi(koiRequest, token);
        return new ApiResponse<>();
    }

    @GetMapping("/status/{status}")
    //@PreAuthorize("hasRole('STAFF')")
    public ApiResponse<List<KoiResponse>> getKoiByStatus(@PathVariable String status){
        ApiResponse<List<KoiResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(koiService.getKoiByStatus(status));
        return apiResponse;
    }

    @GetMapping("/farm/{farmId}")
    //@PreAuthorize("hasRole('STAFF') || hasRole('BREEDER')")
    public ApiResponse<List<KoiResponse>> getKoiByFarmId(@PathVariable  short farmId){
        ApiResponse<List<KoiResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(koiService.getKoiByFarmId(farmId));
        return apiResponse;
    }

    @GetMapping
    public ApiResponse<List<KoiResponse>> getFarmKoi(@RequestHeader("Authorization") String token) throws ParseException {
        ApiResponse<List<KoiResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(koiService.getFarmKoi(token));
        return apiResponse;
    }

    @GetMapping("/{koiId}")
    public ApiResponse<KoiResponse> getFarmKoiDetail(@RequestHeader("Authorization") String token, @PathVariable short koiId) throws ParseException {
        ApiResponse<KoiResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(koiService.getFarmKoiDetail(token, koiId));
        return apiResponse;
    }


    @GetMapping("/search/{varietyId}")
    @PreAuthorize("hasRole('STAFF')")
    public ApiResponse<List<KoiResponse>> getKoiByVariety(@PathVariable short varietyId){
        ApiResponse<List<KoiResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(koiService.getKoiByVariety(varietyId));
        return apiResponse;
    }


    @PatchMapping
    //@PreAuthorize("hasRole('STAFF')")
    public ApiResponse<?> updateKoiStatus(@RequestBody KoiRequest koiRequest){
        ApiResponse<List<KoiResponse>> apiResponse = new ApiResponse<>();
        koiService.updateKoi(koiRequest);
        apiResponse.setMessage("Success");
        return apiResponse;
    }
}

package com.mnky.kas.mapper;

import com.mnky.kas.dto.response.VarietyResponse;
import com.mnky.kas.model.Variety;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface VarietyMapper {
    VarietyResponse toVarietyResponse(Variety variety);
}

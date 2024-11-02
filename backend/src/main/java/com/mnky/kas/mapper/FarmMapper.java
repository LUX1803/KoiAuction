package com.mnky.kas.mapper;

import com.mnky.kas.dto.response.FarmResponse;
import com.mnky.kas.model.Farm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface FarmMapper {

    @Mapping(target = "ownerId", source = "owner.id")
    @Mapping(target = "ownerFirstname", source = "owner.firstname")
    @Mapping(target = "ownerLastname", source = "owner.lastname")
    @Mapping(target = "address", source = "owner.address")
    @Mapping(target = "phone", source = "owner.phone")
    FarmResponse toFarmResponse(Farm farm);


}

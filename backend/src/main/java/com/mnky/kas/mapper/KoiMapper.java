package com.mnky.kas.mapper;

import com.mnky.kas.dto.request.KoiRequest;
import com.mnky.kas.dto.response.KoiResponse;
import com.mnky.kas.model.Koi;
import com.mnky.kas.model.Variety;
import com.mnky.kas.repository.VarietyRepository;
import org.mapstruct.*;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.FIELD)
public interface KoiMapper {
    @Mapping(target = "variety", expression = "java(mapVarietyIdToVariety(koiRequest.getVarietyId()))")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Koi toKoi(@MappingTarget Koi koi, KoiRequest koiRequest);

    @Mapping(source = "variety.id", target = "varietyId")
    @Mapping(source = "variety.name", target = "varietyName")
    @Mapping(source = "farm.id", target = "farmId")
    @Mapping(source = "farm.name", target = "farmName")
    @Mapping(source = "staff.eeid", target = "staffId")
    KoiResponse toKoiResponse(Koi koi);


    default Variety mapVarietyIdToVariety(short varietyId) {
        if (varietyId == 0) {
            return null;
        }
        Variety variety = new Variety();
        variety.setId(varietyId);
        return variety;
    }
}

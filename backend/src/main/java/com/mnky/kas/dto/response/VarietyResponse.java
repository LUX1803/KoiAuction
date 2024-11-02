package com.mnky.kas.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VarietyResponse {
    private short id;
    private String name;
    private String description;
}

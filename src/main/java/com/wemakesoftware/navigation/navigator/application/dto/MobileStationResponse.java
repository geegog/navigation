package com.wemakesoftware.navigation.navigator.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MobileStationResponse {

    private UUID mobileId;

    private Float x;

    private Float y;

    @JsonProperty("error_radius")
    private Float errorRadius;

    @JsonProperty("error_code")
    private Integer errorCode;

    @JsonProperty("error_description")
    private String errorDescription;
}

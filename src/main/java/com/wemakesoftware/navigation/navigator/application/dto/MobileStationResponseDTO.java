package com.wemakesoftware.navigation.navigator.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MobileStationResponseDTO extends RepresentationModel<MobileStationResponseDTO> {

    private UUID mobileId;

    private Float x;

    private Float y;

    private Float error_radius;

    private Integer error_code;

    private String error_description;
}

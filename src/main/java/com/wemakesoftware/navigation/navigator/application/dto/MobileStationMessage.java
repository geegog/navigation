package com.wemakesoftware.navigation.navigator.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MobileStationMessage {

    private UUID mobileStationId;

    private Float distance;

    private LocalDateTime timestamp;
}

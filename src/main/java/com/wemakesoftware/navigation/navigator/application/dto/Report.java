package com.wemakesoftware.navigation.navigator.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Report {

    private UUID baseStationId;

    private List<MobileStationMessage> reports;
}

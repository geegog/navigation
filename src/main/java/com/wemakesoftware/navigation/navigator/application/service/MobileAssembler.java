package com.wemakesoftware.navigation.navigator.application.service;

import com.wemakesoftware.navigation.navigator.application.dto.MobileStationResponseDTO;
import com.wemakesoftware.navigation.navigator.domain.model.MobileStation;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Service;

@Service
public class MobileAssembler extends RepresentationModelAssemblerSupport<MobileStation, MobileStationResponseDTO> {

    public MobileAssembler() {
        super(NavigatorService.class, MobileStationResponseDTO.class);
    }

    @Override
    public MobileStationResponseDTO toModel(MobileStation entity) {
        MobileStationResponseDTO dto = instantiateModel(entity);
        dto.setX(entity.getLastKnownLocation().getX());
        dto.setY(entity.getLastKnownLocation().getY());
        dto.setMobileId(entity.getId());
        return dto;
    }
}

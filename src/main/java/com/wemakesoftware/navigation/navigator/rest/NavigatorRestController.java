package com.wemakesoftware.navigation.navigator.rest;

import com.wemakesoftware.navigation.common.application.exception.BaseNotFoundException;
import com.wemakesoftware.navigation.common.application.exception.MobileNotFoundException;
import com.wemakesoftware.navigation.navigator.application.dto.MobileStationResponseDTO;
import com.wemakesoftware.navigation.navigator.application.dto.ReportDTO;
import com.wemakesoftware.navigation.navigator.application.service.NavigatorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("/api/navigation")
@Slf4j
public class NavigatorRestController {

    private final NavigatorService navigatorService;

    @Autowired
    public NavigatorRestController(NavigatorService navigatorService) {
        this.navigatorService = navigatorService;
    }

    @PostMapping("/report/create")
    public ResponseEntity<?> create(@RequestBody ReportDTO reportDTO) {
        try {
            return new ResponseEntity<>(navigatorService.report(reportDTO), HttpStatus.ACCEPTED);
        } catch (BaseNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @GetMapping("/location/{uuid}")
    public ResponseEntity<?> getPayment(@PathVariable String uuid) {
        MobileStationResponseDTO mobileStationResponseDTO = new MobileStationResponseDTO();
        try {
            mobileStationResponseDTO = navigatorService.findMobileStation(UUID.fromString(uuid));
            return new ResponseEntity<>(mobileStationResponseDTO, HttpStatus.OK);
        } catch (MobileNotFoundException e) {
            mobileStationResponseDTO.setErrorCode(HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(mobileStationResponseDTO, HttpStatus.NOT_FOUND);
        }
    }

}

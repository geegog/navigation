package com.wemakesoftware.navigation.navigator.rest;

import com.wemakesoftware.navigation.common.application.exception.BaseNotFoundException;
import com.wemakesoftware.navigation.common.application.exception.MobileNotFoundException;
import com.wemakesoftware.navigation.navigator.application.dto.MobileStationResponse;
import com.wemakesoftware.navigation.navigator.application.dto.Report;
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
    public ResponseEntity<?> create(@RequestBody Report report) {
        try {
            return new ResponseEntity<>(navigatorService.report(report), HttpStatus.ACCEPTED);
        } catch (BaseNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @GetMapping("/location/{uuid}")
    public ResponseEntity<?> getPayment(@PathVariable String uuid) {
        MobileStationResponse mobileStationResponse = new MobileStationResponse();
        try {
            mobileStationResponse = navigatorService.findMobileStation(UUID.fromString(uuid));
            return new ResponseEntity<>(mobileStationResponse, HttpStatus.OK);
        } catch (MobileNotFoundException e) {
            mobileStationResponse.setErrorCode(HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(mobileStationResponse, HttpStatus.NOT_FOUND);
        }
    }

}

package com.wemakesoftware.navigation.navigator.application.service;

import com.wemakesoftware.navigation.common.application.exception.BaseNotFoundException;
import com.wemakesoftware.navigation.common.application.exception.MobileNotFoundException;
import com.wemakesoftware.navigation.navigator.application.dto.MobileStationResponse;
import com.wemakesoftware.navigation.navigator.application.dto.Report;
import com.wemakesoftware.navigation.navigator.domain.model.BaseStation;
import com.wemakesoftware.navigation.navigator.domain.model.MobileStation;
import com.wemakesoftware.navigation.navigator.domain.repository.BaseStationRepository;
import com.wemakesoftware.navigation.navigator.domain.repository.MobileStationRepository;
import com.wemakesoftware.navigation.navigator.domain.repository.ReportRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;

@Service
@Slf4j
public class NavigatorService {

    private final MobileStationRepository mobileStationRepository;

    private final BaseStationRepository baseStationRepository;

    private final ReportRepository reportRepository;

    @Autowired
    public NavigatorService(MobileStationRepository mobileStationRepository,
                            BaseStationRepository baseStationRepository,
                            ReportRepository reportRepository) {
        this.mobileStationRepository = mobileStationRepository;
        this.baseStationRepository = baseStationRepository;
        this.reportRepository = reportRepository;
    }

    public boolean isWithinRadius(float baseX, float baseY, float detectionRadius,
                                  float targetX, float targetY) {
        float distance = distance(baseX, baseY, targetX, targetY);
        return distance < detectionRadius;
    }

    public Float distance (float baseX, float baseY,
                          float targetX, float targetY) {
        return (float) Math.sqrt((Math.pow((targetX - baseX), 2) + Math.pow((targetY - baseY), 2)));
    }

    private BaseStation getBase(UUID id) {
        return baseStationRepository.findById(id).orElse(null);
    }

    private MobileStation getMobileStation(UUID id) {
        return mobileStationRepository.findById(id).orElse(null);
    }

    public String report(Report reportObj) throws BaseNotFoundException {

        log.info("Report received : {}", reportObj);

        if (getBase(reportObj.getBaseStationId()) == null) {
            throw new BaseNotFoundException(reportObj.getBaseStationId());
        }

        BaseStation baseStation = baseStationRepository.findById(reportObj.getBaseStationId()).orElse(null);
        reportObj.getReports().forEach(mobileStationMessage -> {
            com.wemakesoftware.navigation.navigator.domain.model.Report report = new com.wemakesoftware.navigation.navigator.domain.model.Report();
            try {
                report.setDistance(mobileStationMessage.getDistance());
                report.setBaseStation(baseStation);
                report.setTimestamp(mobileStationMessage.getTimestamp());
                MobileStation mobileStation = mobileStationRepository.findById(mobileStationMessage.getMobileStationId()).orElse(null);
                report.setMobileStation(mobileStation);
            } catch (EntityNotFoundException e) {
                MobileStation mobileStation = new MobileStation();
                mobileStation.setId(mobileStationMessage.getMobileStationId());
                MobileStation mobileStationEntity = mobileStationRepository.save(mobileStation);
                report.setMobileStation(mobileStationEntity);
            } catch (Exception e) {
              log.error("Some other exception: {} mobile station: {}", e.getMessage(), mobileStationMessage);
            }
            reportRepository.save(report);
            log.info("Mobile station reported : base {} mobile {}", baseStation, mobileStationMessage);
        });
        return null;
    }

    public MobileStationResponse findMobileStation(UUID id) throws MobileNotFoundException {

        log.info("View mobile station request : {}", id);

        MobileStation mobileStation = getMobileStation(id);

        if (mobileStation == null) {
            throw new MobileNotFoundException(id);
        }

        MobileStationResponse mobileStationResponse = new MobileStationResponse();
        mobileStationResponse.setMobileId(mobileStation.getId());
        mobileStationResponse.setY(mobileStationResponse.getY());
        mobileStationResponse.setX(mobileStationResponse.getX());

        return mobileStationResponse;
    }

}

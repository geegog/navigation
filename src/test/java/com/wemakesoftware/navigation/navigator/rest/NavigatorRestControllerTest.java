package com.wemakesoftware.navigation.navigator.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wemakesoftware.navigation.NavigationApplication;
import com.wemakesoftware.navigation.navigator.application.dto.MobileStationMessage;
import com.wemakesoftware.navigation.navigator.application.dto.MobileStationResponse;
import com.wemakesoftware.navigation.navigator.application.dto.Report;
import com.wemakesoftware.navigation.navigator.application.service.NavigatorService;
import com.wemakesoftware.navigation.navigator.domain.repository.BaseStationRepository;
import com.wemakesoftware.navigation.navigator.domain.repository.MobileStationRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = NavigationApplication.class)
@Sql(scripts = "/navigation-dataset.sql")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class NavigatorRestControllerTest {

    @Autowired
    ObjectMapper mapper;
    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private BaseStationRepository baseStationRepository;
    @Autowired
    private MobileStationRepository mobileStationRepository;
    @Autowired
    NavigatorService navigatorService;
    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void testMobileStationReporting() {
        baseStationRepository.findAll().forEach(baseStation -> {
            log.info("Base: {}", baseStation);
            Report reportDTO = new Report();
            List<MobileStationMessage> reports = new ArrayList<>();
            mobileStationRepository.findAll().forEach(mobileStation -> {
                MobileStationMessage mobileStationMessage = new MobileStationMessage();
                boolean isInRadius = navigatorService.isWithinRadius(baseStation.getLocation().getX(),
                        baseStation.getLocation().getY(),
                        baseStation.getDetectionRadiusInMeters(),
                        mobileStation.getLastKnownLocation().getX(),
                        mobileStation.getLastKnownLocation().getY());
                log.info("Mobile station: {} Within: {}", mobileStation, isInRadius);

                if (isInRadius) {
                    Float distance = navigatorService.distance(baseStation.getLocation().getX(),
                            baseStation.getLocation().getY(),
                            mobileStation.getLastKnownLocation().getX(),
                            mobileStation.getLastKnownLocation().getY());
                    mobileStationMessage.setDistance(distance);
                    mobileStationMessage.setTimestamp(LocalDateTime.now());
                    mobileStationMessage.setMobileStationId(mobileStation.getId());
                    reports.add(mobileStationMessage);
                    log.info("Mobile station: {} distance: {}", mobileStation, distance);
                }

            });
            reportDTO.setBaseStationId(baseStation.getId());
            reportDTO.setReports(reports);
            log.info("Report: {} Base: {}", reportDTO, baseStation);

            try {
                String jsonRequest = mapper.writeValueAsString(reportDTO);
                MvcResult result = mockMvc.perform(
                        post("http://localhost:8080/api/navigation/report/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest))
                        .andExpect(status().isAccepted())
                        .andReturn();

                Assert.assertNotNull(result);
                Assert.assertEquals(202, result.getResponse().getStatus());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }

        });
    }

    @SneakyThrows
    @Test
    public void testMobileStationReportingBaseNotFound() {
        Report reportDTO = new Report();
        reportDTO.setBaseStationId(UUID.randomUUID());
        String jsonRequest = mapper.writeValueAsString(reportDTO);
        MvcResult result = mockMvc.perform(
                post("http://localhost:8080/api/navigation/report/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isNotFound())
                .andReturn();

        Assert.assertNotNull(result);
        Assert.assertEquals(404, result.getResponse().getStatus());

    }

    @SneakyThrows
    @Test
    public void testFindMobileStation() {
        MvcResult result = mockMvc.perform(
                get("http://localhost:8080/api/navigation/location/90184019-cffe-41cc-ac77-dff5520bd20c")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        MobileStationResponse mobileStationResponse =
                mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<MobileStationResponse>() {
                });

        Assert.assertNotNull(result);
        Assert.assertEquals(200, result.getResponse().getStatus());
        Assert.assertEquals(UUID.fromString("90184019-cffe-41cc-ac77-dff5520bd20c"), mobileStationResponse.getMobileId());
    }

    @SneakyThrows
    @Test
    public void testFindMobileStationNotFound() {
        MvcResult result = mockMvc.perform(
                get("http://localhost:8080/api/navigation/location/d4b91d14-8ede-11ea-bc55-0242ac130003")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        MobileStationResponse mobileStationResponse =
                mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<MobileStationResponse>() {
                });

        Assert.assertNotNull(result);
        Assert.assertEquals(404, result.getResponse().getStatus());
        Assert.assertEquals(Integer.valueOf("404"), mobileStationResponse.getErrorCode());
    }

}

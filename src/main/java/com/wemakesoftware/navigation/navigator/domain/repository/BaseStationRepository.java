package com.wemakesoftware.navigation.navigator.domain.repository;

import com.wemakesoftware.navigation.navigator.domain.model.BaseStation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BaseStationRepository extends JpaRepository<BaseStation, UUID> {
}

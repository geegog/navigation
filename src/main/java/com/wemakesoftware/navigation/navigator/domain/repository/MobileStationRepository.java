package com.wemakesoftware.navigation.navigator.domain.repository;

import com.wemakesoftware.navigation.navigator.domain.model.MobileStation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MobileStationRepository extends JpaRepository<MobileStation, UUID> {
}

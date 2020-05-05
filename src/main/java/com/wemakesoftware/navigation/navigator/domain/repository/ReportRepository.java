package com.wemakesoftware.navigation.navigator.domain.repository;

import com.wemakesoftware.navigation.navigator.domain.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReportRepository extends JpaRepository<Report, UUID> {
}

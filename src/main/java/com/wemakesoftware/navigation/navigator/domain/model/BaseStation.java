package com.wemakesoftware.navigation.navigator.domain.model;

import com.wemakesoftware.navigation.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class BaseStation extends BaseEntity {

    @NotNull
    private String name;

    @Embedded
    private Point location;

    @NotNull
    private Float detectionRadiusInMeters;

}

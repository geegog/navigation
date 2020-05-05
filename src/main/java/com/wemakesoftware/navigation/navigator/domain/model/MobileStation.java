package com.wemakesoftware.navigation.navigator.domain.model;

import com.wemakesoftware.navigation.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class MobileStation extends BaseEntity {

    @Embedded
    private Point lastKnownLocation;

}

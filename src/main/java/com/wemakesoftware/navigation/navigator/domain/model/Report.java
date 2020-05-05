package com.wemakesoftware.navigation.navigator.domain.model;

import com.wemakesoftware.navigation.common.domain.BaseEntity;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
public class Report extends BaseEntity {

    @ManyToOne
    private MobileStation mobileStation;

    @ManyToOne
    private BaseStation baseStation;

    private Float distance;

}

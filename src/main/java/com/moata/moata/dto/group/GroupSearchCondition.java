package com.moata.moata.dto.group;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupSearchCondition {
    private Boolean hasCar;
    private String favoriteArea;
    private Integer coOwnerMax;
    private String carType;
}

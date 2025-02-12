package com.moata.moata.dto.group;

import com.moata.moata.entity.group.Group;
import com.moata.moata.entity.user.User;
import lombok.Data;

@Data
public class GroupSaveRequest {
    private Boolean hasCar;
    private String favoriteArea;
    private int coOwnerMax;
    private int carUseFrequency;
    private String carType;
    private String carModelName;

    public Group toModel(final User user) {
        return Group.builder()
                .hasCar(hasCar)
                .favoriteArea(favoriteArea)
                .coOwnerMax(coOwnerMax)
                .carUseFrequency(carUseFrequency)
                .carType(carType)
                .carModelName(carModelName)
                .build();
    }
}

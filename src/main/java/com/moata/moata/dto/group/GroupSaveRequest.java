package com.moata.moata.dto.group;

import com.moata.moata.entity.group.Group;
import com.moata.moata.entity.user.User;
import lombok.Data;

@Data
public class GroupSaveRequest {
    private long ownerId;
    private Boolean hasCar;
    private String favoriteArea;
    private int coOwnerMax;
    private int carUseFrequency;
    private String carType;
    private String carModelName;

    //추후 해당 부분에 토큰 정보를 통해 대푶자 회원 번호(owner_id) 저장해야 함
    public Group toModel(final User user) {
        return Group.builder()
                .ownerId(user)
                .hasCar(hasCar)
                .favoriteArea(favoriteArea)
                .coOwnerMax(coOwnerMax)
                .carUseFrequency(carUseFrequency)
                .carType(carType)
                .carModelName(carModelName)
                .build();
    }
}

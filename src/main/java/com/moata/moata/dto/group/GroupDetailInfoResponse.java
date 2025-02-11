package com.moata.moata.dto.group;

import com.moata.moata.entity.group.Group;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GroupDetailInfoResponse {

    private String ownerName;
    private String ownerLocation;
    private String carModelName;
    private int coOwnerMax;
    private int matchedCount;

    //추후 User 추가해야 함(user_id를 통해 ownerName, ownerLocation 조회)
    public static GroupDetailInfoResponse from(Group group) {
        return GroupDetailInfoResponse.builder()
                .ownerName(null)
                .ownerLocation(null)
                .carModelName(group.getCarModelName())
                .coOwnerMax(group.getCoOwnerMax())
                .matchedCount(group.getMatchedCount())
                .build();
    }
}

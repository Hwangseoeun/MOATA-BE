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

    public static GroupDetailInfoResponse from(Group group) {
        return GroupDetailInfoResponse.builder()
                .ownerName(group.getOwnerId().getName())
                .ownerLocation(group.getOwnerId().getLocation())
                .carModelName(group.getCarModelName())
                .coOwnerMax(group.getCoOwnerMax())
                .matchedCount(group.getMatchedCount())
                .build();
    }
}

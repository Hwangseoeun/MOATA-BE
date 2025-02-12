package com.moata.moata.dto.group;

import com.moata.moata.entity.group.Group;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GroupInfoResponse {

    private Long groupId;
    private String ownerName;
    private String ownerLocation;
    private String carModelName;
    private int coOwnerMax;
    private int matchedCount;

    public static GroupInfoResponse from(Group group) {
        return GroupInfoResponse.builder()
                .groupId(group.getGroupId())
                .ownerName(group.getOwnerId().getName())
                .ownerLocation(group.getOwnerId().getLocation())
                .carModelName(group.getCarModelName())
                .coOwnerMax(group.getCoOwnerMax())
                .matchedCount(group.getMatchedCount())
                .build();
    }
}

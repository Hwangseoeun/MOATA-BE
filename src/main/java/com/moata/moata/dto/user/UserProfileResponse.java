package com.moata.moata.dto.user;

import com.moata.moata.entity.group.Group;
import com.moata.moata.entity.user.User;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserProfileResponse {

    private String name;
    private int sharedCarCnt;
    private List<Long> groupIds;

    public static UserProfileResponse from(User user, int sharedCarCnt, List<Group> groups) {
        return UserProfileResponse.builder()
                .name(user.getName())
                .sharedCarCnt(sharedCarCnt)
                .groupIds(groups.stream().map(Group::getGroupId).toList())
                .build();
    }
}

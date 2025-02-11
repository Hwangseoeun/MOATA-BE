package com.moata.moata.dto.user;

import com.moata.moata.entity.user.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProfileResponse {

    private String name;
    private int sharedCarCnt;

    public static UserProfileResponse from(User user) {
        return UserProfileResponse.builder()
                .name(user.getName())
                .sharedCarCnt(user.getSharedCarCnt())
                .build();
    }
}

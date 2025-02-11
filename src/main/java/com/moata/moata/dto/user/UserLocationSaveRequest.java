package com.moata.moata.dto.user;

import com.moata.moata.entity.user.User;
import lombok.Data;

@Data
public class UserLocationSaveRequest {

    private String location;
    private double latitude;
    private double longitude;

    public User toModel() {
        return User.builder()
                .location(location)
                .latitude(latitude)
                .build();
    }
}

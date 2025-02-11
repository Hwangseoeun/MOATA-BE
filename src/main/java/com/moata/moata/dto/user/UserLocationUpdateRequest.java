package com.moata.moata.dto.user;

import com.moata.moata.entity.user.User;
import lombok.Data;

@Data
public class UserLocationUpdateRequest {

    private String location;
    private double latitude;
    private double longitude;

    public User toModel() {
        return User.builder()
                .location(location)
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }
}

package com.moata.moata.dto.user;


import com.moata.moata.entity.user.User;
import lombok.Data;

@Data
public class UserNameUpdateRequest {

    private String name;

    public User toModel() {
        return User.builder()
                .name(name)
                .build();
    }
}

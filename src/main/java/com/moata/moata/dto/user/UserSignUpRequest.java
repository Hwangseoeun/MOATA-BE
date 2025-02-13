package com.moata.moata.dto.user;

import com.moata.moata.entity.user.User;
import lombok.Data;

@Data
public class UserSignUpRequest {
    private String name;
    private String phone;

    public User toModel() {
        return User.builder()
                .name(this.name)
                .phone(this.phone)
                .build();
    }
}

package com.moata.moata.dto.user;

import com.moata.moata.constant.UserTelcoType;
import com.moata.moata.entity.user.User;
import lombok.Data;

@Data
public class UserSaveRequest {
    private String name;
    private String phone;
    private UserTelcoType telco;
    private String location;

    public User toModel() {
        return User.builder()
                .name(name)
                .phone(phone)
                .telco(telco)
                .location(location)
                .build();
    }
}

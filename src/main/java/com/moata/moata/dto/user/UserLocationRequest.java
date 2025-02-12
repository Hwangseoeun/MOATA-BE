package com.moata.moata.dto.user;

import lombok.Data;

@Data
public class UserLocationRequest {

    private String location;
    private double latitude;
    private double longitude;

}

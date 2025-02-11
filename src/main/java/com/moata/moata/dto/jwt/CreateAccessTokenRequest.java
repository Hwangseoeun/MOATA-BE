package com.moata.moata.dto.jwt;

import lombok.Data;

@Data
public class CreateAccessTokenRequest {
    private String refreshToken;
}

package com.moata.moata.dto.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CreateAccessTokenResponse {
    private String accessToken;
}

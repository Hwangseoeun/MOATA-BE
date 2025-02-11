package com.moata.moata.dto.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthTokens {

    private String accessToken;
    private String refreshToken;

}

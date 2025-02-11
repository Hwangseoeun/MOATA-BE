package com.moata.moata.service.jwt;

import com.moata.moata.common.UnauthorizedException;
import com.moata.moata.config.jwt.TokenProvider;
import com.moata.moata.entity.user.User;
import com.moata.moata.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    public String createNewAccessToken(String refreshToken) {
        if(!tokenProvider.validToken(refreshToken)) {
            throw new UnauthorizedException("Invalid or expired refresh token.");
        }

        Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUser().getUserId();
        User user = userService.findById(userId);

        return tokenProvider.generateToken(user, Duration.ofHours(1));
    }
}

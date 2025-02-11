package com.moata.moata.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "kakao")
@Getter
@Setter
public class KakaoConfig {
    private String redirectUri;
    private Key key;

    @Getter
    @Setter
    public static class Key {
        private String clientId;
    }
}

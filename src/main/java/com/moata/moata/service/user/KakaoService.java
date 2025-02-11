package com.moata.moata.service.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moata.moata.config.KakaoConfig;
import com.moata.moata.config.jwt.TokenProvider;
import com.moata.moata.dto.user.AuthTokens;
import com.moata.moata.dto.user.KakaoUserInfo;
import com.moata.moata.entity.user.User;
import com.moata.moata.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final KakaoConfig kakaoConfig;

    public AuthTokens kakaoLogin(String code) {

        // 1. "인가 코드"로 "액세스 토큰" 요청
        String accessToken = getAccessToken(code);

        // 2. 토큰으로 카카오 API 호출
        KakaoUserInfo userInfo= getUserInfoFromKakao(accessToken);

        AuthTokens authTokens = kakaoLogin(userInfo);

        return authTokens;
    }

    //1. "인가 코드"로 "액세스 토큰" 요청
    private String getAccessToken(String code) {

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoConfig.getKey().getClientId());
        body.add("redirect_uri", kakaoConfig.getRedirectUri());
        body.add("code", code);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(responseBody);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonNode.get("access_token").asText();
    }

    public KakaoUserInfo getUserInfoFromKakao(String accessToken) {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> entity = new HttpEntity<>("", headers);
        ResponseEntity<String> response;

        try {
            response = restTemplate.exchange(
                    "https://kapi.kakao.com/v2/user/me",
                    HttpMethod.GET,
                    entity,
                    String.class
            );
            log.info("kakao response : {}", response);
            log.info("kakao response body : {}", response.getBody());
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("카카오 API 호출에 실패했습니다: " + e.getMessage());
        }

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("카카오 API 응답 상태가 좋지 않습니다: " + response.getStatusCode());
        }

        ObjectMapper objectMapper = new ObjectMapper();

        KakaoUserInfo kakaoUserInfo;
        try {
            kakaoUserInfo = objectMapper.readValue(response.getBody(), KakaoUserInfo.class);
            log.info("kakao user info : {}", kakaoUserInfo);
            log.info("kakao user name : {}", kakaoUserInfo.getName());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("카카오 API 응답을 처리하는 중 오류가 발생했습니다: " + e.getMessage());
        }

        return kakaoUserInfo;
    }

    public String formatPhone(String phone) {

        if (phone == null || phone.isEmpty()) {
            return "전화번호 없음";
        }

        if (phone.startsWith("+82")) {
            phone = phone.replace("+82", "");
        }
        phone = phone.replaceAll("[^0-9]", "");

        if (phone.startsWith("10")) {
            phone = "0" + phone;
        }
        return phone;
    }

    public AuthTokens kakaoLogin(KakaoUserInfo kakaoUserInfo) {

        Optional<User> existingUser = userRepository.findByPhone(kakaoUserInfo.getPhone());

        User user;
        if (existingUser.isPresent()) {
            user = existingUser.get();
        } else {
            user = User.builder()
                    .name(kakaoUserInfo.getName())
                    .phone(formatPhone(kakaoUserInfo.getPhone()))
                    .build();

            userRepository.save(user);
        }

        // Access Token (1시간 유효)
        String accessToken = tokenProvider.makeToken(new Date(System.currentTimeMillis() + 3600* 1000L), user);
        // Refresh Token (7일 유효)
        String refreshToken = tokenProvider.makeToken(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000L), user);

        AuthTokens authTokens = AuthTokens.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        return authTokens;
    }
}

package com.moata.moata.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoUserInfo {

    @JsonProperty("id")
    private Long kakaoId;

    @JsonProperty("kakao_account")
    private JsonNode kakaoAccount;

    public String getName() {
        return kakaoAccount != null && kakaoAccount.has("name") ? kakaoAccount.get("name").asText() : null;
    }

    public String getPhone() {
        return kakaoAccount != null && kakaoAccount.has("phone_number") ? kakaoAccount.get("phone_number").asText() : null;
    }
}

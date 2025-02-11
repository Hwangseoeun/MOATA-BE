package com.moata.moata.dto.info;

import com.moata.moata.entity.info.Info;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InfoResponse {
    private String title;
    private String content;

    public static InfoResponse from(Info info) {
        return InfoResponse.builder()
                .title(info.getTitle())
                .content(info.getContent())
                .build();
    }
}

package com.moata.moata.dto.group;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GroupSaveResponse {

    private Boolean isSuccess;
    private int code;

}

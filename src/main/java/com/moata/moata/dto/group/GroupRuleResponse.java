package com.moata.moata.dto.group;

import com.moata.moata.entity.group.GroupRule;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GroupRuleResponse {
    private long groupId;
    private String washingFrequency;
    private String fuelManager;
    private String parkingLocation;
    private String etc;

    public static GroupRuleResponse from(GroupRule rule) {
        return GroupRuleResponse.builder()
                .groupId(rule.getGroupId().getGroupId())
                .washingFrequency(rule.getWashingFrequency())
                .fuelManager(rule.getFuelManager())
                .parkingLocation(rule.getParkingLocation())
                .etc(rule.getEtc())
                .build();
    }
}

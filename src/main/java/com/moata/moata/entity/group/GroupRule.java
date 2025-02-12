package com.moata.moata.entity.group;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Table(name = "group_rule")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class GroupRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_rule_id", nullable = false)
    private long groupRuleId;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    @OnDelete(action= OnDeleteAction.CASCADE)
    private Group groupId;

    @Column(name = "washing_frequency", nullable = false, length = 30)
    private String washingFrequency;

    @Column(name = "fuel_manager", nullable = false, length = 10)
    private String fuelManager;

    @Column(name = "parking_location", nullable = false)
    private String parkingLocation;

    @Column(name = "etc", length = 5000)
    private String etc;

    @Builder
    public GroupRule(String washingFrequency, String fuelManager, String parkingLocation, String etc) {
        this.washingFrequency = washingFrequency;
        this.fuelManager = fuelManager;
        this.parkingLocation = parkingLocation;
        this.etc = etc;
    }
}

package com.moata.moata.entity.user;

import com.moata.moata.constant.UserTelcoType;
import com.moata.moata.dto.user.UserLocationUpdateRequest;
import com.moata.moata.dto.user.UserNameUpdateRequest;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private long userId;

    @Column(name = "name", nullable = false, length = 10)
    private String name;

    @Column(name = "phone", length = 11)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "telco")
    private UserTelcoType telco;

    @Column(name = "location")
    private String location;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "longitude")
    private double longitude;

    @Column(name = "shared_car_cnt", nullable = false)
    @ColumnDefault("0")
    private int sharedCarCnt;

    @Builder
    public User(String name, String phone, UserTelcoType telco, String location, double latitude, double longitude, int sharedCarCnt) {
        this.name = name;
        this.phone = phone;
        this.telco = telco;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.sharedCarCnt = sharedCarCnt;
    }

    public void updateUserName(UserNameUpdateRequest userNameUpdateRequest) {
        this.name = userNameUpdateRequest.getName();
    }

    public void updateUserLocation(UserLocationUpdateRequest userLocationUpdateRequest) {
        this.location = userLocationUpdateRequest.getLocation();
        this.latitude = userLocationUpdateRequest.getLatitude();
        this.longitude = userLocationUpdateRequest.getLongitude();
    }
}

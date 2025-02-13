package com.moata.moata.entity.user;

import com.moata.moata.constant.UserTelcoType;
import com.moata.moata.dto.user.UserLocationRequest;
import com.moata.moata.dto.user.UserNameUpdateRequest;
import jakarta.persistence.*;
import lombok.*;

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

    @Builder
    public User(String name, String phone, UserTelcoType telco, String location, double latitude, double longitude) {
        this.name = name;
        this.phone = phone;
        this.telco = telco;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void saveUserLocation(UserLocationRequest userLocationSaveRequest) {
        this.location = userLocationSaveRequest.getLocation();
        this.latitude = userLocationSaveRequest.getLatitude();
        this.longitude = userLocationSaveRequest.getLongitude();
    }

    public void updateUserName(UserNameUpdateRequest userNameUpdateRequest) {
        this.name = userNameUpdateRequest.getName();
    }

    public void updateUserLocation(UserLocationRequest userLocationUpdateRequest) {
        this.location = userLocationUpdateRequest.getLocation();
        this.latitude = userLocationUpdateRequest.getLatitude();
        this.longitude = userLocationUpdateRequest.getLongitude();
    }
}

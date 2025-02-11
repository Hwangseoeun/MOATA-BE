package com.moata.moata.entity.user;

import com.moata.moata.constant.UserTelcoType;
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

    @Builder
    public User(String name, String phone, UserTelcoType telco, String location) {
        this.name = name;
        this.phone = phone;
        this.telco = telco;
        this.location = location;
    }
}

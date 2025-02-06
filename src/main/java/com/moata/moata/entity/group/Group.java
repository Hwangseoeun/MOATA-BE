package com.moata.moata.entity.group;

import com.moata.moata.entity.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@Table(name = "user_group")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicInsert
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id", nullable = false)
    private long groupId;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User ownerId;

    @Column(name = "has_car", nullable = false)
    private Boolean hasCar;

    @Column(name = "favorite_area")
    private String favoriteArea;

    @Column(name = "co_owner_max")
    @ColumnDefault("0")
    private int coOwnerMax;

    @Column(name = "car_use_frequency")
    @ColumnDefault("0")
    private int carUseFrequency;

    @Column(name = "car_type")
    private String carType;

    @Column(name = "car_model_name")
    private String carModelName;

    @Column(name = "matched_count", nullable = false)
    @ColumnDefault("0")
    private int matchedCount;

    @Builder
    public Group(User ownerId, Boolean hasCar, String favoriteArea, int coOwnerMax, int carUseFrequency, String carType, String carModelName, int matchedCount) {
        this.ownerId = ownerId;
        this.hasCar = hasCar;
        this.favoriteArea = favoriteArea;
        this.coOwnerMax = coOwnerMax;
        this.carUseFrequency = carUseFrequency;
        this.carType = carType;
        this.carModelName = carModelName;
        this.matchedCount = matchedCount;
    }
}

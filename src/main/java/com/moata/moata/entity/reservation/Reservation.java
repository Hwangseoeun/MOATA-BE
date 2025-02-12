package com.moata.moata.entity.reservation;

import com.moata.moata.constant.RideSharingRole;
import com.moata.moata.dto.reservation.ReservationRideSharingRequest;
import com.moata.moata.entity.group.Group;
import com.moata.moata.entity.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "reservation")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id", nullable = false)
    private long reservationId;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    @OnDelete(action= OnDeleteAction.CASCADE)
    private Group groupId;

    @ManyToOne
    @JoinColumn(name = "reserver_id", nullable = false)
    @OnDelete(action=OnDeleteAction.CASCADE)
    private User reserverId;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "is_ride_sharing", nullable = false)
    private Boolean isRideSharing;

    @Enumerated(EnumType.STRING)
    @Column(name = "ride_sharing_role")
    private RideSharingRole rideSharingRole;

    @Column(name = "departure")
    private String departure;

    @Column(name = "destination")
    private String destination;

    @Column(name = "pickup_point")
    private String pickupPoint;

    @Builder
    public Reservation(Group groupId, User reserverId, LocalDateTime startTime, LocalDateTime endTime, Boolean isRideSharing, RideSharingRole rideSharingRole, String departure, String destination, String pickupPoint) {
        this.groupId = groupId;
        this.reserverId = reserverId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isRideSharing = isRideSharing;
        this.rideSharingRole = rideSharingRole;
        this.departure = departure;
        this.destination = destination;
        this.pickupPoint = pickupPoint;
    }

    public void addRideSharing(ReservationRideSharingRequest reservationRideSharingRequest) {
        this.pickupPoint = reservationRideSharingRequest.getPickUpPoint();
    }
}

package com.moata.moata.dto.reservation;

import com.moata.moata.entity.reservation.Reservation;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReservationResponse {
    private long reservationId;
    private long groupId;
    private long reserverId;
    private String startTime;
    private String endTime;
    private Boolean isRideSharing;
    private String rideSharingRole;
    private String departure;
    private String destination;

    public static ReservationResponse from(Reservation reservation) {
        return ReservationResponse.builder()
                .reservationId(reservation.getReservationId())
                .groupId(reservation.getGroupId().getGroupId())
                .reserverId(reservation.getReserverId().getUserId())
                .startTime(reservation.getStartTime().toString())
                .endTime(reservation.getEndTime().toString())
                .isRideSharing(reservation.getIsRideSharing())
                .rideSharingRole(reservation.getRideSharingRole().name())
                .departure(reservation.getDeparture())
                .destination(reservation.getDestination())
                .build();
    }
}

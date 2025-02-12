package com.moata.moata.repository.reservation;

import com.moata.moata.entity.reservation.Reservation;
import com.moata.moata.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query("SELECT r FROM Reservation r WHERE " +
            "(COALESCE(:from, r.startTime) IS NULL OR r.startTime >= :from) " +
            "AND (COALESCE(:to, r.startTime) IS NULL OR r.startTime <= :to) " +
            "AND (:user IS NULL OR r.reserverId = :user)")
    List<Reservation> searchReservations(
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to,
            @Param("user") User user);
    List<Reservation> findByReserverId(User reserverId);
}

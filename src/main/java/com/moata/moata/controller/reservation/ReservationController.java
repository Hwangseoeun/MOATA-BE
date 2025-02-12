package com.moata.moata.controller.reservation;

import com.moata.moata.dto.reservation.ReservationResponse;
import com.moata.moata.dto.reservation.ReservationRideSharingRequest;
import com.moata.moata.dto.reservation.ReservationSaveRequest;
import com.moata.moata.service.reservation.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/reservation")
public class ReservationController {
    private final ReservationService reservationService;

    @GetMapping("/all")
    public ResponseEntity<List<ReservationResponse>> getAllReservations() {
        return ResponseEntity.ok().body(reservationService.findAll());
    }

    @GetMapping("/search")
    public ResponseEntity<List<ReservationResponse>> searchReservation(@RequestParam(name = "startDate", required = false) String startDate,
                                               @RequestParam(name = "endDate", required = false) String endDate,
                                               @RequestParam(name = "userId", required = false, defaultValue = "-1") Long userId) {
        return ResponseEntity.ok().body(reservationService.searchReservation(startDate, endDate, userId));
    }

    @GetMapping("/my")
    public ResponseEntity<List<ReservationResponse>> getMyReservations(@AuthenticationPrincipal long userId) {
        return ResponseEntity.ok().body(reservationService.findMy(userId));
    }

    @PostMapping
    public ResponseEntity<String> createReservation(@AuthenticationPrincipal long userId,
                                                    @RequestBody ReservationSaveRequest reservationSaveRequest) {
        reservationService.saveReservation(userId, reservationSaveRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/ridesharing")
    public ResponseEntity<String> createRidesharing(@AuthenticationPrincipal long userId, ReservationRideSharingRequest rideSharingRequest) {
        reservationService.saveRidesharing(userId, rideSharingRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{reservation_id}")
    public ResponseEntity<String> deleteReservation(@PathVariable(name = "reservation_id") long reservationId) {
        reservationService.deleteReservation(reservationId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

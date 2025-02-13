package com.moata.moata.controller.reservation;

import com.moata.moata.config.jwt.TokenProvider;
import com.moata.moata.dto.reservation.ReservationResponse;
import com.moata.moata.dto.reservation.ReservationRideSharingRequest;
import com.moata.moata.dto.reservation.ReservationSaveRequest;
import com.moata.moata.service.reservation.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/reservation")
public class ReservationController {
    private final ReservationService reservationService;
    private final TokenProvider tokenProvider;

    @GetMapping("/all")
    public ResponseEntity<List<ReservationResponse>> getAllReservations(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        Long userId = tokenProvider.getUserId(token);

        return ResponseEntity.ok().body(reservationService.findAll(userId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ReservationResponse>> searchReservation(@RequestHeader("Authorization") String authorizationHeader,
                                                                       @RequestParam(name = "startDate", required = false) String startDate,
                                                                       @RequestParam(name = "endDate", required = false) String endDate,
                                                                       @RequestParam(name = "userId", required = false, defaultValue = "-1") Long userId) {
        String token = authorizationHeader.replace("Bearer ", "");
        Long myId = tokenProvider.getUserId(token);

        return ResponseEntity.ok().body(reservationService.searchReservation(myId, startDate, endDate, userId));
    }

    @GetMapping("/my")
    public ResponseEntity<List<ReservationResponse>> getMyReservations(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        Long userId = tokenProvider.getUserId(token);

        return ResponseEntity.ok().body(reservationService.findMy(userId));
    }

    @PostMapping
    public ResponseEntity<String> createReservation(@RequestHeader("Authorization") String authorizationHeader,
                                                    @RequestBody ReservationSaveRequest reservationSaveRequest) {
        String token = authorizationHeader.replace("Bearer ", "");
        Long userId = tokenProvider.getUserId(token);

        reservationService.saveReservation(userId, reservationSaveRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/ridesharing")
    public ResponseEntity<String> createRidesharing(@RequestHeader("Authorization") String authorizationHeader,
                                                    @RequestBody ReservationRideSharingRequest rideSharingRequest) {
        String token = authorizationHeader.replace("Bearer ", "");
        Long userId = tokenProvider.getUserId(token);

        reservationService.saveRidesharing(userId, rideSharingRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{reservation_id}")
    public ResponseEntity<String> deleteReservation(@PathVariable(name = "reservation_id") long reservationId) {
        reservationService.deleteReservation(reservationId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
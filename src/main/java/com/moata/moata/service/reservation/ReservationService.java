package com.moata.moata.service.reservation;

import com.moata.moata.dto.reservation.ReservationResponse;
import com.moata.moata.dto.reservation.ReservationRideSharingRequest;
import com.moata.moata.dto.reservation.ReservationSaveRequest;
import com.moata.moata.entity.group.Group;
import com.moata.moata.entity.group.MatchingGroup;
import com.moata.moata.entity.reservation.Reservation;
import com.moata.moata.entity.user.User;
import com.moata.moata.repository.group.GroupRepository;
import com.moata.moata.repository.group.MatchingGroupRepository;
import com.moata.moata.repository.reservation.ReservationRepository;
import com.moata.moata.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final MatchingGroupRepository matchingGroupRepository;

    public List<Group> findMatchingGroup(long userId) {
        User user = userRepository.findById(userId).orElseThrow(NoSuchElementException::new);
        return matchingGroupRepository.findByParticipantId(user).stream().map(MatchingGroup::getGroupId).collect(Collectors.toList());
    }

    //matching_group 적용 이후 추가 수정
    public List<ReservationResponse> findAll(long userId) {
        List<Reservation> reservations = reservationRepository.findByGroupIdIn(findMatchingGroup(userId));
        return reservations.stream().map(ReservationResponse::from).toList();
    }

    public List<ReservationResponse> searchReservation(long myId, String startDate, String endDate, long userId) {

        LocalDateTime from = Optional.ofNullable(startDate)
                .map(s -> LocalDateTime.parse(s, DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .orElse(null);
        LocalDateTime to = Optional.ofNullable(endDate)
                .map(s -> LocalDateTime.parse(s, DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .orElse(null);

        User user = userRepository.findById(userId)
                .orElse(null);

        return reservationRepository.searchReservations(from, to, user, findMatchingGroup(myId)).stream().map(ReservationResponse::from).toList();
    }

    public List<ReservationResponse> findMy(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저"));

        List<Reservation> reservations = reservationRepository.findByReserverId(user);
        return reservations.stream().map(ReservationResponse::from).toList();
    }

    public void saveReservation(long userId, ReservationSaveRequest saveRequest) {
        Group group = groupRepository.findByGroupId(saveRequest.getGroupId())
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 그룹"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저"));

        reservationRepository.save(saveRequest.toModel(group, user));
    }

    public void saveRidesharing(long userId, ReservationRideSharingRequest saveRequest) {
        Reservation reservation = reservationRepository.findById(saveRequest.getReservationId())
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 예약"));

        reservation.addRideSharing(saveRequest);
        reservationRepository.save(reservation);
    }

    public void deleteReservation(long reservationId) {
        reservationRepository.deleteById(reservationId);
    }
}

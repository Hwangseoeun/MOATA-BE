package com.moata.moata.service.user;

import com.moata.moata.dto.user.UserLocationSaveRequest;
import com.moata.moata.dto.user.UserLocationUpdateRequest;
import com.moata.moata.dto.user.UserNameUpdateRequest;
import com.moata.moata.dto.user.UserProfileResponse;
import com.moata.moata.entity.user.User;
import com.moata.moata.repository.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }

    public UserProfileResponse findUserProfileByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return UserProfileResponse.from(user);
    }

    public User saveUserLocation(Long userId, UserLocationSaveRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return userRepository.save(request.toModel());
    }

    public User updateUserName(Long userId, UserNameUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return userRepository.save(request.toModel());
    }

    public User updateUserLocation(Long userId, UserLocationUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return userRepository.save(request.toModel());
    }
}

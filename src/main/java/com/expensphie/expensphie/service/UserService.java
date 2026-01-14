package com.expensphie.expensphie.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.expensphie.expensphie.dto.UserDTO;
import com.expensphie.expensphie.model.UserEntity;
import com.expensphie.expensphie.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final EmailService emailService;

    public UserDTO registerUser(UserDTO userDTO) {
        UserEntity newUser = toEntity(userDTO);
        newUser.setActivationToken(UUID.randomUUID().toString());
        newUser = userRepository.save(newUser);
        // Send activation email
        String activationLink = "http://localhost:8080/api/v1/activate?token=" + newUser.getActivationToken();
        String subject = "Activate your Money Manger account";
        String body = "Click on the following link to activate your account: " + activationLink;
        emailService.sendEmail(newUser.getEmail(), subject, body);
        return toDTO(newUser);
    }

    public UserEntity toEntity(UserDTO userDTO) {
        return UserEntity.builder()
                .id(userDTO.getId())
                .fullName(userDTO.getFullName())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .userImageUrl(userDTO.getUserImageUrl())
                .createdAt(userDTO.getCreatedAt())
                .updatedAt(userDTO.getUpdatedAt())
                .build();
    }

    public UserDTO toDTO(UserEntity userEntity) {
        return UserDTO.builder()
                .id(userEntity.getId())
                .fullName(userEntity.getFullName())
                .email(userEntity.getEmail())
                .userImageUrl(userEntity.getUserImageUrl())
                .createdAt(userEntity.getCreatedAt())
                .updatedAt(userEntity.getUpdatedAt())
                .build();
    }

    public boolean activateUser(String activationToken) {
        return userRepository.findByActivationToken(activationToken)
                .map(user -> {
                    user.setIsActive(true);
                    userRepository.save(user);
                    return true;
                })
                .orElse(false);
    }
}

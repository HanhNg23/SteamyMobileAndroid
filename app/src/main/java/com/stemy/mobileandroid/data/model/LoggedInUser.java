package com.stemy.mobileandroid.data.model;

import com.stemy.mobileandroid.type.Role;
import com.stemy.mobileandroid.type.UserStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoggedInUser {
    private String userId;
    private String userMail;
    private String fullName;
    private Role role;
    private UserStatus status;
    private String phone;
    @Builder.Default
    private boolean isAuthenticated = false;
    private String accessToken;

}
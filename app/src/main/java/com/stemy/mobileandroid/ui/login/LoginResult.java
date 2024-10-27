package com.stemy.mobileandroid.ui.login;

import androidx.annotation.Nullable;

/**
 * Authentication result : success (user details) or error message.
 */
class LoginResult {
    @Nullable
    private LoggedInUserView success;
    @Nullable
    private Integer error;

    @Nullable
    private String errorMessage;

    LoginResult(@Nullable Integer error) {
        this.error = error;
    }
    LoginResult(@Nullable String errorMessage) {
        this.errorMessage = errorMessage;
    }
    LoginResult(@Nullable LoggedInUserView success) {
        this.success = success;
    }

    @Nullable
    LoggedInUserView getSuccess() {
        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }

    @Nullable
    String getErrorMessage() {
        return errorMessage;
    }
}
package com.stemy.mobileandroid.ui.employee.updatestaff;

import androidx.annotation.Nullable;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateStaffFormState {
    @Nullable
    @Builder.Default
    private String emailError = null;
    @Nullable
    @Builder.Default
    private String addressError = null;
    @Nullable
    @Builder.Default
    private String phoneError = null;
    @Nullable
    @Builder.Default
    private String fullnameError = null;
    @Nullable
    @Builder.Default
    private String accountRoleError = null;
    @Nullable
    @Builder.Default
    private String accountStatusError = null;

    @Builder.Default
    private boolean isDataValid = false;

}

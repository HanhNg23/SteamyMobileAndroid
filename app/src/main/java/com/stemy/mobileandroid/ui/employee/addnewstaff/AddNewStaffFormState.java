package com.stemy.mobileandroid.ui.employee.addnewstaff;

import androidx.annotation.Nullable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddNewStaffFormState {
    @Nullable
    private Integer emailError;
    @Nullable
    private Integer passwordError;
    @Nullable
    private Integer fullnameError;
    @Nullable
    private Integer accountRoleError;
    @Nullable
    private Integer accountStatusError;

    private boolean isDataValid;

    public AddNewStaffFormState(@Nullable Integer accountStatusError, @Nullable Integer accountRoleError, @Nullable Integer fullnameError, @Nullable Integer passwordError, @Nullable Integer emailError) {
        this.isDataValid = false;
        this.accountStatusError = accountStatusError;
        this.accountRoleError = accountRoleError;
        this.fullnameError = fullnameError;
        this.passwordError = passwordError;
        this.emailError = emailError;
    }

    AddNewStaffFormState(boolean isDataValid) {
        this.accountStatusError = null;
        this.accountRoleError = null;
        this.fullnameError = null;
        this.passwordError = null;
        this.emailError = null;
        this.isDataValid = isDataValid;
    }

}

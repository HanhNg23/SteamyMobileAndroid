package com.stemy.mobileandroid.ui.employee.updatestaff;

import androidx.annotation.Nullable;

import com.stemy.mobileandroid.data.model.AccountUser;
import com.stemy.mobileandroid.ui.employee.addnewstaff.AddNewStaffSuccessInUserView;

public class UpdateStaffResult {
    @Nullable
    private AccountUser success;
    @Nullable
    private String errorMessage;

    UpdateStaffResult(@Nullable String errorMessage) {
        this.errorMessage = errorMessage;
    }

    UpdateStaffResult(@Nullable AccountUser success) {
        this.success = success;
    }

    @Nullable
    AccountUser getSuccess() {
        return success;
    }

    @Nullable
    String getErrorMessage() {
        return errorMessage;
    }
}

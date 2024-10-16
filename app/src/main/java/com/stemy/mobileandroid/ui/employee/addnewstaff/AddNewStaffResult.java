package com.stemy.mobileandroid.ui.employee.addnewstaff;

import androidx.annotation.Nullable;

public class AddNewStaffResult {
    @Nullable
    private AddNewStaffSuccessInUserView success;
    @Nullable
    private Integer error;

    AddNewStaffResult(@Nullable Integer error) {
        this.error = error;
    }

    AddNewStaffResult(@Nullable AddNewStaffSuccessInUserView success) {
        this.success = success;
    }

    @Nullable
    AddNewStaffSuccessInUserView getSuccess() {
        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }
}

package com.stemy.mobileandroid.ui.employeeslist;

import androidx.annotation.Nullable;

public class RemoveEmployeeResult {
    @Nullable
    private boolean successRemove;

    @Nullable
    private String errorMessage;

    RemoveEmployeeResult(String errorMessage){
        this.errorMessage = errorMessage;
    }

    RemoveEmployeeResult(boolean successRemove){
        this.successRemove = successRemove;
    }
    @Nullable
    boolean getSuccess() {
        return successRemove;
    }

    @Nullable
    String getErrorMessage() {
        return errorMessage;
    }
}

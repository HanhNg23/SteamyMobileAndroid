package com.stemy.mobileandroid.ui.employeeslist;

import androidx.annotation.Nullable;

import com.stemy.mobileandroid.ui.employee.addnewstaff.AddNewStaffSuccessInUserView;

public class GetAllEmployeesResult {

    @Nullable
    private GetAllEmpployeesSuccessInUserView success;

    @Nullable
    private Integer error;

    GetAllEmployeesResult(@Nullable Integer error){
        this.error = error;
    }

    GetAllEmployeesResult(@Nullable GetAllEmpployeesSuccessInUserView getAllEmpployeesSuccessInUserView){
        this.success = getAllEmpployeesSuccessInUserView;
    }
    @Nullable
    GetAllEmpployeesSuccessInUserView getSuccess() {
        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }

}

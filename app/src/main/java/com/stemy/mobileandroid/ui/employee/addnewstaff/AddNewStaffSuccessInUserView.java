package com.stemy.mobileandroid.ui.employee.addnewstaff;

import com.stemy.mobileandroid.data.model.AccountUser;

public class AddNewStaffSuccessInUserView {

    private AccountUser registeredStaff;

    public AddNewStaffSuccessInUserView(AccountUser registeredStaff){
        this.registeredStaff = registeredStaff;
    }
    AccountUser getRegisteredStaff(){
        return this.registeredStaff;
    }


}

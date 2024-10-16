package com.stemy.mobileandroid.ui.employeeslist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.common.base.Strings;
import com.stemy.mobileandroid.R;
import com.stemy.mobileandroid.data.ListCallbackGeneric;
import com.stemy.mobileandroid.data.SystemUser.GetAllUserRepository;
import com.stemy.mobileandroid.data.model.AccountUser;
import com.stemy.mobileandroid.ui.employee.addnewstaff.AddNewStaffResult;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class EmployeesViewModel extends ViewModel {

    private GetAllUserRepository getAllUserRepository;
    private MutableLiveData<GetAllEmployeesResult> getAllEmployeesResult = new MutableLiveData<GetAllEmployeesResult>();


    @Inject
    public EmployeesViewModel(GetAllUserRepository getAllUserRepository) {
     this.getAllUserRepository = getAllUserRepository;
    }

    LiveData<GetAllEmployeesResult> getAllEmployeesResult(){
        return getAllEmployeesResult;
    }

    public void getAllEmployeesUser(){
        this.getAllUserRepository.GetAllUsers(new ListCallbackGeneric<AccountUser>() {
            @Override
            public void onSuccess(List<AccountUser> list) {
                getAllEmployeesResult.setValue(new GetAllEmployeesResult(new GetAllEmpployeesSuccessInUserView(list)));
            }

            @Override
            public void onError(Exception e) {
                getAllEmployeesResult.setValue(new GetAllEmployeesResult(Integer.valueOf("Get all system users failed !")));
            }
        });
    }


}

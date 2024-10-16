package com.stemy.mobileandroid.ui.employee.addnewstaff;

import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.stemy.mobileandroid.R;
import com.stemy.mobileandroid.data.AccountCallback;
import com.stemy.mobileandroid.data.Register.RegisterRepository;
import com.stemy.mobileandroid.data.model.AccountUser;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AddNewStaffViewModel extends ViewModel {

    private MutableLiveData<AddNewStaffFormState> addNewStaffFormState = new MutableLiveData<>();
    private MutableLiveData<AddNewStaffResult> addNewStaffResult = new MutableLiveData<>();

    private RegisterRepository registerRepository;
    @Inject
    public AddNewStaffViewModel(RegisterRepository registerRepository){
        this.registerRepository = registerRepository;
    }

    LiveData<AddNewStaffFormState> getAddNewStaffFormState(){
        return addNewStaffFormState;
    }

    LiveData<AddNewStaffResult> getAddNewStaffResult(){
        return addNewStaffResult;
    }

    public void register(AccountUser accountUser){
        registerRepository.register(accountUser, new AccountCallback() {
            @Override
            public void onSuccess(AccountUser user) {
                addNewStaffResult.setValue(new AddNewStaffResult(new AddNewStaffSuccessInUserView(user)));
            }

            @Override
            public void onError(Exception e) {
                   addNewStaffResult.setValue(new AddNewStaffResult(R.string.register_failed));
            }
        });
    }

    public void registerDataChanged(AccountUser accountUser){
        if(!isEmailValid(accountUser.getUserMail())){
            addNewStaffFormState.setValue(new AddNewStaffFormState(null, null, null, null, R.string.invalid_email));
        } else if (!isFullNameValid(accountUser.getFullName())) {
            addNewStaffFormState.setValue(new AddNewStaffFormState(null, null, R.string.invalid_fullname, null, null));
        }
//        else if(!isAccountRole(loggedInUser.getRole().name)){
//            addNewStaffFormState.setValue(new AddNewStaffFormState(null, R.string.invalid_role, null, null, null));
//        }
//        else if(!isAccountStatus(loggedInUser.getStatus().toString())){
//            addNewStaffFormState.setValue(new AddNewStaffFormState(R.string.invalid_status, null, null, null, null));
//        }
        else if (!isPasswordValid(accountUser.getPassword())) {
            addNewStaffFormState.setValue(new AddNewStaffFormState(null, null, null, R.string.invalid_password, null));
        } else {
            addNewStaffFormState.setValue(new AddNewStaffFormState(true));
        }
    }

    private boolean isEmailValid(String email) {
        if (email == null) {
            return false;
        }
        if (email.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        } else {
            return !email.trim().isEmpty();
        }
    }

    private boolean isFullNameValid(String fullName) {
        if (fullName == null) {
            return false;
        } else {
            return !fullName.trim().isEmpty();
        }
    }

    private boolean isAccountRole(String role) {
        if (role == null) {
            return false;
        } else {
            return !role.trim().isEmpty();
        }
    }

    private boolean isAccountStatus(String status) {
        if (status == null) {
            return false;
        } else {
            return !status.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}

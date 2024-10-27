package com.stemy.mobileandroid.ui.employee.updatestaff;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.stemy.mobileandroid.data.AccountCallback;
import com.stemy.mobileandroid.data.UpdateStaff.UpdateStaffRepository;
import com.stemy.mobileandroid.data.model.AccountUser;
import com.stemy.mobileandroid.ui.employee.ValidatorViewModelDefine.AccountUserValidator;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class UpdateStaffViewModel extends ViewModel {

    private MutableLiveData<UpdateStaffFormState> updateStaffFormState = new MutableLiveData<>();
    private MutableLiveData<UpdateStaffResult> updateStaffResult = new MutableLiveData<>();

    private UpdateStaffRepository updateStaffRepository;

    @Inject
    public UpdateStaffViewModel(UpdateStaffRepository updateStaffRepository) {
        this.updateStaffRepository = updateStaffRepository;
    }

    LiveData<UpdateStaffFormState> getUpdateStaffFormState() {
        return updateStaffFormState;
    }

    LiveData<UpdateStaffResult> getUpdateStaffResult() {
        return updateStaffResult;
    }

    public void updateStaff(AccountUser accountUser) {
        updateStaffRepository.updateStaff(accountUser, new AccountCallback() {
            @Override
            public void onSuccess(AccountUser user) {
                updateStaffResult.setValue(new UpdateStaffResult(user));
            }

            @Override
            public void onError(Exception e) {
                updateStaffResult.setValue(new UpdateStaffResult(e.getMessage()));
            }
        });
    }

    //check data input
    public void updateUserDataChange(AccountUser accountUser) {
        if (!AccountUserValidator.isEmailValid(accountUser.getUserMail())) {
            updateStaffFormState.setValue(new UpdateStaffFormState.UpdateStaffFormStateBuilder()
                    .emailError(AccountUserValidator.getMessageError()).isDataValid(false).build()
            );

        } else if (!AccountUserValidator.isPhoneValid(accountUser.getPhone())) {
            updateStaffFormState.setValue(new UpdateStaffFormState.UpdateStaffFormStateBuilder()
                    .phoneError(AccountUserValidator.getMessageError()).isDataValid(false).build()
            );
        } else if (!AccountUserValidator.isFullNameValid(accountUser.getFullName())) {
            updateStaffFormState.setValue(new UpdateStaffFormState.UpdateStaffFormStateBuilder()
                    .fullnameError(AccountUserValidator.getMessageError()).isDataValid(false).build()
            );
        } else if (!AccountUserValidator.isAccountRoleValid(accountUser.getRole())) {
            updateStaffFormState.setValue(new UpdateStaffFormState.UpdateStaffFormStateBuilder()
                    .accountRoleError(AccountUserValidator.getMessageError()).isDataValid(false).build()
            );
        } else if (!AccountUserValidator.isAccountStatusValid(accountUser.getStatus())) {
            updateStaffFormState.setValue(new UpdateStaffFormState.UpdateStaffFormStateBuilder()
                    .accountStatusError(AccountUserValidator.getMessageError()).isDataValid(false).build()
            );
        } else {
            updateStaffFormState.setValue(new UpdateStaffFormState.UpdateStaffFormStateBuilder()
                    .isDataValid(true).build());
        }
    }


}

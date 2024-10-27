package com.stemy.mobileandroid.ui.employeeslist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.stemy.mobileandroid.data.AccountCallback;
import com.stemy.mobileandroid.data.RemoveStaff.RemoveRepository;
import com.stemy.mobileandroid.data.model.AccountUser;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class EmployeeDetailViewModel extends ViewModel {
    private final MutableLiveData<AccountUser> currentUser = new MutableLiveData<>();
    private RemoveRepository removeRepository;
    private final MutableLiveData<RemoveEmployeeResult> removeEmployeeResult = new MutableLiveData<>();

    @Inject
    public EmployeeDetailViewModel(RemoveRepository removeRepository) {
        this.removeRepository = removeRepository;
    }


    public LiveData<AccountUser> getCurrentUser() {
        return currentUser;
    }

    public LiveData<RemoveEmployeeResult> removeEmployeeResult() {
        return removeEmployeeResult;
    }

    public void setCurrentUser(AccountUser user) {
        currentUser.setValue(user);
    }

    public void removeEmployee(int userId){
        this.removeRepository.removeStaff(userId, new AccountCallback() {

            @Override
            public void onSuccess(AccountUser user) {
                removeEmployeeResult.setValue(new RemoveEmployeeResult(true));
            }

            @Override
            public void onError(Exception e) {
                removeEmployeeResult.setValue(new RemoveEmployeeResult(e.getMessage()));

            }
        });
    }




}

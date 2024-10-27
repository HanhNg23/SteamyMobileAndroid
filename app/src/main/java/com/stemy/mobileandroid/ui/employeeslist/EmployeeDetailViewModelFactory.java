package com.stemy.mobileandroid.ui.employeeslist;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.stemy.mobileandroid.data.RemoveStaff.RemoveRepository;
import com.stemy.mobileandroid.data.SystemUser.GetAllUserRepository;

import javax.inject.Inject;

import dagger.hilt.android.scopes.ActivityScoped;

@ActivityScoped
public class EmployeeDetailViewModelFactory implements ViewModelProvider.Factory {
    private RemoveRepository removeRepository;

    @Inject
    public EmployeeDetailViewModelFactory(RemoveRepository removeRepository){
        this.removeRepository = removeRepository;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass){
        if(modelClass.isAssignableFrom(EmployeeDetailViewModel.class)){
            return (T) new EmployeeDetailViewModel(removeRepository);
        }else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }

}
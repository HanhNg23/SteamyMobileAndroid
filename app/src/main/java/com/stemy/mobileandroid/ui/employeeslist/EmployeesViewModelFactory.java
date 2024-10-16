package com.stemy.mobileandroid.ui.employeeslist;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.stemy.mobileandroid.data.SystemUser.GetAllUserRepository;

import javax.inject.Inject;

import dagger.hilt.android.scopes.ActivityScoped;

@ActivityScoped
public class EmployeesViewModelFactory implements ViewModelProvider.Factory {
    private GetAllUserRepository getAllUserRepository;

    @Inject
    public EmployeesViewModelFactory(GetAllUserRepository getAllUserRepository){
        this.getAllUserRepository = getAllUserRepository;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass){
        if(modelClass.isAssignableFrom(EmployeesViewModel.class)){
            return (T) new EmployeesViewModel(getAllUserRepository);
        }else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }

}

package com.stemy.mobileandroid.ui.employee.updatestaff;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.stemy.mobileandroid.data.UpdateStaff.UpdateStaffRepository;
import com.stemy.mobileandroid.ui.employee.addnewstaff.AddNewStaffViewModel;

import javax.inject.Inject;

import dagger.hilt.android.scopes.ActivityScoped;

@ActivityScoped
public class UpdateStaffViewModelFactory implements ViewModelProvider.Factory {
    private UpdateStaffRepository updateStaffRepository;

    @Inject
    public UpdateStaffViewModelFactory(UpdateStaffRepository updateStaffRepository){
        this.updateStaffRepository = updateStaffRepository;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass){
        if(modelClass.isAssignableFrom(UpdateStaffViewModel.class)){
            return (T) new UpdateStaffViewModel(updateStaffRepository);
        }else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }



}

package com.stemy.mobileandroid.ui.employee.addnewstaff;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.stemy.mobileandroid.data.Register.RegisterRepository;
import javax.inject.Inject;
import dagger.hilt.android.scopes.ActivityScoped;

@ActivityScoped
public class AddNewStaffViewModelFactory implements ViewModelProvider.Factory {
    private RegisterRepository registerRepository;

    @Inject
    public AddNewStaffViewModelFactory(RegisterRepository registerRepository){
        this.registerRepository = registerRepository;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass){
        if(modelClass.isAssignableFrom(AddNewStaffViewModel.class)){
            return (T) new AddNewStaffViewModel(registerRepository);
        }else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }

}

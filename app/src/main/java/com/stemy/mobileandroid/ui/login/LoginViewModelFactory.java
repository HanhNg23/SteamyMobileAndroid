package com.stemy.mobileandroid.ui.login;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import com.stemy.mobileandroid.data.Login.LoginRepository;

import javax.inject.Inject;

import dagger.hilt.android.scopes.ActivityScoped;

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
@ActivityScoped
public class LoginViewModelFactory implements ViewModelProvider.Factory {


    private LoginRepository loginRepository;

    @Inject
    public LoginViewModelFactory(LoginRepository loginRepository){
        this.loginRepository = loginRepository;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel(loginRepository);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
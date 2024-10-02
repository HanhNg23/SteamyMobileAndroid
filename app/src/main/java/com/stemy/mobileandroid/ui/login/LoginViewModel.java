package com.stemy.mobileandroid.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;

import com.stemy.mobileandroid.data.LoginCallback;
import com.stemy.mobileandroid.data.LoginRepository;
import com.stemy.mobileandroid.data.Result;
import com.stemy.mobileandroid.data.model.LoggedInUser;
import com.stemy.mobileandroid.R;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.disposables.Disposable;

@HiltViewModel
public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();


    LoginRepository loginRepository;

    @Inject
    public LoginViewModel(LoginRepository loginRepository){
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

//    public void login(String username, String password) {
//        // can be launched in a separate asynchronous job
//        Result<LoggedInUser> result = loginRepository.login(username, password);
//
//        if (result instanceof Result.Success) {
//            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
//            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getFullName())));
//        } else {
//            loginResult.setValue(new LoginResult(R.string.login_failed));
//        }
//    }

    public void login(String username, String password){
        loginRepository.login(username, password, new LoginCallback() {
            @Override
            public void onSuccess(LoggedInUser user) {
                loginResult.setValue(new LoginResult(new LoggedInUserView(user.getFullName())));
            }

            @Override
            public void onError(Exception e) {
                loginResult.setValue(new LoginResult(R.string.login_failed));
            }
        });
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}
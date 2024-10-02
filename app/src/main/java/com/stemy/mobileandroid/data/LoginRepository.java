package com.stemy.mobileandroid.data;

import android.annotation.SuppressLint;
import android.util.Log;

import com.stemy.mobileandroid.data.model.LoggedInUser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
@Singleton
public class LoginRepository {

    private LoginDataSource dataSource;

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private LoggedInUser user = null;

    // private constructor : singleton access
    @Inject
    public LoginRepository(LoginDataSource dataSource) {
        this.dataSource = dataSource;
    }



    public boolean isLoggedIn() {
        return user != null;
    }

    public void logout() {
        user = null;
        dataSource.logout();
    }

    private void setLoggedInUser(LoggedInUser user) {
        this.user = user;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    @SuppressLint("CheckResult")
    public void login(String username, String password, LoginCallback callback){
        Log.d("LoginRepository", "Starting login for user: " + username);
        dataSource.loginAsync(username, password)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(result -> {
                        Log.d("LoginRepository", "Login API call completed");
                        if(result instanceof Result.Success){
                            LoggedInUser loggedInUser = ((Result.Success<LoggedInUser>) result).getData();
                            setLoggedInUser(loggedInUser);
                            Log.d("LoginRepository", "Login successful, retrieving user data");
                            Log.d("USER DATA", user.getAccessToken());
                            dataSource.getCurrentLoggedInUser()
                                    .subscribe(user -> {
                                        Log.d("LoginRepository", "User data retrieved successfully");
                                        Log.d("USER DATA", user.toString());
                                        callback.onSuccess(user);
                                    }, error -> {
                                        Log.e("LoginRepository", "Error retrieving user data: " + error.getMessage());
                                        callback.onError(new Exception("Error logging in", error));
                                    });
                        } else if (result instanceof  Result.Error) {
                            Log.e("LoginRepository", "Login failed: " + ((Result.Error) result).getError().getMessage());
                            callback.onError(((Result.Error) result).getError());
                        }
                    }, throwable -> {
                        Log.e("LoginRepository", "Error logging in: " + throwable.getMessage());
                        callback.onError(new Exception("Error logging in", throwable));
                    });
    }



}
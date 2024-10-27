package com.stemy.mobileandroid.data.SystemUser;

import android.annotation.SuppressLint;
import android.util.Log;
import com.stemy.mobileandroid.data.ListCallbackGeneric;
import com.stemy.mobileandroid.data.model.AccountUser;
import com.stemy.mobileandroid.data.model.Result;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

@Singleton
public class GetAllUserRepository {
    private GetAllUserDatasource getAllUserDatasource;

    private List<AccountUser> usersList = null;


    @Inject
    public GetAllUserRepository(GetAllUserDatasource datasource){
        this.getAllUserDatasource = datasource;
    }

    public void setUsersList(List<AccountUser> allUsers){
        this.usersList = allUsers;
    }

    @SuppressLint("CheckResult")
    public void getAllUsers(ListCallbackGeneric<AccountUser> accountUsersCallBack){
        Log.d("GetAllUserRepository", "Starting getting all user: ");
        getAllUserDatasource.getAllSystemUsers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                   Log.d("GetAllUserRepository", "Get all user API call");
                   if(result instanceof Result.Success) {
                       List<AccountUser> allAccountUsers = ((Result.Success<List<AccountUser>>) result).getData();
                       this.setUsersList(allAccountUsers);
                       Log.d("GetAllUserRepository", "Get all users successfully");
                       Log.d("First User Data", usersList.get(0).toString());
                       accountUsersCallBack.onSuccess(this.usersList);
                   } else if (result instanceof Result.Error) {
                       Log.e("GetAllUserRepository", "Get All User Failed" + ((Result.Error) result).getError().getMessage());
                       accountUsersCallBack.onError(((Result.Error) result).getError());
                   }
                }, throwable -> {
                    Log.e(this.getClass().getName(), "Error getting all system users: " + throwable.getMessage());
                    accountUsersCallBack.onError(new Exception("Error getting all system users ", throwable));
                });
    }


}

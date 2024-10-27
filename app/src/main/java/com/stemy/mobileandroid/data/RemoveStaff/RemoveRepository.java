package com.stemy.mobileandroid.data.RemoveStaff;

import android.annotation.SuppressLint;
import android.util.Log;

import com.stemy.mobileandroid.data.AccountCallback;
import com.stemy.mobileandroid.data.Image.UploadImageAvatarDatasource;
import com.stemy.mobileandroid.data.Login.LoginDataSource;
import com.stemy.mobileandroid.data.UpdateStaff.UpdateStaffDatasource;
import com.stemy.mobileandroid.data.model.AccountUser;
import com.stemy.mobileandroid.data.model.Result;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

@Singleton
public class RemoveRepository {

    private RemoveStaffDatasource removeStaffDataSource;
    private boolean removeSuccess = false;
    private String theClassName = this.getClass().getName();

    @Inject
    public RemoveRepository(RemoveStaffDatasource removeStaffDataSource){
        this.removeStaffDataSource = removeStaffDataSource;
    }

    public boolean isRemoveSuccess() {
        return this.removeSuccess;
    }

    public boolean setIsRemoveSuccess(boolean isSuccess) {
        return this.removeSuccess = isSuccess;
    }



    @SuppressLint("CheckResult")
    public void removeStaff(int userId, AccountCallback accountCallback) {
        Log.d(theClassName, "Starting remove user id " + userId);
        removeStaffDataSource.removeSaff(userId)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> Log.d(theClassName, "Update of user image first if not null"))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    if(result instanceof Result.Success){
                        boolean isSuccessRemove = ((Result.Success<Boolean>) result).getData();
                            Log.d(theClassName, "Remove successful for user: " + isSuccessRemove);
                        setIsRemoveSuccess(isSuccessRemove);
                        accountCallback.onSuccess(null);
                    }else if (result instanceof Result.Error){
                        Log.e(theClassName, "Error updating user : " + ((Result.Error) result).getError().getMessage());
                        accountCallback.onError(((Result.Error) result).getError());
                    }
                }, throwable -> {
                    // Handle any errors during the registration or update process
                    Log.e(theClassName, "Error during updating user process: " + throwable.getMessage());
                    accountCallback.onError(new Exception("Error during updating process", throwable));
                });
    }
}

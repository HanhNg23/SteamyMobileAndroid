package com.stemy.mobileandroid.data.UpdateStaff;

import android.annotation.SuppressLint;
import android.util.Log;

import com.stemy.mobileandroid.data.AccountCallback;
import com.stemy.mobileandroid.data.Image.UploadImageAvatarDatasource;
import com.stemy.mobileandroid.data.Login.LoginDataSource;
import com.stemy.mobileandroid.data.Register.RegisterDatasource;
import com.stemy.mobileandroid.data.model.AccountUser;
import com.stemy.mobileandroid.data.model.Result;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

@Singleton
public class UpdateStaffRepository {

    private UpdateStaffDatasource updateStaffDatasource;
    private LoginDataSource loginDataSource;
    private UploadImageAvatarDatasource imageAvatarDatasource;
    private AccountUser userUpdateSuccess = null;
    private String avatarUrl = "";
    private String theClassName = this.getClass().getName();

    @Inject
    public UpdateStaffRepository(UpdateStaffDatasource updateStaffDatasource,
                              LoginDataSource loginDataSource,
                              UploadImageAvatarDatasource imageAvatarDatasource){
        this.updateStaffDatasource = updateStaffDatasource;
        this.loginDataSource = loginDataSource;
        this.imageAvatarDatasource = imageAvatarDatasource;
    }

    public boolean isUpdateSuccess() {
        return userUpdateSuccess != null;
    }

    private void setUserRegistrationSuccess(AccountUser userUpdateSuccess){
        this.userUpdateSuccess = userUpdateSuccess;
    }

    @SuppressLint("CheckResult")
    public void updateStaff(AccountUser accountUser, AccountCallback accountCallback) {
        Log.d(theClassName, "Starting update user " + accountUser.getUserMail());
        this.avatarUrl = accountUser.getAvatar();
       imageAvatarDatasource.getImageAvatar(accountUser.getAvatarFile())
               .subscribeOn(Schedulers.io())
               .doOnSubscribe(disposable -> Log.d(theClassName, "Update of user image first if not null"))
               .observeOn(AndroidSchedulers.mainThread())
               .flatMap(imageResult -> {
                  if(imageResult instanceof Result.Success){
                      String uploadedImagePath = ((Result.Success<String>) imageResult).getData();
                      if(uploadedImagePath != null || !uploadedImagePath.isEmpty()){
                          this.avatarUrl = uploadedImagePath;
                          accountUser.setAvatar(this.avatarUrl);
                      }
                      Log.d(theClassName, "Image upload successful, path: " + this.avatarUrl);

                      return updateStaffDatasource.updateUserStaff(accountUser).subscribeOn(Schedulers.io())
                              .doOnSubscribe(diposal -> Log.d(theClassName, "Update of user full data "));
                  }else {
                      Log.e(theClassName, "Image upload failed");
                      return Single.error(new Exception("Error uploading image"));
                  }
               })
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe(result -> {
                   if(result instanceof Result.Success){
                       AccountUser resultUpdate = ((Result.Success<AccountUser>) result).getData();
                       Log.d(theClassName, "Update successful for user: " + resultUpdate.toString());
                       setUserRegistrationSuccess(resultUpdate);
                       accountCallback.onSuccess(this.userUpdateSuccess);
                   }else if (result instanceof Result.Error){
                       Log.e(theClassName, "Error updating user : " + ((Result.Error) result).getError().getMessage());
                       accountCallback.onError(((Result.Error) result).getError()); // Notify the callback of the error
                   }
               }, throwable -> {
                   // Handle any errors during the registration or update process
                   Log.e(theClassName, "Error during updating user process: " + throwable.getMessage());
                   accountCallback.onError(new Exception("Error during updating process", throwable));
               });
    }
}

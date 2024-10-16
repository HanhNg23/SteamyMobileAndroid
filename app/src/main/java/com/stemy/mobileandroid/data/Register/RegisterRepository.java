package com.stemy.mobileandroid.data.Register;

import android.annotation.SuppressLint;
import android.util.Log;

import com.stemy.mobileandroid.data.AccountCallback;
import com.stemy.mobileandroid.data.Image.UploadImageAvatarDatasource;
import com.stemy.mobileandroid.data.Login.LoginDataSource;
import com.stemy.mobileandroid.data.model.Result;
import com.stemy.mobileandroid.data.model.AccountUser;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Singleton;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

@Singleton
public class RegisterRepository {
    private RegisterDatasource registerDatasource;
    private LoginDataSource loginDataSource;
    private UploadImageAvatarDatasource imageAvatarDatasource;
    private AccountUser userResgisterSuccess = null;
    private String avatarUrl = "";

    @Inject
    public RegisterRepository(RegisterDatasource registerDatasource,
                              LoginDataSource loginDataSource,
                              UploadImageAvatarDatasource imageAvatarDatasource){
        this.registerDatasource = registerDatasource;
        this.loginDataSource = loginDataSource;
        this.imageAvatarDatasource = imageAvatarDatasource;
    }

    public boolean isRegisterSuccess() {
        return userResgisterSuccess != null;
    }

    private void setUserRegistrationSuccess(AccountUser resultRegister){
        this.userResgisterSuccess = resultRegister;
    }

    @SuppressLint("CheckResult")
    /*To address the thread-safety concerns when handling multiple asynchronous API
      --> need to ensure each blocking or time-consuming task (like network calls)
      is executed on a background thread (example here using Schedulers.io())
      --> the results return back to the maind thread for processing ( example here: using AndroidSchedulers.mainThread()).
    * */
    public void register(AccountUser newUser, AccountCallback callback) {
        Log.d("RegisterRepository", "Starting register user " + newUser.getUserMail());

        // Assuming registerAsync returns a Single<Result>
        registerDatasource.registerAsync(newUser)
                .subscribeOn(Schedulers.io())   // Run registration on a background thread
                .doOnSubscribe(disposable -> Log.d("RegisterProcess", "Starting registration process"))
                .observeOn(AndroidSchedulers.mainThread())   // Process the result on the main thread
                .flatMap(result -> {
                    if (result instanceof Result.Success) {
                        // Extract the registered user data from the successful registration
                        AccountUser resultRegister = ((Result.Success<AccountUser>) result).getData();
                        Log.d("RegisterProcess", "Registration successful for user: " + resultRegister.toString());
                        setUserRegistrationSuccess(resultRegister);

                        // Assuming updateRegisterUser returns a Single<Result>
                        // Upload the avatar image first, then update user with the avatar URL/path
                        return imageAvatarDatasource.getImageAvatar(newUser.getAvatarFile())
                                .subscribeOn(Schedulers.io())   // Run the image upload on a background thread
                                .flatMap(imageResult -> {
                                    if (imageResult instanceof Result.Success) {
                                        // Set the uploaded image path in the user object
                                        String uploadedImagePath = ((Result.Success<String>) imageResult).getData();
                                        this.avatarUrl = uploadedImagePath;
                                        this.userResgisterSuccess.setAvatar(uploadedImagePath);  // Update user with new avatar URL
                                        Log.d("RegisterProcess", "Image upload successful, path: " + uploadedImagePath);
                                        Log.d("RegisterProcess", "User register success full data: " + userResgisterSuccess.toString());
                                        // Now call updateRegisterUser with the updated user data
                                        return registerDatasource.updateRegisterUser(this.userResgisterSuccess)
                                                .subscribeOn(Schedulers.io());   // Run the update process on a background thread
                                    } else {
                                        Log.e("RegisterProcess", "Image upload failed");
                                        return Single.error(new Exception("Error uploading image"));
                                    }
                                });
                    } else {
                        Log.e("RegisterProcess", "Registration failed");
                        // Return an error Observable if registration fails
                        return Single.error(new Exception("Registration failed")); // Must return Single here
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())   // Process avatar image retrieval on the main thread
                .subscribe(result -> {
                    if (result instanceof Result.Success) {
                        // Handle the result of updating the user's status and avatar image
                        callback.onSuccess(this.userResgisterSuccess); // Notify the callback of success
                    } else if (result instanceof Result.Error) {
                        Log.e("RegisterProcess", "Error updating user role/status or fetching avatar: " + ((Result.Error) result).getError().getMessage());
                        callback.onError(((Result.Error) result).getError()); // Notify the callback of the error
                    }
                }, throwable -> {
                    // Handle any errors during the registration or update process
                    Log.e("RegisterProcess", "Error during registration/update process: " + throwable.getMessage());
                    callback.onError(new Exception("Error during registration process", throwable)); // Notify the callback of the error
                });
    }








}

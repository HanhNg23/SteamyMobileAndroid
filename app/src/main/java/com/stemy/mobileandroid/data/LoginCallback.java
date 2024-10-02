package com.stemy.mobileandroid.data;

import com.stemy.mobileandroid.data.model.LoggedInUser;

public interface LoginCallback {
    void onSuccess(LoggedInUser user);
    void onError(Exception e);
}

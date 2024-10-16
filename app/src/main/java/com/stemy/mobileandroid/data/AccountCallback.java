package com.stemy.mobileandroid.data;

import com.stemy.mobileandroid.data.model.AccountUser;

public interface AccountCallback {
    void onSuccess(AccountUser user);
    void onError(Exception e);
}

package com.stemy.mobileandroid.data;

import com.stemy.mobileandroid.data.model.AccountUser;

import java.util.List;

public interface ListCallbackGeneric<T>  {
    void onSuccess(List<T> list);
    void onError(Exception e);



}

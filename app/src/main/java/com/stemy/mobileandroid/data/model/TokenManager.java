package com.stemy.mobileandroid.data.model;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.qualifiers.ApplicationContext;

@Singleton
public class TokenManager {
    private static final String PREF_CURRENT_USER = "current_user_pref";
    public static final String TOKEN_KEY = "auth_token";
    public SharedPreferences sharedPreferences;

    @Inject
    public TokenManager(@ApplicationContext Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_CURRENT_USER, Context.MODE_PRIVATE);
    }

    public void saveAccessToken(String accessToken) {
        if(accessToken != null){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(TOKEN_KEY, accessToken);
            editor.apply();
        }
    }

    public String getToken() {
        return sharedPreferences.getString(TOKEN_KEY, null);
    }

    public void clearToken() {
        if(getToken() != null){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(TOKEN_KEY);
            editor.apply();
        }
    }
}



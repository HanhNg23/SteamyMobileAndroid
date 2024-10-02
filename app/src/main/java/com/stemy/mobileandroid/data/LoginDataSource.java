package com.stemy.mobileandroid.data;
import android.annotation.SuppressLint;
import android.util.Log;

import com.apollographql.apollo.api.ApolloResponse;
import com.apollographql.java.client.ApolloCall;
import com.apollographql.java.rx3.Rx3Apollo;
import com.stemy.mobileandroid.LoginMutation;
import com.stemy.mobileandroid.MeQuery;
import com.stemy.mobileandroid.apiclient.GraphQLClient;
import com.stemy.mobileandroid.data.model.TokenManager;
import com.stemy.mobileandroid.data.model.LoggedInUser;
import java.io.IOException;
import javax.inject.Inject;
import javax.inject.Singleton;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import  io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

@Singleton
public class LoginDataSource {

    private TokenManager tokenManager;
    private GraphQLClient graphQLClient;

    @Inject
    public LoginDataSource(TokenManager tokenManager, GraphQLClient graphQLClient){
        this.tokenManager = tokenManager;
        this.graphQLClient = graphQLClient;
    }


    @SuppressLint("CheckResult")
    public Single<Result<LoggedInUser>> loginAsync(String username, String password){
        return Single.create(emitter -> {
            try {
                LoginMutation loginMutation = new LoginMutation(username, password);
                ApolloCall<LoginMutation.Data> mutationCall = (ApolloCall<LoginMutation.Data>) graphQLClient.getClient().mutation(loginMutation);
                Single<ApolloResponse<LoginMutation.Data>> mutationResponse = Rx3Apollo.single(mutationCall);
                mutationResponse.subscribeOn(Schedulers.io());
                mutationResponse.subscribe(response -> {
                    if (response.data != null && response.data.login != null) {
                        String accessToken = response.data.login.access_token;
                        if (accessToken != null) {
                            tokenManager.saveAccessToken(accessToken);
                            LoggedInUser loggedInUser = new LoggedInUser();
                            loggedInUser.setAccessToken(accessToken);
                            loggedInUser.setAuthenticated(true);
                            emitter.onSuccess(new Result.Success<>(loggedInUser));
                            return;
                        }
                    }
                    emitter.onError(new IOException("Login failed: Invalid response data"));
                }, throwable -> {
                    emitter.onError(new IOException("Error logging in", throwable));
                });
            }catch (Exception e){
                emitter.onError(new IOException("Login failed", e));
            }
        });
    }

    public void logout() {
       tokenManager.clearToken();
    }

    public Single<LoggedInUser> getCurrentLoggedInUser() {
        String accessToken = tokenManager.getToken();
        if (accessToken == null) {
            return Single.error(new IOException("No access token found"));
        }

        MeQuery meQuery = new MeQuery();
        ApolloCall<MeQuery.Data> meQueryCall = graphQLClient.getClient().query(meQuery);
        Single<ApolloResponse<MeQuery.Data>> meQueryResponse = Rx3Apollo.single(meQueryCall);

        return meQueryResponse.map(response -> {
            if (response.data != null && response.data.me != null) {
                // Tạo đối tượng LoggedInUser và điền thông tin từ MeQuery
                LoggedInUser user = new LoggedInUser();
                user.setAuthenticated(true);
                user.setAccessToken(accessToken);
                user.setUserId(response.data.me.id);
                user.setRole(response.data.me.role);
                user.setUserMail(response.data.me.email);
                user.setPhone(response.data.me.phone);
                user.setStatus(response.data.me.status);
                user.setFullName(response.data.me.fullName);
                return user;
            }
            throw new IOException("Faild to retriev user data");
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }
}

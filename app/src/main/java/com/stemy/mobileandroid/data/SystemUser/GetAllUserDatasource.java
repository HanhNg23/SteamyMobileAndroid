package com.stemy.mobileandroid.data.SystemUser;

import android.annotation.SuppressLint;
import android.util.Log;
import com.apollographql.apollo.api.ApolloResponse;
import com.apollographql.java.client.ApolloCall;
import com.apollographql.java.rx3.Rx3Apollo;
import com.stemy.mobileandroid.UsersQuery;
import com.stemy.mobileandroid.apiclient.graphql.GraphQLClient;
import com.stemy.mobileandroid.data.model.AccountUser;
import com.stemy.mobileandroid.data.model.Result;
import com.stemy.mobileandroid.data.model.TokenManager;
import com.stemy.mobileandroid.type.Role;
import com.stemy.mobileandroid.type.UserStatus;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

@Singleton
public class GetAllUserDatasource {
    private TokenManager tokenManager;
    private GraphQLClient graphQLClient;
    private List<AccountUser> accountUsers;
        @Inject
        public GetAllUserDatasource(TokenManager tokenManager, GraphQLClient graphQLClient){
            this.tokenManager = tokenManager;
            this.graphQLClient = graphQLClient;
        }

    @SuppressLint("CheckResult")
    public Single<Result<List<AccountUser>>> getAllSystemUsers(){
            return Single.create(emitter -> {
                try {
                    UsersQuery usersQuery = new UsersQuery();
                    ApolloCall<UsersQuery.Data> usersQueryCall = graphQLClient.getClient().query(usersQuery);
                    Single<ApolloResponse<UsersQuery.Data>> usersQueryResponse = Rx3Apollo.single(usersQueryCall);
                    usersQueryResponse.subscribeOn(Schedulers.io());
                    usersQueryResponse.subscribe(response -> {
                        if (response.data != null && response.data.users != null) {
                            if (response.data.users.isEmpty()) {
                                emitter.onSuccess(new Result.Success<>(List.of()));
                            }
                            accountUsers = response.data.users.parallelStream().map(user -> AccountUser.builder()
                                    .userId(user.id)
                                    .userMail(user.email)
                                    .fullName(user.fullName)
                                    .role(Role.safeValueOf(user.role.rawValue))  // Assuming Role is an enum or a class
                                    .status(UserStatus.safeValueOf(user.status.rawValue))  // Assuming UserStatus is an enum or a class
                                    .phone(user.phone)
                                    .address(user.address)
                                    .avatar(user.avatar)
                                    .build()).collect(Collectors.toList());
                            emitter.onSuccess(new Result.Success<>(accountUsers));
                            return;
                        }
                        emitter.onError(new IOException("Get All User Failed: Invalid response data"));

                    }, throwable -> {
                        emitter.onError(new IOException("Error logging in", throwable));
                    });
                } catch (Exception e) {
                    Log.e("GetAllUserDatasource", "Exception while getting all system users: ", e);
                    emitter.onError(new IOException("Error getting all system users", e));
                }
            });
    }
}

package com.stemy.mobileandroid.data.Register;

import android.annotation.SuppressLint;
import android.util.Log;

import com.apollographql.apollo.api.ApolloResponse;
import com.apollographql.java.client.ApolloCall;
import com.apollographql.java.rx3.Rx3Apollo;
import com.stemy.mobileandroid.RegisterMutation;
import com.stemy.mobileandroid.apiclient.graphql.GraphQLClient;
import com.stemy.mobileandroid.apiclient.grpc.AccountRegisterGrpcClient;
import com.stemy.mobileandroid.data.model.Result;
import com.stemy.mobileandroid.data.model.AccountUser;
import com.stemy.mobileandroid.proto.AccountRegisterResponse;

import java.io.IOException;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

@Singleton
public class RegisterDatasource {
    private final GraphQLClient graphQLClient;

    @Inject
    public RegisterDatasource(GraphQLClient graphQLClient){
        this.graphQLClient = graphQLClient;
    }


    @SuppressLint("CheckResult")
    public Single<Result<AccountUser>> registerAsync(AccountUser newUser){
        return Single.create(emitter -> {
            try{
            RegisterMutation registerMutation = new RegisterMutation(
                                                newUser.getUserMail().toLowerCase(Locale.ROOT),
                                                newUser.getFullName(),
                                                newUser.getPassword(),
                                                newUser.getPhone());
            ApolloCall<RegisterMutation.Data> mutationCall = (ApolloCall<RegisterMutation.Data>) graphQLClient.getClient().mutation(registerMutation);
            Single<ApolloResponse<RegisterMutation.Data>> mutationResponse = Rx3Apollo.single(mutationCall);
            mutationResponse.subscribeOn(Schedulers.io());
            mutationResponse.subscribe(response -> {
                if(response.data != null && response.data.register != null){
                    String accessTokenOfStaff = response.data.register.access_token;
                    if(accessTokenOfStaff != null){
                        Log.d( "RegisterDatasource","Register succesfully " + newUser.getFullName());
                        newUser.setAccessToken(accessTokenOfStaff);
                        newUser.setAuthenticated(false);
                        emitter.onSuccess(new Result.Success<>(newUser));
                        return;
                    }
                }
                emitter.onError(new IOException("Register Failed: invalid response data"));
            }, throwable -> {
                emitter.onError(new IOException("Error register ", throwable));
            });
            }catch (Exception e){
                emitter.onError(new IOException("Register failed", e));
            }
        });
    }

    public Single<Result<AccountUser>> updateRegisterUser(AccountUser accountUser) {
        //Single.create() - creates a Single that emits a single item (either success or error) and allows the operation to be performed asynchronously
        return Single.create(emitter -> {
            Log.d("RegisterDatasource", "Updating staff role and status");
            // Initialize your gRPC client
            AccountRegisterGrpcClient accountRegisterGrpcClient = new AccountRegisterGrpcClient();

            try {



                // Attempt to call the gRPC client to register the user
                AccountRegisterResponse response = accountRegisterGrpcClient.registerUser(accountUser);

                // Emit success or failure
                if (response.getSuccess()) {
                    Log.d("RegisterDatasource", "Register successful: " + response.getMessage());
                    // Emit the success result
                    emitter.onSuccess(new Result.Success<AccountUser>(accountUser));
                } else {
                    Log.e("RegisterDatasource", "Register failed: " + response.getMessage());
                    // Emit an error if the registration failed
                    emitter.onSuccess(new Result.Error(new IOException("Update staff role status failed: " + response.getMessage())));
                }
                accountRegisterGrpcClient.shutdown();
            } catch (Exception e) {
                // Handle any exceptions that occurred during the gRPC call
                Log.e("RegisterDatasource", "Exception occurred while updating staff role status: ", e);
                // Emit the error
                emitter.onError(new IOException("Error updating staff role status", e));
            }finally {
                accountRegisterGrpcClient.shutdown();
            }
        });
    }





}




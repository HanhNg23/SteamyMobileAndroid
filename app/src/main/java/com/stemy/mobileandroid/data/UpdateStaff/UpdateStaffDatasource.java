package com.stemy.mobileandroid.data.UpdateStaff;

import android.util.Log;

import com.stemy.mobileandroid.apiclient.graphql.GraphQLClient;
import com.stemy.mobileandroid.apiclient.grpc.AccountUpdateStaffGrpcClient;
import com.stemy.mobileandroid.data.model.AccountUser;
import com.stemy.mobileandroid.data.model.Result;
import com.stemy.mobileandroid.proto.AccountToUpdateResponse;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.rxjava3.core.Single;

@Singleton
public class UpdateStaffDatasource {
    private final GraphQLClient graphQLClient;
    private String theClassName = this.getClass().getName().toString();
    @Inject
    public UpdateStaffDatasource(GraphQLClient graphQLClient){
        this.graphQLClient = graphQLClient;
    }

    public Single<Result<AccountUser>> updateUserStaff(AccountUser accountUser){
        return Single.create(emitter -> {
            Log.d(theClassName, "Update the staff");
            AccountUpdateStaffGrpcClient accountUpdateStaffGrpcClient = new AccountUpdateStaffGrpcClient();
            try{
                AccountToUpdateResponse response = accountUpdateStaffGrpcClient.updateStaff(accountUser);

                if(response.getSuccess()){
                    Log.d(theClassName, "Update staff success " + response.getMessage());
                    emitter.onSuccess(new Result.Success<AccountUser>(accountUser));
                }else{
                    Log.e(theClassName, "Update failed: " + response.getMessage());
                }

            }catch (Exception e){
                // Handle any exceptions that occurred during the gRPC call
                Log.e(theClassName, "Exception occurred while updating staff: ", e);
                // Emit the error
                emitter.onError(new IOException("Error updating staff", e));
            }finally {
                accountUpdateStaffGrpcClient.shutdown();
            }

        });
    }



}

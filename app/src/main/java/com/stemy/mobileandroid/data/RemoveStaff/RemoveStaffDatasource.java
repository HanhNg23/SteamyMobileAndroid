package com.stemy.mobileandroid.data.RemoveStaff;

import android.util.Log;

import com.stemy.mobileandroid.apiclient.graphql.GraphQLClient;
import com.stemy.mobileandroid.apiclient.grpc.AccountRemoveStaffGrpcClient;
import com.stemy.mobileandroid.data.model.Result;
import com.stemy.mobileandroid.proto.AccountToDeleteResponse;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.rxjava3.core.Single;

@Singleton
public class RemoveStaffDatasource {
    private String theClassName = this.getClass().getName().toString();
    private final GraphQLClient graphQLClient;

    @Inject
    public RemoveStaffDatasource(GraphQLClient graphQLClient){
        this.graphQLClient = graphQLClient;
    }
    public Single<Result<Boolean>> removeSaff(int userId){
        return Single.create(emitter -> {
            Log.d(theClassName, "Remove the staff");
            AccountRemoveStaffGrpcClient accountRemoveStaffGrpcClient = new AccountRemoveStaffGrpcClient();

            try{

                AccountToDeleteResponse response = accountRemoveStaffGrpcClient.removeById(userId);

                if(response.getSuccess()){
                    Log.d(theClassName, "Delete staff success " + response.getMessage());
                    emitter.onSuccess(new Result.Success<Boolean>(true));
                }else{
                    Log.e(theClassName, "Delete staff failed: " + response.getMessage());
                }

            }catch (Exception e){
                // Handle any exceptions that occurred during the gRPC call
                Log.e(theClassName, "Exception occurred while deleting staff: ", e);
                // Emit the error
                emitter.onError(new IOException("Error deleting staff", e));
            }finally {
                accountRemoveStaffGrpcClient.shutdown();
            }

        });
    }
}

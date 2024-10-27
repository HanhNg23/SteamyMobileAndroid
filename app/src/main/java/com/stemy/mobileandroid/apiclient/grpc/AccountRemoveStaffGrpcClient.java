package com.stemy.mobileandroid.apiclient.grpc;

import android.util.Log;
import com.stemy.mobileandroid.costants.StemyGrpcServiceNetworkConfig;
import com.stemy.mobileandroid.proto.AccountStaffGrpc;
import com.stemy.mobileandroid.proto.AccountToDeleteRequest;
import com.stemy.mobileandroid.proto.AccountToDeleteResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class AccountRemoveStaffGrpcClient {
    private final ManagedChannel channel;
    private final AccountStaffGrpc.AccountStaffBlockingStub stub;

    public AccountRemoveStaffGrpcClient(){
        this.channel = ManagedChannelBuilder.forAddress(StemyGrpcServiceNetworkConfig.STEMY_GRPC_SERVICE_HOST, StemyGrpcServiceNetworkConfig.STEMY_GRPC_SERVICE_PORT)
                .usePlaintext()
                .build();
        this.stub = AccountStaffGrpc.newBlockingStub(channel);
    }

    public AccountToDeleteResponse removeById(int userId){
        Log.d("AccountRegisterGrpcClient", "Starting Delete via grpc client");
        AccountToDeleteRequest request = AccountToDeleteRequest.newBuilder()
                .setId(userId)
                .build();
        AccountToDeleteResponse response = stub.removeStaff(request);
        return response;
    }

    public void shutdown(){
        if(channel != null){
            channel.shutdown();
        }
    }

}

package com.stemy.mobileandroid.apiclient.grpc;

import android.util.Log;
import com.stemy.mobileandroid.costants.StemyGrpcServiceNetworkConfig;
import com.stemy.mobileandroid.data.model.AccountUser;
import com.stemy.mobileandroid.proto.AccountRegisterRequest;
import com.stemy.mobileandroid.proto.AccountRegisterResponse;
import com.stemy.mobileandroid.proto.AccountStaffGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class AccountRegisterGrpcClient {
    private final ManagedChannel channel;
    private final AccountStaffGrpc.AccountStaffBlockingStub stub;

    public AccountRegisterGrpcClient(){
        this.channel = ManagedChannelBuilder.forAddress(StemyGrpcServiceNetworkConfig.STEMY_GRPC_SERVICE_HOST, StemyGrpcServiceNetworkConfig.STEMY_GRPC_SERVICE_PORT)
                .usePlaintext()
                .build();
        this.stub = AccountStaffGrpc.newBlockingStub(channel);
    }

    public AccountRegisterResponse registerUser(AccountUser newUser){
        Log.d("AccountRegisterGrpcClient", "Starting Update user role and status via grpc client");
        AccountRegisterRequest request = AccountRegisterRequest.newBuilder()
                .setEmail(newUser.getUserMail())
                .setRole(AccountRegisterRequest.RoleType.valueOf(newUser.getRole().rawValue))
                .setStatus(AccountRegisterRequest.StatusType.valueOf(newUser.getStatus().rawValue))
                .setPhone(newUser.getPhone())
                .setFullname(newUser.getFullName())
                .setAddress(newUser.getAddress())
                .setAvatar(newUser.getAvatar())
                .build();
        AccountRegisterResponse response = stub.register(request);
        return response;
    }

    public void shutdown(){
        if(channel != null){
            channel.shutdown();
        }
    }
}

package com.stemy.mobileandroid.apiclient.grpc;

import android.util.Log;

import com.stemy.mobileandroid.costants.StemyGrpcServiceNetworkConfig;
import com.stemy.mobileandroid.data.model.AccountUser;
import com.stemy.mobileandroid.proto.AccountStaffGrpc;
import com.stemy.mobileandroid.proto.AccountToUpdateRequest;
import com.stemy.mobileandroid.proto.AccountToUpdateResponse;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class AccountUpdateStaffGrpcClient {
    private final ManagedChannel channel;
    private final AccountStaffGrpc.AccountStaffBlockingStub stub;

    public AccountUpdateStaffGrpcClient(){
        this.channel = ManagedChannelBuilder.forAddress(StemyGrpcServiceNetworkConfig.STEMY_GRPC_SERVICE_HOST, StemyGrpcServiceNetworkConfig.STEMY_GRPC_SERVICE_PORT)
                .usePlaintext()
                .build();
        this.stub = AccountStaffGrpc.newBlockingStub(channel);
    }

    public AccountToUpdateResponse updateStaff(AccountUser newUser){
        Log.d("AccountRegisterGrpcClient", "Starting Update user role and status via grpc client");
        AccountToUpdateRequest request = AccountToUpdateRequest.newBuilder()
                .setId(newUser.getUserId())
                .setEmail(newUser.getUserMail())
                .setRole(AccountToUpdateRequest.RoleType.valueOf(newUser.getRole().rawValue))
                .setStatus(AccountToUpdateRequest.StatusType.valueOf(newUser.getStatus().rawValue))
                .setPhone(newUser.getPhone())
                .setFullname(newUser.getFullName())
                .setAddress(newUser.getAddress())
                .setAvatar(newUser.getAvatar())
                .build();
        AccountToUpdateResponse response = stub.updateStaff(request);
        return response;
    }

    public void shutdown(){
        if(channel != null){
            channel.shutdown();
        }
    }

}

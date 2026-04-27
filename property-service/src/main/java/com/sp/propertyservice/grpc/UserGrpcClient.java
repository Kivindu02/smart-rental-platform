package com.sp.propertyservice.grpc;

import com.sp.user.UserRequest;
import com.sp.user.UserResponse;
import com.sp.user.UserServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class UserGrpcClient {

    @GrpcClient("auth-service")
    private UserServiceGrpc.UserServiceBlockingStub userServiceStub;

    public UserResponse getUserById(String userId) {
        UserRequest request = UserRequest.newBuilder()
                .setUserId(userId)
                .build();

        return userServiceStub.getUserById(request);
    }

}

package com.sp.authservice.grpc;

import com.sp.authservice.model.User;
import com.sp.authservice.repository.UserRepository;
import com.sp.user.UserRequest;
import com.sp.user.UserResponse;
import com.sp.user.UserServiceGrpc;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.UUID;

@GrpcService
public class UserGrpcService extends UserServiceGrpc.UserServiceImplBase {

    private final UserRepository userRepository;

    public UserGrpcService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void getUserById(UserRequest request,
                            StreamObserver<UserResponse> responseObserver) {
        try {
            User user = userRepository.findById(UUID.fromString(request.getUserId()))
                    .orElseThrow(() -> new RuntimeException("User not found"));

            UserResponse response = UserResponse.newBuilder()
                    .setId(user.getId().toString())
                    .setFirstName(user.getFirstName())
                    .setLastName(user.getLastName())
                    .setEmail(user.getEmail())
                    .setPhoneNo(user.getPhoneNo())
                    .setRole(user.getRole().name())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {
            responseObserver.onError(
                    Status.NOT_FOUND
                            .withDescription("User not found")
                            .asRuntimeException()
            );

        }
    }
}

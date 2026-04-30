package com.sp.propertyservice.grpc;

import com.sp.property.PropertyRequest;
import com.sp.property.PropertyResponse;
import com.sp.property.PropertyServiceGrpc;
import com.sp.propertyservice.repository.PropertyRepository;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.UUID;

@GrpcService
public class PropertyGrpcService extends PropertyServiceGrpc.PropertyServiceImplBase {

    private final PropertyRepository propertyRepository;

    public PropertyGrpcService(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    @Override
    public void existsById(PropertyRequest request,
                           StreamObserver<PropertyResponse> responseObserver) {


        try {
            UUID propertyId = UUID.fromString(request.getPropertyId());
            boolean exists = propertyRepository.existsById(propertyId);

            PropertyResponse response = PropertyResponse.newBuilder()
                    .setExists(exists)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        }catch (IllegalArgumentException e) {
            responseObserver.onError(
                Status.INVALID_ARGUMENT
                    .withDescription("Invalid property ID format")
                    .withCause(e)
                    .asRuntimeException()
            );

        }catch (Exception e) {
            responseObserver.onError(
                Status.INTERNAL
                    .withDescription("Internal server error")
                    .withCause(e)
                    .asRuntimeException()
            );

        }


    }
}

package com.sp.reviewservice.grpc;

import com.sp.property.PropertyRequest;
import com.sp.property.PropertyResponse;
import com.sp.property.PropertyServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class PropertyGrpcClient {

    @GrpcClient("property-service")
    private PropertyServiceGrpc.PropertyServiceBlockingStub propertyServiceStub;

    public boolean propertyExists(String propertyId) {
        PropertyRequest request = PropertyRequest.newBuilder()
                .setPropertyId(propertyId)
                .build();

        PropertyResponse response = propertyServiceStub.existsById(request);

        return response.getExists();
    }
}

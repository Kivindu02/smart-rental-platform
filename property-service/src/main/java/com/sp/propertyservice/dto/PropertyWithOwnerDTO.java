package com.sp.propertyservice.dto;

import com.sp.propertyservice.model.Property;
import com.sp.user.UserResponse;

import java.util.List;
import java.util.UUID;

public class PropertyWithOwnerDTO {

    private final UUID id;
    private final String name;
    private final String address;
    private final String type;
    private final Double price;
    private final String description;
    private final List<String> imageUrls;
    private final UserDTO owner;

    public PropertyWithOwnerDTO(Property property, UserResponse owner) {
        this.id = property.getId();
        this.name = property.getName();
        this.address = property.getAddress();
        this.type = property.getType();
        this.price = property.getPrice();
        this.description = property.getDescription();
        this.imageUrls = property.getImageUrls();
        this.owner = new UserDTO(
                owner.getId(),
                owner.getFirstName(),
                owner.getLastName(),
                owner.getEmail(),
                owner.getPhoneNo()
        );
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getType() {
        return type;
    }

    public Double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public UserDTO getOwner() {
        return owner;
    }
}

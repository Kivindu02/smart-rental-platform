package com.sp.propertyservice.mapper;

import com.sp.propertyservice.dto.PropertyRequestDTO;
import com.sp.propertyservice.model.Property;

public class PropertyMapper {

    public static Property  toModel(PropertyRequestDTO propertyRequestDTO){
        Property property = new Property();
        property.setImageUrls(propertyRequestDTO.getImageUrls());
        property.setName(propertyRequestDTO.getName());
        property.setAddress(propertyRequestDTO.getAddress());
        property.setType(propertyRequestDTO.getType());
        property.setPrice(Double.parseDouble(propertyRequestDTO.getPrice()));
        property.setDescription(propertyRequestDTO.getDescription());
        return property;
    }
}

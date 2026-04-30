package com.sp.reviewservice.dto;

public class UserDTO {

    private final String id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String phoneNo;

    public UserDTO(String id, String firstName, String lastName, String email, String phoneNo) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNo = phoneNo;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }
}

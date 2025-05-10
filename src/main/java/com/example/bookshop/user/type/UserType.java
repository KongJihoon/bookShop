package com.example.bookshop.user.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserType {


    USER("ROLE_USER"),
    SELLER("ROLE_SELLER"),;


    private final String description;
}

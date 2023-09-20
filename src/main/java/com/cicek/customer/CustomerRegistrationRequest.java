package com.cicek.customer;

public record CustomerRegistrationRequest(
        String name,
        String email,
        Integer age
) {
}

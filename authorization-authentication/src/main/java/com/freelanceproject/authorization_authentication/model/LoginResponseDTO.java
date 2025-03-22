package com.freelanceproject.authorization_authentication.model;


public record LoginResponseDTO(String username, String email, String firstName, String lastName, String account_type, Role role) {
}
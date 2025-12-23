package org.example.resource;

import org.example.entity.UserEntity;
import org.example.enums.AuthResult;

public record AuthResultWithUser(AuthResult authResult, UserEntity user, String token) {

}
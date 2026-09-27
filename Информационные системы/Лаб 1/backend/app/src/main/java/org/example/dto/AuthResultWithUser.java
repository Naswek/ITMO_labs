package org.example.dto;

import org.example.enums.AuthResult;

public record AuthResultWithUser(AuthResult authResult, Long userId, String login) {

}

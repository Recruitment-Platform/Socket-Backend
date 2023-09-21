package com.project.socket.security.oauth2.handler;

public record LoginSuccessResponse(
    boolean profileSetup,
    String accessToken,
    String refreshToken) { }

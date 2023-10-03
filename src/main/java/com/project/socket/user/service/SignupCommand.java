package com.project.socket.user.service;

public record SignupCommand(Long userId, String nickname, String githubLink) {

}

package com.project.socket.user.service.usecase;

import com.project.socket.user.model.User;
import com.project.socket.user.service.SignupCommand;

public interface SignupUseCase {

  User signup(SignupCommand signupCommand);
}

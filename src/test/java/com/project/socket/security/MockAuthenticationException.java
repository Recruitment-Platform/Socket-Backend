package com.project.socket.security;

import org.springframework.security.core.AuthenticationException;

public class MockAuthenticationException extends AuthenticationException {

  public MockAuthenticationException(String msg) {
    super(msg);
  }
}

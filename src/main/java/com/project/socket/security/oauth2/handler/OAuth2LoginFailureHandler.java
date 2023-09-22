package com.project.socket.security.oauth2.handler;

import static com.project.socket.common.error.ErrorCode.OAUTH2_LOGIN_FAIL;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.socket.common.error.ProblemDetailFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuth2LoginFailureHandler implements AuthenticationFailureHandler {

  private final ObjectMapper objectMapper;

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException exception) throws IOException, ServletException {
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType(APPLICATION_PROBLEM_JSON_VALUE);
    objectMapper.writeValue(
        response.getOutputStream(),
        ProblemDetailFactory.of(OAUTH2_LOGIN_FAIL, request.getRequestURI()));
  }
}

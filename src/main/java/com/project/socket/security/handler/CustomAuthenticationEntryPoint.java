package com.project.socket.security.handler;

import static com.project.socket.common.error.ErrorCode.AUTHENTICATION_ENTRY_POINT;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.socket.common.error.ErrorCode;
import com.project.socket.common.error.ProblemDetailFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private final ObjectMapper objectMapper;

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException, ServletException {
    setResponse(request, response);
  }

  private ErrorCode getErrorCode(HttpServletRequest request) {
    return (ErrorCode) request.getAttribute("exception");
  }

  private void setResponse(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    ErrorCode errorCode = getErrorCode(request);
    response.setContentType(APPLICATION_PROBLEM_JSON_VALUE);
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

    if (Objects.isNull(errorCode)) {
      objectMapper.writeValue(
          response.getOutputStream(),
          ProblemDetailFactory.of(AUTHENTICATION_ENTRY_POINT, request.getRequestURI()));
    } else {
      objectMapper.writeValue(
          response.getOutputStream(),
          ProblemDetailFactory.of(errorCode, request.getRequestURI()));
    }
  }
}

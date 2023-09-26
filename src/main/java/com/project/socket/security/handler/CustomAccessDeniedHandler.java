package com.project.socket.security.handler;

import static com.project.socket.common.error.ErrorCode.HANDLE_ACCESS_DENIED;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.socket.common.error.ProblemDetailFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

  private final ObjectMapper objectMapper;

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException accessDeniedException) throws IOException, ServletException {
    setResponse(request, response);
  }

  private void setResponse(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    response.setContentType(APPLICATION_PROBLEM_JSON_VALUE);
    response.setStatus(HttpServletResponse.SC_FORBIDDEN);

    objectMapper.writeValue(
        response.getOutputStream(),
        ProblemDetailFactory.of(HANDLE_ACCESS_DENIED, request.getRequestURI()));
  }
}

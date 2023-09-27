package com.project.socket.security.handler;

import static com.project.socket.common.error.ErrorCode.HANDLE_ACCESS_DENIED;
import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.nio.charset.Charset;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.access.AccessDeniedException;

@DisplayNameGeneration(ReplaceUnderscores.class)
class CustomAccessDeniedHandlerTest {

  CustomAccessDeniedHandler customAccessDeniedHandler =
      new CustomAccessDeniedHandler(new ObjectMapper());

  MockHttpServletRequest request = new MockHttpServletRequest();
  MockHttpServletResponse response = new MockHttpServletResponse();

  @Test
  void HANDLE_ACCESS_DENIED_403_응답을_한다() throws ServletException, IOException {
    customAccessDeniedHandler.handle(request, response, new AccessDeniedException("denied"));

    String content = response.getContentAsString(Charset.defaultCharset());

    assertThat(content).contains(HANDLE_ACCESS_DENIED.getCode());
  }
}
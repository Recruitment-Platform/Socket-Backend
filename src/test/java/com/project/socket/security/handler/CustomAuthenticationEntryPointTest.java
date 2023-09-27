package com.project.socket.security.handler;

import static com.project.socket.common.error.ErrorCode.AUTHENTICATION_ENTRY_POINT;
import static com.project.socket.common.error.ErrorCode.INVALID_JWT;
import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.socket.security.MockAuthenticationException;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.nio.charset.Charset;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

@DisplayNameGeneration(ReplaceUnderscores.class)
class CustomAuthenticationEntryPointTest {

  CustomAuthenticationEntryPoint customAuthenticationEntryPoint =
      new CustomAuthenticationEntryPoint(new ObjectMapper());


  MockHttpServletRequest request = new MockHttpServletRequest();
  MockHttpServletResponse response = new MockHttpServletResponse();

  @AfterEach
  void reset() {
    request.removeAttribute("exception");
    response.reset();
  }

  @Test
  void request에_exception_attribute가_없으면_AUTHENTICATION_ENTRY_POINT_에러_응답을_한다()
      throws ServletException, IOException {
    customAuthenticationEntryPoint.commence(
        request, response, new MockAuthenticationException("error"));

    String content = response.getContentAsString(Charset.defaultCharset());

    assertThat(content).contains(AUTHENTICATION_ENTRY_POINT.getCode());
  }

  @Test
  void request에_exception_attribute가_존재하면_해당_에러_응답을_한다()
      throws ServletException, IOException {
    request.setAttribute("exception", INVALID_JWT);

    customAuthenticationEntryPoint.commence(
        request, response, new MockAuthenticationException("error"));

    String content = response.getContentAsString(Charset.defaultCharset());

    assertThat(content).contains(INVALID_JWT.getCode());
  }

}
package com.project.socket.security.oauth2.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

@DisplayNameGeneration(ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class OAuth2LoginFailureHandlerTest {

  MockHttpServletRequest request;
  MockHttpServletResponse response;
  @InjectMocks
  OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
  @Mock
  ObjectMapper objectMapper;

  @BeforeEach
  void init() {
    request = new MockHttpServletRequest();
    response = new MockHttpServletResponse();
  }

  @Test
  void oauth2_로그인에_실패하면_401응답을_한다() throws ServletException, IOException {
    oAuth2LoginFailureHandler.onAuthenticationFailure(
        request, response, new OAuth2AuthenticationException("401"));
    assertAll(
        () -> assertThat(response.getStatus()).isEqualTo(401),
        () -> assertThat(response.getContentType()).isEqualTo(APPLICATION_PROBLEM_JSON_VALUE)
    );
  }
}
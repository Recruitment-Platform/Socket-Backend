package com.project.socket.security.oauth2.handler;

import static jakarta.servlet.http.HttpServletResponse.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.socket.security.JwtProvider;
import com.project.socket.user.model.User;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

@DisplayNameGeneration(ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class OAuth2LoginSuccessHandlerTest {
//
//  MockHttpServletRequest request;
//  MockHttpServletResponse response;
//
//  @InjectMocks
//  OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
//
//  @Mock
//  Function<User, User> userRepositoryOAuth2UserHandler;
//
//  @Mock
//  JwtProvider jwtProvider;
//
//  @Mock
//  ObjectMapper objectMapper;
//
//
//  @BeforeEach
//  void init() {
//    request = new MockHttpServletRequest();
//    response = new MockHttpServletResponse();
//  }
//
//  @Test
//  void OAuth2_로그인이_성공하면_LoginSuccessResponse_응답을_한다() throws ServletException, IOException {
//    try (MockedStatic<UserFactory> mockUserFactory = mockStatic(UserFactory.class)) {
//      mockUserFactory
//          .when(() -> UserFactory.of(anyString(), anyMap()))
//          .thenReturn(User.builder().build());
//
//      when(userRepositoryOAuth2UserHandler.apply(any())).thenReturn(User.builder().build());
//      when(jwtProvider.createAccessToken(any())).thenReturn("accessToken");
//      when(jwtProvider.createRefreshToken(any())).thenReturn("refreshToken");
//
//      oAuth2LoginSuccessHandler.onAuthenticationSuccess(request, response,
//          createAuthentication());
//
//      assertAll(
//          () -> assertThat(response.getStatus()).isEqualTo(SC_OK),
//          () -> assertThat(response.getContentType()).isEqualTo(APPLICATION_JSON_VALUE),
//          () -> verify(objectMapper).writeValue(eq(response.getOutputStream()),
//              any(LoginSuccessResponse.class))
//      );
//    }
//  }
//
//  OAuth2AuthenticationToken createAuthentication() {
//    List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("role"));
//    OAuth2User oAuth2User = new DefaultOAuth2User(authorities, Map.of("id", "test"), "id");
//    return new OAuth2AuthenticationToken(oAuth2User, authorities, "provider");
//  }
}
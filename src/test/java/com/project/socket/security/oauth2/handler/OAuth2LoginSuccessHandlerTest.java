package com.project.socket.security.oauth2.handler;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import com.project.socket.security.CookieUtils;
import com.project.socket.security.JwtProvider;
import com.project.socket.security.exception.RedirectBadRequestException;
import com.project.socket.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.project.socket.user.model.User;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.UnaryOperator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockCookie;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

@DisplayNameGeneration(ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class OAuth2LoginSuccessHandlerTest {

  MockHttpServletRequest request;
  MockHttpServletResponse response;

  @InjectMocks
  OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

  @Mock
  UnaryOperator<User> userRepositoryOAuth2UserHandler;

  @Mock
  JwtProvider jwtProvider;

  @Mock
  HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;


  @BeforeEach
  void init() {
    request = new MockHttpServletRequest();
    response = new MockHttpServletResponse();
  }

  @Test
  void OAuth2_로그인이_성공하면_redirect_한다() {
    try (MockedStatic<UserFactory> mockUserFactory = mockStatic(UserFactory.class);
        MockedStatic<CookieUtils> mockCookieUtils = mockStatic(CookieUtils.class)) {
      mockUserFactory
          .when(() -> UserFactory.of(anyString(), anyMap()))
          .thenReturn(User.builder().build());

      mockCookieUtils
          .when(() -> CookieUtils.getCookie(any(), any()))
          .thenReturn(Optional.of(new MockCookie("test", "test")));

      when(userRepositoryOAuth2UserHandler.apply(any())).thenReturn(User.builder().build());
      doNothing().when(httpCookieOAuth2AuthorizationRequestRepository)
                 .removeAuthorizationRequestCookies(any(), any());
      when(jwtProvider.createAccessToken(any())).thenReturn("accessToken");
      when(jwtProvider.createRefreshToken(any())).thenReturn("refreshToken");

      assertThatCode(() -> oAuth2LoginSuccessHandler.onAuthenticationSuccess(request, response,
          createAuthentication())).doesNotThrowAnyException();
    }
  }

  @Test
  void redirect_url_쿠키가_없으면_RedirectBadRequestException_에외가_발생한다() {
    try (MockedStatic<UserFactory> mockUserFactory = mockStatic(UserFactory.class);
        MockedStatic<CookieUtils> mockCookieUtils = mockStatic(CookieUtils.class)
    ) {
      mockUserFactory
          .when(() -> UserFactory.of(anyString(), anyMap()))
          .thenReturn(User.builder().build());
      mockCookieUtils
          .when(() -> CookieUtils.getCookie(any(), any()))
          .thenReturn(Optional.empty());
      OAuth2AuthenticationToken authentication = createAuthentication();

      when(userRepositoryOAuth2UserHandler.apply(any())).thenReturn(User.builder().build());
      assertThatThrownBy(() -> oAuth2LoginSuccessHandler.onAuthenticationSuccess(request, response,
          authentication)).isInstanceOf(RedirectBadRequestException.class);
    }
  }

  OAuth2AuthenticationToken createAuthentication() {
    List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("role"));
    OAuth2User oAuth2User = new DefaultOAuth2User(authorities, Map.of("id", "test"), "id");
    return new OAuth2AuthenticationToken(oAuth2User, authorities, "provider");
  }
}
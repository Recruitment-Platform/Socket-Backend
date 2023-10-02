package com.project.socket.security.oauth2.handler;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mockStatic;

import com.project.socket.security.CookieUtils;
import com.project.socket.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import jakarta.servlet.http.Cookie;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockCookie;
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
  HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

  @BeforeEach
  void init() {
    request = new MockHttpServletRequest();
    response = new MockHttpServletResponse();
  }

  @ParameterizedTest
  @MethodSource("provideOptionalCookie")
  void oauth2_로그인에_실패하면_target_url로_redirect_한다(Optional<Cookie> cookie) {
    try (MockedStatic<CookieUtils> mockCookieUtils = mockStatic(CookieUtils.class)) {
      mockCookieUtils.when(() -> CookieUtils.getCookie(any(), any()))
                     .thenReturn(cookie);

      doNothing().when(httpCookieOAuth2AuthorizationRequestRepository)
                 .removeAuthorizationRequestCookies(any(), any());

      assertThatCode(() -> oAuth2LoginFailureHandler.onAuthenticationFailure(
          request, response, new OAuth2AuthenticationException("fail"))).doesNotThrowAnyException();
    }
  }

  private static Stream<Arguments> provideOptionalCookie() {
    return Stream.of(
        Arguments.of(Optional.of(new MockCookie("test", "test"))),
        Arguments.of(Optional.empty())
    );
  }
}
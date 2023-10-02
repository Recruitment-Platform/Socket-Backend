package com.project.socket.security.oauth2;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.project.socket.security.CookieUtils;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockCookie;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

@DisplayNameGeneration(ReplaceUnderscores.class)
class HttpCookieOAuth2AuthorizationRequestRepositoryTest {

  MockHttpServletRequest request = new MockHttpServletRequest();
  MockHttpServletResponse response = new MockHttpServletResponse();
  HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository
      = new HttpCookieOAuth2AuthorizationRequestRepository();

  final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_url";
  final String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request";

  @BeforeEach
  void setup() {
    request = new MockHttpServletRequest();
    response.reset();
  }

  @Test
  void request_쿠키에서_AuthorizationRequest를_가져와_역직렬화한다() {
    OAuth2AuthorizationRequest oAuth2AuthorizationRequest =
        OAuth2AuthorizationRequest.authorizationCode().clientId("clientId").authorizationUri("URI")
                                  .build();

    request.setCookies(
        new Cookie(OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME,
            CookieUtils.serialize(oAuth2AuthorizationRequest)));

    OAuth2AuthorizationRequest authorizationRequest =
        httpCookieOAuth2AuthorizationRequestRepository.loadAuthorizationRequest(request);

    assertThat(authorizationRequest.getClientId())
        .isEqualTo(oAuth2AuthorizationRequest.getClientId());
  }

  @Test
  void request에_oauth2_auth_request_쿠키가_없으면_null을_반환한다() {
    OAuth2AuthorizationRequest authorizationRequest =
        httpCookieOAuth2AuthorizationRequestRepository.loadAuthorizationRequest(request);

    assertThat(authorizationRequest).isNull();
  }

  @Test
  void authorizationRequest가_null이면_인증_쿠키를_제거한다() {
    request.setCookies(new MockCookie(REDIRECT_URI_PARAM_COOKIE_NAME, "url"));

    httpCookieOAuth2AuthorizationRequestRepository
        .saveAuthorizationRequest(null, request, response);

    assertThat(response.getCookie(REDIRECT_URI_PARAM_COOKIE_NAME).getMaxAge()).isZero();
  }

  @Test
  void redirect_url쿼리가_존재하면_response에_redirect_url_쿠키를_추가한다() {
    request.addParameter(REDIRECT_URI_PARAM_COOKIE_NAME, "url");
    OAuth2AuthorizationRequest oAuth2AuthorizationRequest =
        OAuth2AuthorizationRequest.authorizationCode().clientId("clientId").authorizationUri("URI")
                                  .build();

    httpCookieOAuth2AuthorizationRequestRepository
        .saveAuthorizationRequest(oAuth2AuthorizationRequest, request, response);

    assertThat(response.getCookie(REDIRECT_URI_PARAM_COOKIE_NAME).getMaxAge()).isNotZero();
  }

  @Test
  void redirect_url쿼리가_존재지않으면_response에_redirect_url_쿠키를_추가하지않는다() {
    OAuth2AuthorizationRequest oAuth2AuthorizationRequest =
        OAuth2AuthorizationRequest.authorizationCode().clientId("clientId").authorizationUri("URI")
                                  .build();

    httpCookieOAuth2AuthorizationRequestRepository
        .saveAuthorizationRequest(oAuth2AuthorizationRequest, request, response);

    assertThat(response.getCookie(REDIRECT_URI_PARAM_COOKIE_NAME)).isNull();
  }

  @Test
  void 인증요청_쿠키와_redirect_url_쿠키를_삭제한다() {
    request.setCookies(
        new MockCookie(REDIRECT_URI_PARAM_COOKIE_NAME, "url"),
        new MockCookie(OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME, "oauth2"));

    httpCookieOAuth2AuthorizationRequestRepository
        .removeAuthorizationRequestCookies(request, response);

    assertAll(
        () -> assertThat(response.getCookie(REDIRECT_URI_PARAM_COOKIE_NAME).getMaxAge())
            .isZero(),
        () -> assertThat(response.getCookie(OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME).getMaxAge())
            .isZero()
    );
  }

  @Test
  void removeAuthorizationRequest_테스트() {
    OAuth2AuthorizationRequest authorizationRequest =
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequest(
            request, response);

    assertThat(authorizationRequest).isNull();
  }
}
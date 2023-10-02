package com.project.socket.security;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.servlet.http.Cookie;
import java.util.Base64;
import java.util.Optional;
import org.hibernate.internal.util.SerializationHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

@DisplayNameGeneration(ReplaceUnderscores.class)
class CookieUtilsTest {

  public static final String VALID_NAME = "cookie";
  MockHttpServletRequest request = new MockHttpServletRequest();
  MockHttpServletResponse response = new MockHttpServletResponse();

  @BeforeEach
  void setup() {
    request = new MockHttpServletRequest();
    response.reset();
  }

  @Test
  void cookies가_null이면_Empty_Optional을_반환한다() {
    Optional<Cookie> cookie = CookieUtils.getCookie(request, "name");

    assertThat(cookie).isEmpty();
  }

  @Test
  void cookies가_비어있는_array면_Empty_Optional을_반환한다() {
    Optional<Cookie> cookie = CookieUtils.getCookie(new MockRequest(), "empty");

    assertThat(cookie).isEmpty();
  }

  @Test
  void name에_해당하는_cookie가_없으면_Empty_Optional을_반환한다() {
    request.setCookies(new Cookie(VALID_NAME, "cookie"));
    Optional<Cookie> cookie = CookieUtils.getCookie(request, "name");

    assertThat(cookie).isEmpty();
  }

  @Test
  void name에_해당하는_cookie가_있으면_값이_담긴_Optional을_반환한다() {
    request.setCookies(new Cookie(VALID_NAME, "cookie"));
    Optional<Cookie> cookie = CookieUtils.getCookie(request, "cookie");

    assertThat(cookie).isNotEmpty();
  }

  @Test
  void response에_cookie를_추가한다() {
    CookieUtils.addCookie(response, VALID_NAME, "cookie", 1);

    assertThat(response.getCookie(VALID_NAME)).isNotNull();
  }

  @Test
  void name에_해당하는_cookie를_삭제한다() {
    request.setCookies(new Cookie(VALID_NAME, "cookie"));
    CookieUtils.deleteCookie(request, response, VALID_NAME);

    assertThat(response.getCookie(VALID_NAME).getMaxAge()).isZero();
  }

  @Test
  void name에_해당하는_cookie가_없으면_response에_삭제된_쿠키정보가_존재하지_않는다() {
    CookieUtils.deleteCookie(request, response, VALID_NAME);

    assertThat(response.getCookie(VALID_NAME)).isNull();
  }

  @Test
  void url을_직렬화하고_Base64_인코딩한다() {
    String url = "http://localhost:8080";
    String serializedValue = CookieUtils.serialize(url);
    Object deserialize = SerializationHelper.deserialize(
        Base64.getUrlDecoder().decode(serializedValue));

    assertThat(deserialize).hasToString(url);
  }

  @Test
  void cookie_value를_Base64_디코딩하고_역직렬화한다() {
    String url = "http://localhost:8080";
    CookieUtils.addCookie(response, VALID_NAME, CookieUtils.serialize(url), 1);
    Cookie cookie = response.getCookie(VALID_NAME);

    String deserializedValue = CookieUtils.deserialize(cookie, String.class);

    assertThat(deserializedValue).isEqualTo(url);
  }

  private static class MockRequest extends MockHttpServletRequest {

    @Override
    public Cookie[] getCookies() {
      return new Cookie[]{};
    }
  }

}
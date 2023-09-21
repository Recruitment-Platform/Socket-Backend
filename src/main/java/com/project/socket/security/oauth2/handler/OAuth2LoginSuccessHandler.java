package com.project.socket.security.oauth2.handler;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.socket.security.JwtProvider;
import com.project.socket.user.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

  private final Function<User, User> userRepositoryOAuth2UserHandler;
  private final JwtProvider jwtProvider;
  private final ObjectMapper objectMapper;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {
    OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
    User savedUser = userRepositoryOAuth2UserHandler.apply(
        UserFactory.of(
            oAuth2AuthenticationToken.getAuthorizedClientRegistrationId(),
            oAuth2AuthenticationToken.getPrincipal().getAttributes()));

    LoginSuccessResponse loginSuccessResponse = new LoginSuccessResponse(
        savedUser.isProfileSetup(),
        jwtProvider.createAccessToken(savedUser),
        jwtProvider.createRefreshToken(savedUser));

    setupResponse(response);

    objectMapper.writeValue(response.getOutputStream(), loginSuccessResponse);
  }

  private void setupResponse(HttpServletResponse response) {
    response.setStatus(HttpServletResponse.SC_OK);
    response.setContentType(APPLICATION_JSON_VALUE);
    response.addCookie(createExpiredJSessionCookie());
  }

  private Cookie createExpiredJSessionCookie() {
    Cookie jsessionid = new Cookie("JSESSIONID", null);
    jsessionid.setMaxAge(0);
    jsessionid.setPath("/");
    return jsessionid;
  }
}
